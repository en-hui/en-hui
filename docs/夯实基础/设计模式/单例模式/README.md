# 单例模式

所谓单例模式，就是保证在整个软件系统中，某个类**只能存在一个对象实例**，并且该类只提供一个取得其对象的方法（静态方法）

## 1.饿汉式（静态常量） - 可以使用，可能会内存浪费
>实例步骤：      
1.私有构造器     
2.创建静态对象并实例化        
3.提供一个静态的公共方法       

```puml
/**
 *  饿汉式（静态常量）
 * @author: HuEnhui
 * @date: 2019/12/19 10:44
 */
class Singleton1{
    // 1.私有构造器
    private Singleton1(){}
    // 2.创建静态对象并实例化 
    private final static Singleton1 instance = new Singleton1();
    // 3.向外暴露一个静态的公共方法
    public static Singleton1 getInstance(){
        return instance;
    }
}
```
> 优点:   
写法简单，在类装载的时候就完成了实例化，避免了线程同步问题     
缺点：     
没有达到懒加载，如果不使用这个实例，就会造成内存浪费      
结论：实际开发中可以使用，但可能造成内存浪费      

## 2.饿汉式（静态代码块） - 可以使用，可能会内存浪费
> 实例步骤：     
1.私有构造器    
2.创建静态对象        
3.静态代码块中实例化对象   
4.提供一个静态的公共方法  

```puml
/**
 *  饿汉式（静态代码块）
 * @author: HuEnhui
 * @date: 2019/12/19 10:44
 */
class Singleton2{
    // 1.私有构造器
    private Singleton2(){}
    // 2.类的内部创建静态对象
    private static Singleton2 instance;
    // 3.静态代码块实例化
    static{
        instance = new Singleton2();
    }
    // 4.公共方法返回实例对象
    public static Singleton2 getInstance(){
        return instance;
    }
}

```
> 和第一种一样，只是写法不同，将实例化放在静态代码块中

## 3.懒汉式（线程不安全） - 不能使用
> 实例步骤：     
1.私有构造器     
2.创建静态对象        
3.提供公共方法返回对象        
4.公共方法内判断，对象为空则实例化      

```puml
/** 
 * 懒汉式（线程不安全）
 * @author: HuEnhui
 * @date: 2019/12/19 13:26
 */
class Singleton3{
    // 1.私有构造器
    private Singleton3(){}
    // 2.创建静态对象
    private static Singleton3 instance;
    // 3。提供一个公共方法
    public static Singleton3 getInstance() {
        // 4.公共方法内判断，对象为空则实例化
        if (instance == null) {
            instance = new Singleton3();
        }
        return instance;
    }
}
```
> 优点：   
起到了懒加载的效果       
缺点：     
只能在单线程下使用，多线程环境下，线程1通过if (instance == null)判断还未实例化，线程2也通过这个判断语句，这样就创建了多个实例      
结论：实际开发中，不要使用这种方式

## 4.懒汉式（线程安全，同步方法） - 不推荐使用
> 实例步骤:     
1.私有构造器     
2.创建静态对象        
3.提供同步的静态公共方法       
4.在公共方法中判断，对象为空则实例化     

```puml
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
```
> 优点：       
起到了懒加载的效果，解决了线程安全问题     
缺点：     
效率太低了，所有要调用 静态同步方法 的都要顺序执行      
结论：实际开发中，不推荐使用      

## 5.懒汉式（线程不安全，同步代码块） - 不能使用
```
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
```
> 这种方式的本意是改进第四种方案，解决效率低下问题，但这种方式不能起到线程同步的作用，因为多线程可以同时进入if
结论：实际开发中，不要使用
## 6.双重检查 - 推荐使用
> 1.私有构造器       
2.创建私有静态轻量同步对象      
3.提供公共静态方法      
4.方法内 判断+同步+判断 保证线程安全       

```puml
class Singleton6{
    // 1.私有构造器
    private Singleton6(){}
    // 2.创建私有静态轻量同步对象
    private static volatile instance;
    // 3.提供公共静态方法
    public static Singleton6 getInstance(){
        // 4.方法内 判断+同步+判断 保证线程安全
        if (instance == null) {
            synchronized(Singleton6.class){
                if (instance == null){
                    instance = new Singleton6();
                }
            }
        }
        return instance;
    }
}
```
> 优点：       
起到了懒加载的效果，保证了线程安全，效率较高      
缺点：--     
结论：实际开发中，推荐使用这种单例模式     

## 7.静态内部类 - 推荐使用
静态内部类两个特点：1.加载类的时候，不会立即加载静态内部类 2.调用静态内部类方法时会加载静态内部类，且是线程安全的
> 实例步骤：     
1.私有构造器     
2.私有静态内部类，类中有一个静态对象并实例化     
3.提供公共方法，方法内返回静态内部类的属性对象        

```puml
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
```
> 优点：       
1.类的静态属性只有加载类的时候初始化，所以JVM帮我们保证了线程安全       
2.利用静态内部类特点实现延迟加载       
3.效率较高       
结论：推荐使用

## 8.枚举 - 推荐使用
> 实例步骤：     
1.一个枚举类     
2.一个属性      

```puml
package fun.enhui.design.singleton;

public class Type8 {
    public static void main(String[] args) {
        Singleton8 singleton = Singleton8.INSTANCE;
        Singleton8 singletonTwo = Singleton8.INSTANCE;
        System.out.println(singleton == singletonTwo);
        System.out.println(singleton.equals(singletonTwo));
        singleton.say();
    }
}
/** 
 * 枚举实现单例模式 
 * @author: HuEnhui
 * @date: 2019/12/19 14:43  
 */
enum Singleton8{
    // 1.枚举类        
    // 2.属性     
    INSTANCE;       
    public void say(){      
        System.out.println("hello");        
    }   
}   
```

## JDK中使用单例模式的案例

```puml
package fun.enhui.design.singleton;
public class TestJDKRuntime {
    public static void main(String[] args) {
        // 使用了单例模式，饿汉式
        Runtime runtime;
    }
}

// JDK java.lang包 Runtime类使用饿汉式
    private static Runtime currentRuntime = new Runtime();
    public static Runtime getRuntime() {
        return currentRuntime;
    }
    private Runtime() {}
```

## 单例模式的注意事项和使用细节
1. 单例模式保证了系统内存中该类只存在一个对象，节省了系统资源，
对于一些需要频繁创建销毁的对象，使用单例模式可以提高系统性能
2. 当想实例化一个单例类的对象的时候，要使用相应获取对象的方法，而不是使用new
3. 单例模式使用的场景：需要频繁创建和销毁的对象、创建对象时耗时过多或耗费资源过多，
但又经常用到的对象、工具类对象、频繁访问数据库或文件的对象（比如数据源、session工厂等）
