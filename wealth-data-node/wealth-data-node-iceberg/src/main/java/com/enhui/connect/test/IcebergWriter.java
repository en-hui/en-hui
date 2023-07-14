package com.enhui.connect.test;

import com.enhui.IcebergClient;
import com.enhui.connect.PartitionedAppendWriter;
import com.enhui.connect.PartitionedDeltaWriter;
import com.enhui.connect.UnpartitionedDeltaWriter;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.FileFormat;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.data.GenericAppenderFactory;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.io.FileAppenderFactory;
import org.apache.iceberg.io.OutputFileFactory;
import org.apache.iceberg.io.TaskWriter;
import org.apache.iceberg.io.UnpartitionedWriter;
import org.apache.iceberg.io.WriteResult;
import org.apache.iceberg.types.Types;

public class IcebergWriter {

  static boolean isPartition = false;
  static boolean isDelta = false;

  public static void main(String[] args) throws IOException {
    IcebergWriter icebergWriter = new IcebergWriter();

    Configuration conf = new Configuration();
    System.setProperty("HADOOP_USER_NAME", "hdfs");
    conf.set("dfs.client.use.datanode.hostname", "true");
    String warehousePath = "hdfs://cdh1:8020/user/heh/iceberg";
    HadoopCatalog catalog = new HadoopCatalog(conf, warehousePath);

    String namespace = "icebergdb";
    String tableName = "writer_test_" + isPartition + "_" + isDelta;
    TableIdentifier name = TableIdentifier.of(namespace, tableName);

    //        catalog.dropTable(name);

    final Table table = IcebergClient.createOrLoadTable(catalog, name, isPartition);

    TaskWriter<Record> taskWriter = null;

    taskWriter =
        icebergWriter.getUnpartitionedWriter(table, FileFormat.PARQUET, isPartition, isDelta);
    for (int i = 0; i < 5000; i++) {
      taskWriter.write(icebergWriter.getRecord(table, i));
    }
    taskWriter.write(icebergWriter.getRecord(table, 2));
    taskWriter.write(icebergWriter.getRecord(table, 1));
    taskWriter.close();
    final WriteResult complete = taskWriter.complete();
    System.out.println(Arrays.toString(complete.referencedDataFiles()));
    System.out.println(Arrays.toString(complete.deleteFiles()));
    System.out.println(Arrays.toString(complete.dataFiles()));

    table.refresh();

    IcebergClient.selectAndPrint(table);
  }

  public Record getRecord(Table table, int i) {
    GenericRecord record = GenericRecord.create(table.schema());
    for (Types.NestedField column : table.schema().columns()) {
      if (column.type() == Types.IntegerType.get()) {
        record.setField(column.name(), i);
      } else if (column.type() == Types.StringType.get()) {
        record.setField(column.name(), String.valueOf(i));
      } else {
        throw new RuntimeException("没处理的类型，换成int 和 string 测试");
      }
    }
    return record;
  }

  public TaskWriter<Record> getUnpartitionedWriter(
      Table table, FileFormat format, boolean isPartition, boolean isDelta) {
    long targetFileSize = 536870912L;
    FileAppenderFactory<Record> appenderFactory =
        (new GenericAppenderFactory(
                table.schema(),
                table.spec(),
                Ints.toArray(table.schema().identifierFieldIds()),
                table.schema(),
                (Schema) null))
            .setAll(table.properties());
    OutputFileFactory fileFactory =
        OutputFileFactory.builderFor(table, 1, System.currentTimeMillis())
            .defaultSpec(table.spec())
            .operationId(UUID.randomUUID().toString())
            .format(format)
            .build();
    if (isPartition) {
      if (isDelta) {
        System.out.println("PartitionedDeltaWriter--是否分区：" + isPartition + ",是否增量：" + isDelta);
        return new PartitionedDeltaWriter(
            table.spec(),
            format,
            appenderFactory,
            fileFactory,
            table.io(),
            targetFileSize,
            table.schema(),
            true);
      }
      System.out.println("PartitionedAppendWriter--是否分区：" + isPartition + ",是否增量：" + isDelta);
      return new PartitionedAppendWriter(
          table.spec(),
          format,
          appenderFactory,
          fileFactory,
          table.io(),
          targetFileSize,
          table.schema());
    }
    if (isDelta) {
      System.out.println("UnpartitionedDeltaWriter--是否分区：" + isPartition + ",是否增量：" + isDelta);
      return new UnpartitionedDeltaWriter(
          table.spec(),
          format,
          appenderFactory,
          fileFactory,
          table.io(),
          targetFileSize,
          table.schema(),
          true);
    }
    System.out.println("UnpartitionedWriter--是否分区：" + isPartition + ",是否增量：" + isDelta);
    return new UnpartitionedWriter<>(
        table.spec(), format, appenderFactory, fileFactory, table.io(), targetFileSize);
  }
}
