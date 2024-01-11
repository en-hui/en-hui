package com.enhui.pipeline.cdc;

import com.ververica.cdc.connectors.base.source.jdbc.JdbcIncrementalSource;
import com.ververica.cdc.connectors.postgres.source.PostgresSourceBuilder;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class PostgreCdcPipeline {

    public static void main(String[] args) throws Exception {
        DebeziumDeserializationSchema<String> deserializer =
                new JsonDebeziumDeserializationSchema();

        String pluginName = getPluginName();
        String slotName = "test_flink_" + pluginName;

        JdbcIncrementalSource<String> postgresIncrementalSource =
                PostgresSourceBuilder.PostgresIncrementalSource.<String>builder()
                        .hostname("dev_huenhui")
                        .port(5432)
                        .database("postgres")
                        .schemaList("public")
                        .tableList("public.heh_test_flink")
                        .username("postgres")
                        .password("123456")
                        .slotName(slotName)
                        .decodingPluginName(pluginName) // decoderbufs: use pgoutput for PostgreSQL 10+
                        .deserializer(deserializer)
                        .includeSchemaChanges(true) // output the schema changes as well
                        .splitSize(2) // the split size of each snapshot split
                        .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.enableCheckpointing(3000);

        env.fromSource(
                        postgresIncrementalSource,
                        WatermarkStrategy.noWatermarks(),
                        "PostgresParallelSource")
                .setParallelism(2)
                .print();

        env.execute("Output Postgres Snapshot");
    }

    public static String getPluginName() {
        String pluginName1 = "pgoutput";
        String pluginName2 = "wal2json";
        String pluginName3 = "decoderbufs";
        return pluginName3;
    }
}
