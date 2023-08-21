package com.enhui.mppdb;

import com.enhui.NodeService;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.replication.LogSequenceNumber;
import org.postgresql.replication.PGReplicationStream;
import org.postgresql.replication.fluent.logical.ChainedLogicalStreamBuilder;

public class SlotReadMain {

  private static PgConnection soltConn = null;
  private static PgConnection conn = null;

  String slotName = "replication_slot_teradata";
  String database = "teradata_base";

  @Test
  public void createSolt() throws SQLException {
    soltConn
        .getReplicationAPI()
        .createReplicationSlot()
        .logical()
        .withSlotName(slotName) // 这里字符串如包含大写字母则会自动转化为小写字母
        .withOutputPlugin("mppdb_decoding")
        .make();
  }

  @Test
  public void dropSolt() throws SQLException {
    soltConn.getReplicationAPI().dropReplicationSlot(slotName);
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
            .withSlotName(slotName)
            //            .withStartPosition(startLsn)
            .withSlotOption("include-xids", true)
            .withSlotOption("include-timestamp", true)
            .withSlotOption("skip-empty-xacts", true)
            .withSlotOption(
                "white-table-list",
                "public.heh_oracle_all_type1,public.heh_mysql_all_type1,public.heh_teradata_all_type1,public.heh_pg_all_type1") // 白名单列表
            .withSlotOption("standby-connection", false); // 强制备机解码

    // 是否支持并行解析
    int parallerNum;
    parallerNum = 3;
    //    parallerNum = 1;
    if (parallerNum > 1) {
      streamBuilder
          .withSlotOption("decode-style", "b")
          // 解码线程并发度
          .withSlotOption("parallel-decode-num", parallerNum)
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

      if (parallerNum == 1) {
        System.out.println("非并行解析");
        LogSequenceNumber lastReceiveLsn = stream.getLastReceiveLSN();
        NonRecursiveHelper helper =
            new NonRecursiveHelper(
                true, null, startLsn.asLong(), lastReceiveLsn.asLong(), byteBuffer);
        while (helper != null && helper.isContinue()) {
          helper =
              defaultMppdbDecoder.processMessage(
                  typeNameOidMap, helper.getLastReceiveLsn(), helper.getByteBuffer());
        }
      } else {
        System.out.println("并行解析");
        LogSequenceNumber lastReceiveLsn = stream.getLastReceiveLSN();
        NonRecursiveHelper helper =
            new NonRecursiveHelper(
                true, null, startLsn.asLong(), lastReceiveLsn.asLong(), byteBuffer);
        while (helper != null && helper.isContinue()) {
          helper =
              mppdbDecoder.processMessage(
                  typeNameOidMap,
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
    try {
      conn = NodeService.getSlaveConn(database);
      soltConn = NodeService.getSlaveSoltConn(database);
      System.out.println("connection success!");
      initTypeOidMap();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  Map<Integer, String> typeNameOidMap = new HashMap<>();

  private void initTypeOidMap() {
    try (Statement statement = conn.createStatement();
        ResultSet rs =
            statement.executeQuery(
                "SELECT t.oid AS oid, t.typname AS name\n"
                    + "FROM pg_catalog.pg_type t\n"
                    + "         JOIN\n"
                    + "     pg_catalog.pg_namespace n ON (t.typnamespace = n.oid)\n"
                    + "WHERE n.nspname != 'pg_toast'\n"
                    + "  AND (t.typrelid = 0 OR (SELECT c.relkind = 'c'\n"
                    + "                          FROM pg_catalog.pg_class c\n"
                    + "                          WHERE c.oid = t.typrelid));")) {
      while (rs.next()) {
        typeNameOidMap.put(rs.getInt("oid"), rs.getString("name"));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
