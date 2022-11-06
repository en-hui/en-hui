package com.enhui.netty.my;

import java.io.IOException;

/**
 * 模拟netty的骨架
 * 多selector 多线程
 */
public class MyNetty {

    public static void main(String[] args) throws IOException {
        EventLoopGroup boss = new EventLoopGroup(1, "bossThread-");
        EventLoopGroup worker = new EventLoopGroup(3, "workerThread-");
        boss.setWorker(worker);

        boss.bind(9999);

    }

}
