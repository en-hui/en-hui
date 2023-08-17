package com.enhui;

import static com.enhui.model.AllTypeTableColumn.ALL_COLUMN_TYPE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.postgresql.jdbc.PgConnection;

public class NodeMetaDataService {

  @Test
  public void createAllTypeTable() throws SQLException, ClassNotFoundException {
    AtomicInteger i = new AtomicInteger(1);
    String columnSql =
        ALL_COLUMN_TYPE.stream()
            .map(
                colu ->
                    String.format("%s %s", colu.getName() + i.getAndIncrement(), colu.getType()))
            .collect(Collectors.joining(",\n"));

    String createSql = String.format("CREATE TABLE heh_all_type1(%s);", columnSql);

    try (PgConnection slaveConn = NodeService.getSlaveConn();
        Statement statement = slaveConn.createStatement()) {
      initDatabase(statement);
    }
  }

  private void initDatabase(Statement statement) throws SQLException {
    List<String> databases = new ArrayList<>();
    ResultSet resultSet = statement.executeQuery("select * from pg_database");
    while (resultSet.next()) {
      final String s = resultSet.getString(1) + "--" + resultSet.getString(12);
      //        System.out.println(s);
      databases.add(s);
    }
    // DBCOMPATIBILITY [ = ] compatibility_type
    // 指定兼容的数据库的类型，默认兼容O。
    // 取值范围：A、B、C、PG。分别表示兼容Oracle、MySQL、Teradata和POSTGRES。
    if (!databases.contains("oracle_base--A")) {
      System.out.println("创建数据库：oracle_base");
      statement.execute("CREATE DATABASE oracle_base WITH DBCOMPATIBILITY = 'A'");
    }
    if (!databases.contains("mysql_base--B")) {
      System.out.println("创建数据库：mysql_base");
      statement.execute("CREATE DATABASE mysql_base WITH DBCOMPATIBILITY = 'B'");
    }
    if (!databases.contains("teradata_base--C")) {
      System.out.println("创建数据库：teradata_base");
      statement.execute("CREATE DATABASE teradata_base WITH DBCOMPATIBILITY = 'C'");
    }
    if (!databases.contains("pg_base--PG")) {
      System.out.println("创建数据库：pg_base");
      statement.execute("CREATE DATABASE pg_base WITH DBCOMPATIBILITY = 'PG'");
    }
  }
}
