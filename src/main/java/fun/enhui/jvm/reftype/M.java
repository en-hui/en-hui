package fun.enhui.jvm.reftype;

/**
 * 演示垃圾回收的时机
 *
 * @author 胡恩会
 * @date 2020/8/21 21:48
 */
public class M {
    /**
     * 垃圾回收时被调用
     *
     * @param
     * @return void
     * @author 胡恩会
     * @date 2020/8/21 21:49
     **/
    @Override
    protected void finalize() {
        System.out.println("垃圾回收了");
    }
}
