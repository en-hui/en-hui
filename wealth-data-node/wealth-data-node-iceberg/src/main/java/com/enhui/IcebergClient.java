package com.enhui;

import static org.apache.iceberg.data.parquet.GenericParquetReaders.buildReader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.DataFile;
import org.apache.iceberg.FileFormat;
import org.apache.iceberg.FileScanTask;
import org.apache.iceberg.MetadataColumns;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.TableScan;
import org.apache.iceberg.avro.Avro;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.IdentityPartitionConverters;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.data.avro.DataReader;
import org.apache.iceberg.data.orc.GenericOrcReader;
import org.apache.iceberg.data.parquet.GenericParquetWriter;
import org.apache.iceberg.flink.actions.Actions;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.io.CloseableIterable;
import org.apache.iceberg.io.CloseableIterator;
import org.apache.iceberg.io.DataWriter;
import org.apache.iceberg.io.FileIO;
import org.apache.iceberg.io.InputFile;
import org.apache.iceberg.io.OutputFile;
import org.apache.iceberg.orc.ORC;
import org.apache.iceberg.parquet.Parquet;
import org.apache.iceberg.types.TypeUtil;
import org.apache.iceberg.types.Types;
import org.apache.iceberg.util.PartitionUtil;

public class IcebergClient {

  // hive

  //  add jar
  // /opt/cloudera/parcels/CDH-6.1.1-1.cdh6.1.1.p0.875250/lib/iceberg/iceberg-hive-runtime-1.3.0.jar

  //  create table iceberg_nopar_tbl1(
  //  id int,
  //  name string,
  //  loc string)
  //  stored by 'org.apache.iceberg.mr.hive.HiveIcebergStorageHandler'
  //  LOCATION 'hdfs://cdh1:8020/user/heh/iceberg/icebergdb/iceberg_nopar_tbl1'
  //  TBLPROPERTIES('iceberg.catalog'='location_based_table');

  //  select * from iceberg_test_tbl1;
  public static void main(String[] args) throws IOException {
    Configuration conf = new Configuration();
    System.setProperty("HADOOP_USER_NAME", "hdfs");
    conf.set("dfs.client.use.datanode.hostname", "true");
    String warehousePath = "hdfs://cdh1:8020/user/heh/iceberg";
    HadoopCatalog catalog = new HadoopCatalog(conf, warehousePath);

    String namespace = "icebergdb";
    String tableName = "java_test";
    TableIdentifier name = TableIdentifier.of(namespace, tableName);

    final List<Namespace> namespaces = catalog.listNamespaces();
    System.out.println("所有的库：" + namespaces);
    for (Namespace namespace1 : namespaces) {
      final List<TableIdentifier> tableIdentifiers = catalog.listTables(namespace1);
      System.out.println(namespace1 + " 中所有的表：" + tableIdentifiers);
    }

    // 删除表
    System.out.println("删除表：" + tableName);
    catalog.dropTable(name);

    // 创建或加载表
    Table table = createOrLoadTable(catalog, name, false, false);

    DataFile dataFile = getDataFileWithRecords(table, table.schema(), 2);
    table.newAppend().appendFile(dataFile).commit();

    // 查询数据
    selectAndPrint(table);

    // 添加列，新增数据，查看
    table.updateSchema().addColumn("add_column", Types.IntegerType.get()).commit();
    System.out.println("添加列");
    selectAndPrint(table);

    // 重命名列，新增数据，查看
    table.updateSchema().renameColumn("add_column", "operate_column").commit();
    System.out.println("重命名列");
    selectAndPrint(table);

    // 更新列，新增数据，查看
    table.updateSchema().updateColumn("operate_column", Types.LongType.get()).commit();
    System.out.println("更新列");
    selectAndPrint(table);

    // 删除列，新增数据，查看
    table.updateSchema().deleteColumn("operate_column").commit();
    System.out.println("删除列");
    selectAndPrint(table);

    try {
      System.out.println("持续写入生成诸多小文件");
      // 测试持续写入后，生成很多data file。合并data file并清理snapshot
      for (int i = 0; i < 10; i++) {
        dataFile = getDataFileWithRecords(table, table.schema(), 3 * i);
        table.newAppend().appendFile(dataFile).commit();
      }

      System.out.println("合并小文件 compact data file");
      // 合并data file小文件；指定合并后文件为5MB。依赖flink或spark
      Actions.forTable(table).rewriteDataFiles().targetSizeInBytes(536870912L).execute();

      System.out.println("清理历史快照 snap文件");
      long tsToExpire = System.currentTimeMillis() - (1000); // 1 second
      table.expireSnapshots().expireOlderThan(tsToExpire).commit();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static GenericRecord getRecord(Schema schema, int num) {
    GenericRecord record = GenericRecord.create(schema);
    record.setField("id", num);
    record.setField("name", String.valueOf(num));
    record.setField("loc", String.valueOf(num));
    return record;
  }

  public static ImmutableList<GenericRecord> listRecord(Schema schema, int max) {
    ImmutableList.Builder<GenericRecord> builder = ImmutableList.builder();
    for (int i = 0; i < max; i++) {
      GenericRecord record = getRecord(schema, i);
      builder.add(record);
    }
    return builder.build();
  }

  public static DataFile getDataFileWithRecords(Table table, Schema schema, int max)
      throws IOException {
    // 1. 构建记录
    ImmutableList<GenericRecord> records = listRecord(schema, max);

    // 2. 将记录写入parquet文件
    String filepath = table.location() + "/data/" + UUID.randomUUID().toString();
    OutputFile file = table.io().newOutputFile(filepath);
    DataWriter<GenericRecord> dataWriter =
        Parquet.writeData(file)
            .schema(schema)
            .createWriterFunc(GenericParquetWriter::buildWriter)
            .overwrite()
            .withSpec(PartitionSpec.unpartitioned())
            .build();
    try {
      for (GenericRecord genericRecord : records) {
        dataWriter.write(genericRecord);
      }
    } finally {
      dataWriter.close();
    }

    // 3. 将文件写入table中
    return dataWriter.toDataFile();
  }

  public static Table createOrLoadTable(
      HadoopCatalog catalog, TableIdentifier name, boolean isPartition, boolean isDelta) {
    Table table = null;
    // 创建或加载现有的Iceberg表
    if (!catalog.tableExists(name)) {
      System.out.println("创建新表,是否有分区:" + isPartition);

      // 创建Iceberg表的schema
      Schema schema =
          new Schema(
              Arrays.asList(
                  Types.NestedField.required(1, "id", Types.IntegerType.get()),
                  Types.NestedField.required(2, "name", Types.StringType.get()),
                  Types.NestedField.required(3, "loc", Types.StringType.get())),
              isDelta ? Sets.newHashSet(1) : null);
      PartitionSpec spec = null;
      if (isPartition) {
        spec = PartitionSpec.builderFor(schema).identity("loc").build();
      } else {
        // 没有分区
        spec = PartitionSpec.unpartitioned();
      }
      // 指定iceberg表数据格式化为parquet
      ImmutableMap<String, String> props =
          ImmutableMap.of(
              TableProperties.DEFAULT_FILE_FORMAT,
              FileFormat.PARQUET.name(),
              // upsert用的
              TableProperties.FORMAT_VERSION,
              "2",
              TableProperties.UPSERT_ENABLED,
              "true",
              // 清理json元数据文件用的
              TableProperties.METADATA_DELETE_AFTER_COMMIT_ENABLED,
              "true",
              TableProperties.METADATA_PREVIOUS_VERSIONS_MAX,
              "5");
      table = catalog.createTable(name, schema, spec, props);
    } else {
      System.out.println("加载已有表");
      table = catalog.loadTable(name);
    }
    return table;
  }

  public static void selectAndPrint(Table table) {
    System.out.println("name：" + table.name() + ",schema：" + table.schema());
    System.out.println("行级别查询");
    CloseableIterable<Record> result = IcebergGenerics.read(table).build();
    for (Record record : result) {
      System.out.println("查询到的数据：" + record);
    }

    System.out.println("文件级别查询, 参考即可，不结合元数据，被删除的数据还能被看到");
    TableScan scan = table.newScan();
    CloseableIterable<FileScanTask> fileScanTasks = scan.planFiles();
    for (FileScanTask fileScanTask : fileScanTasks) {
      System.out.println(
          "数据文件："
              + (fileScanTask.deletes().size() > 0
                  ? fileScanTask.deletes().size() + " 已删除文件  "
                  : " ")
              + fileScanTask);
      try (final FileIO io = table.io()) {
        final CloseableIterable<Record> records = openFile(io, fileScanTask, table.schema());
        final CloseableIterator<Record> iterator = records.iterator();
        System.out.println("数据文件内容：");
        while (iterator.hasNext()) {
          System.out.println(iterator.next());
        }
      }
      System.out.println();
    }
  }

  public static CloseableIterable<Record> openFile(
      FileIO fileIo, FileScanTask task, Schema fileProjection) {
    boolean reuseContainers = true;
    boolean caseSensitive = true;
    if (task.isDataTask()) {
      throw new RuntimeException("Cannot read data task.");
    }
    InputFile input = fileIo.newInputFile(task.file().path().toString());
    Map<Integer, ?> partition =
        PartitionUtil.constantsMap(task, IdentityPartitionConverters::convertConstant);

    switch (task.file().format()) {
      case AVRO:
        Avro.ReadBuilder avro =
            Avro.read(input)
                .project(fileProjection)
                .createReaderFunc(
                    avroSchema -> DataReader.create(fileProjection, avroSchema, partition))
                .split(task.start(), task.length());
        if (reuseContainers) {
          avro.reuseContainers();
        }
        return avro.build();
      case PARQUET:
        Parquet.ReadBuilder parquet =
            Parquet.read(input)
                .caseSensitive(caseSensitive)
                .project(fileProjection)
                .createReaderFunc(fileSchema -> buildReader(fileProjection, fileSchema, partition))
                .split(task.start(), task.length())
                .filter(task.residual());
        if (reuseContainers) {
          parquet.reuseContainers();
        }
        return parquet.build();
      case ORC:
        Schema projectionWithoutConstantAndMetadataFields =
            TypeUtil.selectNot(
                fileProjection, Sets.union(partition.keySet(), MetadataColumns.metadataFieldIds()));
        ORC.ReadBuilder orc =
            ORC.read(input)
                .caseSensitive(caseSensitive)
                .project(projectionWithoutConstantAndMetadataFields)
                .createReaderFunc(
                    fileSchema ->
                        GenericOrcReader.buildReader(fileProjection, fileSchema, partition))
                .split(task.start(), task.length())
                .filter(task.residual());
        return orc.build();
      default:
        throw new RuntimeException(
            String.format(
                "Cannot read %s file: %s", task.file().format().name(), task.file().path()));
    }
  }
}
