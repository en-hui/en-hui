package com.enhui;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;

@Slf4j
public class TestWrite {

  /**
   * create table heh_table ( column_1 varchar(255), column_2 varchar(255), column_3 varchar(255),
   * column_4 varchar(255), column_5 varchar(255), column_6 varchar(255), column_7 varchar(255),
   * column_8 varchar(255), column_9 varchar(255), column_10 varchar(255), id int null );
   *
   * @throws SQLException
   */
  @Test
  @Benchmark
  public void testPerformance() throws SQLException, InterruptedException {
    String dbName = "heh_test";
    String tableName = "heh_table";
    final TdSqlMysqlClient tdSqlMysqlClient =
        new TdSqlMysqlClient("82.157.66.105", 3306, "root", "123456");
    final List<String> allDatabases = tdSqlMysqlClient.getAllDatabases();
    log.info("all database: {}", allDatabases);
    if (!allDatabases.contains(dbName)) {
      return;
    }
    final List<Table> allTables = tdSqlMysqlClient.getAllTables(dbName);
    log.info(
        "all table: {} in database: {}",
        allTables.stream().map(Table::getFullName).collect(Collectors.joining(",")),
        dbName);
    if (tdSqlMysqlClient.tableExists(dbName, tableName)) {
      final Table table = Table.ofSchema(dbName, tableName);
      tdSqlMysqlClient.truncateTable(table);
      final Set<Table> tables = Collections.singleton(table);
      EntityWeight startEntityWeight = tdSqlMysqlClient.entityWeights(table);
      log.info("before write record,data length is {} MB", startEntityWeight.getWeight());
      final Map<String, List<EntityField>> entityFieldsMap =
          tdSqlMysqlClient.getEntityFieldsMap(tables);
      int min = 1;
      int max = 1;
      int step = 50000;
      long cost = 0;
      // 数据准备 && 数据写入
      for (int i = min; i <= max; i++) {
        List<TestData> testData =
            TestData.listData(entityFieldsMap.get(table.getFullName()), i * step, (i + 1) * step);
        cost += tdSqlMysqlClient.handleWrite(TestData.TestType.INSERT, table, testData);
      }

      EntityWeight endEntityWeight = tdSqlMysqlClient.entityWeights(table);
      double dataSize = endEntityWeight.getWeight() - startEntityWeight.getWeight();
      double finalDataSize = 0;
      while (dataSize == 0 || dataSize != finalDataSize) {
        finalDataSize = dataSize;
        log.info("after write record,data length is {} MB", endEntityWeight.getWeight());
        Thread.sleep(5 * 1000);
        endEntityWeight = tdSqlMysqlClient.entityWeights(table);
        dataSize = endEntityWeight.getWeight() - startEntityWeight.getWeight();
      }
      log.info(
          "table {} write record: {}, cost: {} s,data length: {} MB, speed is {} MB/s",
          endEntityWeight.getTable().getFullName(),
          ((max - min + 1) * step),
          cost / 1000,
          dataSize,
          dataSize / (cost / 1000));
    } else {
      log.warn("表不存在");
    }
  }
}
