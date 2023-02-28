package move.topic.api;

import move.topic.model.ApplicationParam;
import move.topic.model.TopicMapping;
import move.topic.util.KafkaHelper;
import move.topic.util.MoveTopicLog;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class DataValidate {
  private static final MoveTopicLog log = new MoveTopicLog(DataValidate.class);
  private long totalCount = 0;
  private final CountDownLatch latch = new CountDownLatch(2);
  private final Map<Integer, List<ConsumerRecord<Object, Object>>> sourceData = new HashMap<>();
  private final Map<Integer, List<ConsumerRecord<Object, Object>>> destData = new HashMap<>();
  private KafkaConsumer<Object, Object> sourceConsumer = null;
  private KafkaConsumer<Object, Object> destConsumer = null;
  private String sourceTopic;
  private String destTopic;

  public static void dataValidate(KafkaHelper kafkaHelper, ApplicationParam applicationParam) {
    try {
      String consumerGroup = "validate-" + applicationParam.getConsumerGroup();
      log.msg("使用消费者组[" + consumerGroup + "]消费全部数据，校验topic与迁移topic一致，明细如下：");
      List<TopicMapping> topicMappings = TopicMapping.initTopicMapping(applicationParam);
      for (TopicMapping topicMapping : topicMappings) {
        try {
          DataValidate validate = new DataValidate();
          String deserializer = topicMapping.getDeserializer();
          validate.sourceTopic = topicMapping.getSourceTopic();
          validate.destTopic = topicMapping.getDestTopic();
          Properties consumerProperties = kafkaHelper.getConsumerProperties(deserializer);
          consumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
          consumerProperties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, consumerGroup);
          // 两个线程分别消费数据，并放到集合中暂存
          validate.sourceConsumer = new KafkaConsumer<Object, Object>(consumerProperties);
          validate.destConsumer = new KafkaConsumer<Object, Object>(consumerProperties);
          validate.getSourceData();
          validate.getDestData();
          // 等待两个线程执行完毕
          validate.latch.await();
          // 数据收集完毕后，进行比较（主要测试多分区，所以等全部数据消费完毕）
          validate.validate();
        } catch (Exception e) {
          log.info(topicMapping.getSourceTopic() + "  ==>  " + e.getMessage());
        }
      }
      log.msg("程序执行完毕，正常退出");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void validate() throws InterruptedException {
    // 分区数相同
    if (sourceData.keySet().size() != destData.keySet().size()) {
      throw new RuntimeException(
          "数据不一致,分区数不同" + sourceData.keySet().size() + "——" + destData.keySet().size());
    }
    // 分区值相同
    if (!sourceData.keySet().containsAll(destData.keySet())) {
      throw new RuntimeException("数据不一致，分区值不同");
    }
    Set<Integer> paritions = sourceData.keySet();
    for (Integer parition : paritions) {
      List<ConsumerRecord<Object, Object>> sourcePRecord = sourceData.get(parition);
      List<ConsumerRecord<Object, Object>> destPRecord = destData.get(parition);
      // 每个分区的数据条数相同
      if (sourcePRecord.size() != destPRecord.size()) {
        throw new RuntimeException(
            "数据不一致，分区内数据条数不同" + sourcePRecord.size() + "——" + destPRecord.size());
      } else {
        totalCount += sourcePRecord.size();
      }
      // 每个分区的数据内容相同
      int total = destPRecord.size();
      for (int i = 0; i < total; i++) {
        ConsumerRecord<Object, Object> oneSourceRecord = sourcePRecord.get(i);
        ConsumerRecord<Object, Object> oneDestRecord = destPRecord.get(i);
        if (!(oneSourceRecord.key() == null && oneDestRecord.key() == null)
            && (!oneSourceRecord.key().equals(oneDestRecord.key()))) {
          throw new RuntimeException("数据不一致，分区内key值不同");
        }
        Object sourceValue = oneSourceRecord.value();
        Object sinkValue = oneDestRecord.value();
        if (sourceTopic.contains("config_connect_sink_dp")) {
//          sourceValue = TopicMover.dataCleanWithConfigTopic(sourceValue);
        }
        if (!sourceValue.equals(sinkValue)) {
          throw new RuntimeException("数据不一致，分区内value值不同");
        }
      }
    }
    log.info(sourceTopic + "与迁移后数据一致，数据总条数 ：" + totalCount);
  }

  public void getSourceData() {
    new Thread(
            () -> {
              int zeroRecordsCount = 0;
              List<PartitionInfo> partitionInfos = sourceConsumer.partitionsFor(sourceTopic);
              List<TopicPartition> allTopicPartition = new ArrayList<>();
              for (PartitionInfo partitionInfo : partitionInfos) {
                allTopicPartition.add(
                    new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
              }
              sourceConsumer.assign(allTopicPartition);
              ConsumerRecords<Object, Object> records;
              while (true) {
                records = sourceConsumer.poll(Duration.ofMillis(3000));
                if (records.count() == 0) {
                  zeroRecordsCount++;
                }
                if (zeroRecordsCount >= 3) {
                  latch.countDown();
                  sourceConsumer.close();
                  break;
                }
                Set<TopicPartition> partitions = records.partitions();
                for (TopicPartition partition : partitions) {
                  List<ConsumerRecord<Object, Object>> pRecords = records.records(partition);
                  List<ConsumerRecord<Object, Object>> pAllRecords =
                      sourceData.get(partition.partition());
                  if (pAllRecords != null) {
                    pAllRecords.addAll(pRecords);
                  } else {
                    // records.records返回的 Collections.unmodifiableList UnsupportedOperationException
                    sourceData.put(partition.partition(), new ArrayList<>(pRecords));
                  }
                }
              }
            })
        .start();
  }

  public void getDestData() {
    new Thread(
            () -> {
              int zeroRecordsCount = 0;
              List<PartitionInfo> partitionInfos = destConsumer.partitionsFor(destTopic);
              List<TopicPartition> allTopicPartition = new ArrayList<>();
              for (PartitionInfo partitionInfo : partitionInfos) {
                allTopicPartition.add(
                    new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
              }
              destConsumer.assign(allTopicPartition);
              ConsumerRecords<Object, Object> records;
              while (true) {
                records = destConsumer.poll(Duration.ofMillis(3000));
                if (records.count() == 0) {
                  zeroRecordsCount++;
                }
                if (zeroRecordsCount >= 3) {
                  latch.countDown();
                  destConsumer.close();
                  break;
                }
                Set<TopicPartition> partitions = records.partitions();
                for (TopicPartition partition : partitions) {
                  List<ConsumerRecord<Object, Object>> pRecords = records.records(partition);
                  List<ConsumerRecord<Object, Object>> pAllRecords =
                      destData.get(partition.partition());
                  if (pAllRecords != null) {
                    pAllRecords.addAll(pRecords);
                  } else {
                    // records.records返回的 Collections.unmodifiableList UnsupportedOperationException
                    destData.put(partition.partition(), new ArrayList<>(pRecords));
                  }
                }
              }
            })
        .start();
  }
}
