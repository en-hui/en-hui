package com.enhui.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;

public class ConsumerTopic {

  @Test
  public void testConsumer() throws UnsupportedEncodingException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    KafkaConsumer<String, GenericRecord> consumer = null;
    boolean isAgent = true;
    String topic = "";
    String groupId = "huenhui_test_group";
    Properties properties = new Properties();
    if (isAgent) {
      topic = "DP_9_.db2inst1.lnlnsjrn";
      properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "82.157.170.197:9092");
      properties.put(
          AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
          String.format("http://%s:%s", "82.157.170.197", 8081));
    } else {
      topic = "v2_dptask_9.DP_GBK.DB2INST1.LNLNSJRN.9959";
      //      topic = "test_gbk_avro";
      properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092");
      properties.put(
          AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
          String.format("http://%s:%s", "schema_registry", 8081));
    }

    properties.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    properties.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    int count = 1000;
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    // 不提交--关闭自动提交，且不手动提交
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    // 指定消费条数（如果有100条，就拉一百条，不够就算了）
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, String.valueOf(count));
    consumer = new KafkaConsumer<String, GenericRecord>(properties);

    List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
    List<TopicPartition> topicPartitions = new ArrayList<>();
    for (PartitionInfo partitionInfo : partitionInfos) {
      topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
    }
    consumer.assign(topicPartitions);
    consumer.seekToBeginning(topicPartitions);

    ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(1000));
    while (records.isEmpty()) {
      consumer.seek(new TopicPartition(topic, 0), 0);
      records = consumer.poll(Duration.ofMillis(1000));
    }
    String srcTableKey = "3220230101160242963001";
    System.out.printf("topic：%s 拉到数据了,根据源表 LNJRN_KEY = %s 来分析 LNJRN_TEXT hex数据 \n", topic, srcTableKey);
    for (ConsumerRecord<String, GenericRecord> record : records) {
      if (record.value().toString().contains(srcTableKey)) {
        LinkedHashMap<String, Object> map =
            mapper.readValue(
                record.value().toString(),
                mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        GenericRecord recordValue = record.value();
        Object content = "";
        if (recordValue.get("LNJRN_TEXT") != null) {
          content = recordValue.get("LNJRN_TEXT");
        } else {
          Object after = map.get("after");
          if (after != null) {
            LinkedHashMap<String, Object> afterMap = (LinkedHashMap<String, Object>) after;
            content = afterMap.get("LNJRN_TEXT");
          }
        }

        String dbHex =
            "204C4E303330383030304832303233303130313244000000001369924C00000003A1A100000C30310000000000000C30303036313530313132303144303320202020202020202020202032303232303830313230323330313031153C000848250C000000000000000C2020202020202020202020202020202020313630352020202020202020202020202020202020202020363135303131323031443033303030303030363030303030303030303030303030303020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020204E20000000000000000C000000000000000C543130303030303030303030303030303030303030202020202020202020202020202020202020202020202020202020202020202020000000000C000000";
        String str = content.toString();
        byte[] utfBytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] gbkBytes = str.getBytes(Charset.forName("gbk"));
        String utfBinaryValue = convertBinaryValue(utfBytes);
        String gbkBinaryValue = convertBinaryValue(gbkBytes);
        if (dbHex.equals(utfBinaryValue)) {
          System.out.println("utf8编码出的hex与数据库hex一致");
        }
        if (dbHex.equals(gbkBinaryValue)) {
          System.out.println("gbk编码出的hex与数据库hex一致");
        }
        System.out.printf("源表数据库的十六进制：%s\n", dbHex);
        System.out.printf("utf8编码十六进制：%s\n", utfBinaryValue);
        System.out.printf("gbk编码十六进制：%s\n", gbkBinaryValue);
        System.out.println();
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

  public void printLnjrnText(Map<String, Object> record) {
    Object o = record.get("LNJRN_TEXT");
    if (o != null) {
      String utfHex = convertBinaryValue(o.toString().getBytes(Charset.forName("utf-8")));
      String gbkHex = convertBinaryValue(o.toString().getBytes(Charset.forName("gbk")));
      System.out.printf("utfHex: %s\n", utfHex);
      System.out.printf("gbkHex: %s\n", gbkHex);
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
