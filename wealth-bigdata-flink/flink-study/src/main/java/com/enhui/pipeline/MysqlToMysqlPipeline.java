package com.enhui.pipeline;

import com.enhui.pipeline.source.MysqlSourceFunction;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MysqlToMysqlPipeline {
    // JDBC连接参数
    private static final String jdbcUrl = "jdbc:mysql://dp-mysql:3306/test_flink";
    private static final String username = "root";
    private static final String password = "Datapipeline123";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 只会读一次源表，无法使用where实现增量查询
        DataStream<String> stream = env.addSource(new MysqlSourceFunction());

        // 看看读了什么东西
        stream.print();

        // upsert : insert into on DUPLICATE KEY UPDATE
        stream.addSink(JdbcSink.sink("insert into flink_sink_table (id,name,age) values (?,?,?) ON DUPLICATE KEY UPDATE name=VALUES(name),age=VALUES(age)",
                (ps, t) -> {
                    String[] arr = ((String) t).split(",");
                    ps.setInt(1, Integer.parseInt(arr[0]));
                    ps.setString(2, arr[1]);
                    ps.setInt(3, Integer.parseInt(arr[2]));
                },
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl(jdbcUrl)
                        .withDriverName("com.mysql.cj.jdbc.Driver")
                        .withUsername(username)
                        .withPassword(password)
                        .build()));

        env.execute("mysql to kafka");
    }

}