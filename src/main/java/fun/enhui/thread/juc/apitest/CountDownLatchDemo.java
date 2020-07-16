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
