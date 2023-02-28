package move.topic.service.move;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import move.topic.model.ApplicationParam;
import move.topic.model.TopicMapping;
import move.topic.util.KafkaHelper;
import move.topic.util.MoveTopicLog;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class TopicMover implements Runnable {
  private static final MoveTopicLog log = new MoveTopicLog(TopicMover.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private final KafkaHelper kafkaHelper;
  private final String groupId;
  private final String sourceTopic;
  private final String destTopic;
  private final String deserializer;
  private final String serializer;
  private long moveCount = 0;
  private KafkaConsumer<Object, Object> consumer = null;
  private Map<TopicPartition, Long> endPosition = null;
  private final boolean isCleanPre;
  private final boolean isOverride;
  private final String prefix;

  public TopicMover(ApplicationParam param, TopicMapping topicMapping, KafkaHelper kafkaHelper) {
    this.kafkaHelper = kafkaHelper;
    this.groupId = param.getConsumerGroup();
    this.isCleanPre = param.isAddPrefix();
    this.isOverride = param.isOverride();
    this.prefix = param.getAddPrefix();
    this.sourceTopic = topicMapping.getSourceTopic();
    this.destTopic = topicMapping.getDestTopic();
    this.serializer = topicMapping.getSerializer();
    this.deserializer = topicMapping.getDeserializer();
  }

  @Override
  public void run() {
    log.info("开始迁移::" + sourceTopic);
    long start = System.currentTimeMillis();
    try {
      Properties properties = kafkaHelper.getConsumerProperties(deserializer);
      consumer = new KafkaConsumer<Object, Object>(properties);

      consumerWithCommitPollPartition();
      long end = System.currentTimeMillis();
      log.info("完成迁移::" + sourceTopic + ",耗时：" + (end - start) + "，共迁移数据条数：" + moveCount);
    } catch (Exception e) {
      throw new RuntimeException(
          "迁移失败::源topic名称为【"
              + sourceTopic
              + "】，目的topic名称为【"
              + destTopic
              + "】,序列化方式为【"
              + (KafkaAvroSerializer.class.getName().equals(serializer) ? "avro" : "string")
              + "】。如果是avro序列化失败，请检查schema_registry的端口是否未暴露",
          e);
    }
  }

  public void consumerWithCommitPollPartition() throws InterruptedException, ExecutionException {
    KafkaProducer<Object, Object> producer;
    if (Objects.equals(deserializer, KafkaAvroDeserializer.class.getName())
        || Objects.equals(serializer, KafkaAvroSerializer.class.getName())) {
      producer = kafkaHelper.getAvroProducer();
    } else {
      producer = kafkaHelper.getStringProducer();
    }
    // config_connect_sink_dp 中数据，要将topic字符串换成新的
    boolean isConfigTopic = false;
    if (sourceTopic.contains("config_connect_sink_dp")) {
      isConfigTopic = true;
    }
    boolean isOffsetTopic = false;
    if (sourceTopic.contains("offset_connect_source_dp")) {
      isOffsetTopic = true;
    }
    boolean isSchema = false;
    if (sourceTopic.contains(".schema-changes")) {
      isSchema = true;
    }
    // 查询每个分区的end offset,以判断消费结束
    List<PartitionInfo> partitionInfos = consumer.partitionsFor(sourceTopic);
    List<TopicPartition> allTopicPartition = new ArrayList<>();
    for (PartitionInfo partitionInfo : partitionInfos) {
      allTopicPartition.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
    }
    endPosition = consumer.endOffsets(allTopicPartition);

    // 开始消费 不能用subscribe方式
    consumer.assign(allTopicPartition);
    ConsumerRecords<Object, Object> records;
    while (true) {
      records = consumer.poll(Duration.ofMillis(5000));
      if (records.count() == 0 && isComplect()) {
        log.debug("当前任务执行结束,【" + sourceTopic + "】");
        consumer.close();
        break;
      }
      Set<TopicPartition> partitions = records.partitions();
      for (TopicPartition partition : partitions) {
        // 当前分区的所有记录（本次poll微批）
        List<ConsumerRecord<Object, Object>> pRecords = records.records(partition);
        // 先拿到当前分区的最后一条记录（提交offset时，需要知道首或者尾的offset）
        ConsumerRecord<Object, Object> plastRecord = pRecords.get(pRecords.size() - 1);
        for (ConsumerRecord<Object, Object> record : pRecords) {
          Object key = record.key();
          Object value = record.value();
          boolean cleanPrefix = this.isCleanPrefix();
          if (isConfigTopic) {
            value = dataCleanWithConfigTopic(value, cleanPrefix, prefix);
          }
          if (isOffsetTopic) {
            key = dataCleanWithOffsetTopic(key, cleanPrefix, prefix);
          }
          if (isSchema) {
            value = dataCleanWithSchemaTopic(value, cleanPrefix, prefix);
          }
          // 清洗前缀
          if (cleanPrefix) {
            if (this.isOverride) {
              // 覆盖一遍垃圾数据，用null值
              log.debug(key + "==old==" + null);
              producer.send(new ProducerRecord<>(destTopic, key, null));
            }
            if (key != null) {
              String oldKey = key.toString();
              key = addPrefix(oldKey, prefix);
            }
            if (value != null) {
              String oldValue = value.toString();
              value = addPrefix(oldValue, prefix);
            }
            log.debug(key + "==new==" + value);
          }
          // 相同key进到一个分区，算法都是默认的，所以源topic和目的topic如果分区数一样，理论上还进到相同分区
          producer.send(new ProducerRecord<>(destTopic, key, value));
        }
        // poll中根据分区处理，分区分别提交
        TopicPartition topicPartition = new TopicPartition(sourceTopic, plastRecord.partition());
        OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(plastRecord.offset() + 1);
        log.debug(
            "迁移进度：topic:["
                + sourceTopic
                + "]-->["
                + destTopic
                + "]，partition:["
                + partition.partition()
                + "],offset:["
                + plastRecord.offset()
                + "]");
        moveCount += pRecords.size();
        consumer.commitSync(Collections.singletonMap(topicPartition, offsetAndMetadata));
      }
    }
  }

  public static String addPrefix(String value, String prefix) {
    String oldValue = value.toString();
    oldValue = oldValue.replace("dp-kafka-connector-", prefix + "dp-kafka-connector-");
    oldValue = oldValue.replace("dbz-mysql-connector-", prefix + "dbz-mysql-connector-");
    oldValue = oldValue.replace("dp-mysql-batch-connector-", prefix + "dp-mysql-batch-connector-");
    oldValue = oldValue.replace("dp-hdfs-connector-", prefix + "dp-hdfs-connector-");
    oldValue = oldValue.replace("dp-hdfsfile-connector-", prefix + "dp-hdfsfile-connector-");
    oldValue = oldValue.replace("dp-tidb-connector-", prefix + "dp-tidb-connector-");
    oldValue = oldValue.replace("dp-kafka08-connector-", prefix + "dp-kafka08-connector-");
    return oldValue;
  }

  /**
   * 只给server name加前缀 ==>
   * ["rap-rdpl-sitcls01-dbz-mysql-connector-dptask_5_1",{"server":"rap-rdpl-sitcls01-dptask-5"}] ==
   * {"ts_sec":1660273322,"file":"bin-log.000332","pos":1484972,
   * "gtids":"6305bb3d-b876-11eb-9036-525400158759:1-1277958",
   * "rollback.version":0,"server_id":1,"event":1}
   *
   * @param key
   * @param isCleanPre
   * @param prefix
   * @return
   */
  public static String dataCleanWithOffsetTopic(Object key, boolean isCleanPre, String prefix) {
    if (key == null) {
      return null;
    }
    String oldKey = key.toString();
    try {
      JsonNode jsonNode = OBJECT_MAPPER.readTree(oldKey);
      String oldV = null;
      String newV = null;
      if (jsonNode instanceof ArrayNode) {
        ArrayNode array = (ArrayNode) jsonNode;
        for (JsonNode node : array) {
          JsonNode dbzServer = node.get("server");
          if (dbzServer != null) {
            oldV = dbzServer.toString();
            newV = oldV.replace("_", "-");
          }
        }
      }
      if (oldV != null) {
        if (isCleanPre) {
          // 引号包裹住外面
          newV = newV.replace("\"", "");
          return oldKey.replace(oldV, "\"" + prefix + newV + "\"");
        } else {
          return oldKey.replace(oldV, newV);
        }
      }
      return oldKey;
    } catch (Exception e) {
      log.debug("清洗失败,使用原有值进行迁移：" + oldKey);
      e.printStackTrace();
      return oldKey;
    }
  }

  /**
   * 洗server name这个topic名称，加前缀 "source" : { "server" : "rap-rdpl-sitcls01-dptask_3" }, "position" :
   * { "file" : "bin-log.000327", "pos" : 46300177, "gtids" :
   * "6305bb3d-b876-11eb-9036-525400158759:1-1251107", "rollback.version" : 0 }, "ddl" : "DROP TABLE
   * IF EXISTS `dp_test`.`heh_sample1`" }
   *
   * @param value
   * @param isCleanPre
   * @param prefix
   * @return
   */
  public static String dataCleanWithSchemaTopic(Object value, boolean isCleanPre, String prefix) {
    if (value == null) {
      return null;
    }
    String oldValue = value.toString();
    try {
      JsonNode jsonNode = OBJECT_MAPPER.readTree(oldValue);
      JsonNode source = jsonNode.get("source");
      if (source != null) {
        JsonNode server = source.get("server");
        if (server != null) {
          String oldTopicStr = server.toString();
          String newTopicStr = oldTopicStr.replace(".", "-").replace("_", "-");
          if (isCleanPre) {
            // 引号包裹住外面
            newTopicStr = newTopicStr.replace("\"", "");
            return oldValue.replace(oldTopicStr, "\"" + prefix + newTopicStr + "\"");
          } else {
            return oldValue.replace(oldTopicStr, newTopicStr);
          }
        }
      }
      return oldValue;
    } catch (Exception e) {
      log.debug("清洗失败,使用原有值进行迁移：" + oldValue);
      e.printStackTrace();
      return oldValue;
    }
  }

  /**
   * 清洗前缀
   *
   * @return
   */
  public boolean isCleanPrefix() {
    boolean isClean = false;
    if (sourceTopic.contains("offset_connect_sink_dp")
        || sourceTopic.contains("offset_connect_source_dp")
        || sourceTopic.contains("status_connect_source_dp")
        || sourceTopic.contains("status_connect_sink_dp")
        || sourceTopic.contains("config_connect_sink_dp")
        || sourceTopic.contains("config_connect_source_dp")) {
      isClean = true;
    }
    return isClean && this.isCleanPre;
  }

  /**
   * 换value中的topic ==> task-rap-rdpl-sitcls01-dp-tidb-connector-dptask_1_2-0 ==
   * {"properties":{"connector.class":"com.datapipeline.sink.connector.tidb.TidbConnector",
   * "destination.id":"2","task.class":"com.datapipeline.sink.connector.tidb.TidbConnectorTask",
   * "dptask.id":"1","tasks.max":"5","topics":"rap-rdpl-sitcls01-v2-dptask-1-dp-test-heh-sample-1098",
   * "name":"rap-rdpl-sitcls01-dp-tidb-connector-dptask_1_2"}}
   *
   * @param value
   * @return
   */
  public static String dataCleanWithConfigTopic(Object value, boolean isCleanPre, String prefix) {
    if (value == null) {
      return null;
    }
    String oldValue = value.toString();
    try {
      JsonNode jsonNode = OBJECT_MAPPER.readTree(oldValue);
      JsonNode properties = jsonNode.get("properties");
      if (properties != null) {
        JsonNode topics = properties.get("topics");
        if (topics != null) {
          String oldTopicStr = topics.toString();
          String newTopicStr = oldTopicStr.replace(".", "-").replace("_", "-");
          if (isCleanPre) {
            newTopicStr = newTopicStr.replace("\"", "");
            return oldValue.replace(oldTopicStr, "\"" + prefix + newTopicStr + "\"");
          } else {
            return oldValue.replace(oldTopicStr, newTopicStr);
          }
        }
      }
      return oldValue;
    } catch (Exception e) {
      log.debug("清洗失败,使用原有值进行迁移：" + oldValue);
      e.printStackTrace();
      return oldValue;
    }
  }

  /**
   * 用是否有lag标识迁移完成，更加精确
   *
   * @return
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public boolean isComplect() throws ExecutionException, InterruptedException {
    Map<TopicPartition, OffsetAndMetadata> currentOffset =
        kafkaHelper
            .getAdminClient()
            .listConsumerGroupOffsets(groupId)
            .partitionsToOffsetAndMetadata()
            .get();
    for (Map.Entry<TopicPartition, Long> pEndOffset : endPosition.entrySet()) {
      if (pEndOffset.getValue() == 0) {
        continue;
      }
      OffsetAndMetadata offsetAndMetadata = currentOffset.get(pEndOffset.getKey());
      if (offsetAndMetadata != null) {
        long offset = offsetAndMetadata.offset();
        long end = pEndOffset.getValue();
        // 只要有一个分区有lag，就算没同步完成
        if (end - offset > 0) {
          return false;
        }
      } else {
        log.debug("查询不到该topic有消费者组lag记录：" + sourceTopic);
      }
    }
    return true;
  }
}
