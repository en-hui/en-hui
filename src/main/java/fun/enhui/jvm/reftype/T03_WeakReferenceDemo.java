package fun.enhui.jvm.reftype;

import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;

/**
 * 弱引用 一般用在容器中
 * 当一个对象被弱引用指向时，只要遇到gc，就会被回收
 *
 * @author 胡恩会
 * @date 2020/8/21 22:16
 */
public class T03_WeakReferenceDemo {
    public static void main(String[] args) {
        // m 指向一个弱引用对象，弱引用对象指向一个对象
        WeakReference<M> m = new WeakReference<>(new M());

        System.out.println(m.get());
        System.gc();
        System.out.println(m.get());

        // ThreadLocal 中的 Entry 继承自 WeakReference<ThreadLocal<?>> 去读源码
        ThreadLocal<M> tl = new ThreadLocal<>();
        tl.set(new M());
        // remove 避免内存泄露
        tl.remove();
    }
}
