package com.enhui;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TdSqlMysqlClient extends MysqlClient {
  protected String getJdbcUrl() {
    return "jdbc:tdsql-mysql://127.0.0.1:3306/dbname";
  }

  protected String getDriverClassName() {
    return "com.tencentcloud.tdsql.mysql.cj.jdbc.Driver";
  }

  @Override
  protected String getUserName() {
    return null;
  }

  @Override
  protected String getPassword() {
    return null;
  }
}
