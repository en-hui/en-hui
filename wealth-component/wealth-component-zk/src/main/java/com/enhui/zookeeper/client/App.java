package com.enhui.zookeeper.client;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

/**
 * zk客户端基本操作与watch体验
 *
 * @author huenhui
 * @date 2022-04-30
 */
@Slf4j
public class App {
  ZooKeeper zk = null;
  String rootPath = "/";
  String testPath = "/huenhui";

  /**
   * 事先创建zk客户端连接
   *
   * @throws IOException
   */
  @Before
  public void before() throws IOException {
    zk = new ZooKeeper("39.100.158.215:2181", 5000, new SessionWatch());
  }

  /**
   * 判断节点是否存在、删除节点、创建节点、获取该节点数据、重新设置节点数据
   *
   * @throws InterruptedException
   * @throws KeeperException
   */
  @Test
  public void testZkClient() throws InterruptedException, KeeperException {
    List<String> children = zk.getChildren(rootPath, false);
    log.info("根目录『{}』下有子节点：{}", rootPath, children);
    Stat exists = zk.exists(testPath, false);
    log.info("判断节点『{}』是否存在，结果为：{}", testPath, exists);
    if (exists != null) {
      zk.delete(testPath, -1);
      log.info("『{}』节点已存在，删除该节点", testPath);
    }
    zk.create(testPath, "胡恩会节点数据".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    log.info("创建节点：『{}』", testPath);
    byte[] data = zk.getData(testPath, new NodeWatch(), new Stat());
    log.info("『{}』节点的数据为：『{}』", testPath, new String(data));

    // get时，放了一个watch，所以第一次set会触发回调
    Stat stat1 = zk.setData(testPath, "数据1".getBytes(), 0);
    // 第二次不会触发回调，如果需要，则应该再第一次回调时，重新添加watch
    Stat stat2 = zk.setData(testPath, "数据2".getBytes(), stat1.getVersion());

    // 异步回调方式，获取数据后，再回调方法中写逻辑。而非阻塞等待数据返回
    System.out.println("----- async start----------");
    zk.getData(
        "/huenhui",
        false,
        new AsyncCallback.DataCallback() {
          @Override
          public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
            System.out.println("----- async callback----------");
            System.out.println(new String(bytes));
          }
        },
        "abc");
    System.out.println("----- async over----------");

    Thread.sleep(5000);
  }

  public static void main(String[] args) throws Exception {
    // zk 是有session概念的，没有连接池的概念
    // watch:观察、回调
    // 第一类 new zk 时候，传入的watch，这个watch是session级别的，跟path和node没有关系
    CountDownLatch countDownLatch = new CountDownLatch(1);
    ZooKeeper zk =
        new ZooKeeper(
            "39.100.158.215:2181",
            3000,
            new Watcher() {
              @Override
              public void process(WatchedEvent event) {
                Event.KeeperState state = event.getState();
                Event.EventType type = event.getType();
                String path = event.getPath();
                log.info("创建 session 级别watch,事件为：{}", event.toString());

                switch (state) {
                  case Unknown:
                    break;
                  case Disconnected:
                    break;
                  case NoSyncConnected:
                    break;
                  case SyncConnected:
                    countDownLatch.countDown();
                    log.info("同步连接");
                    break;
                  case AuthFailed:
                    break;
                  case ConnectedReadOnly:
                    break;
                  case SaslAuthenticated:
                    break;
                  case Expired:
                    break;
                  case Closed:
                    break;
                }

                switch (type) {
                  case None:
                    break;
                  case NodeCreated:
                    break;
                  case NodeDeleted:
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
            });

    countDownLatch.await();
    ZooKeeper.States state = zk.getState();
    switch (state) {
      case CONNECTING:
        System.out.println("连接中");
        break;
      case ASSOCIATING:
        break;
      case CONNECTED:
        System.out.println("已连接");
        break;
      case CONNECTEDREADONLY:
        break;
      case CLOSED:
        break;
      case AUTH_FAILED:
        break;
      case NOT_CONNECTED:
        break;
    }

    String pathName =
        zk.create("/xxoo", "oldData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

    Stat stat = new Stat();
    byte[] node =
        zk.getData(
            "/xxoo",
            new Watcher() {
              @Override
              public void process(WatchedEvent event) {
                System.out.println("getData watch: " + event.toString());

                try {
                  // true:default watch 被重新注册 -- new zk的那个watch
                  zk.getData("/xxoo", this, stat);
                } catch (KeeperException e) {
                  e.printStackTrace();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            },
            stat);

    System.out.println(new String(node));
    // 触发回调
    Stat stat1 = zk.setData("/xxoo", "newData".getBytes(), 0);
    // 还会触发吗？   不会，因为事件回调是一次性的，需要再次注册事件
    Stat stat2 = zk.setData("/xxoo", "newData01".getBytes(), stat1.getVersion());

    System.out.println("----- async start----------");
    zk.getData(
        "/xxoo",
        false,
        new AsyncCallback.DataCallback() {
          @Override
          public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
            System.out.println("----- async callback----------");
            System.out.println(new String(bytes));
          }
        },
        "abc");
    System.out.println("----- async over----------");

    Thread.sleep(5000);
  }
}
