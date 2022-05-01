package com.enhui.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

@Slf4j
public class ZkUtil {
  public static ZooKeeper getZkClient() {
    ZooKeeper zooKeeper = null;
    CountDownLatch initLatch = new CountDownLatch(1);
    try {
      zooKeeper =
          new ZooKeeper(
              "39.100.158.215:2181/zkStudy",
              3000,
              (watchedEvent) -> {
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                  log.info("客户端连接完成，阻塞关闭");
                  initLatch.countDown();
                }
              });
    } catch (IOException e) {
      log.error("zkStudy====ZooKeeper 客户端连接失败", e);
    }
    log.info("阻塞::等待zk客户端连接完成");
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
