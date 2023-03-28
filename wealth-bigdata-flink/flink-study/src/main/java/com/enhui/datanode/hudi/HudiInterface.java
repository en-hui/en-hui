package com.enhui.datanode.hudi;

import java.util.HashMap;
import java.util.Map;
import org.apache.hudi.common.model.HoodieTableType;
import org.apache.hudi.configuration.FlinkOptions;
import org.apache.hudi.util.HoodiePipeline;

public interface HudiInterface {
  String targetTable = "hudi_table1";
  String basePath = "hdfs://cdh1:8020/user/heh/hudi/" + targetTable;
  String tableType = HoodieTableType.MERGE_ON_READ.name();

  static HoodiePipeline.Builder getBuilder() {
    Map<String, String> options = new HashMap<>();
    options.put(FlinkOptions.PATH.key(), basePath);
    options.put(FlinkOptions.TABLE_TYPE.key(), tableType);
    options.put(
        FlinkOptions.READ_AS_STREAMING.key(), "true"); // this option enable the streaming read

      options.put(FlinkOptions.PRECOMBINE_FIELD.key(), "ts");
    //    options.put(
    //        FlinkOptions.READ_START_COMMIT.key(),
    //        "'20210316134557'"); // specifies the start commit instant time

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
}
