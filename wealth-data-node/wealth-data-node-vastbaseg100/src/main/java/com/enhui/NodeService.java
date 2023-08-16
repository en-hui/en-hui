package com.enhui;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.postgresql.PGProperty;
import org.postgresql.jdbc.PgConnection;

public class NodeService {
  private static String driver = "org.postgresql.Driver";
  private static String singleIp = "152.136.37.221";
  private static int singlePort = 5432;
  private static int singleSoltPort = 5433;

  public static PgConnection getSingleConn() throws ClassNotFoundException, SQLException {
    return getConn(singleIp, singlePort, "dp_test", "Datapipeline123", false);
  }

  public static PgConnection getSingleSoltConn() throws ClassNotFoundException, SQLException {
    return getConn(singleIp, singleSoltPort, "dp_test", "Datapipeline123", true);
  }

  public static PgConnection getConn(
      String ip, int port, String username, String password, boolean solt)
      throws ClassNotFoundException, SQLException {
    String url = String.format("jdbc:postgresql://%s:%s/dp_test", ip, port);

    Class.forName(driver);
    Properties properties = new Properties();
    PGProperty.USER.set(properties, username);
    PGProperty.PASSWORD.set(properties, password);
    if (solt) {
      // 对于逻辑复制，以下三个属性是必须配置项
      PGProperty.ASSUME_MIN_SERVER_VERSION.set(properties, "9.4");
      PGProperty.REPLICATION.set(properties, "database");
      PGProperty.PREFER_QUERY_MODE.set(properties, "simple");
    }
    return (PgConnection) DriverManager.getConnection(url, properties);
  }
}
