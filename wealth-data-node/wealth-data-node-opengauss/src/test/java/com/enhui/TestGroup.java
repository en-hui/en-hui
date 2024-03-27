package com.enhui;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.PGProperty;
import org.postgresql.jdbc.PgConnection;

public class TestGroup {

  private static final String driver = "org.postgresql.Driver";
  private static final String ip = "dev_huenhui";
  private static final int port = 5432;
  private static final String database = "opengauss";
  private static PgConnection conn = null;

  @BeforeAll
  public static void before() throws ClassNotFoundException, SQLException {
    Class.forName(driver);
    Properties properties = new Properties();
    PGProperty.USER.set(properties, "opengauss");
    PGProperty.PASSWORD.set(properties, "openGauss@123");
    String sourceURL = String.format("jdbc:postgresql://%s:%s/%s", ip, port, database);
    conn = (PgConnection) DriverManager.getConnection(sourceURL, properties);
  }

  @AfterAll
  public static void after() throws SQLException {
    if (conn != null) {
      conn.close();
    }
  }

  @Test
  public void testBatchDelete() {
    try (final Statement statement = conn.createStatement()) {
      statement.execute("delete from test_group;");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testBatchUpdate() {
    final Random random = new Random();
    try (final Statement statement = conn.createStatement()) {
      final int num = random.nextInt();
      System.out.println(num);
      statement.execute("update test_group set pk1 = " + num + ";");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testUpdateOne() {
    int before = 1;
    int after = 5555;
    String updateSql = "update test_group set pk2 = ? where pk2 = ?";
    try (final PreparedStatement pst = conn.prepareStatement(updateSql)) {
      for (int i = 1; i < 5000; i++) {

        pst.setInt(1, after);
        pst.setInt(2, before);
        System.out.println(before + "--" + after);
        pst.addBatch();
        before = after;
        after = before + 1;
      }
      after = 1;
      pst.setInt(1, after);
      pst.setInt(2, before);
      System.out.println(before + "--" + after);
      pst.addBatch();
      pst.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testBatchInsert() {
    String insertSql =
        String.format(
            "INSERT INTO public.test_group (pk1, pk2, uk1_col1, uk1_col2, uk2_col1, uk3_col1, col1,"
                + " col2) VALUES (%s);",
            String.join(",", Collections.nCopies(8, "?")));

    try (final PreparedStatement pst = conn.prepareStatement(insertSql)) {
      for (int i = 1; i <= 5000; i++) {
        pst.setInt(1, i);
        pst.setInt(2, i);
        pst.setInt(3, i);
        pst.setInt(4, i);
        pst.setString(5, "uk2_col1_" + i);
        pst.setString(6, "uk3_col1_" + i);
        pst.setInt(7, i);
        pst.setInt(8, i);
        pst.addBatch();
      }
      pst.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testDorpTable() {
    try (final Statement statement = conn.createStatement()) {
      statement.execute("drop table test_group;");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testCreateTable() {
    try (final Statement statement = conn.createStatement()) {

      statement.addBatch(
          "create table test_group\n"
              + "(\n"
              + "    pk1      int not null,\n"
              + "    pk2      int not null\n"
              + "        constraint test_group_pk\n"
              + "            primary key,\n"
              + "    uk1_col1 int,\n"
              + "    uk1_col2 int,\n"
              + "    uk2_col1 varchar,\n"
              + "    uk3_col1 varchar,\n"
              + "    col1     int,\n"
              + "    col2     int\n"
              + ");");

      statement.addBatch(
          "create unique index test_group_uk1_col1_col2_uindex on test_group (uk1_col1,"
              + " uk1_col2);");
      statement.addBatch(
          "create unique index test_group_uk2_col1_uindex on test_group (uk2_col1);");
      statement.addBatch(
          "create unique index test_group_uk3_col1_uindex on test_group (uk3_col1);");

      statement.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
