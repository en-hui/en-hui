package fun.enhui.jvm.reftype;

import java.io.IOException;

/**
 * 普通的引用，即强引用。
 * 只要有强引用指向某块内存，垃圾回收器就一定不会回收这块区域
 *
 * @author 胡恩会
 * @date 2020/8/21 21:50
 */
public class T01_NormalReferenceDemo {
    public static void main(String[] args) throws IOException {
        // 强引用指向对象，不会被垃圾回收，引用取消，对象可以被回收
        M m = new M();
        // 注释后永远不会被回收
        m = null;
        System.gc();

        // 阻塞观察垃圾回收效果
        System.in.read();
    }
}
