package com.enhui.datanode.hudi.table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.StringData;
import org.apache.flink.table.data.TimestampData;
import org.apache.hudi.common.model.HoodieTableType;
import org.apache.hudi.configuration.FlinkOptions;
import org.apache.hudi.util.HoodiePipeline;

public class HudiTable1 {
  static final String hdfsRootPath = "hdfs://cdh1:8020/user/heh/hudi/";
  static String targetTable = "hudi_table1";
  static String basePath = hdfsRootPath + targetTable;
  static String tableType = HoodieTableType.MERGE_ON_READ.name();

  public static HoodiePipeline.Builder getBuilder(boolean isRead) {
    Map<String, String> options = new HashMap<>();
    options.put(FlinkOptions.PATH.key(), basePath);
    options.put(FlinkOptions.TABLE_TYPE.key(), tableType);

    if (isRead) {
      options.put(
          FlinkOptions.READ_AS_STREAMING.key(), "false"); // this option enable the streaming read
      /**
       * 1) Snapshot mode (obtain latest view, based on row & columnar data)<br>
       * 2) incremental mode (new data since an instantTime)<br>
       * 3) Read Optimized mode (obtain latest view, based on columnar data)<br>
       * Default: snapshot
       */
      String queryType = FlinkOptions.QUERY_TYPE_SNAPSHOT;
      options.put(FlinkOptions.QUERY_TYPE.key(), queryType);
      if (queryType.equals(FlinkOptions.QUERY_TYPE_READ_OPTIMIZED)) {
        throw new RuntimeException("不能使用此种查询方式，会报错At least one file path must be specified.");
      }
    } else {
      options.put(FlinkOptions.PRECOMBINE_FIELD.key(), "ts");
    }

    HoodiePipeline.Builder builder =
        HoodiePipeline.builder(targetTable)
            .column("uuid VARCHAR(20)")
            .column("name VARCHAR(10)")
            .column("age INT")
            .column("ts TIMESTAMP(3)")
            .column("`partition` VARCHAR(20)")
            .pk("uuid")
            .partition("partition")
            .options(options);
    return builder;
  }

  public static List<RowData> getDataList() {
    List<RowData> list = new ArrayList<>();
    GenericRowData rowData = new GenericRowData(5);
    rowData.setField(0, StringData.fromString("uuid_value1"));
    rowData.setField(1, StringData.fromString("name_value1"));
    rowData.setField(2, 11);
    rowData.setField(3, TimestampData.fromLocalDateTime(LocalDateTime.now()));
    rowData.setField(4, StringData.fromString("partition_value"));
    list.add(rowData);
    return list;
  }
}
