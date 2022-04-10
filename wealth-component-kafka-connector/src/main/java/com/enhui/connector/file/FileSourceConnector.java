package com.enhui.connector.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kafka源码直接粘贴的
 * 加注释及打印，方便理解
 */
public class FileSourceConnector extends SourceConnector {
  private static final Logger log = LoggerFactory.getLogger(FileSourceTask.class);
  public static final String TOPIC_CONFIG = "topic";
  public static final String FILE_CONFIG = "file";
  public static final String TASK_BATCH_SIZE_CONFIG = "batch.size";

  public static final int DEFAULT_TASK_BATCH_SIZE = 2000;

  private static final ConfigDef CONFIG_DEF = new ConfigDef()
          .define(FILE_CONFIG, ConfigDef.Type.STRING, null, ConfigDef.Importance.HIGH, "Source filename. If not specified, the standard input will be used")
          .define(TOPIC_CONFIG, ConfigDef.Type.LIST, ConfigDef.Importance.HIGH, "The topic to publish data to")
          .define(TASK_BATCH_SIZE_CONFIG, ConfigDef.Type.INT, DEFAULT_TASK_BATCH_SIZE, ConfigDef.Importance.LOW,
                  "The maximum number of records the Source task can read from file one time");

  private String filename;
  private String topic;
  private int batchSize;

  @Override
  public String version() {
    return AppInfoParser.getVersion();
  }

  @Override
  public void start(Map<String, String> props) {
    System.out.println("FileSourceConnector::start——从source配置文件得到的参数：" + props);
    AbstractConfig parsedConfig = new AbstractConfig(CONFIG_DEF, props);
    filename = parsedConfig.getString(FILE_CONFIG);
    List<String> topics = parsedConfig.getList(TOPIC_CONFIG);
    if (topics.size() != 1) {
      throw new ConfigException("'topic' in FileStreamSourceConnector configuration requires definition of a single topic");
    }
    topic = topics.get(0);
    batchSize = parsedConfig.getInt(TASK_BATCH_SIZE_CONFIG);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return FileSourceTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    ArrayList<Map<String, String>> configs = new ArrayList<>();
    // Only one input stream makes sense.
    Map<String, String> config = new HashMap<>();
    if (filename != null)
      config.put(FILE_CONFIG, filename);
    config.put(TOPIC_CONFIG, topic);
    config.put(TASK_BATCH_SIZE_CONFIG, String.valueOf(batchSize));
    System.out.println("FileSourceConnector::taskConfigs——参数处理结果：" + config);
    configs.add(config);
    return configs;
  }

  @Override
  public void stop() {
    // Nothing to do since FileStreamSourceConnector has no background monitoring.
  }

  @Override
  public ConfigDef config() {
    System.out.println("connector中返回了一个参数，{}" + CONFIG_DEF.names());
    return CONFIG_DEF;
  }
}
