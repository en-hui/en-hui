package com.enhui.kafka;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** @Author 胡恩会 @Date 2021/7/20 0:19 */
public class ConsumerTest {
  KafkaConsumer<String, String> consumer = null;
  String topic = "first";
  String groupId = "huenhui_test";
  Properties properties = new Properties();

  @BeforeEach
  public void before() {
    // 基本配置
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092");
    properties.setProperty(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
  }

  /** 普通的正常消费 */
  @Test
  public void consumer() {
    // 消费者的细节
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    // kafka is MQ;is storage (存储)   所以要指定从哪里开始消费

    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    // 自动提交（异步提交）；自动提交可能会重复消费||丢失消息
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    // 默认5s自动提交
    // properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"");
    // POLL 拉取数据，弹性、按需，设置每次拉取多少（根据消费能力设定）
    // properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,"");

    consumer = new KafkaConsumer<String, String>(properties);
    // 订阅 topic
    consumer.subscribe(Arrays.asList(topic));

    while (true) {
      // 0~N
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(0));

      // 消费部分优化很重要
      Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
      while (iterator.hasNext()) {
        // 一个consumer可以消费多个分区，但是一个分区只能给一个组里的一个consumer消费
        ConsumerRecord<String, String> record = iterator.next();
        System.out.println(
            "key:"
                + record.key()
                + " val:"
                + record.value()
                + " topic:"
                + record.topic()
                + " partition:"
                + record.partition()
                + " offset:"
                + record.offset());
      }
    }
  }

  /** 指定时间消费指定条数 */
  @Test
  public void consumerByTime() {
    // 【指定开始时间】
    long fetchDataTime = new Date().getTime() - 1000 * 60 * 30;
    int count = 100;

    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    // 不提交--关闭自动提交，且不手动提交
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    // 指定消费条数（如果有100条，就拉一百条，不够就算了）
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, String.valueOf(count));
    consumer = new KafkaConsumer<String, String>(properties);

    // 获取topic的分区信息
    List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
    List<TopicPartition> topicPartitions = new ArrayList<>();

    HashMap<TopicPartition, Long> timestampsToSearch = new HashMap<>();

    for (PartitionInfo partitionInfo : partitionInfos) {
      topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
      timestampsToSearch.put(
          new TopicPartition(partitionInfo.topic(), partitionInfo.partition()), fetchDataTime);
    }

    consumer.assign(topicPartitions);

    // 获取每个分区指定时间的偏移量
    Map<TopicPartition, OffsetAndTimestamp> map = consumer.offsetsForTimes(timestampsToSearch);

    System.out.println("开始设置各分区初始偏移量....");
    OffsetAndTimestamp offsetAndTimestamp = null;
    for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : map.entrySet()) {
      offsetAndTimestamp = entry.getValue();
      if (offsetAndTimestamp != null) {
        int partition = entry.getKey().partition();
        long timestamp = offsetAndTimestamp.timestamp();
        long offset = offsetAndTimestamp.offset();

        // 设置读取消息的偏移量
        consumer.seek(entry.getKey(), offset);
      }
    }
    System.out.println("设置各分区初始偏移量结束....");

    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
    for (ConsumerRecord<String, String> record : records) {
      System.out.println(
          "partition:"
              + record.partition()
              + ",offset:"
              + record.offset()
              + ",value:"
              + record.value());
    }
  }

  /** 指定offset消费指定条数 */
  @Test
  public void consumerByOffset() {
    long offset = 10;
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
    for (TopicPartition topicPartition : topicPartitions) {

      consumer.seek(topicPartition, offset);
    }

    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
    for (ConsumerRecord<String, String> record : records) {
      System.out.println(
          "partition:"
              + record.partition()
              + ",offset:"
              + record.offset()
              + ",value:"
              + record.value());
    }
  }

  /** 从beginning拉取【100】条 */
  @Test
  public void consumerByBeginning() {
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

    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
    for (ConsumerRecord<String, String> record : records) {
      System.out.println(
          "partition:"
              + record.partition()
              + ",offset:"
              + record.offset()
              + ",value:"
              + record.value());
    }
  }

  /** 从结尾倒数消费指定条数，特定的消费者组，无需提交 */
  @Test
  public void consumeByEndNoCommit() {
    int count = 10;
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    // 不提交--关闭自动提交，且不手动提交
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    // 指定消费条数（如果有100条，就拉一百条，不够就算了）
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, String.valueOf(count));
    consumer = new KafkaConsumer<String, String>(properties);

    try (KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties)) {
      List<PartitionInfo> partitionInfos = consumer.partitionsFor("huenhui");
      // 每个分区分别消费
      List<ConsumerRecords<Object, Object>> multRecords = new ArrayList<>();
      for (PartitionInfo partitionInfo : partitionInfos) {
        TopicPartition topicPartition =
                new TopicPartition(partitionInfo.topic(), partitionInfo.partition());
        consumer.assign(Collections.singleton(topicPartition));
        consumer.seekToEnd(Collections.singleton(topicPartition));
        long position = consumer.position(topicPartition);
        System.out.println("分区：【" + topicPartition.partition() + "】的最大偏移：" + position);
        // position是下一次消息的偏移，而非最后一条的偏移，所以-1，减去数量需要+1才是第一个位置的offset，+-抵消
        long currentMaxOffset = position - count > 0 ? position - count : 0;
        consumer.seek(topicPartition, currentMaxOffset);
        ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofSeconds(10));
        multRecords.add(records);
      }
      spliceMessageKV(count, multRecords);
    }
  }

  @Test
  public void test() {
    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    list.add(5);
    list.add(3);
    list.add(4);
    list = list.stream().sorted(Comparator.comparing(Integer::intValue)).collect(Collectors.toList());
    System.out.println(list);
    int size = list.size();
    int consumerCount = 2;
    list = list.subList(size - consumerCount, size);
    System.out.println(list);
  }
  private static void spliceMessageKV(
    Integer consumerCount, Collection<ConsumerRecords<Object, Object>> mulRecords) {
      List<ConsumerRecord<Object, Object>> allPartitionsRecords = new ArrayList<>();
      for (ConsumerRecords<Object, Object> records : mulRecords) {
        for (ConsumerRecord<Object, Object> record : records) {
          allPartitionsRecords.add(record);
        }
      }
      Integer finalCount = consumerCount;
      if (mulRecords.size() > 1) {
        allPartitionsRecords =
                allPartitionsRecords.stream()
                        .sorted(Comparator.comparing(ConsumerRecord::timestamp))
                        .collect(Collectors.toList());
        int size = allPartitionsRecords.size();
        if (size > finalCount) {
          allPartitionsRecords = allPartitionsRecords.subList(size - consumerCount, size);
        }
      }
    for (ConsumerRecord<Object, Object> record : allPartitionsRecords) {
      StringBuilder sb = new StringBuilder(100);
      long timestamp = record.timestamp();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime localDateTime =
              LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
      String dateTime = localDateTime.format(formatter);
      sb.append("partition: ")
              .append(record.partition())
              .append("  ")
              .append("offset: ")
              .append(record.offset())
              .append("  ")
              .append("timestamp: ")
              .append(dateTime)
              .append("\n");
      if (record.key() != null) {
        sb.append("key: ").append(record.key().toString()).append("\n");
      } else {
        sb.append("key: ").append("{}").append("\n");
      }
      if (record.value() != null) {
        sb.append("value: ").append(record.value().toString());
      } else {
        sb.append("value: ").append("{}");
      }
      sb.append("\n");
      System.out.println(sb.toString());
    }
  }

}
