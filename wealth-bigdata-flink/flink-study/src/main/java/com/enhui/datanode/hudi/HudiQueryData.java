package com.enhui.datanode.hudi;

import com.enhui.datanode.hudi.table.HudiTable1;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;

public class HudiQueryData {
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    DataStream<RowData> rowDataDataStream = HudiTable1.getBuilder().source(env);
    rowDataDataStream.print();
    env.execute("Api_Source");
  }
}
