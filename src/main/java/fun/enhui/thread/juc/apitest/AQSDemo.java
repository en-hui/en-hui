package fun.enhui.thread.juc.apitest;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 胡恩会
 * @date 2020/8/21 16:40
 */
public class AQSDemo {


    public static void main(String[] args) {
        // 公平锁
        Lock lock = new ReentrantLock();
        try {
            ThreadLocal threadLocal = new ThreadLocal();
            lock.lock();
        }finally {
            lock.unlock();
        }

    }
}
