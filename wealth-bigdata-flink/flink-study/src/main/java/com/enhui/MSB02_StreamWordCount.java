package com.enhui;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class MSB02_StreamWordCount {
    public static void main(String[] args) throws Exception {
        // 1.准备flink环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2.读取文件数据
        DataStreamSource<String> lineDS = env.readTextFile("./wealth-bigdata-flink/data/word.txt");

        SingleOutputStreamOperator<Tuple2<String, Long>> kvWordDS = lineDS.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Long>> collector) throws Exception {
                String[] arr = line.split(" ");
                for (String word : arr) {
                    collector.collect(Tuple2.of(word, 1L));
                }
            }
        });

        kvWordDS.keyBy(new KeySelector<Tuple2<String, Long>, String>() {
            @Override
            public String getKey(Tuple2<String, Long> tp2) throws Exception {
                return tp2.f0;
            }
        }).sum(1).print();

        // 流处理必须进行触发
        env.execute();
    }
}
