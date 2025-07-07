package com.enhui.kafka;

import io.confluent.connect.avro.AvroConverter;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.storage.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AvroConsumerTest {

  KafkaConsumer<byte[], byte[]> consumer;
  Properties properties = new Properties();
  String topic = "v2_dptask_2.orcl.LIUKANG.LKTEST01.5625";

  Converter taskKeyConverter = new AvroConverter();
  Converter taskValueConverter = new AvroConverter();

  @BeforeEach
  public void before() {
    // 基本配置
    properties.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
    properties.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "10000");
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test_avro");

    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092");

    properties.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "30000");
    properties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "600000");
    properties.setProperty(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "900000");
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, "dp-mac");

    consumer = new KafkaConsumer<>(properties);
    taskKeyConverter.configure(
        Collections.singletonMap("schema.registry.url", "http://schema_registry:8081"), true);
    taskValueConverter.configure(
        Collections.singletonMap("schema.registry.url", "http://schema_registry:8081"), false);
  }

  /**
   * {"after":{"after":{"ID":{"bytes":"\u0004Ç"},"COL1":{"string":"444"}}}, "before":null,
   * "source":{"database":{"string":"orcl"},"schema":{"string":"LIUKANG"},"entity":"LKTEST01","entity2":{"string":""},
   * "partition":0,"size":{"long":2},"islastone":{"boolean":false},"snapshot":{"boolean":false},"idx":{"long":12794},
   * "totalb":103998,"b":7,"totalc":12794,"ts_sec":{"long":1751873943231},"msg_t":{"string":"I"},"eqr":{"boolean":false},
   * "src_schema_id":{"int":5625},"sink_entity_id":{"int":0},"start_time":1751873942612,"collect_ts":{"long":1751873943231},
   * "ver":0,"custom_param":{"map":{"ID":{"string":"AABCEcAAHAAAOItAAA"}}},"src_address":{"string":""},"clear_dest_ids":null}}
   */
  @Test
  public void consume() {
    consumer.assign(Collections.singleton(new TopicPartition(topic, 0)));

    while (true) {
      ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofMillis(1000));
      for (ConsumerRecord<byte[], byte[]> record : records) {

        SchemaAndValue key =
            taskKeyConverter.toConnectData(record.topic(), record.headers(), record.key());
        SchemaAndValue value =
            taskValueConverter.toConnectData(record.topic(), record.headers(), record.value());
        System.out.println("=======================start=====================");
        System.out.println("key: " + key);
        System.out.println("value: " + value);
        System.out.println("=======================end=====================");
      }
    }
  }
}
