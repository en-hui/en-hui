package move.topic.util;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import move.topic.model.ApplicationParam;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaHelper {
  public static final String KAFKA_PLAIN_JAAS_CONF =
      "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\""
          + " password=\"%s\";";
  private final boolean plain;
  private final String securityProtocol;
  private final String saslMechanism;
  private final String plainUserName;
  private final String plainPassword;
  private final String kafka;
  private final String schemaRegistry;
  private final String groupId;
  private final long pollBatchCount;
  private volatile KafkaProducer<Object, Object> stringProducer = null;
  private volatile KafkaProducer<Object, Object> avroProducer = null;
  private volatile AdminClient adminClient = null;

  public KafkaHelper(ApplicationParam param) {
    this.plain = param.isPlain();
    this.securityProtocol = param.getSecurityProtocol();
    this.saslMechanism = param.getSaslMechanism();
    this.plainUserName = param.getPlainUserName();
    this.plainPassword = param.getPlainPassword();
    this.kafka = param.getKafka();
    this.schemaRegistry = param.getSchemaRegistry();
    this.groupId = param.getConsumerGroup();
    this.pollBatchCount = param.getPollBatchCount();
  }

  public Properties getConsumerProperties(String deserializer) {
    if (KafkaAvroDeserializer.class.getName().equals(deserializer)) {
      return getConsumerProperties(true);
    } else {
      return getConsumerProperties(false);
    }
  }

  public Properties getConsumerProperties(boolean isAvro) {
    Properties properties = new Properties();
    // 基本配置
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka);

    if (isAvro) {
      properties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
      properties.setProperty(
          ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
      properties.setProperty(
          ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    } else {
      properties.setProperty(
          ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
      properties.setProperty(
          ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    }
    if (plain) {
      properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
      properties.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
      properties.put(
          SaslConfigs.SASL_JAAS_CONFIG,
          String.format(KAFKA_PLAIN_JAAS_CONF, plainUserName, plainPassword));
    }
    // 消费者的细节
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, groupId);
    // kafka is MQ;is storage (存储)   所以要指定从哪里开始消费
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    // POLL 拉取数据，弹性、按需，设置每次拉取多少（根据消费能力设定）
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, String.valueOf(pollBatchCount));
    return properties;
  }

  public AdminClient getAdminClient() {
    if (adminClient == null) {
      synchronized (KafkaHelper.class) {
        if (adminClient == null) {
          Properties properties = getCommonProperties();
          adminClient = AdminClient.create(properties);
        }
      }
    }
    return adminClient;
  }

  public Properties getCommonProperties() {
    Properties properties = new Properties();
    properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka);
    if (plain) {
      properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
      properties.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
      properties.put(
          SaslConfigs.SASL_JAAS_CONFIG,
          String.format(KAFKA_PLAIN_JAAS_CONF, plainUserName, plainPassword));
    }
    return properties;
  }

  public KafkaProducer<Object, Object> getStringProducer() {
    if (stringProducer == null) {
      synchronized (KafkaHelper.class) {
        if (stringProducer == null) {
          Properties properties = getCommonProperties();
          properties.setProperty(
              ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
          properties.setProperty(
              ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
          stringProducer = new KafkaProducer<Object, Object>(properties);
        }
      }
    }
    return stringProducer;
  }

  public KafkaProducer<Object, Object> getAvroProducer() {
    if (avroProducer == null) {
      synchronized (KafkaHelper.class) {
        if (avroProducer == null) {
          Properties properties = getCommonProperties();
          properties.setProperty(
              ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
          properties.setProperty(
              ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
          properties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
          avroProducer = new KafkaProducer<Object, Object>(properties);
        }
      }
    }
    return avroProducer;
  }

  public void close() {
    if (stringProducer != null) {
      stringProducer.close();
    }
    if (avroProducer != null) {
      avroProducer.close();
    }
    if (adminClient != null) {
      adminClient.close();
    }
  }
}
