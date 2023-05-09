package com.enhui;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class TdSqlMysqlClient extends MysqlClient {
  protected String getJdbcUrl() {
    //    return "jdbc:tdsql-mysql://127.0.0.1:3306/dbname";
    return super.getJdbcUrl();
  }

  protected String getDriverClassName() {
    //    return "com.tencentcloud.tdsql.mysql.cj.jdbc.Driver";
    return super.getDriverClassName();
  }

  @Override
  protected String getUserName() {
    return "root";
  }

  @Override
  protected String getPassword() {
    return "123456";
  }
}
