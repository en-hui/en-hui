package move.topic.model;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import move.topic.util.KafkaHelper;
import move.topic.util.MoveTopicLog;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class TopicMapping {
  private static final MoveTopicLog log = new MoveTopicLog(TopicMapping.class);
  private String sourceTopic;
  private String destTopic;
  private String deserializer;
  private String serializer;

  public static List<TopicMapping> initTopicMapping(ApplicationParam applicationParam)
      throws ExecutionException, InterruptedException, IOException {
    List<TopicMapping> topicMappings;
    if (applicationParam.isAutoCheck()) {
      log.info("迁移列表——自动识别迁移列表，查看kafka的topic列表进行过滤");
      topicMappings = TopicMapping.loadTopicByKafka(applicationParam);
    } else {
      String topicMapping = System.getProperty("topicMappingPath");
      if (topicMapping == null || "".equals(topicMapping)) {
        throw new RuntimeException("配置检查失败：未开启自动检查迁移，请设置配置文件位置：-DtopicMappingPath=xxx");
      }
      log.info("迁移列表——根据指定的配置文件进行迁移: " + topicMapping);
      topicMappings = TopicMapping.loadTopicMappingByFile(topicMapping);
    }
    log.msg("【———————————————————迁移明细展示开始———————————————————————】");
    for (TopicMapping mapping : topicMappings) {
      log.msg(
          "【"
              + mapping.getSourceTopic()
              + "】==>【"
              + mapping.getDestTopic()
              + "】序列化方式为【"
              + (KafkaAvroSerializer.class.getName().equals(mapping.getSerializer())
                  ? "avro"
                  : "string")
              + "】");
    }
    log.msg("【———————————————————迁移明细展示结束———————————————————————】");
    return topicMappings;
  }
  /**
   * 根据配置文件加载 topic映射
   *
   * @param topicMappingFile 文件路径
   */
  public static List<TopicMapping> loadTopicMappingByFile(String topicMappingFile)
      throws IOException {
    File file = new File(topicMappingFile);
    List<TopicMapping> list = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        TopicMapping mapping = parseLine(line);
        if (mapping != null) {
          list.add(mapping);
        }
      }
    }
    return list;
  }

  /**
   * 根据topic名称规范获取迁移列表（规则根据业务来）
   *
   * @param param kafka服务地址、认证方式等
   * @return
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public static List<TopicMapping> loadTopicByKafka(ApplicationParam param)
      throws ExecutionException, InterruptedException {
    Properties props = new Properties();
    props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, param.getKafka());
    if (param.isPlain()) {
      props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, param.getSecurityProtocol());
      props.put(SaslConfigs.SASL_MECHANISM, param.getSaslMechanism());
      props.put(
          SaslConfigs.SASL_JAAS_CONFIG,
          String.format(
              KafkaHelper.KAFKA_PLAIN_JAAS_CONF,
              param.getPlainUserName(),
              param.getPlainPassword()));
    }
    AdminClient client = AdminClient.create(props);

    // 不包含内部topic
    ListTopicsResult result = client.listTopics(new ListTopicsOptions().timeoutMs(10 * 60 * 1000));
    Set<String> names = result.names().get();
    ArrayList<TopicMapping> topicMappings = new ArrayList<>();
    TopicMapping topicMapping;
    for (String name : names) {
      if (name.contains("dptask_")
          && !name.contains("debezium-heartbeat.dptask_")
          && !name.contains("v2_dptask_")) {
        if (name.contains("schema-changes")) {
          topicMapping = new TopicMapping();
          topicMapping.setSourceTopic(name);
          topicMapping.setDestTopic(getDestTopicBySource(name));
          topicMapping.setSerializer(StringSerializer.class.getName());
          topicMapping.setDeserializer(StringDeserializer.class.getName());
          topicMappings.add(topicMapping);
        } else {
          topicMapping = new TopicMapping();
          topicMapping.setSourceTopic(name);
          topicMapping.setDestTopic(getDestTopicBySource(name));
          topicMapping.setSerializer(KafkaAvroSerializer.class.getName());
          topicMapping.setDeserializer(KafkaAvroDeserializer.class.getName());
          topicMappings.add(topicMapping);
        }
      }
      if (name.contains("config_connect_sink_dp")
          || name.contains("config_connect_source_dp")
          || name.contains("offset_connect_sink_dp")
          || name.contains("offset_connect_source_dp")
          || name.contains("status_connect_sink_dp")
          || name.contains("status_connect_source_dp")) {
        topicMapping = new TopicMapping();
        topicMapping.setSourceTopic(name);
        topicMapping.setDestTopic(getDestTopicBySource(name));
        topicMapping.setSerializer(StringSerializer.class.getName());
        topicMapping.setDeserializer(StringDeserializer.class.getName());
        topicMappings.add(topicMapping);
      }
    }
    return topicMappings;
  }

  public static String getDestTopicBySource(String sourceTopic) {
    if (sourceTopic == null) {
      return sourceTopic;
    }
    return sourceTopic.replace("_", "-").replace(".", "-");
  }

  public static TopicMapping parseLine(String line) {
    if (line.contains("source,dest,serializer")) {
      log.info("file head::source,dest,serializer");
      return null;
    }
    String[] split = line.split(",");
    if (split.length == 2 || split.length == 3) {
      log.info("file data::" + line);
      TopicMapping mapping = new TopicMapping();
      String sourceTopic = split[0];
      String destTopic = split[1];
      if (sourceTopic == null
          || "".equals(sourceTopic)
          || destTopic == null
          || "".equals(destTopic)) {
        throw new RuntimeException(fileErrorMsg());
      }
      mapping.setSourceTopic(sourceTopic);
      mapping.setDestTopic(destTopic);
      String serAndDeser = split[2];
      if (serAndDeser == null || "".equals(serAndDeser) || "string".equalsIgnoreCase(serAndDeser)) {
        mapping.setSerializer(StringSerializer.class.getName());
        mapping.setDeserializer(StringDeserializer.class.getName());
      } else if ("avro".equalsIgnoreCase(serAndDeser)) {
        mapping.setSerializer(KafkaAvroSerializer.class.getName());
        mapping.setDeserializer(KafkaAvroDeserializer.class.getName());
      } else {
        throw new RuntimeException(fileErrorMsg());
      }
      return mapping;
    } else {
      throw new RuntimeException(fileErrorMsg());
    }
  }

  public static String fileErrorMsg() {
    return "请保证topicMappingPath文件内容正确，以逗号分割。"
        + "第一行为表头，内容为：source,dest,serializer。"
        + "第一列为【迁移前topic】，第二列为【迁移后topic】，第三列为【序列化方式：string或avro，可忽略不写，则当做string处理】。";
  }

  public String getSourceTopic() {
    return sourceTopic;
  }

  public void setSourceTopic(String sourceTopic) {
    this.sourceTopic = sourceTopic;
  }

  public String getDestTopic() {
    return destTopic;
  }

  public void setDestTopic(String destTopic) {
    this.destTopic = destTopic;
  }

  public String getDeserializer() {
    return deserializer;
  }

  public void setDeserializer(String deserializer) {
    this.deserializer = deserializer;
  }

  public String getSerializer() {
    return serializer;
  }

  public void setSerializer(String serializer) {
    this.serializer = serializer;
  }
}
