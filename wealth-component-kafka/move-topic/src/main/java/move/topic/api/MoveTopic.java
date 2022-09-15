package move.topic.api;

import move.topic.model.ApplicationParam;
import move.topic.model.TopicMapping;
import move.topic.service.move.TopicMover;
import move.topic.util.KafkaHelper;
import move.topic.util.MoveTopicLog;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MoveTopic {
  private static final MoveTopicLog log = new MoveTopicLog(MoveTopic.class);

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
    moveTopic(kafkaHelper, applicationParam);
    kafkaHelper.close();
  }

  public static void moveTopic(KafkaHelper kafkaHelper, ApplicationParam applicationParam) {
    try {
      List<TopicMapping> topicMappings = TopicMapping.initTopicMapping(applicationParam);
      if (topicMappings.size() < 1) {
        log.info("没有要迁移的数据，程序正常退出");
        return;
      }
      final AtomicInteger increment = new AtomicInteger();
      // 如果要迁移的topic数量比设置并行数小，使用迁移topic数量做并行度
      int parallelCount = Integer.min(applicationParam.getParallelCount(), topicMappings.size());
      ThreadPoolExecutor executor =
          new ThreadPoolExecutor(
              parallelCount,
              parallelCount,
              1000,
              TimeUnit.MILLISECONDS,
              new LinkedBlockingQueue<>(topicMappings.size()),
              r -> new Thread(r, "move-topic-" + increment.incrementAndGet()),
              new ThreadPoolExecutor.AbortPolicy());
      for (TopicMapping topicMapping : topicMappings) {
        TopicMover topicMover = new TopicMover(applicationParam, topicMapping, kafkaHelper);
        executor.execute(topicMover);
      }
      long completedTaskCount = executor.getCompletedTaskCount();
      while (completedTaskCount != topicMappings.size()) {
        Thread.sleep(5000);
        log.debug("主线程等待工作线程结束...");
        completedTaskCount = executor.getCompletedTaskCount();
      }
      log.info("全部同步结束，topic迁移个数为：" + completedTaskCount);
      executor.shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
