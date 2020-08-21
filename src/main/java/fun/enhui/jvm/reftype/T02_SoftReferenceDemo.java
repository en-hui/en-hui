package fun.enhui.jvm.reftype;

import java.lang.ref.SoftReference;

/**
 * 软引用  主要用于缓存
 * 测试启动时设置参数：堆内存为20M
 * VM options：-Xms20M -Xmx20M
 * 当一个对象被软引用指向时，只有当堆内存不够用时，才会回收这个对象
 *
 * @author 胡恩会
 * @date 2020/8/21 21:56
 */
public class T02_SoftReferenceDemo {
    public static void main(String[] args) {
        // m 指向一个软引用对象，软引用对象指向了一个字节数组
        SoftReference<byte[]> m = new SoftReference(new byte[1024 * 1024 * 10]);

        System.out.println(m.get());

        // 堆内存足够时，执行垃圾回收，看是否会被回收
        System.gc();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(m.get());

        // 再分配一个数组，heap装不下，这时候系统会垃圾回收，先回收一次，如果不够，会把软引用干掉
        byte[] b = new byte[1024 * 1024 * 10];
        System.out.println(m.get());

    }
}
