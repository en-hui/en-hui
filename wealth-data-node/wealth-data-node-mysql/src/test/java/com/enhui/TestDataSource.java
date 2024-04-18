package com.enhui;

import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class TestDataSource {
  MysqlClient mysqlClient;

  @BeforeEach
  public void before() {
    mysqlClient = new MysqlClient("heh-dev", 3306, "root", "Datapipeline123");
  }

  @Test
  void test() {
    int retry = 3;
    while (retry > 0) {
      retry--;
      try (Connection connection = mysqlClient.dataSource.getConnection()) {
        if (retry != 0) {
          connection.close();
        }
        log.info("connection not closed, autoCommit: {}",connection.getAutoCommit());
      } catch (Exception e) {
        log.error("retry: {}, e: {}", retry, e.getMessage(), e);
      }
    }
  }
}
