package com.enhui.write;

import com.microsoft.sqlserver.jdbc.SQLServerResultSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;

public class TestCdc {

  @Test
  public void testCdcRead() throws SQLException {
    SqlServerClient client = SqlServerClient.getInstance();

    long methodStart = System.currentTimeMillis();
    Statement statement = null;
    try (final Connection connection = client.getConnection()) {
      connection.setAutoCommit(false);
      statement =
          connection.createStatement(
              SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
      statement.setFetchSize(100);
      // -- 从上次读到的lsn开始 查询操作类型、lsn 以及 数据 : where __$start_lsn > 0x0000258D000001580005 AND
      // -- __$operation：(主键变更是D+I)
      String sql =
          "SELECT __$operation, __$start_lsn,id,col1,col2 FROM"
              + " [heh_cdc].[cdc].[test_cdc_instance_CT]  WHERE __$start_lsn >"
              + " 0x000000DB000063600008 AND __$operation != 3 ORDER BY __$start_lsn ASC;";

      final long sqlStart = System.currentTimeMillis();
      final ResultSet rst = statement.executeQuery(sql);
      final long sqlExecuteTime = System.currentTimeMillis() - sqlStart;
      System.out.println("sql execute cost time: " + sqlExecuteTime + " ms");

      long lastNextTime = System.currentTimeMillis();
      int index = 1;
      while (rst.next()) {
        byte[] recordLsn = rst.getBytes("__$start_lsn");
        final String lsn = unsignedBigEndianBytesToUnsignedHexString(recordLsn);
        String type;
        switch (rst.getInt("__$operation")) {
          case 1:
            type = "DELETE";
            break;
          case 2:
            type = "INSERT";
            break;
          case 4:
            type = "UPDATE";
            break;
          default:
            type = "DUMMY";
        }
        long currentNextTime = System.currentTimeMillis();
        final long cost = currentNextTime - lastNextTime;
        if (cost >= 0.8 * sqlExecuteTime) {
          System.out.println(
              "next cost time: "
                  + cost
                  + " ms "
                  + "type: "
                  + type
                  + ", lsn: "
                  + lsn
                  + ",index: "
                  + index);
        }
        lastNextTime = currentNextTime;
        index++;
      }
      System.out.println("共处理数据条数： " + index);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (statement != null) {
        statement.close();
      }
      if (client != null) {
        client.close();
      }
    }

    long methodEnd = System.currentTimeMillis();
    final long cost = methodEnd - methodStart;
    System.out.println("method cost time: " + cost + " ms");
  }

  public static String unsignedBigEndianBytesToUnsignedHexString(byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      return "0";
    }
    // 0 -> 48
    // A -> 65
    StringBuilder sb = new StringBuilder("0x");
    int temp;
    for (int i = 0; i >>> 1 < bytes.length; i++) {
      byte data = bytes[i >>> 1];
      if ((i & 1) == 0) {
        temp = (data & 0xF0) >>> 4;
      } else {
        temp = data & 0XF;
      }
      if (temp > 9) {
        sb.append((char) (temp + 55));
      } else {
        sb.append((char) (temp + 48));
      }
    }
    return sb.toString();
  }
}
