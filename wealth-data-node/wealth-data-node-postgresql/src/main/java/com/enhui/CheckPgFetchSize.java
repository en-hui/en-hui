package com.enhui;

import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;
import org.postgresql.PGProperty;
import org.postgresql.core.TypeInfo;
import org.postgresql.jdbc.PgConnection;

public class CheckPgFetchSize {
  private static final String driver = "org.postgresql.Driver";
  private static final String ip = "heh-node02";
  private static final int port = 5432;
  private static final String database = "postgres";
  private static PgConnection conn = null;
  /** 1 byte * 1024 = 1kb */
  private static String singleContent = String.join("", Collections.nCopies(1024 * 128, "a"));

  public static void before() throws ClassNotFoundException, SQLException {
    Class.forName(driver);
    Properties properties = new Properties();
    PGProperty.USER.set(properties, "postgres");
    PGProperty.PASSWORD.set(properties, "123456");
    String sourceURL = String.format("jdbc:postgresql://%s:%s/%s", ip, port, database);
    conn = (PgConnection) DriverManager.getConnection(sourceURL, properties);
  }

  public static void after() throws SQLException {
    if (conn != null) {
      conn.close();
    }
  }

  public static void main(String[] args)
      throws ClassNotFoundException, SQLException, InterruptedException {
    before();

    //    insert();

    query();

    after();
  }

  public static void insert() throws SQLException {

    String dropSql = "drop table public.heh_test_memory;";
    String createSql = "create table public.heh_test_memory\n" +
            "(\n" +
            "    id serial\n" +
            "        constraint table_name_pk\n" +
            "            primary key,\n" +
            "    content varchar\n" +
            ");";
    String insertSql =
        String.format("insert into public.heh_test_memory (content) values ('%s')", singleContent);
    try (Statement connStatement = conn.createStatement()) {
      System.out.println(dropSql);
      connStatement.execute(dropSql);

      System.out.println(createSql);
      connStatement.execute(createSql);

      System.out.println(insertSql);
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 2000; j++) {
          connStatement.addBatch(insertSql);
        }
        connStatement.executeBatch();
      }
    }
  }

  public static void query() throws SQLException, InterruptedException {
    ResultSet rs = null;
    final int singleContentSize = singleContent.getBytes(StandardCharsets.UTF_8).length / 1024;
    int fetchSize = 2000;
    System.out.println("单条数据的大小：" + singleContentSize + " kb");
    System.out.println("fetchSize的数据的大小：" + fetchSize * singleContentSize / 1024 + " mb");

    // 必须关闭自动提交事务
    conn.setAutoCommit(false);
    try (Statement connStatement =
        conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
      connStatement.setFetchSize(fetchSize);
      printMemory("查询前 " + LocalDateTime.now());
      rs = connStatement.executeQuery("select id,content from public.heh_test_memory");
      printMemory("查询后 " + LocalDateTime.now());
      int i = 0;
      while (rs.next()) {
        i++;
        rs.getString(1);
        if (i % fetchSize == 0 || i % fetchSize == 1) printMemory(i + " next时 " + LocalDateTime.now());
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    Thread.sleep(3000);
    printMemory("关闭资源后 " + LocalDateTime.now());
  }

  private static void printMemory(String msg) {
    System.out.println(
        msg
            + " total: "
            + Runtime.getRuntime().totalMemory() / 1024 / 1024
            + "MB, free: "
            + Runtime.getRuntime().freeMemory() / 1024 / 1024
            + "MB");
  }

  private static void printOid(PgConnection conn) throws SQLException {
    final TypeInfo typeInfo = conn.getTypeInfo();
    int intOid1 = typeInfo.longOidToInt(3686786606L);
    System.out.println(intOid1);

    int intOid = typeInfo.longOidToInt(3686786612L);
    long longOid = typeInfo.intOidToLong(-608180684);
    System.out.println(intOid);
    System.out.println(longOid);

    int intOid111 = typeInfo.longOidToInt(300L);
    System.out.println(intOid111);
  }
}
