package com.enhui.datanode.hudi;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;

public class HudiWriteData implements HudiInterface {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    List<RowData> list = new ArrayList<>();

    DataStream<RowData> dataStream = env.fromCollection(list);

    HudiInterface.getBuilder()
        .sink(
            dataStream,
            false); // The second parameter indicating whether the input data stream is bounded
    env.execute("Api_Sink");
  }
}
