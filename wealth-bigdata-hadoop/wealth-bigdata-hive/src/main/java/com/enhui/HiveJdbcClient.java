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
    Connection conn =
        DriverManager.getConnection("jdbc:hive2://heh-node02:10000/dp_test", "root", "root");
    Statement stmt = conn.createStatement();
// 事务参数
//    stmt.execute("set hive.support.concurrency=true");
//    stmt.execute("set hive.exec.dynamic.partition.mode=nonstrict");
//    stmt.execute("set hive.txn.manager=org.apache.hadoop.hive.ql.lockmgr.DbTxnManager");
    String insertSql =
        "INSERT INTO TABLE `dp_test`.`heh_sample1` (`id`, `name`, `age`, `smi`) VALUES (1,1,1,1)";
    boolean execute = stmt.execute(insertSql);
    System.out.println("插入结果：" + execute);
    String sql = "select * from heh_sample1 limit 5";
    ResultSet resultSet = stmt.executeQuery(sql);
    while (resultSet.next()) {
      System.out.println(resultSet.getString(1) + "-" + resultSet.getString("name"));
    }
  }
}
