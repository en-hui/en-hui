import com.enhui.NodeService;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.replication.LogSequenceNumber;
import org.postgresql.replication.PGReplicationStream;
import org.postgresql.replication.fluent.logical.ChainedLogicalStreamBuilder;

/**
 * https://github.com/en-hui/en-hui/issues/18
 */
public class TestOrderOfCreateSlotAndReadSnapshot {
  private static PgConnection slotConn = null;
  private static PgConnection conn = null;
  String slotName = "testdatamove";

  @BeforeEach
  public void before() {
    try {
      conn =
          NodeService.getConn(
              "49.232.214.94", 5432, "opengauss", "openGauss@123", "opengauss", false);
      slotConn =
          NodeService.getConn(
              "49.232.214.94", 5432, "opengauss", "openGauss@123", "opengauss", true);
      System.out.println("connection success!");
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void init() {
    try (Statement statement = conn.createStatement()) {
      statement.execute(
          "create table table_name\n"
              + "(\n"
              + "    pk1  int not null\n"
              + "        constraint table_name_pk\n"
              + "            primary key,\n"
              + "    col1 int,\n"
              + "    col2 int\n"
              + ");");
    } catch (SQLException e) {
      if (!e.getMessage().contains("already")) {
        e.printStackTrace();
      }
    }
    try (Statement statement = conn.createStatement()) {
      statement.execute("truncate table public.pk_demo;");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    try (Statement statement = conn.createStatement()) {
      statement.execute("INSERT INTO public.pk_demo (pk1, col1, col2) VALUES (1, 1, 1);");
    } catch (SQLException e) {
      if (!e.getMessage().contains("duplicate key")) {
        e.printStackTrace();
      }
    }
    try (Statement statement = conn.createStatement()) {
      statement.execute("select pg_drop_replication_slot('" + slotName + "');");
    } catch (SQLException e) {
      if (!e.getMessage().contains("does not exist")) {
        e.printStackTrace();
      }
    }
  }

  @AfterEach
  public void after() throws SQLException {
    if (conn != null) {
      conn.close();
    }
    if (slotConn != null) {
      slotConn.close();
    }
  }

  @Test
  public void testDataMove() throws SQLException, InterruptedException {
    // 1. 创建复制槽
    // 2. 查询最新点位
    // 3. select
    // 4. 继续点位读取

    // 是否打乱顺序
    boolean shuffleTheOrder = true;

    // TODO：打乱顺序，看是否有问题
    if (!shuffleTheOrder) {
      slotConn
              .getReplicationAPI()
              .createReplicationSlot()
              .logical()
              .withSlotName(slotName) // 这里字符串如包含大写字母则会自动转化为小写字母
              .withOutputPlugin("mppdb_decoding")
              .make();
    }

    LogSequenceNumber snapshotLsn = null;
    try (Statement statement = conn.createStatement()) {
      int majorVersion = conn.getMetaData().getDatabaseMajorVersion();
      String sql =
          majorVersion >= 10
              ? "select * from pg_current_wal_lsn()"
              : "select * from pg_current_xlog_location()";
      final ResultSet resultSet = statement.executeQuery(sql);
      if (resultSet.next()) {
        snapshotLsn = LogSequenceNumber.valueOf(resultSet.getString(1));
      }

      statement.execute("DELETE FROM public.pk_demo WHERE pk1 = 8;");
      statement.execute("INSERT INTO public.pk_demo (pk1, col1, col2) VALUES (8, 8, 8);");
      statement.execute("DELETE FROM public.pk_demo WHERE pk1 = 8;");
      statement.execute("INSERT INTO public.pk_demo (pk1, col1, col2) VALUES (8, 8, 8);");
    }
    System.out.println("snapshot lsn: " + snapshotLsn);

    try (Statement statement = conn.createStatement()) {
      final ResultSet resultSet = statement.executeQuery("select * from public.pk_demo;");
      final ResultSetMetaData metaData = resultSet.getMetaData();
      while (resultSet.next()) {
        StringBuilder col = new StringBuilder();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          col.append(metaData.getColumnName(i)).append("=").append(resultSet.getString(i)).append("; ");
        }
        System.out.println("col: " + col);
      }
    }

    // 此处数据会丢失
    try (Statement statement = conn.createStatement()) {
      statement.execute("DELETE FROM public.pk_demo WHERE pk1 = 9;");
      statement.execute("INSERT INTO public.pk_demo (pk1, col1, col2) VALUES (9, 9, 9);");
      statement.execute("DELETE FROM public.pk_demo WHERE pk1 = 9;");
      statement.execute("INSERT INTO public.pk_demo (pk1, col1, col2) VALUES (9, 9, 9);");
    }

    // TODO：打乱顺序，看是否有问题
    if (shuffleTheOrder) {
      slotConn
              .getReplicationAPI()
              .createReplicationSlot()
              .logical()
              .withSlotName(slotName) // 这里字符串如包含大写字母则会自动转化为小写字母
              .withOutputPlugin("mppdb_decoding")
              .make();
    }

    ChainedLogicalStreamBuilder streamBuilder =
        slotConn
            .getReplicationAPI()
            .replicationStream()
            .logical()
            .withSlotName(slotName)
            .withStartPosition(snapshotLsn)
            .withSlotOption("include-xids", true)
            .withSlotOption("include-timestamp", true)
            .withSlotOption("skip-empty-xacts", true)
            .withSlotOption("white-table-list", "public.pk_demo") // 白名单列表
            .withSlotOption("standby-connection", false); // 强制备机解码
    PGReplicationStream stream = streamBuilder.start();
    while (true) {
      ByteBuffer byteBuffer = stream.readPending();

      if (byteBuffer == null) {
        TimeUnit.MILLISECONDS.sleep(10L);
        continue;
      }

      int offset = byteBuffer.arrayOffset();
      byte[] source = byteBuffer.array();
      int length = source.length - offset;
      System.out.println(new String(source, offset, length));

      // 如果需要flush lsn，根据业务实际情况调用以下接口
      // LogSequenceNumber lastRecv = stream.getLastReceiveLSN();
      // stream.setFlushedLSN(lastRecv);
      // stream.forceUpdateStatus();

    }
  }
}
