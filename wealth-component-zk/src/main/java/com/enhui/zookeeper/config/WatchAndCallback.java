package com.enhui.zookeeper.config;

import com.alibaba.fastjson.JSONObject;
import java.util.concurrent.CountDownLatch;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/** 节点watch、状态回调、数据回调 */
@Slf4j
@Data
public class WatchAndCallback
    implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

  private ZooKeeper zk;
  private AppConf appConf;
  private String confPath = "/appConf";
  private CountDownLatch existLatch = new CountDownLatch(1);

  public WatchAndCallback(ZooKeeper zk) {
    this.zk = zk;
  }

  @Override
  public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
    String dataStr = new String(data);
    log.info("获取数据的回调方法::获取到数据：{}", dataStr);
    appConf = JSONObject.parseObject(dataStr, AppConf.class);
    existLatch.countDown();
  }

  @Override
  public void processResult(int rc, String path, Object ctx, Stat stat) {
    if (stat == null) {
      log.info("是否存在的回调方法::节点不存在，阻塞住");
    }
    log.info("是否存在的回调方法::节点存在，获取节点数据");
    zk.getData(confPath, this, this, "");
  }

  @Override
  public void process(WatchedEvent event) {
    log.info(
        "节点操作回调::path:{},type:{},state:{}", event.getPath(), event.getType(), event.getState());
    switch (event.getType()) {
      case None:
        break;
      case NodeCreated:
        log.info("节点被创建::重新获取配置");
        zk.getData(confPath, this, this, "");
        break;
      case NodeDeleted:
        log.info("节点被删除::不同方案：将配置设置为空？使用原有配置？");
        appConf = null;
        existLatch = new CountDownLatch(1);
        break;
      case NodeDataChanged:
        log.info("节点被修改::重新获取配置");
        zk.getData(confPath, this, this, "");
        break;
      case NodeChildrenChanged:
        break;
      case DataWatchRemoved:
        break;
      case ChildWatchRemoved:
        break;
      case PersistentWatchRemoved:
        break;
    }
  }

  public void aWait() {
    zk.exists(confPath, this, this, "");
    try {
      existLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void initConf() throws InterruptedException, KeeperException {
    AppConf conf = new AppConf();
    conf.setOpenAuth(true);
    conf.setServiceA("localhost:8081");
    conf.setServiceB("localhost:8082");
    Stat exists = zk.exists(confPath, false);
    if (exists == null) {
      zk.create(
          confPath,
          JSONObject.toJSONString(conf).getBytes(),
          ZooDefs.Ids.OPEN_ACL_UNSAFE,
          CreateMode.PERSISTENT);
      log.info("程序初始化::初始化配置节点");
    }
  }
}
