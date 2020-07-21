package fun.enhui.thread.base;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * synchronized 和 ReentrantLock 可重入性验证
 * 可重入锁：进入同步方法后，调用另一个同步方法
 *
 * @Author: HuEnhui
 * @Date: 2019/10/17 16:45
 */
public class T03_ReentrancyTest {
    public static void main(String[] args) {
        PhoneA PhoneA = new PhoneA();

        System.out.println("验证可重入锁-----synchronized");
        new Thread(() -> {
            PhoneA.sendSMS();
        }, "A").start();
        new Thread(() -> {
            PhoneA.sendSMS();
        }, "B").start();

        // 暂停一会线程
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        // ==========================
        System.out.println("验证可重入锁-----ReentrantLock");
        Thread c = new Thread(PhoneA, "C");
        Thread d = new Thread(PhoneA, "D");
        c.start();
        d.start();
    }

}
class PhoneA implements Runnable {
    // ======================== synchronized =========================================
    public synchronized void sendSMS() {
        System.out.println(Thread.currentThread().getName() + "\t 发送短信");
        // 获取外部锁后，到内层后，直接获取内部锁
        sendEmail();
    }

    public synchronized void sendEmail() {
        System.out.println(Thread.currentThread().getName() + "\t ###发送邮件");
    }

    // ======================== ReentrantLock =========================================
    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        get();
    }

    public void get() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t get方法");
            set();
        } finally {
            lock.unlock();
        }
    }

    public void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t ###set方法");
        } finally {
            lock.unlock();
        }
    }

    // ======================== ReentrantLock =========================================


}
