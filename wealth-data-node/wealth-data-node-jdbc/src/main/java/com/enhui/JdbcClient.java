package com.enhui;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

  public void handleWrite(TestData.TestType testType, Table table, List<TestData> list)
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
    log.info("handle {} {} records consume {} ms", testType.name(), list.size(), (end - start));
  }

  public void insert(Table table, List<TestData> list) throws SQLException {
    final TestData testData = list.get(0);
    String sql =
        String.format(
            "insert into %s (%s) VALUES (%s)",
            table.getName(),
            String.join(",", testData.getColNames()),
            IntStream.range(0, testData.getColNames().size())
                .mapToObj(index -> "?")
                .collect(Collectors.joining()));
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      for (int i = 0; i < list.size(); i++) {
        final TestData data = list.get(i);
        for (int j = 0; j < data.getColNames().size(); j++) {
          if ("INT".equals(data.getColTypes().get(j))) {
            preparedStatement.setInt(j, Integer.parseInt(data.getColVals().get(i)));
          } else {
            preparedStatement.setString(j, data.getColVals().get(i));
          }
        }
        preparedStatement.addBatch();
      }
      preparedStatement.executeBatch();
    }
  }

  public void update(Table table, List<TestData> list) {}

  public void upsert(Table table, List<TestData> list) {}

  public void delete(Table table, List<TestData> list) {}
}
