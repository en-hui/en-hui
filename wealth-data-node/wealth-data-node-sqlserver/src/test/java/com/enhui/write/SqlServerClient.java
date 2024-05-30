package com.enhui.write;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SqlServerClient {

  private HikariDataSource ds;

  private SqlServerClient() {
    HikariConfig configuration = new HikariConfig();

    configuration.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    configuration.setJdbcUrl("jdbc:sqlserver://bj-mssql-oukom5ky.sql.tencentcdb.com:29797");
    configuration.setUsername("dp_test");
    configuration.setPassword("Datapipeline123!");
    configuration.setMaximumPoolSize(1);
    configuration.setMinimumIdle(0);
    configuration.setConnectionTimeout(10 * 1000);
    configuration.setMaxLifetime(1800 * 1000);
    configuration.setIdleTimeout(30 * 1000);
    configuration.setPoolName(
        this.getClass().getName() + "-connection-pool-" + System.currentTimeMillis());

    ds = new HikariDataSource(configuration);
  }

  private static SqlServerClient INSTANCE = new SqlServerClient();

  public static SqlServerClient getInstance() {
    return INSTANCE;
  }

  public Connection getConnection() throws SQLException {
    return ds.getConnection();
  }

  public void close() {
    if (ds != null) {
      ds.close();
    }
  }
}
