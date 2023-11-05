package com.enhui.datanode.iceberg;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Map;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.StringData;
import org.apache.flink.types.RowKind;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.FileFormat;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.flink.TableLoader;
import org.apache.iceberg.flink.sink.FlinkSink;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.types.Types;

public class IcebergFlinkClient {
  public static void main(String[] args) throws Exception {
    // 1.创建Flink环境
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    // 2.设置checkpoint
    env.enableCheckpointing(5000);

    System.setProperty("HADOOP_USER_NAME", "hdfs");
    // 3.Flink 读取Kafka 中数据
    // kafka-topics --bootstrap-server kafka1:9092 --topic flink-iceberg-topic --delete
    // kafka-console-producer --bootstrap-server kafka1:9092 --topic flink-iceberg-topic
    // kafka-topics  --bootstrap-server kafka1:9092 --topic flink-iceberg-topic --create
    // 1,zs,18,bj,I
    // 1,zs,18,bj,D
    KafkaSource<String> source =
        KafkaSource.<String>builder()
            .setBootstrapServers("kafka1:9092")
            .setTopics("flink-iceberg-topic")
            .setGroupId("my-group-id1")
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .setStartingOffsets(OffsetsInitializer.earliest())
            .build();

    DataStreamSource<String> kafkads =
        env.fromSource(source, WatermarkStrategy.noWatermarks(), "kafka source");

    SingleOutputStreamOperator<RowData> dataStream =
        kafkads.map(
            new MapFunction<String, RowData>() {
              @Override
              public RowData map(String line) throws Exception {
                String[] split = line.split(",");
                if (split.length == 0) {
                  return null;
                }
                GenericRowData row = new GenericRowData(4);
                row.setField(0, Integer.valueOf(split[0]));
                row.setField(1, StringData.fromString(split[1]));
                row.setField(2, Integer.valueOf(split[2]));
                row.setField(3, StringData.fromString(split[3]));
                if (split.length > 4) {
                  switch (split[4]) {
                    case "D":
                      row.setRowKind(RowKind.DELETE);
                      break;
                    case "I":
                    default:
                      row.setRowKind(RowKind.INSERT);
                  }
                }
                return row;
              }
            });

    dataStream.print();

    // 4.创建Hadoop配置、Catalog配置和表的Schema，方便后续向路径写数据时可以找到对应的表
    Configuration hadoopConf = new Configuration();
    Catalog catalog = new HadoopCatalog(hadoopConf, "hdfs://cdh1:8020/user/heh/iceberg");
    // 配置iceberg 库名和表名
    TableIdentifier name = TableIdentifier.of("icebergdb", "flink_iceberg_tbl");
    // 创建Icebeng表Schema
    Schema schema =
        new Schema(
            Arrays.asList(
                Types.NestedField.required(1, "id", Types.IntegerType.get()),
                Types.NestedField.required(2, "name", Types.StringType.get()),
                Types.NestedField.required(3, "age", Types.IntegerType.get()),
                Types.NestedField.required(4, "loc", Types.StringType.get())),
            Sets.newHashSet(4, 1));

    // 如果有分区指定对应分区，这里“loc”列为分区列，可以指定unpartitioned 方法不设置表分区
    //     PartitionSpec spec = PartitionSpec.unpartitioned();
    PartitionSpec spec = PartitionSpec.builderFor(schema).identity("loc").build();
    // 指定Iceberg表数据格式化为Parquet存储
    Map<String, String> props =
        ImmutableMap.of(
            TableProperties.DEFAULT_FILE_FORMAT,
            FileFormat.PARQUET.name(),
            TableProperties.FORMAT_VERSION,
            "2",
            TableProperties.UPSERT_ENABLED,
            "true");
    Table table = null;
    catalog.dropTable(name);
    // 通过catalog判断表是否存在，不存在就创建，存在就加载
    if (!catalog.tableExists(name)) {
      System.out.println("创建新表");
      table = catalog.createTable(name, schema, spec, props);
    } else {
      System.out.println("加载已有表");
      table = catalog.loadTable(name);
    }

    TableLoader tableLoader =
        TableLoader.fromHadoopTable(
            "hdfs://cdh1:8020/user/heh/iceberg/icebergdb/flink_iceberg_tbl", hadoopConf);

    // 5.将流式结果写出Iceberg表中
    FlinkSink.forRowData(dataStream)
        .table(table)
        .tableLoader(tableLoader)
        // 什么都不开，是append
        //         .overwrite(true) // 覆盖写开启
        .upsert(true) // upsert 开启，必须是'format-version'='2' 且 有主键的表才支持
        .append();

    env.execute("DataStream API Write Iceberg Table");
  }
}
