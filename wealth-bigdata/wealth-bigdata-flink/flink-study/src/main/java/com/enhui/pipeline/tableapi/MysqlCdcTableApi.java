package com.enhui.pipeline.tableapi;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

public class MysqlCdcTableApi {

  public static void main(String[] args) {

    EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
    TableEnvironment tableEnv = TableEnvironment.create(settings);

    // 注册 MySQL 数据源
    tableEnv.executeSql(
        "CREATE TABLE mysql_source ("
            + "pk_id INT NOT NULL,"
            + "uid_name STRING,"
            + "age INT"
            + ") WITH ("
            + " 'connector' = 'jdbc',"
            + " 'url' = 'jdbc:mysql://dp-mysql:3306/test_flink',"
            + " 'table-name' = 'flink_source_table',"
            + " 'username' = 'root',"
            + " 'password' = 'Datapipeline123'"
            + ")");

    // 执行查询
    tableEnv.executeSql("SELECT * FROM mysql_source").print();
  }
}
