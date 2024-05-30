package com.enhui.write;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;

public class TestLoadData {

  @Test
  public void testLoadData() {
    SqlServerClient client = SqlServerClient.getInstance();
    try (final Connection connection = client.getConnection();
        final Statement statement = connection.createStatement()) {

      for (int i = 0; i < 100 * 10000; i++) {
        statement.execute("UPDATE heh_cdc.dbo.test_cdc SET col1 = " + i);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      client.close();
    }
  }
}
