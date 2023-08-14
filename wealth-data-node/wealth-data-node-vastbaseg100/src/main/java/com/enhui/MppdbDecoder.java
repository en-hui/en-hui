package com.enhui;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class MppdbDecoder {

  DateTimeFormatter TIMESTAMP_FORMAT =
      new DateTimeFormatterBuilder()
          .parseCaseInsensitive()
          .append(ISO_LOCAL_DATE)
          .appendLiteral(' ')
          .append(ISO_LOCAL_TIME)
          .optionalStart()
          .appendFraction(MILLI_OF_SECOND, 0, 6, true)
          .optionalStart()
          .appendOffset("+HH", "+00")
          .toFormatter();

  public NonRecursiveHelper processMessage(
      Long startLsn, String commitTime, long lastReceiveLsn, ByteBuffer byteBuffer) {
    try {
      if (!byteBuffer.hasArray()) {
        throw new IllegalStateException(
            "Invalid buffer received from GaussDB server during streaming replication");
      }
      int metaLength = byteBuffer.getInt();
      if (metaLength == 0) {
        return null;
      }
      long lsn = byteBuffer.getLong();
      String type = new String(new byte[] {byteBuffer.get()});
      System.out.println("mppdb handle type : {}" + type);
      switch (type) {
          // begin
        case "B":
          {
            long csn = byteBuffer.getLong();
            long firstLsn = byteBuffer.getLong();
            String flag = getFlag(byteBuffer);
            if (tsFlag(flag)) {
              // commit时间作为本次事务所有消息的ts_sec
              commitTime = ts(byteBuffer);
              flag = getFlag(byteBuffer);
            }
            return new NonRecursiveHelper(
                hasMoreFlag(flag), commitTime, startLsn, lastReceiveLsn, byteBuffer);
          }
          // commit
        case "C":
          {
            String flag = getFlag(byteBuffer);
            long xid = 0;
            String ts = null;
            if ("X".equals(flag)) {
              xid = byteBuffer.getLong();
              flag = getFlag(byteBuffer);
            }
            if (tsFlag(flag)) {
              ts = ts(byteBuffer);
              flag = getFlag(byteBuffer);
            }
            return new NonRecursiveHelper(
                hasMoreFlag(flag), commitTime, startLsn, lastReceiveLsn, byteBuffer);
          }
        case "I":
        case "U":
        case "D":
          {
            final String schemaName = schema(byteBuffer);
            final String tableName = tableName(byteBuffer);
            StringBuilder builder = new StringBuilder();
            builder
                .append("schame:")
                .append(schemaName)
                .append("tableName:")
                .append(tableName)
                .append("lsn:")
                .append(lsn)
                .append("type:")
                .append(type);

            if (commitTime != null) {
              // 使用B事件中拿到的时间作为kafka中每个消息的ts_sec
              builder
                  .append("commitTime:")
                  .append(Instant.from(TIMESTAMP_FORMAT.parse(commitTime)).toEpochMilli());
            }

            System.out.println(builder);
            String flag = getFlag(byteBuffer);
            if ("N".equals(flag)) {
              parseRecord(byteBuffer, builder, false);
              flag = getFlag(byteBuffer);
            }
            if ("O".equals(flag)) {
              parseRecord(byteBuffer, builder, true);
              flag = getFlag(byteBuffer);
            }
            return new NonRecursiveHelper(
                hasMoreFlag(flag), commitTime, startLsn, lastReceiveLsn, byteBuffer);
          }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  private void parseRecord(ByteBuffer byteBuffer, StringBuilder builder, boolean before)
      throws Exception {
    int attrnum = byteBuffer.getShort();
    for (int i = 0; i < attrnum; i++) {
      int colLength = byteBuffer.getShort();
      byte[] colBytes = new byte[colLength];
      byteBuffer.get(colBytes);
      String colName = new String(colBytes);
      int oid = byteBuffer.getInt();
      byte[] value = null;
      int valueLength = byteBuffer.getInt();
      if (valueLength == 0XFFFFFFFF) {
        value = null;
      } else if (valueLength == 0) {
        value = new byte[0];
      } else {
        byte[] valueBytes = new byte[valueLength];
        byteBuffer.get(valueBytes);
        value = valueBytes;
      }
      System.out.println(colName + "--" + oid + "--" + value);
    }
  }

  private String getFlag(ByteBuffer byteBuffer) {
    return new String(new byte[] {byteBuffer.get()});
  }

  private boolean hasMoreFlag(String flag) {
    if ("P".equals(flag)) {
      return true;
    } else if ("F".equals(flag)) {
      return false;
    } else {
      throw new IllegalArgumentException();
    }
  }

  private boolean tsFlag(String flag) {
    return "T".equals(flag);
  }

  private String schema(ByteBuffer byteBuffer) {
    int schemaLength = byteBuffer.getShort();
    byte[] schemaBytes = new byte[schemaLength];
    byteBuffer.get(schemaBytes);
    return new String(schemaBytes);
  }

  private String tableName(ByteBuffer byteBuffer) {
    int tableLength = byteBuffer.getShort();
    byte[] tableBytes = new byte[tableLength];
    byteBuffer.get(tableBytes);
    return new String(tableBytes);
  }

  private String ts(ByteBuffer byteBuffer) {
    int tsLength = byteBuffer.getInt();
    byte[] tsBytes = new byte[tsLength];
    byteBuffer.get(tsBytes);
    return new String(tsBytes);
  }
}
