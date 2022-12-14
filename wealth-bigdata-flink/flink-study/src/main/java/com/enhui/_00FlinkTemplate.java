package com.enhui;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class _00FlinkTemplate {
  /**
   * flink编程模版：<br>
   * 1、创建一个编程入口环境env<br>
   * 2、通过source算子，映射数据源为dataStream<br>
   * 3、通过算子对数据流进行各种转换（计算逻辑）<br>
   * 4、通过sink算子，将数据流输出<br>
   * 5、触发程序的提交运行
   *
   * @param args
   */
  public static void main(String[] args) throws Exception {
    // 1、流批一体的入口环境
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    // 2、连接 socket 得到数据流
    DataStreamSource<String> dataStream = env.socketTextStream("127.0.0.1", 9999);
    // 3.1、单词切割
    final SingleOutputStreamOperator<Tuple2<String, Integer>> words =
        dataStream.flatMap(
            new FlatMapFunction<String, Tuple2<String, Integer>>() {
              @Override
              public void flatMap(String s, Collector<Tuple2<String, Integer>> collector)
                  throws Exception {
                final String[] split = s.split("\\s+");
                for (String word : split) {
                  collector.collect(Tuple2.of(word, 1));
                }
              }
            });
    // 3.2、按单词分组
    final KeyedStream<Tuple2<String, Integer>, String> keyed =
        words.keyBy(
            new KeySelector<Tuple2<String, Integer>, String>() {
              @Override
              public String getKey(Tuple2<String, Integer> t) throws Exception {
                return t.f0;
              }
            });
    // 3.3、相同单词个数求和
    final SingleOutputStreamOperator<Tuple2<String, Integer>> resultStream = keyed.sum("f1");

    // 4、输出到控制台
    resultStream.print();
    // 5、运行
    env.execute();
  }
}
