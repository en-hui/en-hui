package com.enhui.pipeline.cdc;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * https://ververica.github.io/flink-cdc-connectors/master/content/connectors/mysql-cdc%28ZH%29.html#id6
 */
public class MysqlCdcPipeline {
    private static final String jdbcUrl = "jdbc:mysql://dp-mysql:3306/test_flink";
    private static final String username = "dp_test";
    private static final String binlog_password = "123456";

    public static void main(String[] args) throws Exception {
        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname("mysql-binlog")
                .port(3306)
                .databaseList("heh_test") // set captured database
                .tableList("heh_test.test_binlog") // set captured table
                .username(username)
                .password(binlog_password)
                .scanNewlyAddedTableEnabled(false)
                .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
                //MySQLSource.builder()
                //    .startupOptions(StartupOptions.earliest()) // 从最早位点启动
                //    .startupOptions(StartupOptions.latest()) // 从最晚位点启动
                //    .startupOptions(StartupOptions.specificOffset("mysql-bin.000003", 4L) // 从指定 binlog 文件名和位置启动
                //    .startupOptions(StartupOptions.specificOffset("24DA167-0C0C-11E8-8442-00059A3C7B00:1-19")) // 从 GTID 集合启动
                //    .startupOptions(StartupOptions.timestamp(1667232000000L) // 从时间戳启动
                .startupOptions(StartupOptions.initial())
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // enable checkpoint
        env.enableCheckpointing(3000);

        DataStreamSource<String> stream = env
                .fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source")
                // set 4 parallel source tasks
                .setParallelism(4);

        stream.print("-----------").setParallelism(1); // use parallelism 1 for sink to keep message ordering

        env.execute("Print MySQL Snapshot + Binlog");
    }
}
