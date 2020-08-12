# JUC下CountDownLatch、CyclicBarrier、Semaphore
# CountDownLatch
让一些线程阻塞直到另外一些完成后才被唤醒
> 案例：秦国灭六国，统一。   
秦国派六支军队去灭敌，只有当六个军队全部胜利，才算统一六国。

```java
package fun.enhui.thread.juc.apitest;
import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch演示：
 * 1.初始化传一个参数（整数值）
 * 2.每次执行countDown()，相当于-1
 * 3.当整数值为0，await才放行
 *
 * @Author: 胡恩会
 * @Date: 2019/10/18 10:31
 */
public class CountDownLatchDemo {
    public static final Integer NUMBER = 6;

    public static void main(String[] args) throws InterruptedException {
        // 构造函数，给定一个次数
        CountDownLatch countDownLatch = new CountDownLatch(NUMBER);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t被灭**");
                // 使用 countDown() 相当于将次数-1
                countDownLatch.countDown();
                // 线程名称为六国名称
            }, CountryEnum.forEach_CountryEnum(i).getRetMessage()).start();
        }
        // 次数为0之前，阻塞当前线程.当减少到零 await() 才放行
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t秦国统一");
    }
    enum CountryEnum {

        ONE(1,"齐"),
        TWO(2,"楚"),
        THREE(3,"燕"),
        FOUR(4,"赵"),
        FIVE(5,"魏"),
        SIX(6,"韩");

        private Integer retCode;
        private String retMessage;

        CountryEnum(Integer retCode, String retMessage) {
            this.retCode = retCode;
            this.retMessage = retMessage;
        }

        public Integer getRetCode() {
            return retCode;
        }

        public String getRetMessage() {
            return retMessage;
        }

        /**
         *  根据retCode获取message
         * @author: HuEnhui
         * @date: 2019/10/18 10:42
         * @param index
         * @return: fun.enhui.interview.Enum.CountryEnum
         */
        public static CountryEnum forEach_CountryEnum(int index) {
            CountryEnum[] myArray = CountryEnum.values();
            for (CountryEnum element :myArray) {
                if(index == element.retCode) {
                    return element;
                }
            }
            return null;
        }
    }
}

```

# CyclicBarrier
CyclicBarrier的字面意思是可循环(Cyclic)使用的屏障(barrier).
它要做的事情是,让一组线程到达一个屏障(也可以叫做同步点)时被阻塞,直到最后一个线程到达屏障时,屏障才会开门,
所有被屏障拦截的线程才会继续干活,线程进入屏障通过CyclicBarrier的await()方法.
> 与CountDownLatch类似，CountDownLatch做减法，初始值为n，减到0执行主方法    
CyclicBarrier做加法，初始值为0，加到n执行主方法
```java
package fun.enhui.thread.juc.apitest;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier演示：
 * 1.初始化传递两个参数，屏障数量 和 要做的事
 * 2.每次调用await相当于+1
 * 3.加到屏障次数后，执行要做的事
 * API : public CyclicBarrier(int parties, Runnable barrierAction)
 * @Author: 胡恩会
 * @Date: 2019/10/18 10:12
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        // 1.初始化
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,() -> {
            System.out.println("****召唤神龙");
        });
        for (int i = 0; i < 7; i++) {
            final int tempInt = i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t收集到第"+tempInt+"颗龙珠");
                try {
                    // 每次调用相当于次数+1  次数到n后，执行主方法
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}

```

# Semaphore（信号量）
信号量主要用于两个目的
1. 用于多个共享资源的互斥使用
2. 用于并发线程数的控制
```
// 构造函数，给定一个资源个数（资源可以循环使用）  eg:停车位个数
public Semaphore(int permits) 
// 每次调用相当于资源数-1     eg：停车位被占，车位数-1
public void acquire() throws InterruptedException
// 每次调用相当于资源数+1     eg：车离开车位，车位数+1
public void release()
```

```java
package fun.enhui.thread.juc.apitest;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore，资源循环使用
 * @Author: 胡恩会
 * @Date: 2019/10/18 11:14
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        // 三个车位（三个资源）
        Semaphore semaphore = new Semaphore(3);
        // 六个车要找车位
        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                try {
                    // 资源-1
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "\t抢到车位");
                    // 停车三秒
                    try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
                    System.out.println(Thread.currentThread().getName() + "\t--离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    // 资源+1
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}

```
