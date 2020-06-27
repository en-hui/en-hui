# 模板方法

模板方法的角色：    
1.抽象类：包括一个模板方法（定义一个算法的轮廓或骨架）    
2.具体子类：实现了抽象类的抽象方法    

抽象类：    
```java
/**
 * 抽象类
 * @Author 胡恩会
 * @Date 2020/6/27 16:59
 **/
public abstract class BasePageLoadTemplate {
    public void templateMethod(){
        init();
        loadContent();
    }
    public abstract void init();
    public abstract void loadContent();
}
```
具体子类：    
```java
/**
 * 具体子类
 * @Author 胡恩会
 * @Date 2020/6/27 17:04
 **/
public class PageLoad extends BasePageLoadTemplate {
    @Override
    public void init() {
        System.out.println("页面初始化");
    }

    @Override
    public void loadContent() {
        System.out.println("加载页面内容");
    }
}
```
程序入口：    
```java
public class Main {
    public static void main(String[] args) {
        BasePageLoadTemplate baseTemplate = new PageLoad();
        baseTemplate.templateMethod();
    }
}
```

![Alt](./img/TemplateMethod.png)