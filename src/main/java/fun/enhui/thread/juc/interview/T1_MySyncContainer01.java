package fun.enhui.thread.juc.interview;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.实现一个容器，提供两个方法add size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示
 * <p>
 * 两组wait() notify()
 * 两组countDownLatch
 * 两组LockSupport
 *
 * @author 胡恩会
 * @date 2020/8/148:28
 */
public class T1_MySyncContainer01 {

    public static void main(String[] args) {
        final T1_MySyncContainer01 lock = new T1_MySyncContainer01();

        MySyncContainer01<Integer> list = new MySyncContainer01();
        new Thread(() -> {
            synchronized (lock) {
                try {
                    if (list.size() != 5) {
                        System.out.println("t2 wait");
                        lock.wait();
                    }
                    System.out.println(Thread.currentThread().getName() + "监测到个数新增到五个了，t2 结束");
                    System.out.println("t1 notify");
                    lock.notify();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();

        new Thread(() -> {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    try {
                        list.add(i);
                        System.out.println(Thread.currentThread().getName() + " add " + i);
                        if (i == 5) {
                            System.out.println("t2 notify");
                            lock.notify();
                            System.out.println("t1 wait");
                            lock.wait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }, "t1").start();

    }
}

/**
 * 我的同步容器
 *
 * @param <T>
 */
class MySyncContainer01<T> {
    volatile List list = new ArrayList<>();

    public void add(Object o) {
        list.add(o);
    }

    public int size() {
        return list.size();
    }
}