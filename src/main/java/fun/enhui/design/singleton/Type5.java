package fun.enhui.design.singleton;

public class Type5 {

    public static void main(String[] args) {
        Singleton5 singleton = Singleton5.getInstance();
        Singleton5 singletonTwo = Singleton5.getInstance();
        System.out.println(singleton == singletonTwo);
        System.out.println(singleton.equals(singletonTwo));
    }
}
/**
 *  懒汉式（线程不安全，同步代码块）
 * @author: HuEnhui
 * @date: 2019/12/19 14:02
 */
class Singleton5{
    // 1.私有构造器
    private Singleton5(){}
    // 2.创建私有的静态对象
    private static Singleton5 instance;
    // 3.提供静态的公共方法
    public static Singleton5 getInstance(){
        // 4.在公共方法中判断，对象为空则在同步代码块中进行实例化
        if (instance == null){
            synchronized (Singleton5.class) {
                instance = new Singleton5();
            }
        }
        return null;
    }

}
