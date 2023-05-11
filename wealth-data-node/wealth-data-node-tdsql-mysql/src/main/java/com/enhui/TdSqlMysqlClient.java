package com.enhui;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TdSqlMysqlClient extends MysqlClient {
  public TdSqlMysqlClient(String ip, int port, String userName, String password) {
    super(ip, port, userName, password);
  }

  protected String getJdbcUrlTemplate() {
    return "jdbc:tdsql-mysql://%s:%s";
  }

  @Override
  protected String getDriverClassName() {
    return "com.tencentcloud.tdsql.mysql.cj.jdbc.Driver";
  }
}
