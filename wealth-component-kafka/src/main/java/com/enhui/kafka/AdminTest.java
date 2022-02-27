package com.enhui.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeLogDirsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.LogDirDescription;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.ReplicaInfo;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.requests.DescribeLogDirsResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdminTest {
  AdminClient client = null;
  long start = 0L;

  @BeforeEach
  public void before() {
    start = System.currentTimeMillis();
    Properties props = new Properties();
    props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092");
    client = AdminClient.create(props);
  }

  @AfterEach
  public void after() {
    client.close();
    System.out.println("总耗时：【" + (System.currentTimeMillis() - start) + "】毫秒");
  }

  @Test
  public void addATopic() throws ExecutionException, InterruptedException {
    NewTopic newTopic = new NewTopic("first", 3, (short) 1);
    client.createTopics(Arrays.asList(newTopic)).all().get();
    System.out.println(client.listTopics().names().get());
  }

  @Test
  public void addTopic() throws ExecutionException, InterruptedException {
    String topicName = "heh_test_topic_";
    int num = 1;
    int loop = 5;
    int count = 20000;
    List<CreateTopicsResult> resultList = new ArrayList<>();
    for (int i = 0; i < loop; i++) {
      ArrayList<NewTopic> topics = new ArrayList<>();
      int start = num;
      for (int j = 0; j < count; j++) {
        NewTopic newTopic = new NewTopic(topicName + num, 1, (short) 1);
        topics.add(newTopic);
        num++;
      }
      System.out.println(
          "要创建的 topic 个数：" + topics.size() + "   ==>   [" + start + "~" + (num - 1) + "]");
      resultList.add(client.createTopics(topics));
    }

    for (CreateTopicsResult result : resultList) {
      // 阻塞等待新增完成
      result.all().get();
    }
  }

  @Test
  public void clearTestTopic() throws ExecutionException, InterruptedException {
    Boolean continueDel = true;
    while (continueDel) {
      Set<String> topics = client.listTopics().names().get();
      System.out.println("查询到的 topic 个数：" + topics.size());
      List<String> delTopics =
          topics.stream()
              .filter(name -> name.startsWith("heh_test_topic_"))
              .collect(Collectors.toList());
      System.out.println("要删除的 topic 个数：" + delTopics.size());
      if (delTopics.size() == 0) {
        continueDel = false;
      }
      DeleteTopicsResult result = client.deleteTopics(delTopics);
      result.all().get();
    }
  }

  @Test
  public void listTopic() throws ExecutionException, InterruptedException, TimeoutException {
    // 不包含内部topic
    ListTopicsResult result =
        client.listTopics(new ListTopicsOptions().listInternal(false).timeoutMs(60 * 1000));
    Set<String> names = result.names().get();

    System.out.println(names);
    System.out.println("查询到的 topic 总数：" + names.size());
  }

  @Test
  public void descTopic() throws ExecutionException, InterruptedException {
    String name = "first";
    DescribeTopicsResult desc = client.describeTopics(Arrays.asList(name));
    Map<String, TopicDescription> map = desc.all().get();
    TopicDescription topicDescription = map.get(name);
    for (TopicPartitionInfo partition : topicDescription.partitions()) {
      System.out.println("leader 的broker ID：" + partition.leader().id());
    }
    System.out.println(map);

    System.out.println("======================");

    ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, name);
    Map<ConfigResource, Config> configResourceConfigMap = client.describeConfigs(Collections.singleton(configResource)).all().get();
    for (Map.Entry<ConfigResource, Config> configResourceConfigEntry : configResourceConfigMap.entrySet()) {
      System.out.println(configResourceConfigEntry.getKey() + "——" + configResourceConfigEntry.getValue());
    }
  }

  @Test
  public void topicSize() throws ExecutionException, InterruptedException {
    String topicName = "first";
    Set<Integer> brokerIds = new HashSet<>();
    DescribeTopicsResult desc = client.describeTopics(Collections.singletonList(topicName));
    Map<String, TopicDescription> map = desc.all().get();
    TopicDescription topicDescription = map.get(topicName);
    for (TopicPartitionInfo partition : topicDescription.partitions()) {
      System.out.println("leader 的broker ID：" + partition.leader().id());
      brokerIds.add(partition.leader().id());
    }
    long sum = 0;
    DescribeLogDirsResult ret = client.describeLogDirs(brokerIds);
    Map<Integer, Map<String, LogDirDescription>> integerMapMap = ret.allDescriptions().get();
    for (Map.Entry<Integer, Map<String, LogDirDescription>> integerMapEntry : integerMapMap.entrySet()) {
      Map<String, LogDirDescription> value = integerMapEntry.getValue();
      for (Map.Entry<String, LogDirDescription> stringLogDirDescriptionEntry : value.entrySet()) {
        LogDirDescription info = stringLogDirDescriptionEntry.getValue();
        Map<TopicPartition, ReplicaInfo> replicaInfoMap = info.replicaInfos();
        for (Map.Entry<TopicPartition, ReplicaInfo> replicas : replicaInfoMap.entrySet()) {
          if (topicName.equals(replicas.getKey().topic())) {
            sum += replicas.getValue().size();
          }
        }
      }
    }
    System.out.println("总字节:" + sum);
  }

}
