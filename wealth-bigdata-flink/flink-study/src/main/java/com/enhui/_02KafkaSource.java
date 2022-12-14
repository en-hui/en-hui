package com.enhui;

import java.util.Collections;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

public class _02KafkaSource {
  public static void main(String[] args) throws Exception {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    final KafkaSource<String> kafkaSource =
        KafkaSource.<String>builder()
            .setBootstrapServers("kafka1:9092")
            .setValueOnlyDeserializer(new SimpleStringSchema())
            // 消费起始位置取之前持久化的，如果没有从最后
            .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.LATEST))
            .setTopics(Collections.singletonList("testTopic"))
            .setGroupId("heh-test-group-1")
            .build();

    final DataStreamSource<String> sourceStream =
        env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka-source-name")
            .setParallelism(2);
    sourceStream.print();

    env.execute();
  }
}
