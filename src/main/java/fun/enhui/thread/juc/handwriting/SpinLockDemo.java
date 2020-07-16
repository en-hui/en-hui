package fun.enhui.thread.juc.handwriting;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 使用 AtomicReference 和 cas 实现自旋锁
 * @Author: 胡恩会
 * @Date: 2019/10/17 17:37
 */
public class SpinLockDemo {
    /**
     * 原子引用线程
     **/
    AtomicReference<Thread> atomicReference = new AtomicReference<>();
    /**
     * 加锁
     **/
    public void myLock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "\t尝试加锁。。。");
        // 主内存为null，则修改，否则循环等待
        while(!atomicReference.compareAndSet(null,thread)){

        }
    }
    /**
     * 解锁
     **/
    public void myUnLock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "\t解锁。。。");
        atomicReference.compareAndSet(thread,null);
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(()->{
            spinLockDemo.myLock();
            System.out.println(Thread.currentThread().getName() + "\t获得锁，正在执行。。。");
            // 暂停一会线程
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
            spinLockDemo.myUnLock();
        },"A").start();

        new Thread(()->{
            spinLockDemo.myLock();
            System.out.println(Thread.currentThread().getName() + "\t获得锁，正在执行。。。");
            // 暂停一会线程
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            spinLockDemo.myUnLock();
        },"B").start();
    }
}
