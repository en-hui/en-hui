package fun.enhui.thread.juc.atomic;

import sun.misc.Unsafe;

import java.time.temporal.ValueRange;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * cas操作
 */
public class AtomicIntegerTest {

    private volatile int value;

    public static void main(String[] args) {
        AtomicIntegerTest test = new AtomicIntegerTest();
        // 类的成员变量，int 类型默认为 0
        System.out.println(test.value);
        // 不赋初始值时，直接使用成员变量 0
        AtomicInteger integer = new AtomicInteger();
        integer.getAndIncrement();

        Unsafe unsafe = Unsafe.getUnsafe();
    }
}
