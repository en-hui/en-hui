package move.topic.api;

import move.topic.model.ApplicationParam;
import move.topic.util.KafkaHelper;
import move.topic.util.MoveTopicLog;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Dashboard {
  private static final int CHECK_LAG = 1;
  private static final int MOVE_TOPIC = 5;
  private static final int VALIDATE_MOVE = 9;
  private static final int QUIT = 0;
  private static final MoveTopicLog log = new MoveTopicLog(Dashboard.class);
  private static final Scanner sc = new Scanner(System.in);

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
    while (true) {
      log.msg("【============================================================================】");
      log.msg("【==============您可以输入对应数字完成以下功能(启动程序前请检查配置文件)===============】");
      log.msg("【==============0:退出当前程序==================================================】");
      log.msg("【==============1:查询存在lag的topic(可以配置过滤关键字)===========================】");
      log.msg("【==============5:迁移topic数据(可以手动指定或使用程序固定过滤规则)==================】");
      //      log.msg("【==============9:校验数据迁移(Beta 消费topic全部数据，数据量过大可能会内存溢出)======】");
      log.msg("【============================================================================】");
      int in = sc.nextInt();
      switch (in) {
        case CHECK_LAG:
          CheckLag.checkLag(kafkaHelper, applicationParam);
          break;
        case MOVE_TOPIC:
          MoveTopic.moveTopic(kafkaHelper, applicationParam);
          break;
          //        case VALIDATE_MOVE:
          //          DataValidate.dataValidate(kafkaHelper, applicationParam);
          //          break;
        case QUIT:
          kafkaHelper.close();
          log.msg("根据用户输入，程序正常退出");
          System.exit(0);
          break;
        default:
          log.msg("请输入正确的选项");
          break;
      }
    }
  }
}
