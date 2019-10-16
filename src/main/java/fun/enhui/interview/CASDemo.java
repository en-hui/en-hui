package fun.enhui.interview;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS简介
 * @Author: HuEnhui
 * @Date: 2019/10/16 17:53
 */
public class CASDemo {

    public static void main(String[] args) {
        AtomicInteger number = new AtomicInteger(10);
        // cas就是compareAndSet()方法   比较并交换
        // 当工作内存和主内存值一样，就进行赋值
        System.out.println(number.compareAndSet(10,15)+"\tcurrent number value:"+number);
        System.out.println(number.compareAndSet(10,20)+"\tcurrent number value:"+number);
    }
}
