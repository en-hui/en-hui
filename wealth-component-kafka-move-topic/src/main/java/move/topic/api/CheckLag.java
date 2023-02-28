package move.topic.api;

import move.topic.model.ApplicationParam;
import move.topic.util.KafkaHelper;
import move.topic.util.MoveTopicLog;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CheckLag {
  private static final MoveTopicLog log = new MoveTopicLog(CheckLag.class);

  public static void main(String[] args)
      throws IOException, ExecutionException, InterruptedException {
    String envParam = System.getenv("envParam");
    ApplicationParam applicationParam;
    if (!Boolean.parseBoolean(envParam)) {
      log.info("从配置文件加载配置...");
      applicationParam = ApplicationParam.initApplicationParam();
    } else {
      log.info("从环境变量加载配置...");
      applicationParam = ApplicationParam.initApplicationParamWithEnv();
    }
    KafkaHelper kafkaHelper = new KafkaHelper(applicationParam);
    checkLag(kafkaHelper, applicationParam);
    kafkaHelper.close();
  }

  public static void checkLag(KafkaHelper kafkaHelper, ApplicationParam applicationParam) {
    try {
      log.msg("仅展示lag不为0且topic名称中包含：[" + applicationParam.getTaskIdsStr() + "]的信息");
      Map<String, Long> topicGroupAndLag = new LinkedHashMap<>();
      Map<String, Long> topicGroupAndEnd = new LinkedHashMap<>();
      Map<String, Long> topicGroupAndCurrent = new LinkedHashMap<>();
      List<Integer> taskIds = applicationParam.getTaskIds();
      AdminClient adminClient = kafkaHelper.getAdminClient();
      Properties consumerProperties = kafkaHelper.getConsumerProperties(false);
      KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(consumerProperties);
      Collection<ConsumerGroupListing> groupListings = adminClient.listConsumerGroups().all().get();
      List<String> consumerGroupIds =
          groupListings.stream().map(ConsumerGroupListing::groupId).collect(Collectors.toList());
      for (String consumerGroupId : consumerGroupIds) {
        ConsumerGroupDescription desc =
            adminClient
                .describeConsumerGroups(Collections.singleton(consumerGroupId))
                .all()
                .get()
                .get(consumerGroupId);
        if (desc == null) {
          continue;
        }
        Map<TopicPartition, OffsetAndMetadata> currentOffsetMap =
            adminClient
                .listConsumerGroupOffsets(consumerGroupId)
                .partitionsToOffsetAndMetadata()
                .get();
        Map<TopicPartition, Long> endLogMap = consumer.endOffsets(currentOffsetMap.keySet());
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : currentOffsetMap.entrySet()) {
          TopicPartition topicPartition = entry.getKey();
          boolean check = false;
          if (topicPartition.topic().contains("dp_error_queue")
              || topicPartition.topic().contains("dp_schema_event")) {
            check = true;
          } else {
            if (taskIds == null || taskIds.size() == 0) {
              if (topicPartition.topic().contains("v2_dptask_")) {
                check = true;
              }
            } else {
              for (Integer taskId : taskIds) {
                if (topicPartition.topic().contains("v2_dptask_" + taskId)) {
                  check = true;
                  break;
                }
              }
            }
          }
          if (check) {
            OffsetAndMetadata offsetAndMetadata = entry.getValue();
            long offset = offsetAndMetadata.offset();
            Long end = endLogMap.get(topicPartition);
            long lag = end - offset;
            String key = "group:[" + consumerGroupId + "]——topic:[" + topicPartition.topic() + "]";
            topicGroupAndLag.merge(key, lag, Long::sum);
            topicGroupAndEnd.merge(key, end, Long::sum);
            topicGroupAndCurrent.merge(key, offset, Long::sum);
          }
        }
      }
      Set<Map.Entry<String, Long>> entries = topicGroupAndLag.entrySet();
      for (Map.Entry<String, Long> entry : entries) {
        if (entry.getValue() != 0) {
          log.msg(
              "lag——["
                  + entry.getValue()
                  + "]——end:["
                  + topicGroupAndEnd.get(entry.getKey())
                  + "]——current:["
                  + topicGroupAndCurrent.get(entry.getKey())
                  + "]——"
                  + entry.getKey());
        }
      }
      log.msg("程序执行完毕，正常退出");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
