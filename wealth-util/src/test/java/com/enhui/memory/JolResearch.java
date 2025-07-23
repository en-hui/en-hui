package com.enhui.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * 使用jol做对象内存计算
 */
public class JolResearch {

  /** 上下文不常更新 */
  @Data
  public static class TaskContext {
    private final Map<String, ?> connectorPartition;
    private final String topic = "v2_dptask_40.dp_test.public.perf512.1719";
    private final String tableName = "dp_test.public.perf512";
    private final Integer partition = 0;
    private final Schema keySchema;
    private final Schema valueSchema;

    public TaskContext() {
      connectorPartition = initConnectorPartition();
      keySchema =
          SchemaBuilder.struct()
              .name(topic + "_key")
              .version(1)
              .optional()
              .field("id", Schema.INT32_SCHEMA)
              .build();
      valueSchema =
          SchemaBuilder.struct()
              .name(tableName)
              .field(
                  "after",
                  SchemaBuilder.struct()
                      .name("after")
                      .version(1)
                      .optional()
                      .field("id", Schema.INT32_SCHEMA)
                      .field("col1", Schema.STRING_SCHEMA)
                      .build())
              .field(
                  "before",
                  SchemaBuilder.struct()
                      .name("before")
                      .version(1)
                      .optional()
                      .field("id", Schema.INT32_SCHEMA)
                      .build())
              .field(
                  "source",
                  SchemaBuilder.struct()
                      .name("source")
                      .field("database", Schema.OPTIONAL_STRING_SCHEMA)
                      .field("schema", Schema.OPTIONAL_STRING_SCHEMA)
                      .field("entity", Schema.STRING_SCHEMA)
                      .build());
    }

    private Map<String, ?> initConnectorPartition() {
      Map<String, String> partition = new HashMap<>(1, 1);
      partition.put("table", String.format("%s.%s", 40, "dp_test.public.perf512"));
      return partition;
    }
  }

  @Test
  public void testJol() {
    TaskContext taskContext = new TaskContext();

    List<SourceRecord> poll = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Struct keyStruct = new Struct(taskContext.getKeySchema());
      keyStruct.put("id", i);

      Struct valueStruct = new Struct(taskContext.getValueSchema());
      Struct beforeStruct = new Struct(taskContext.getValueSchema().field("before").schema());
      beforeStruct.put("id", i);
      Struct afterStruct = new Struct(taskContext.getValueSchema().field("after").schema());
      afterStruct.put("id", i);
      afterStruct.put("col1", "i am col1, value = " + i);
      Struct sourceStruct = new Struct(taskContext.getValueSchema().field("source").schema());
      sourceStruct.put("entity", taskContext.getTableName());

      valueStruct.put("before", beforeStruct);
      valueStruct.put("after", afterStruct);
      valueStruct.put("source", sourceStruct);
      SourceRecord sourceRecord =
          new SourceRecord(
              taskContext.getConnectorPartition(),
              getOffset(),
              taskContext.getTopic(),
              taskContext.getPartition(),
              keyStruct.schema(),
              keyStruct,
              valueStruct.schema(),
              valueStruct);

      poll.add(sourceRecord);
    }

    for (SourceRecord sourceRecord : poll) {
      System.out.println(ClassLayout.parseInstance(sourceRecord).toPrintable());
    }
  }

  Map<String, Object> getOffset() {
    Map<String, Object> offset = new HashMap();
    offset.put("ts_sec", 1000L);
    offset.put("size", 1000L);
    offset.put("idx", 1000L);
    offset.put("total_c", 1000L);
    offset.put("sync_stage", "SNAPSHOTTING");
    return offset;
  }
}
