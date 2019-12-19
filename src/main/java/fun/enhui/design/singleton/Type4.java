package fun.enhui.design.singleton;

public class Type4 {

    public static void main(String[] args) {
        Singleton4 singleton = Singleton4.getInstance();
        Singleton4 singletonTwo = Singleton4.getInstance();
        System.out.println(singleton == singletonTwo);
        System.out.println(singleton.equals(singletonTwo));
    }
}
/**
 * 懒汉式（线程安全，同步方法）
 * @author: HuEnhui
 * @date: 2019/12/19 13:46
 */
class Singleton4{
    // 1.私有构造器
    private Singleton4(){}
    // 2.创建私有静态对象
    private static Singleton4 instance;
    // 3.提供一个同步的静态公共方法
    public static synchronized Singleton4 getInstance(){
        // 4.方法内判断，对象为空时实例化对象
        if (instance == null) {
            instance = new Singleton4();
        }
        return instance;
    }
}
