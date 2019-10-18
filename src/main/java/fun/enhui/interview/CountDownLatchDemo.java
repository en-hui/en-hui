package fun.enhui.interview;

import fun.enhui.interview.Enum.CountryEnum;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch演示，作用，初始化传一个参数  整数值  每次执行countDown()，相当于-1    当整数值为0，await才放行
 * @Author: HuEnhui
 * @Date: 2019/10/18 10:31
 */
public class CountDownLatchDemo {
    public static final Integer NUMBER = 6;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(NUMBER);
        for (int i = 1; i <= 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t被灭**");
                countDownLatch.countDown();
            }, CountryEnum.forEach_CountryEnum(i).getRetMessage()).start();
        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"\t秦国统一");
    }
}
