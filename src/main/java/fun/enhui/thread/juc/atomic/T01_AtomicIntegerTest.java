package fun.enhui.thread.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * 测试 AtomicInteger的递增方法
 * cas操作
 */
public class T01_AtomicIntegerTest {

    private volatile int value;

    public static void main(String[] args) {
        T01_AtomicIntegerTest test = new T01_AtomicIntegerTest();
        // 类的成员变量，int 类型默认为 0
        System.out.println("类的成员变量不赋初始值，默认值为:" + test.value);
        // 不赋初始值时，直接使用成员变量 0
        // 1.使用AtomicInteger 实现 i++
        AtomicInteger integer = new AtomicInteger();
        int andIncrement = integer.getAndIncrement();
        System.out.println("AtomicInteger 的 getAndIncrement: " + andIncrement);

        // 2.使用LongAdder
        LongAdder longAdder = new LongAdder();
        longAdder.increment();
        System.out.println("LongAdder 的 increment:" + longAdder);

    }
}
