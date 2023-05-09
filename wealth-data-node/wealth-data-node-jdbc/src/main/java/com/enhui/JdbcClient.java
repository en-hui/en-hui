package com.enhui;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class JdbcClient {

  protected HikariDataSource dataSource;

  protected abstract String getJdbcUrl();

  protected abstract String getDriverClassName();

  protected abstract String getUserName();

  protected abstract String getPassword();

  public JdbcClient() {
    init();
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
    dataSource = new HikariDataSource(configuration);
  }

  public void handleWrite(TestData.TestType testType, List<TestData> list) {
    final long start = System.currentTimeMillis();
    switch (testType) {
      case INSERT:
        insert(list);
        break;
      case UPDATE:
        update(list);
        break;
      case UPSERT:
        upsert(list);
        break;
      case DELETE:
        delete(list);
        break;
    }
    final long end = System.currentTimeMillis();
    log.info("handle {} {} records consume {} ms", testType.name(), list.size(), (end - start));
  }

  public void insert(List<TestData> list) {}

  public void update(List<TestData> list) {}

  public void upsert(List<TestData> list) {}

  public void delete(List<TestData> list) {}
}
