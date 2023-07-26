package com.enhui.connect.test;

import com.enhui.IcebergClient;
import com.enhui.connect.Operation;
import com.enhui.connect.PartitionedAppendWriter;
import com.enhui.connect.PartitionedDeltaWriter;
import com.enhui.connect.RecordWrapper;
import com.enhui.connect.UnpartitionedDeltaWriter;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.AppendFiles;
import org.apache.iceberg.DataFile;
import org.apache.iceberg.DeleteFile;
import org.apache.iceberg.FileFormat;
import org.apache.iceberg.RowDelta;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.Transaction;
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

  static boolean isPartition;
  static boolean isDelta;

  public static void main(String[] args) throws IOException {
    IcebergWriter icebergWriter = new IcebergWriter();

    Configuration conf = new Configuration();
    System.setProperty("HADOOP_USER_NAME", "hdfs");
    conf.set("dfs.client.use.datanode.hostname", "true");
    String warehousePath = "hdfs://cdh1:8020/user/heh/iceberg";
    HadoopCatalog catalog = new HadoopCatalog(conf, warehousePath);
    String namespace = "icebergdb";

    // 无分区、非增量 ==> 仅支持插入
    isPartition = false;
    isDelta = false;
    String tableName = "writer_test_" + isPartition + "_" + isDelta;
    TableIdentifier name = TableIdentifier.of(namespace, tableName);
    catalog.dropTable(name);
    Table table = IcebergClient.createOrLoadTable(catalog, name, isPartition, isDelta);
    TaskWriter<Record> allUnPartitionTaskWriter =
            icebergWriter.getUnpartitionedWriter(table, FileFormat.PARQUET, isPartition, isDelta);
    allUnPartitionTaskWriter.write(icebergWriter.getRecord(table, 1, false));
    allUnPartitionTaskWriter.complete();
    IcebergClient.selectAndPrint(table);


    // 有分区、增量 ==> 插入、删除
    isPartition = true;
    isDelta = true;
    tableName = "writer_test_" + isPartition + "_" + isDelta;
    name = TableIdentifier.of(namespace, tableName);
    catalog.dropTable(name);
    table = IcebergClient.createOrLoadTable(catalog, name, isPartition, isDelta);
    TaskWriter<Record> deltaPartitionTaskWriter =
            icebergWriter.getUnpartitionedWriter(table, FileFormat.PARQUET, isPartition, isDelta);
    deltaPartitionTaskWriter.write(new RecordWrapper(icebergWriter.getRecord(table, 1, false), Operation.INSERT));
    deltaPartitionTaskWriter.write(new RecordWrapper(icebergWriter.getRecord(table, 2, false), Operation.INSERT));
    deltaPartitionTaskWriter.write(new RecordWrapper(icebergWriter.getRecord(table, 3, false), Operation.INSERT));
    deltaPartitionTaskWriter.complete();
    IcebergClient.selectAndPrint(table);

    deltaPartitionTaskWriter.write(new RecordWrapper(icebergWriter.getRecord(table, 2, true), Operation.UPDATE));
    IcebergClient.selectAndPrint(table);

    deltaPartitionTaskWriter.write(new RecordWrapper(icebergWriter.getRecord(table, 3, false), Operation.DELETE));
    IcebergClient.selectAndPrint(table);

//    taskWriter.write(icebergWriter.getRecord(table, 1, isUpdate));
//    taskWriter.write(icebergWriter.getRecord(table, 2, isUpdate));
//    taskWriter.write(icebergWriter.getRecord(table, 1, isUpdate));
//    taskWriter.close();
//    final WriteResult complete = taskWriter.complete();
//    System.out.println("\nreferencedDataFiles：" + Arrays.toString(complete.referencedDataFiles()));
//    System.out.println("\ndeleteFiles：" + Arrays.toString(complete.deleteFiles()));
//    System.out.println("\ndataFiles：" + Arrays.toString(complete.dataFiles()));
//
//    final Transaction transaction = table.newTransaction();
//    for (DataFile dataFile : complete.dataFiles()) {
//      AppendFiles appendFiles = transaction.newAppend();
//      appendFiles.appendFile(dataFile).commit();
//    }
//    for (DeleteFile deleteFile : complete.deleteFiles()) {
//      RowDelta rowDelta = transaction.newRowDelta();
//      rowDelta.addDeletes(deleteFile).commit();
//    }
//    transaction.commitTransaction();
//
//    IcebergClient.selectAndPrint(table);
  }

  public Record getRecord(Table table, int i, boolean idUpdate) {
    GenericRecord record = GenericRecord.create(table.schema());
    for (Types.NestedField column : table.schema().columns()) {
      if (column.type() == Types.IntegerType.get()) {
        record.setField(column.name(), i);
      } else if (column.type() == Types.StringType.get()) {
        record.setField(column.name(), String.valueOf(idUpdate ? i + 1 : i));
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
