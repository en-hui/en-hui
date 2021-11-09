package com.enhui.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author 胡恩会
 * @Date 2021/11/9 22:13
 **/
public class TestConf {
    ZooKeeper zk;

    @Before
    public void before() {
        zk = ZKUtil.getZk();
    }

    @After
    public void after() {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getConf() {
        MyConf myConf = new MyConf();
        WatchCallback watchCallback = new WatchCallback(zk, myConf);

        watchCallback.aWait();
        // 场景1：节点不存在
        // 场景2：节点存在

        while (true) {
            if ("".equals(myConf.getMySqlAddress())) {
                System.out.println("配置丢了，进入阻塞，等待");
                watchCallback.aWait();
            } else {
                System.out.println("从zk获取到的配置：" + myConf.getMySqlAddress());
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
