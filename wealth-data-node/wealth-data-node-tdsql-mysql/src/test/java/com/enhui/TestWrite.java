package com.enhui;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
  public void testPerformance() throws SQLException {
    String dbName = "heh_test";
    String tableName = "heh_table";
    final TdSqlMysqlClient tdSqlMysqlClient = new TdSqlMysqlClient();
    final List<String> allDatabases = tdSqlMysqlClient.getAllDatabases();
    log.info("data base: {}", allDatabases);
    if (!allDatabases.contains(dbName)) {
      return;
    }
    final List<Table> allTables = tdSqlMysqlClient.getAllTables(dbName);
    log.info("table: {}", allTables);
    if (tdSqlMysqlClient.tableExists(dbName, tableName)) {
      final Table table = Table.ofSchema(dbName, tableName);
      final Set<Table> tables = Collections.singleton(table);
      final Map<String, List<EntityField>> entityFieldsMap =
          tdSqlMysqlClient.getEntityFieldsMap(tables);
      int min = 1;
      int max = 1;
      int step = 5000;
      for (int i = min; i <= max; i++) {
        List<TestData> testData =
            TestData.listData(entityFieldsMap.get(table.getFullName()), i * step, (i + 1) * step);
        tdSqlMysqlClient.handleWrite(TestData.TestType.INSERT, testData);
      }

      final List<EntityWeight> entityWeights = tdSqlMysqlClient.entityWeights(tables);
      log.info("table length {}", entityWeights);
    } else {
      log.warn("表不存在");
    }
  }
}
