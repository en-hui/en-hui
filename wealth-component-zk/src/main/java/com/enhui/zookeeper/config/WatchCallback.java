package com.enhui.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * 节点是否存在的回调
 * 获取数据后的回调
 * 节点的watch
 *
 * @Author 胡恩会
 * @Date 2021/11/9 22:26
 **/
public class WatchCallback implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {
    ZooKeeper zk;
    MyConf myConf;

    CountDownLatch existLatch = new CountDownLatch(1);

    public WatchCallback(ZooKeeper zk, MyConf myConf) {
        this.zk = zk;
        this.myConf = myConf;
    }

    /**
     * 节点是否存在的回调
     **/
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println(path + "::exists 的回调:rc=" + rc + ",ctx=" + ctx + ",stat=" + stat);
        if (stat != null) {
            zk.getData("/AppConf", this, this, "ctx上下文");
        }
    }

    /**
     * 获取数据后的回调
     **/
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println(path + "::getData 的回调:rc=" + rc + ",ctx=" + ctx + ",stat=" + stat + ",data=" + new String(data));
        if (data != null) {
            String s = new String(data);
            myConf.setMySqlAddress(s);

            System.out.println("获取节点数据成功，节点判断的阻塞解除");
            existLatch.countDown();
        } else {
            System.out.println("节点存在，但数据为null");
        }
    }

    /**
     * 阻塞，节点存在的回调中，取消阻塞
     **/
    public void aWait() {
        // 该目录全称是 /testConf/AppConf  因为基于/testConf连接的zk
        zk.exists("/AppConf", this, this, "ctx上下文");
        try {
            System.out.println("节点判断的阻塞");
            existLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 节点的watch
     **/
    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                System.out.println("创建节点事件：" + event.toString());
                zk.getData("/AppConf", this, this, "ctx上下文");
                break;
            case NodeDeleted:
                // 容忍性
                myConf.setMySqlAddress("");
                System.out.println("重新设置阻塞，删除节点事件：" + event.toString());
                existLatch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                System.out.println("节点数据修改事件：" + event.toString());
                zk.getData("/AppConf", this, this, "ctx上下文");
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
}
