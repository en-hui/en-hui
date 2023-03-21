package com.enhui;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class MSB01_BatchWordCount {

    public static void main(String[] args) throws Exception {
        // 1.准备flink环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 2.读取文件数据
        DataSource<String> lineDS = env.readTextFile("./wealth-bigdata-flink/data/word.txt");

        // 3.对数据进行切分单词
        FlatMapOperator<String, Tuple2<String, Long>> kvWordDS = lineDS.flatMap((String line, Collector<Tuple2<String,Long>> collector) -> {
            String[] arr = line.split(" ");
            for (String word : arr) {
                collector.collect(Tuple2.of(word,1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        // 4.数据分组
        kvWordDS.groupBy(0).sum(1).print();

    }
}
