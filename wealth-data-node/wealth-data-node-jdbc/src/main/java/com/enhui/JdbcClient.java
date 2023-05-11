package com.enhui;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class JdbcClient {

  protected String ip;
  protected int port;
  protected String userName;
  protected String password;
  protected HikariDataSource dataSource;

  public JdbcClient(String ip, int port, String userName, String password) {
    this.ip = ip;
    this.port = port;
    this.userName = userName;
    this.password = password;
    init();
  }

  protected String getJdbcUrl() {
    return String.format(getJdbcUrlTemplate(), ip, port);
  }

  protected abstract String getJdbcUrlTemplate();

  protected abstract String getDriverClassName();

  protected Properties getJdbcProperties() {
    return null;
  }

  public void init() {
    HikariConfig configuration = new HikariConfig();
    configuration.setDriverClassName(getDriverClassName());
    configuration.setJdbcUrl(getJdbcUrl());
    configuration.setUsername(getUserName());
    configuration.setPassword(getPassword());
    configuration.setMaximumPoolSize(1);
    configuration.setMinimumIdle(0);
    configuration.setConnectionTimeout(10 * 1000);
    configuration.setMaxLifetime(1800 * 1000);
    configuration.setIdleTimeout(30 * 1000);
    configuration.setPoolName(
        this.getClass().getName() + "-connection-pool-" + System.currentTimeMillis());
    if (getJdbcProperties() != null) {
      configuration.setDataSourceProperties(getJdbcProperties());
    }
    dataSource = new HikariDataSource(configuration);
  }

  public long handleWrite(TestData.TestType testType, Table table, List<TestData> list)
      throws SQLException {
    final long start = System.currentTimeMillis();
    switch (testType) {
      case INSERT:
        insert(table, list);
        break;
      case UPDATE:
        update(table, list);
        break;
      case UPSERT:
        upsert(table, list);
        break;
      case DELETE:
        delete(table, list);
        break;
    }
    final long end = System.currentTimeMillis();
    final long cost = end - start;
    final double sum =
        list.stream()
                .mapToDouble(
                    one -> one.getColVals().stream().mapToDouble(o -> o.getBytes().length).sum())
                .sum()
            / 1024.0
            / 1024.0;
    log.info(
        "handle {} : {} records, {} MB, consume {} ms, speed {} MB/s",
        testType.name(),
        list.size(),
        sum,
        cost,
        sum / (cost / 1000.0));
    return cost;
  }

  public abstract void insert(Table table, List<TestData> list) throws SQLException;

  public void update(Table table, List<TestData> list) {}

  public void upsert(Table table, List<TestData> list) {}

  public void delete(Table table, List<TestData> list) {}

  public abstract void truncateTable(Table table) throws SQLException;
}
