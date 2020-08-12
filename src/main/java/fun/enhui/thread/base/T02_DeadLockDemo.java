package fun.enhui.thread.base;

import java.util.concurrent.TimeUnit;

/**
 * 死锁演示
 * 死锁是指两个或者以上的 进程/线程 在执行过程中,
 * 因争夺资源而造成的一种相互等待的现象,
 * 若无外力干涉那他们都将无法推进下去
 *
 * @Author: HuEnhui
 * @Date: 2019/10/22 13:26
 */
public class T02_DeadLockDemo {
    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";
        new Thread(new HoldThread(lockA, lockB), "threadA").start();
        new Thread(new HoldThread(lockB, lockA), "threadB").start();
    }
}

class HoldThread implements Runnable {

    private String lockA;

    private String lockB;

    public HoldThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName() + "\t 自己持有锁" + lockA + "尝试获取" + lockB);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + "\t 自己持有锁" + lockB + "尝试获取" + lockA);
            }
        }
    }
}
