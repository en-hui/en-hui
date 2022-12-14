package com.enhui;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * nc -lk 9999<br>
 * 通过socket数据源，去请求一个socket服务，得到数据流<br>
 * 然后统计数据流中出现的单词及个数
 */
public class _01WorkCount {

  public static void main(String[] args) throws Exception {
    // 本地运行开启web
    Configuration configuration = new Configuration();
    configuration.setInteger("rest.port", 8081);
    final StreamExecutionEnvironment env =
        StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
    // 1、流批一体的入口环境
    //    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    env.setParallelism(1); // 本地运行可以指定使用几个并行线程，默认cpu核数
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