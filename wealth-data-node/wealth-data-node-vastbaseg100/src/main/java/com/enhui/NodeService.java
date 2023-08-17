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

  // root/Datapipeline123     vastbase/Vb@admin123

  // 主库：49.232.250.134（192.168.32.2）
  private static String masterIp = "49.232.250.134";

  // 备库：62.234.26.175（192.168.32.12）
  private static String slaveIp = "62.234.26.175";

  public static PgConnection getMasterSslConn(String database)
      throws ClassNotFoundException, SQLException {
    return getConn(
        masterIp,
        singlePort,
        "certtest",
        "Test@123",
        database == null ? "vastbase" : database,
        false,
        true);
  }

  public static PgConnection getMasterConn(String database)
      throws ClassNotFoundException, SQLException {
    return getConn(
        masterIp, singlePort, "tpcc", "tpcc@123", database == null ? "vastbase" : database, false);
  }

  public static PgConnection getMasterSoltConn(String database)
      throws ClassNotFoundException, SQLException {
    return getConn(
        masterIp,
        singleSoltPort,
        "tpcc",
        "tpcc@123",
        database == null ? "vastbase" : database,
        true);
  }

  public static PgConnection getSlaveConn(String database)
      throws ClassNotFoundException, SQLException {
    return getConn(
        slaveIp, singlePort, "tpcc", "tpcc@123", database == null ? "vastbase" : database, false);
  }

  public static PgConnection getSlaveSoltConn(String database)
      throws ClassNotFoundException, SQLException {
    return getConn(
        slaveIp,
        singleSoltPort,
        "tpcc",
        "tpcc@123",
        database == null ? "vastbase" : database,
        true);
  }

  public static PgConnection getSingleConn(String database)
      throws ClassNotFoundException, SQLException {
    return getConn(
        singleIp,
        singlePort,
        "dp_test",
        "Datapipeline123",
        database == null ? "dp_test" : database,
        false);
  }

  public static PgConnection getSingleSoltConn(String database)
      throws ClassNotFoundException, SQLException {
    return getConn(
        singleIp,
        singleSoltPort,
        "dp_test",
        "Datapipeline123",
        database == null ? "dp_test" : database,
        true);
  }

  public static PgConnection getConn(
      String ip, int port, String username, String password, String database, boolean solt)
      throws ClassNotFoundException, SQLException {
    return getConn(ip, port, username, password, database, solt, false);
  }

  public static PgConnection getConn(
      String ip,
      int port,
      String username,
      String password,
      String database,
      boolean solt,
      boolean ssl)
      throws ClassNotFoundException, SQLException {
    String url = String.format("jdbc:postgresql://%s:%s/%s", ip, port, database);

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
    if (ssl) {
      properties.setProperty("ssl", "true");
      final String sslcert = "/Users/huenhui/Documents/vastbase g100/ssl/client.crt";
      final String sslkey = "/Users/huenhui/Documents/vastbase g100/ssl/client.key.pk8";
      final String sslrootcert = "/Users/huenhui/Documents/vastbase g100/ssl/cacert.pem";
      // require、verify-ca、verify-full;
      final String sslmode = "verify-ca";
      properties.setProperty("sslmode", sslmode);
      properties.setProperty("sslcert", sslcert);
      properties.setProperty("sslkey", sslkey);
      properties.setProperty("sslrootcert", sslrootcert);
    }
    return (PgConnection) DriverManager.getConnection(url, properties);
  }

  public static PgConnection getSslConn(
      String ip, int port, String username, String password, String database, boolean solt)
      throws ClassNotFoundException, SQLException {
    String url = String.format("jdbc:postgresql://%s:%s/%s", ip, port, database);

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
