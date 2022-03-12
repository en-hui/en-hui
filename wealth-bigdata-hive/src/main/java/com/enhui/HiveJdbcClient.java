package com.enhui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcClient {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // heh-node02 在host文件配置了 hiveserver2 的ip地址
        Connection conn = DriverManager.getConnection("jdbc:hive2://heh-node02:10000/default", "root", "root");
        Statement stmt = conn.createStatement();
        String sql = "select * from person limit 5";
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + "-" + resultSet.getString("name"));
        }

    }
}
