package fun.enhui.interview;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁：进入同步方法后，调用另一个同步方法
 * @Author: HuEnhui
 * @Date: 2019/10/17 16:45
 */
public class ReenterLockDemo {
    public static void main(String[] args) {
        Phone phone = new Phone();

        System.out.println("可同步锁-----synchronized");
        new Thread(()->{
            phone.sendSMS();
        },"A").start();
        new Thread(()->{
            phone.sendSMS();
        },"B").start();

        // 暂停一会线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println();
        // ==========================
        System.out.println("可同步锁-----ReentrantLock");
        Thread c = new Thread(phone,"C");
        Thread d = new Thread(phone,"D");
        c.start();
        d.start();
    }
}

class Phone implements Runnable{
    // ======================== synchronized =========================================
    public synchronized void sendSMS(){
        System.out.println(Thread.currentThread().getName() + "\t 发送短信");
        // 获取外部锁后，到内层后，直接获取内部锁
        sendEmail();
    }

    public synchronized void sendEmail(){
        System.out.println(Thread.currentThread().getName() + "\t ###发送邮件");
    }

    // ======================== ReentrantLock =========================================
    Lock lock = new ReentrantLock();
    @Override
    public void run() {
        get();
    }
    public void get(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t get方法");
            set();
        }finally {
            lock.unlock();
        }
    }
    public void set(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t ###set方法");
        }finally {
            lock.unlock();
        }
    }

    // ======================== ReentrantLock =========================================


}
