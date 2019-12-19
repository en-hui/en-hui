package fun.enhui.design.singleton;

public class Type7 {
    public static void main(String[] args) {
        Singleton7 singleton = Singleton7.getInstance();
        Singleton7 singletonTwo = Singleton7.getInstance();
        System.out.println(singleton == singletonTwo);
        System.out.println(singleton.equals(singletonTwo));
    }
}
/** 
 * 静态内部类
 * @author: HuEnhui
 * @date: 2019/12/19 14:31
 */
class Singleton7{
    // 1.私有构造器
    private Singleton7(){}
    // 2.私有静态内部类,类中有一个静态对象并实例化
    private static class SingletonInstance{
        private static final Singleton7 INSTANCE = new Singleton7();
    }
    // 3.提供公共方法,返回静态内部类的属性对象
    public static Singleton7 getInstance(){
        return SingletonInstance.INSTANCE;
    }
}
