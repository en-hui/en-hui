package com.enhui.pipeline.cdc;

import com.ververica.cdc.connectors.base.options.StartupOptions;
import com.ververica.cdc.connectors.base.source.jdbc.JdbcIncrementalSource;
import com.ververica.cdc.connectors.oracle.source.OracleSourceBuilder;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import java.util.Properties;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class OracleCdcPipeline {

  public static void main(String[] args) throws Exception {
    Properties debeziumProperties = new Properties();
    debeziumProperties.setProperty("log.mining.strategy", "online_catalog");

    JdbcIncrementalSource<String> oracleChangeEventSource =
        new OracleSourceBuilder()
            .hostname("oracle-source")
            .port(1521)
            .databaseList("orcl")
            .schemaList("DP_TEST")
            .tableList("DP_TEST.BATCH_TEST_BIG")
            .username("dp_test")
            .password("123456")
            .deserializer(new JsonDebeziumDeserializationSchema())
            .includeSchemaChanges(true) // output the schema changes as well
            .startupOptions(StartupOptions.initial())
            .debeziumProperties(debeziumProperties)
            .splitSize(10000)
            .build();

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    // enable checkpoint
    env.enableCheckpointing(3000L);
    // set the source parallelism to 4
    env.fromSource(
            oracleChangeEventSource, WatermarkStrategy.noWatermarks(), "OracleParallelSource")
        .setParallelism(4)
        .print()
        .setParallelism(1);
    env.execute("Print Oracle Snapshot + RedoLog");
  }
}
