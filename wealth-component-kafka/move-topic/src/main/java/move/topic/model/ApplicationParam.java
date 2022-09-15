package move.topic.model;

import move.topic.util.MoveTopicLog;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/** 启动参数 */
public class ApplicationParam {
  private static final MoveTopicLog log = new MoveTopicLog(ApplicationParam.class);

  /** kafka连接地址 */
  private String kafka;
  /** schema registry连接地址 */
  private String schemaRegistry;
  /** 是否启用了plain认证 */
  private boolean plain;
  /** plain认证用户名 */
  private String plainUserName;

  private boolean isAddPrefix;
  private String addPrefix;
  private boolean isOverride;

  private boolean autoCheck;
  private String securityProtocol;
  private String saslMechanism;
  /** plain认证密码 */
  private String plainPassword;
  /** 指定消费者组 */
  private String consumerGroup;
  /** 并行数，使用几个线程同时消费、生产 */
  private int parallelCount;
  /** 消费每批次条数 */
  private long pollBatchCount;
  /** 查lag的过滤关键字 */
  private String taskIdsStr;

  private List<Integer> taskIds;

  /**
   * 初始化工作<br>
   * 1.加载配置文件<br>
   * 2.加载工作文件
   *
   * @throws IOException
   */
  public static ApplicationParam initApplicationParam()
      throws IOException, ExecutionException, InterruptedException {
    String moveConfig = System.getProperty("moveConfig");
    if (moveConfig == null || "".equals(moveConfig)) {
      throw new RuntimeException("请设置配置文件位置：-DmoveConfig=xxx");
    }
    ApplicationParam applicationParam = ApplicationParam.parseParam(moveConfig);

    return applicationParam;
  }

  public List<Integer> getTaskIds() {
    return taskIds;
  }

  public void setTaskIds(List<Integer> taskIds) {
    this.taskIds = taskIds;
  }

  /**
   * 根据配置文件加载配置
   *
   * @param moveConfigFile 配置文件路径
   * @return
   * @throws IOException
   */
  public static ApplicationParam parseParam(String moveConfigFile) throws IOException {
    log.info("Start loading startup parameters,read file::" + moveConfigFile);
    InputStream in = new BufferedInputStream(new FileInputStream(moveConfigFile));
    Properties prop = new Properties();
    prop.load(in);

    // 加载配置文件
    ApplicationParam param = new ApplicationParam();
    param.setKafka(prop.getProperty("kafka"));
    param.setSchemaRegistry(prop.getProperty("schemaRegistry"));
    param.setPlain(Boolean.parseBoolean(prop.getProperty("plain")));
    param.setAutoCheck(Boolean.parseBoolean(prop.getProperty("autoCheck")));
    param.setSecurityProtocol(prop.getProperty("securityProtocol"));
    param.setSaslMechanism(prop.getProperty("saslMechanism"));
    param.setPlainUserName(prop.getProperty("plainUserName"));
    param.setPlainPassword(prop.getProperty("plainPassword"));
    param.setConsumerGroup(prop.getProperty("consumerGroup"));
    param.setParallelCount(Integer.parseInt(prop.getProperty("parallelCount")));
    param.setPollBatchCount(Long.parseLong(prop.getProperty("pollBatchCount")));
    param.setAddPrefix(Boolean.parseBoolean(prop.getProperty("isAddPrefix")));
    param.setAddPrefix(prop.getProperty("addPrefix"));
    param.setOverride(Boolean.parseBoolean(prop.getProperty("isOverride")));
    String taskIds = prop.getProperty("taskIds");
    if (taskIds != null && !"".equals(taskIds)) {
      String[] split = taskIds.split(",");
      List<Integer> tasks = new ArrayList<>();
      for (String s : split) {
        int taskId = Integer.parseInt(s);
        tasks.add(taskId);
      }
      param.setTaskIdsStr(taskIds);
      param.setTaskIds(tasks);
    }
    log.info("startup parameters::" + param);
    return param;
  }

  public static ApplicationParam initApplicationParamWithEnv() {
    String kafka = System.getenv("kafka");
    if (kafka == null) {
      throw new RuntimeException("未配置kafka");
    }
    String plain = System.getenv("plain") == null ? "false" : System.getenv("plain");
    String securityProtocol = System.getenv("securityProtocol");
    String saslMechanism = System.getenv("saslMechanism");
    String plainUserName = System.getenv("plainUserName");
    String plainPassword = System.getenv("plainPassword");
    String schemaRegistry = System.getenv("schemaRegistry");

    // 默认不加前缀
    String isAddPrefix =
        System.getenv("isAddPrefix") == null ? "false" : System.getenv("isAddPrefix");
    String prefix = System.getenv("addPrefix");
    String isOverride = System.getenv("isOverride") == null ? "false" : System.getenv("isOverride");

    if (schemaRegistry == null) {
      throw new RuntimeException("未配置schemaRegistry");
    }
    String consumerGroup =
        System.getenv("consumerGroup") == null ? "dp-move-topic" : System.getenv("consumerGroup");
    String parallelCount =
        System.getenv("parallelCount") == null ? "3" : System.getenv("parallelCount");
    String pollBatchCount =
        System.getenv("pollBatchCount") == null ? "5000" : System.getenv("pollBatchCount");
    String taskIds = System.getenv("taskIds");
    ApplicationParam applicationParam = new ApplicationParam();
    applicationParam.setKafka(kafka);
    applicationParam.setPlain(Boolean.parseBoolean(plain));
    applicationParam.setSecurityProtocol(securityProtocol);
    applicationParam.setSaslMechanism(saslMechanism);
    applicationParam.setPlainUserName(plainUserName);
    applicationParam.setPlainPassword(plainPassword);
    applicationParam.setSchemaRegistry(schemaRegistry);
    applicationParam.setConsumerGroup(consumerGroup);
    applicationParam.setParallelCount(Integer.parseInt(parallelCount));
    applicationParam.setPollBatchCount(Long.parseLong(pollBatchCount));
    applicationParam.setAddPrefix(Boolean.parseBoolean(isAddPrefix));
    applicationParam.setAddPrefix(prefix);
    applicationParam.setOverride(Boolean.parseBoolean(isOverride));
    if (taskIds != null && !"".equals(taskIds)) {
      String[] split = taskIds.split(",");
      List<Integer> tasks = new ArrayList<>();
      for (String s : split) {
        int taskId = Integer.parseInt(s);
        tasks.add(taskId);
      }
      applicationParam.setTaskIdsStr(taskIds);
      applicationParam.setTaskIds(tasks);
    }
    log.info("startup parameters::" + applicationParam);
    return applicationParam;
  }

  public int getParallelCount() {
    return parallelCount;
  }

  public void setParallelCount(int parallelCount) {
    this.parallelCount = parallelCount;
  }

  public String getKafka() {
    return kafka;
  }

  public void setKafka(String kafka) {
    this.kafka = kafka;
  }

  public String getSchemaRegistry() {
    return schemaRegistry;
  }

  public void setSchemaRegistry(String schemaRegistry) {
    this.schemaRegistry = schemaRegistry;
  }

  public boolean isPlain() {
    return plain;
  }

  public void setPlain(boolean plain) {
    this.plain = plain;
  }

  public String getPlainUserName() {
    return plainUserName;
  }

  public void setPlainUserName(String plainUserName) {
    this.plainUserName = plainUserName;
  }

  public String getPlainPassword() {
    return plainPassword;
  }

  public void setPlainPassword(String plainPassword) {
    this.plainPassword = plainPassword;
  }

  public String getConsumerGroup() {
    return consumerGroup;
  }

  public void setConsumerGroup(String consumerGroup) {
    this.consumerGroup = consumerGroup;
  }

  public String getSecurityProtocol() {
    return securityProtocol;
  }

  public void setSecurityProtocol(String securityProtocol) {
    this.securityProtocol = securityProtocol;
  }

  public String getSaslMechanism() {
    return saslMechanism;
  }

  public void setSaslMechanism(String saslMechanism) {
    this.saslMechanism = saslMechanism;
  }

  public long getPollBatchCount() {
    return pollBatchCount;
  }

  public void setPollBatchCount(long pollBatchCount) {
    this.pollBatchCount = pollBatchCount;
  }

  public boolean isAutoCheck() {
    return autoCheck;
  }

  public void setAutoCheck(boolean autoCheck) {
    this.autoCheck = autoCheck;
  }

  public String getTaskIdsStr() {
    return taskIdsStr;
  }

  @Override
  public String toString() {
    return "ApplicationParam{"
        + "kafka='"
        + kafka
        + '\''
        + ", schemaRegistry='"
        + schemaRegistry
        + '\''
        + ", plain="
        + plain
        + ", plainUserName='"
        + plainUserName
        + '\''
        + ", isAddPrefix="
        + isAddPrefix
        + ", addPrefix='"
        + addPrefix
        + '\''
        + ", isOverride="
        + isOverride
        + ", autoCheck="
        + autoCheck
        + ", securityProtocol='"
        + securityProtocol
        + '\''
        + ", saslMechanism='"
        + saslMechanism
        + '\''
        + ", plainPassword='"
        + plainPassword
        + '\''
        + ", consumerGroup='"
        + consumerGroup
        + '\''
        + ", parallelCount="
        + parallelCount
        + ", pollBatchCount="
        + pollBatchCount
        + ", taskIdsStr='"
        + taskIdsStr
        + '\''
        + ", taskIds="
        + taskIds
        + '}';
  }

  public boolean isOverride() {
    return isOverride;
  }

  public void setOverride(boolean override) {
    isOverride = override;
  }

  public boolean isAddPrefix() {
    return isAddPrefix;
  }

  public void setAddPrefix(boolean addPrefix) {
    isAddPrefix = addPrefix;
  }

  public String getAddPrefix() {
    return addPrefix;
  }

  public void setAddPrefix(String addPrefix) {
    this.addPrefix = addPrefix;
  }

  public void setTaskIdsStr(String taskIdsStr) {
    this.taskIdsStr = taskIdsStr;
  }
}
