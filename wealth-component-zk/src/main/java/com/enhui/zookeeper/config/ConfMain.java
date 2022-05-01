package com.enhui.zookeeper.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;

/**
 * 案例：zk做配置中心的使用
 *
 * @author huenhui
 */
@Slf4j
public class ConfMain {

  @Test
  public void testUseConf() throws InterruptedException, KeeperException {
    ZkConf zkConf = new ZkConf(ConfType.REGISTRY_CONF);
    log.info("模拟使用配置中心的配置");
    while (true) {
      System.out.println();
      log.info("客户端使用配置");
      RegistryConf conf = (RegistryConf) zkConf.getConf();
      if (conf == null) {
        log.info("配置节点不存在，程序阻塞");
        zkConf.aWait();
      } else {
        log.info("配置2::服务A的ip地址:{}", conf.getServiceA());
        log.info("配置3::服务B的ip地址:{}", conf.getServiceB());
      }
      System.out.println();
      Thread.sleep(3000);
    }
  }
}
