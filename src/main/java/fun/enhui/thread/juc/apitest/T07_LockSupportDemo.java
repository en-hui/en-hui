package fun.enhui.thread.juc.apitest;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport使用
 * 将线程阻塞或停止阻塞
 * LockSupport.part()   LockSupport.unPank()
 * 线程A打印0-9的数字
 * 线程B打印65-69（A-E）
 * 线程A打印到5后打印B，B打印结束后继续打印A
 *
 * @author 胡恩会
 * @date 2020/8/1321:17
 */
public class T07_LockSupportDemo {
    public static void main(String[] args) {

        Thread a = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                if (i == 5) {
                    // 阻塞当前线程
                    LockSupport.park();
                }
            }
        }, "A");
        a.start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println('A' + i);
            }
            // 取消阻塞 线程 a
            LockSupport.unpark(a);
        }, "B").start();
    }
}

