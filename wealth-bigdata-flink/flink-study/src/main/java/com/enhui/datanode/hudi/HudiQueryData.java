package com.enhui.datanode.hudi;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;

public class HudiQueryData implements HudiInterface {
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    DataStream<RowData> rowDataDataStream = HudiInterface.getBuilder().source(env);
    rowDataDataStream.print();
    env.execute("Api_Source");
  }
}
