package com.enhui;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 流处理的api，读取socket中的数据，统计word count <br>
 * nc -lk 9999 开启一个socket监听服务，然后输入用逗号隔开的数据
 */
public class MSB04_SocketWordCount {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 读取socket数据 hello,flink nc -lk 9999
        DataStreamSource<String> dataStreamSource = env.socketTextStream("localhost", 9999);

        SingleOutputStreamOperator<Tuple2<String, Long>> kvStream = dataStreamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Long>> collector) throws Exception {
                String[] arr = line.split(",");
                for (String word : arr) {
                    collector.collect(Tuple2.of(word, 1L));
                }
            }
        });

        kvStream.keyBy(tuple2 -> tuple2.f0).sum(1).print();

        env.execute();
    }
}
