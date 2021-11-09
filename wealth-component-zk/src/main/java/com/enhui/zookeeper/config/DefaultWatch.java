package com.enhui.zookeeper.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * new zk 时候，传入的watch，这个watch是session级别的，跟path和node没有关系
 *
 * @Author 胡恩会
 * @Date 2021/11/9 22:02
 **/
public class DefaultWatch implements Watcher {

    CountDownLatch latch;

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void process(WatchedEvent event) {
        System.out.println("new zk watch: " + event.toString());

        switch (event.getState()) {
            case Unknown:
                break;
            case Disconnected:
                break;
            case NoSyncConnected:
                break;
            case SyncConnected:
                System.out.println("连接已完成，解除阻塞::CountDownLatch-countDown");
                latch.countDown();
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
    }
}
