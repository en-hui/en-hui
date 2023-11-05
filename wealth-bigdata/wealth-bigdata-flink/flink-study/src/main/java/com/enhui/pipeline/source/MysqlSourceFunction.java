package com.enhui.pipeline.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MysqlSourceFunction implements SourceFunction<String> {

    // JDBC连接参数
    private static final String jdbcUrl = "jdbc:mysql://dp-mysql:3306/test_flink";
    private static final String username = "root";
    private static final String password = "Datapipeline123";
    private volatile boolean isRunning = true;

    @Override
    public void run(SourceContext<String> ctx) throws Exception {

        // 获取数据库连接
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        Statement statement = connection.createStatement();

        // 读取数据
        ResultSet resultSet = statement.executeQuery("SELECT * FROM flink_source_table");

        while (isRunning && resultSet.next()) {
            // 将每条数据转换为字符串
            String data = resultSet.getInt("pk_id")
                    + "," + resultSet.getString("uid_name")
                    + "," + resultSet.getInt("age");
            // 发出数据
            ctx.collect(data);
        }

        // 关闭连接
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
