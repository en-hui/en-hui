package com.enhui.zookeeper.lock;

import com.enhui.zookeeper.ZkUtil;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 分布式锁<br>
 * 使用 临时节点+序列特性<br>
 * （1）临时节点：客户端主动删除或断开连接会导致节点删除，触发回调<br>
 * （2）序列节点：同一节点名称，会以不同编号的方式同时存在<br>
 *
 * <p>1.每台机器去抢锁（创建一个属于自己的临时节点）<br>
 * 每次创建节点后，去查询父节点下所有的子节点，判断当前是否为第一个<br>
 * （1）抢到锁：获取父目录的所有子节点，手动排序，如果当前节点为第一个，则抢锁成功<br>
 * （2）没抢到：对前一个节点添加监听事件，当前一个节点删除时，重新排序抢锁（对前一个节点加事件，避免同时回调所有客户端）<br>
 *
 * @author huenhui
 */
@Slf4j
@Data
public class ZkLock
    implements Watcher,
        AsyncCallback.StringCallback,
        AsyncCallback.Children2Callback,
        AsyncCallback.StatCallback {
  /** 一种业务占一个锁路径 */
  private String lockName;

  private String parentPath;
  private String lockPath;
  private String currentNode;
  private CountDownLatch latch = new CountDownLatch(1);

  private ZooKeeper zk;

  public ZkLock(LockType lockType) {
    this.lockName = lockType.getLockName();
    this.parentPath = "/lock" + "/" + lockName;
    this.lockPath = parentPath + "/" + lockName;
    this.zk = ZkUtil.getZkClient();
  }

  public void tryLock() throws Exception {
    log.info("zkStudy====『{}』 创建节点,尝试加锁", Thread.currentThread().getName());
    zk.create(
        lockPath,
        "".getBytes(),
        ZooDefs.Ids.OPEN_ACL_UNSAFE,
        CreateMode.EPHEMERAL_SEQUENTIAL,
        this,
        "");
    latch.await();
  }

  public void unLock() throws Exception {
    log.info("zkStudy====『{}』删除节点，释放锁", Thread.currentThread().getName());
    zk.delete(currentNode, -1);
    ZkUtil.close(zk);
  }

  @Override
  public void process(WatchedEvent event) {
    switch (event.getType()) {
      case None:
        break;
      case NodeCreated:
        break;
      case NodeDeleted:
        log.info("zkStudy====『{}』节点被删除", event.getPath());
        zk.getChildren(parentPath, this, this, "");
        break;
      case NodeDataChanged:
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

  /** 创建节点的回调 */
  @Override
  public void processResult(int rc, String path, Object ctx, String name) {
    if (name != null) {
      log.info("zkStudy====『{}』 创建临时节点成功——『{}』", Thread.currentThread().getName(), name);
      currentNode = name;
      zk.getChildren(parentPath, false, this, "");
    }
  }
  /** 获取子节点的回调 */
  @SneakyThrows
  @Override
  public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
    Collections.sort(children);
    String current = this.currentNode.replace(parentPath + "/", "");
    int index = children.indexOf(current);
    log.info("zkStudy====全部的子节点：『{}』；；当前节点『{}』的位置『{}』", children, current, index);
    if (index == 0) {
      latch.countDown();
      zk.setData(parentPath, Thread.currentThread().getName().getBytes(), -1);
      log.info(
          "zkStudy====『{}』抢锁成功，将父节点『{}』的数据设置为当前线程名称，后续可实现重入功能",
          Thread.currentThread().getName(),
          parentPath);
    } else {
      zk.exists(parentPath + "/" + children.get(index - 1), this, this, "");
    }
  }

  /** 是否存在的回调 */
  @Override
  public void processResult(int rc, String path, Object ctx, Stat stat) {
    log.info("是否存在的回调");
  }
}
