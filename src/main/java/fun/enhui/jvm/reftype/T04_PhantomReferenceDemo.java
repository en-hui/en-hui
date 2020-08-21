package fun.enhui.jvm.reftype;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.LinkedList;
import java.util.List;

/**
 * 虚引用 主要用于管理堆外内存
 * 测试启动时设置参数：堆内存为20M
 * VM options：-Xms20M -Xmx20M
 *
 * @author 胡恩会
 * @date 2020/8/21 22:32
 */
public class T04_PhantomReferenceDemo {
    private static final List<Object> LIST = new LinkedList<>();

    private static final ReferenceQueue<M> QUEUE = new ReferenceQueue<>();

    public static void main(String[] args) {
        PhantomReference<M> phantomReference = new PhantomReference<>(new M(), QUEUE);

        // 一直创建对象 每个对象占 1M 内存
        new Thread(() -> {
            while (true) {
                LIST.add(new byte[1024 * 1024]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                // 虚引用的值是get不到的
                System.out.println(phantomReference.get());
            }
        }).start();

        // 虚引用指向的对象被回收，会向队列中添加被回收的对象
        new Thread(() -> {
            while (true) {
                Reference<? extends M> poll = QUEUE.poll();
                if (poll != null) {
                    System.out.println("---虚引用对象被jvm回收了---" + poll);
                }
            }
        }).start();
    }
}
