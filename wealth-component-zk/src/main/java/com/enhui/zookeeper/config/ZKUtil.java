package com.enhui.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @Author 胡恩会
 * @Date 2021/11/9 22:00
 **/
public class ZKUtil {
    private static ZooKeeper zk;

    /**
     * 可以指定根目录，所有操作在该目录下
     **/
    private static String address = "39.100.158.215:2181,106.14.158.60:2181,47.95.121.160:2181/testConf";

    private static DefaultWatch defaultWatch = new DefaultWatch();

    private static CountDownLatch latch = new CountDownLatch(1);

    public static ZooKeeper getZk() {
        try {
            defaultWatch.setLatch(latch);
            zk = new ZooKeeper(address, 1000, defaultWatch);
            System.out.println("阻塞，等待连接完成得到zk对象后解除::CountDownLatch-await");
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
}
