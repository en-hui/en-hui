package com.enhui;

import static com.enhui.model.AllTypeTableColumn.ALL_COLUMN_TYPE;

import com.enhui.model.AllTypeTableColumn;
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
    initDatabase();

    createAllTypeTable(
        AllTypeTableColumn.ORACLE_DATABASE,
        AllTypeTableColumn.NotSupportedType.ORACLE,
        "heh_oracle_all_type1");

    createAllTypeTable(
        AllTypeTableColumn.MYSQL_DATABASE,
        AllTypeTableColumn.NotSupportedType.MYSQL,
        "heh_mysql_all_type1");

    createAllTypeTable(
        AllTypeTableColumn.TERADATA_DATABASE,
        AllTypeTableColumn.NotSupportedType.TERADATA,
        "heh_teradata_all_type1");

    createAllTypeTable(
        AllTypeTableColumn.PG_DATABASE, AllTypeTableColumn.NotSupportedType.PG, "heh_pg_all_type1");
  }

  private void createAllTypeTable(
      String database, AllTypeTableColumn.NotSupportedType notSupportedType, String tableName)
      throws SQLException, ClassNotFoundException {
    try (PgConnection conn = NodeService.getSlaveConn(database);
        Statement statement = conn.createStatement()) {
      AtomicInteger i = new AtomicInteger(1);
      final List<AllTypeTableColumn> supportedList =
          ALL_COLUMN_TYPE.stream()
              .filter(
                  t ->
                      t.getNotSupportedTypes() == null
                          || !t.getNotSupportedTypes().contains(notSupportedType))
              .collect(Collectors.toList());

      final List<String> notSupportedList =
          ALL_COLUMN_TYPE.stream()
              .filter(
                  t ->
                      t.getNotSupportedTypes() != null
                          && t.getNotSupportedTypes().contains(notSupportedType))
              .map(AllTypeTableColumn::getType)
              .collect(Collectors.toList());
      System.out.println("vastbase g100的" + notSupportedType + "兼容模式 不支持的类型：" + notSupportedList);
      String columnSql =
          supportedList.stream()
              .map(
                  colu ->
                      String.format("%s %s", colu.getName() + i.getAndIncrement(), colu.getType()))
              .collect(Collectors.joining(",\n"));

      String createSql = String.format("CREATE TABLE public." + tableName + "(%s);", columnSql);
      //      System.out.println("vastbase g100的" + notSupportedType + "兼容模式 全类型建表语句：" + createSql);
      try {
        statement.execute("drop table public." + tableName);
      } catch (Exception ignore) {
      }
      statement.execute(createSql);
    }
  }

  /**
   * 创建不同兼容模式的数据库，已存在则忽略
   *
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  private void initDatabase() throws SQLException, ClassNotFoundException {
    try (PgConnection slaveConn = NodeService.getSlaveConn(null);
        Statement statement = slaveConn.createStatement()) {
      List<String> databases = new ArrayList<>();
      ResultSet resultSet = statement.executeQuery("select * from pg_database");
      while (resultSet.next()) {
        final String s = resultSet.getString(1) + "--" + resultSet.getString(12);
        //                System.out.println(s);
        databases.add(s);
      }
      // DBCOMPATIBILITY [ = ] compatibility_type
      // 指定兼容的数据库的类型，默认兼容O。
      // 取值范围：A、B、C、PG。分别表示兼容Oracle、MySQL、Teradata和POSTGRES。
      if (!databases.contains(AllTypeTableColumn.ORACLE_DATABASE + "--A")) {
        System.out.println("创建数据库：" + AllTypeTableColumn.ORACLE_DATABASE);
        statement.execute(
            "CREATE DATABASE "
                + AllTypeTableColumn.ORACLE_DATABASE
                + " WITH DBCOMPATIBILITY = 'A'");
      }
      if (!databases.contains(AllTypeTableColumn.MYSQL_DATABASE + "--B")) {
        System.out.println("创建数据库：" + AllTypeTableColumn.MYSQL_DATABASE);
        statement.execute(
            "CREATE DATABASE " + AllTypeTableColumn.MYSQL_DATABASE + " WITH DBCOMPATIBILITY = 'B'");
      }
      if (!databases.contains(AllTypeTableColumn.TERADATA_DATABASE + "--C")) {
        System.out.println("创建数据库：" + AllTypeTableColumn.TERADATA_DATABASE);
        statement.execute(
            "CREATE DATABASE "
                + AllTypeTableColumn.TERADATA_DATABASE
                + " WITH DBCOMPATIBILITY = 'C'");
      }
      if (!databases.contains(AllTypeTableColumn.PG_DATABASE + "--PG")) {
        System.out.println("创建数据库：" + AllTypeTableColumn.PG_DATABASE);
        statement.execute(
            "CREATE DATABASE " + AllTypeTableColumn.PG_DATABASE + " WITH DBCOMPATIBILITY = 'PG'");
      }
    }
  }
}
