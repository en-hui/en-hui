package com.enhui.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
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
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.requests.DescribeLogDirsResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdminTest {

  public static final String KAFKA_PLAIN_JAAS_CONF =
          "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\""
                  + " password=\"%s\";";
  AdminClient client = null;
  long start = 0L;

  @BeforeEach
  public void before() {
    start = System.currentTimeMillis();
    Properties props = new Properties();
    props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092");
    if (true) {
      props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
      props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
      props.put(
              SaslConfigs.SASL_JAAS_CONFIG, String.format(KAFKA_PLAIN_JAAS_CONF, "admin", "admin"));
    }

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
    int loop = 10;
    int count = 5000;
    List<CreateTopicsResult> resultList = new ArrayList<>();
    for (int i = 0; i < loop; i++) {
      try{
        ArrayList<NewTopic> topics = new ArrayList<>();
        int start = num;
        for (int j = 0; j < count; j++) {
          NewTopic newTopic = new NewTopic(topicName + num, 1, (short) 1);
          topics.add(newTopic);
          num++;
        }
        System.out.println(
                "要创建的 topic 个数：" + topics.size() + "   ==>   [" + start + "~" + (num - 1) + "]");
        CreateTopicsResult result = client.createTopics(topics, new CreateTopicsOptions().timeoutMs(10 * 60 * 1000));
        // 阻塞等待新增完成.顺序执行，避免都超时
        result.all().get();
      }catch (Exception e) {
        e.printStackTrace();
        System.out.println("进行下一轮....");
      }

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
        client.listTopics(new ListTopicsOptions().timeoutMs(10 * 60 * 1000));
    Set<String> names = result.names().get();
    DescribeTopicsResult desc = client.describeTopics(names);
    Map<String, TopicDescription> map = desc.all().get();
    for (String name : names) {
      TopicDescription topicDescription = map.get(name);
      int partitionCount = topicDescription.partitions().size();
      System.out.println("分区个数：" + partitionCount + "；topic名称：" + name);
    }
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
  public void topicLeaderSize() throws ExecutionException, InterruptedException {
    // 获取全部broker
    Collection<Node> nodes = client.describeCluster().nodes().get();
    List<Integer> brokerIds = nodes.stream().map(Node::id).collect(Collectors.toList());
    System.out.println("broker ID：" + brokerIds);

    // 获取全部分区leader
    Set<String> allTopicNames = client.listTopics().names().get();
    List<String> realTopicNames = allTopicNames.stream().filter(topicName -> topicName.startsWith("heh_")).collect(Collectors.toList());
    Map<String, TopicDescription> map1 = client.describeTopics(realTopicNames).all().get();
    Map<String,Map<Integer,Integer>> topicAndPartitionLeader = new HashMap<>();
    for (Map.Entry<String, TopicDescription> entry : map1.entrySet()) {
      String key = entry.getKey();
      Map<Integer,Integer> partitionAndBrokerId = new HashMap<>();
      for (TopicPartitionInfo partition : entry.getValue().partitions()) {
        int brokerId = partition.leader().id();
        int partition1 = partition.partition();
        partitionAndBrokerId.put(partition1,brokerId);
      }
      topicAndPartitionLeader.put(key,partitionAndBrokerId);
    }

    // topic:  partition,size
    Map<String,Long> resultMap = new HashMap<>();
    Map<Integer, Map<String, LogDirDescription>> brokerAndInfo = client.describeLogDirs(brokerIds).allDescriptions().get();
    for (Map.Entry<Integer, Map<String, LogDirDescription>> entry1 : brokerAndInfo.entrySet()) {
      Integer brokerId = entry1.getKey();
      Map<String, LogDirDescription> dirAndInfo = entry1.getValue();
      for (Map.Entry<String, LogDirDescription> entry2 : dirAndInfo.entrySet()) {
        LogDirDescription topicPartitionAndInfo = entry2.getValue();
        Map<TopicPartition, ReplicaInfo> replicaInfoMap = topicPartitionAndInfo.replicaInfos();
        System.out.println("副本数量==" + replicaInfoMap.size());
        for (Map.Entry<TopicPartition, ReplicaInfo> replicas : replicaInfoMap.entrySet()) {
          Map<Integer, Integer> integerIntegerMap1 = topicAndPartitionLeader.get(replicas.getKey().topic());
          if (integerIntegerMap1 != null && brokerId.equals(integerIntegerMap1.get(replicas.getKey().partition()))) {
            Long oldSize = resultMap.get(replicas.getKey().topic());
            if (oldSize == null) {
              resultMap.put(replicas.getKey().topic(),replicas.getValue().size());
            }else {
              resultMap.put(replicas.getKey().topic(),oldSize + replicas.getValue().size());
            }
          }
        }
      }
    }

    System.out.println(resultMap);
    System.out.println(resultMap.size());
  }

  @Test
  public void topicAllSize() throws ExecutionException, InterruptedException {
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
