package com.enhui.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 生产者
 *
 * @Author 胡恩会
 * @Date 2021/7/19 22:23
 **/
public class ProducerTest {

    String topic = "first";
    KafkaProducer<String, String> producer = null;
    @BeforeEach
    public void before() {
        Properties properties = new Properties();
        // 基本配置
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 默认是1，只要leader持久化成功就返回ack确认
        properties.setProperty(ProducerConfig.ACKS_CONFIG,"1");
        producer = new KafkaProducer<>(properties);
    }

    @AfterEach
    public void after() {
        producer.close();
    }

    @Test
    public void producer() throws ExecutionException, InterruptedException {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 30; j++) {
                // 相同key进入一个分区（partition）
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, "item-" + j, "val-" + i);
                Future<RecordMetadata> send = producer.send(record);
                RecordMetadata rm = send.get();
                System.out.println("key:" + record.key() + " val:" + record.value() + " topic:" + rm.topic() + " partition:" + rm.partition() + " offset:" + rm.offset());
            }
        }
    }
}
