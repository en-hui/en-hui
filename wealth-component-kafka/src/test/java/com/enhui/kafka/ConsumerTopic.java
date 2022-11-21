package com.enhui.kafka;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;

public class ConsumerTopic {
  @Test
  public void testConsumer() throws UnsupportedEncodingException {
    KafkaConsumer consumer = null;
    String topic = "v2_dptask_7.dp_test.DB2INST1.LNLNSJRN.44129";
    String groupId = "huenhui_test";
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092");
    properties.put(
        AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
        String.format("http://%s:%s", "schema_registry", 8081));
    properties.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    properties.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    int count = 100;
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    // 不提交--关闭自动提交，且不手动提交
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    // 指定消费条数（如果有100条，就拉一百条，不够就算了）
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, String.valueOf(count));
    consumer = new KafkaConsumer<String, String>(properties);

    List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
    List<TopicPartition> topicPartitions = new ArrayList<>();
    for (PartitionInfo partitionInfo : partitionInfos) {
      topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
    }
    consumer.assign(topicPartitions);
    consumer.seekToBeginning(topicPartitions);

    ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofMillis(1000));
    for (ConsumerRecord record : records) {
      if (record.value().toString().contains("3220230101160242963001")) {
        record.value().toString().getBytes(Charset.forName("utf-8"));
        System.out.println(
            "partition:"
                + record.partition()
                + ",offset:"
                + record.offset()
                + ",value:"
                + record.value());
      }
    }
  }

  public static String convertBinaryValue(byte[] bytes, BinaryConvertType binaryConvertType) {
    if (bytes == null) {
      return "";
    }
    if (binaryConvertType == BinaryConvertType.BASE64) {
      return Base64.getEncoder().encodeToString(bytes);
    } else if (binaryConvertType == BinaryConvertType.HEX) {
      StringBuilder hex = new StringBuilder();
      for (byte aByte : bytes) {
        // nihao  ->  [110, 105, 104, 97, 111]  ->  6E6968616F
        hex.append(String.format("%02X", aByte));
      }
      return hex.toString();
    } else {
      // null 使用旧版本方案
      return bytes.toString();
    }
  }

  public static String convertBinaryValue(byte[] bytes) {
    if (bytes == null) {
      return "";
    }
    StringBuilder hex = new StringBuilder();
    for (byte aByte : bytes) {
      // nihao  ->  [110, 105, 104, 97, 111]  ->  6E6968616F
      hex.append(String.format("%02X", aByte));
    }
    return hex.toString();
  }
}
