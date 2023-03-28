package com.enhui.datanode.hudi.table;

import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.StringData;
import org.apache.flink.table.data.TimestampData;
import org.apache.hudi.common.model.HoodieTableType;
import org.apache.hudi.configuration.FlinkOptions;
import org.apache.hudi.util.HoodiePipeline;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HudiTable1 {
    static final String hdfsRootPath = "hdfs://cdh1:8020/user/heh/hudi/";
    static String targetTable = "hudi_table1";
    static String basePath = hdfsRootPath + targetTable;
    static String tableType = HoodieTableType.MERGE_ON_READ.name();

    public static HoodiePipeline.Builder getBuilder() {
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

    public static List<RowData> getDataList() {
        List<RowData> list = new ArrayList<>();
        GenericRowData rowData = new GenericRowData(5);
        rowData.setField(0, StringData.fromString("uuid_value"));
        rowData.setField(1, StringData.fromString("name_value"));
        rowData.setField(2, 1);
        rowData.setField(3, TimestampData.fromLocalDateTime(LocalDateTime.now()));
        rowData.setField(4, StringData.fromString("partition_value"));
        list.add(rowData);
        return list;
    }
}
