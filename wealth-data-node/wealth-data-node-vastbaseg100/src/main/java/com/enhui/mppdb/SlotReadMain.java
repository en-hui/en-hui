package com.enhui.mppdb;

import java.nio.ByteBuffer;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.PGProperty;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.replication.LogSequenceNumber;
import org.postgresql.replication.PGReplicationStream;
import org.postgresql.replication.fluent.logical.ChainedLogicalStreamBuilder;

public class SlotReadMain {

  private static PgConnection soltConn = null;
  private static PgConnection conn = null;

  @Test
  public void createSolt() throws SQLException {
    soltConn
        .getReplicationAPI()
        .createReplicationSlot()
        .logical()
        .withSlotName("replication_slot") // 这里字符串如包含大写字母则会自动转化为小写字母
        .withOutputPlugin("mppdb_decoding")
        .make();
  }

  @Test
  public void dropSolt() throws SQLException {
    soltConn.getReplicationAPI().dropReplicationSlot("replication_slot");
  }

  @Test
  public void showSlot() throws SQLException {
    final Statement statement = conn.createStatement();
    final ResultSet resultSet = statement.executeQuery("select * from pg_replication_slots;");
    while (resultSet.next()) {
      final ResultSetMetaData metaData = resultSet.getMetaData();
      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        System.out.print(metaData.getColumnName(i) + ": " + resultSet.getString(i) + ", ");
      }
      System.out.println();
    }
  }

  @Test
  public void readSolt() throws SQLException, InterruptedException {
    LogSequenceNumber startLsn = LogSequenceNumber.valueOf("0/1314A758");
    ChainedLogicalStreamBuilder streamBuilder =
        soltConn
            .getReplicationAPI()
            .replicationStream()
            .logical()
            .withSlotName("replication_slot")
            .withStartPosition(startLsn)
            .withSlotOption("include-xids", true)
            .withSlotOption("include-timestamp", true)
            .withSlotOption("skip-empty-xacts", true)
            .withSlotOption("white-table-list", "public.cqtest") // 白名单列表
            .withSlotOption("standby-connection", false); // 强制备机解码

    // 是否支持并行解析
    boolean isParaller = true;
//    boolean isParaller = false;
    if (isParaller) {
      streamBuilder
          .withSlotOption("decode-style", "b")
          // 解码线程并发度
          .withSlotOption("parallel-decode-num", 3)
          // 批量发送解码结果
          .withSlotOption("sending-batch", 1);
    }

    PGReplicationStream stream = streamBuilder.start();
    final MppdbDecoder mppdbDecoder = new MppdbDecoder();
    final DefaultMessageDecoder defaultMppdbDecoder = new DefaultMessageDecoder();
    while (true) {
      ByteBuffer byteBuffer = stream.readPending();
      if (byteBuffer == null) {
        continue;
      }

      if (!isParaller) {
        LogSequenceNumber lastReceiveLsn = stream.getLastReceiveLSN();
        NonRecursiveHelper helper =
            new NonRecursiveHelper(
                true, null, startLsn.asLong(), lastReceiveLsn.asLong(), byteBuffer);
        while (helper != null && helper.isContinue()) {
          helper =
              defaultMppdbDecoder.processMessage(
                  helper.getLastReceiveLsn(), helper.getByteBuffer());
        }
      } else {
        LogSequenceNumber lastReceiveLsn = stream.getLastReceiveLSN();
        NonRecursiveHelper helper =
            new NonRecursiveHelper(
                true, null, startLsn.asLong(), lastReceiveLsn.asLong(), byteBuffer);
        while (helper.isContinue()) {
          helper =
              mppdbDecoder.processMessage(
                  helper.getStartLsn(),
                  helper.getCommitTime(),
                  helper.getLastReceiveLsn(),
                  helper.getByteBuffer());
        }
      }
    }
  }

  @BeforeEach
  public void before() {
    String driver = "org.postgresql.Driver";
    // 此处配置数据库IP以及端口，这里的端口为haPort，通常默认是所连接DN的port+1端口
    String masterIp = "152.136.37.221";
    int port = 5432;
    int soltPort = 5433;
    String url = String.format("jdbc:postgresql://%s:%s/dp_test", masterIp, port);
    String soltUrl = String.format("jdbc:postgresql://%s:%s/dp_test", masterIp, soltPort);

    try {
      Class.forName(driver);
      Properties properties = new Properties();
      PGProperty.USER.set(properties, "dp_test");
      PGProperty.PASSWORD.set(properties, "Datapipeline123");
      conn = (PgConnection) DriverManager.getConnection(url, properties);
      // 对于逻辑复制，以下三个属性是必须配置项
      PGProperty.ASSUME_MIN_SERVER_VERSION.set(properties, "9.4");
      PGProperty.REPLICATION.set(properties, "database");
      PGProperty.PREFER_QUERY_MODE.set(properties, "simple");
      soltConn = (PgConnection) DriverManager.getConnection(soltUrl, properties);
      System.out.println("connection success!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
