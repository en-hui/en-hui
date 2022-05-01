package com.enhui.zookeeper.config;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 案例：zk做配置中心的使用
 *
 * @author huenhui
 */
@Slf4j
public class ConfMain {

  ZooKeeper zk = null;
  /** 阻塞等待zk客户端连接完成的栅栏 */
  CountDownLatch initZkLatch = new CountDownLatch(1);

  @Before
  public void init() throws IOException, InterruptedException {
    zk =
        new ZooKeeper(
            "39.100.158.215:2181/conf",
            5000,
            (event) -> {
              if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                log.info("客户端连接完成，阻塞关闭");
                initZkLatch.countDown();
              }
            });
    log.info("阻塞::等待zk客户端连接完成");
    initZkLatch.await();
  }

  @After
  public void destroy() throws InterruptedException {
    zk.close();
  }

  @Test
  public void testUseConf() throws InterruptedException, KeeperException {
    WatchAndCallback watchAndCallback = new WatchAndCallback(zk);
    log.info("程序启动，模拟初始化配置");
    watchAndCallback.initConf();
    log.info("模拟使用配置中心的配置");
    while (true) {
      System.out.println();
      log.info("客户端使用配置");
      AppConf appConf = watchAndCallback.getAppConf();
      if (appConf == null) {
        log.info("配置节点不存在，程序阻塞");
        watchAndCallback.aWait();
      } else {
        log.info("配置1::是否开启权限认证:{}", appConf.isOpenAuth());
        log.info("配置2::服务A的ip地址:{}", appConf.getServiceA());
        log.info("配置3::服务B的ip地址:{}", appConf.getServiceB());
      }
      System.out.println();
      Thread.sleep(3000);
    }
  }
}
