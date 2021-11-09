package com.enhui.zookeeper.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * zk 客户端基本操作
 *
 * @Author 胡恩会
 * @Date 2021/11/7 21:46
 **/
public class App {
    public static void main(String[] args) throws Exception {
        // zk 是有session概念的，没有连接池的概念
        // watch:观察、回调
        // 第一类 new zk 时候，传入的watch，这个watch是session级别的，跟path和node没有关系
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("39.100.158.215:2181,106.14.158.60:2181,47.95.121.160:2181",
                3000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                Event.KeeperState state = event.getState();
                Event.EventType type = event.getType();
                String path = event.getPath();
                System.out.println("new zk watch: " + event.toString());

                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        countDownLatch.countDown();
                        System.out.println("connected");
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
                System.out.println("ing...");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("ed...");
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

        String pathName = zk.create("/xxoo", "oldData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        Stat stat = new Stat();
        byte[] node = zk.getData("/xxoo", new Watcher() {
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
        }, stat);

        System.out.println(new String(node));
        // 触发回调
        Stat stat1 = zk.setData("/xxoo", "newData".getBytes(), 0);
        // 还会触发吗？   不会，因为事件回调是一次性的，需要再次注册事件
        Stat stat2 = zk.setData("/xxoo", "newData01".getBytes(), stat1.getVersion());

        System.out.println("----- async start----------");
        zk.getData("/xxoo", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                System.out.println("----- async callback----------");
                System.out.println(new String(bytes));
            }
        }, "abc");
        System.out.println("----- async over----------");

        Thread.sleep(5000);
    }


}
