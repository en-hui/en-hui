package com.enhui.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

/**
 * @Author 胡恩会
 * @Date 2021/7/20 0:19
 **/
public class ConsumerTest {

    @Test
    public void consumer() {
        Properties properties = new Properties();
        // 基本配置
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "39.100.158.215:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // 消费者的细节
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "first_enhui");
        // kafka is MQ;is storage (存储)   所以要指定从哪里开始消费
        /**
         * What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server
         * (e.g. because that data has been deleted):
         * <ul>
         *     <li>earliest: automatically reset the offset to the earliest offset
         *     <li>latest: automatically reset the offset to the latest offset</li>
         *     <li>none: throw exception to the consumer if no previous offset is found for the consumer's group</li>
         *     <li>anything else: throw exception to the consumer.</li>
         * </ul>
         **/
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        // 自动提交（异步提交）；自动提交可能会重复消费||丢失消息
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        // 默认5s自动提交
        // properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"");
        // POLL 拉取数据，弹性、按需，设置每次拉取多少（根据消费能力设定）
        // properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        // 订阅 topic
        consumer.subscribe(Arrays.asList("first"));

        while(true) {
            // 0~N
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(0));

            // 消费部分优化很重要
            Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
            while (iterator.hasNext()) {
                // 一个consumer可以消费多个分区，但是一个分区只能给一个组里的一个consumer消费
                ConsumerRecord<String, String> record = iterator.next();
                System.out.println("key:" + record.key() + " val:" + record.value() + " topic:" + record.topic() + " partition:" + record.partition() + " offset:" + record.offset());
            }
        }
    }
}
