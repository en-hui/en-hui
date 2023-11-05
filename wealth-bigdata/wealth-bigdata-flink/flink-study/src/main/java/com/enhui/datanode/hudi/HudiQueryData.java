package com.enhui.datanode.hudi;

import com.enhui.datanode.hudi.table.HudiTable1;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;

public class HudiQueryData {
  public static void main(String[] args) throws Exception {
    System.setProperty("HADOOP_USER_NAME", "hdfs");
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStream<RowData> rowDataDataStream = HudiTable1.getBuilder(true).source(env);
    rowDataDataStream.print("查询到的数据");
    env.execute("hudi-source");
  }
}
