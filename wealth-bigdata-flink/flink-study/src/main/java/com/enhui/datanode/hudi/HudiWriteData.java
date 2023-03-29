package com.enhui.datanode.hudi;

import com.enhui.datanode.hudi.table.HudiTable1;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.hudi.util.HoodiePipeline;

import java.util.List;

public class HudiWriteData {

    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "hdfs");
        // 直接在resource下放一个hdfs-site.xml，就会被加载，如何在程序中设置，还不会
        // configuration.set("dfs.client.use.datanode.hostname", "true"); 使用主机名通信，否则本地无法连接内网ip
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        List<RowData> dataList = HudiTable1.getDataList();
        HoodiePipeline.Builder builder = HudiTable1.getBuilder(false);
        DataStream<RowData> dataStream = env.fromCollection(dataList);

        builder.sink(dataStream, false); // The second parameter indicating whether the input data stream is bounded
        env.execute("hudi-sink");
    }
}
