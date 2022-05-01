package com.enhui.zookeeper;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;

@Slf4j
public class ZkUtil {
  public static ZooKeeper getZkClient() {
    ZooKeeper zooKeeper = null;
    try {
      zooKeeper =
          new ZooKeeper(
              "39.100.158.215:2181/zkStudy",
              3000,
              (watchedEvent) -> {
                log.info("zkStudy====session watch");
              });
    } catch (IOException e) {
      log.error("zkStudy====ZooKeeper 客户端连接失败", e);
    }
    return zooKeeper;
  }

  public static void close(ZooKeeper zk) {
    try {
      zk.close();
    } catch (InterruptedException e) {
      log.error("zkStudy====ZooKeeper 客户端关闭失败", e);
    }
  }
}
