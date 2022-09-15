package move.topic.util;

import java.time.LocalDateTime;

public class MoveTopicLog {
  private static final boolean debug = false;

  private static final String INFO_FORMAT = "[%s] [%s] [%s] [%s]";
  private static final String MSG_FORMAT = "[%s] [%s] [%s]";
  private final Class clazz;

  public MoveTopicLog(Class clazz) {
    this.clazz = clazz;
  }

  public void info(String msg) {
    String threadName = Thread.currentThread().getName();
    LocalDateTime now = LocalDateTime.now();
    System.out.printf((INFO_FORMAT) + "%n", now, threadName, msg, clazz);
  }

  public void debug(String msg) {
    if (debug) {
      info(msg);
    }
  }

  public void msg(String msg) {
    String threadName = Thread.currentThread().getName();
    LocalDateTime now = LocalDateTime.now();
    System.out.printf((MSG_FORMAT) + "%n", now, threadName, msg);
  }
}
