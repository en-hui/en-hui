# 桥接模式

>桥接（Bridge）模式的定义如下：     
将抽象与实现分离，使它们可以独立变化。     
它是用组合关系代替继承关系来实现，从而降低了抽象和实现这两个可变维度的耦合度。

桥接模式的角色：    
1.抽象化角色：定义抽象类，并包含一个对实现化对象的引用。    
2.扩展抽象化角色：是抽象化角色的子类，实现父类中的业务方法，并通过组合关系调用实现化角色中的业务方法。     
3.实现化角色：定义实现化角色的接口，供扩展抽象化角色调用。     
4.具体实现化角色：给出实现化角色接口的具体实现。    

> 举个例子，在小学课本中，有数学和语文。而又分为六个年级。      
将学科和年纪分别维护，可以避免类爆炸(2*6个类)      

抽象化角色：      
```java
/**
 * 抽象化角色-学科
 *
 * @Author 胡恩会
 * @Date 2020/6/26 16:40
 **/
public abstract class Subject {
    Grade grade;
    public Subject(Grade grade){
        this.grade = grade;
    }
    public abstract String getSubjectName();
}
```
扩展抽象化角色：     
```java
/**
 * 扩展抽象化角色-语文
 *
 * @Author 胡恩会
 * @Date 2020/6/26 16:43
 **/
public class ChineseSubject extends Subject{
    public ChineseSubject(Grade grade) {
        super(grade);
    }
    @Override
    public String getSubjectName() {
        return grade.getGradeName()+"语文";
    }
}

/**
 * 扩展抽象化角色-数学
 *
 * @Author 胡恩会
 * @Date 2020/6/26 16:43
 **/
public class MathSubject extends Subject {
    public MathSubject(Grade grade) {
        super(grade);
    }
    @Override
    public String getSubjectName() {
        return grade.getGradeName()+"数学";
    }
}
```
实现化角色：     
```java
/**
 * 实现化角色-年级
 *
 * @Author 胡恩会
 * @Date 2020/6/26 16:40
 **/
public interface Grade {
    String getGradeName();
}
```
具体实现化角色：     
```java
/**
 * 具体实现化角色-一年级
 *
 * @Author 胡恩会
 * @Date 2020/6/26 16:44
 **/
public class OneGrade implements Grade{
    @Override
    public String getGradeName() {
        return "一年级";
    }
}

/**
 * 具体实现化角色-二年级
 *
 * @Author 胡恩会
 * @Date 2020/6/26 16:44
 **/
public class TwoGrade implements Grade {
    @Override
    public String getGradeName() {
        return "二年级";
    }
}
```

![Alt](./img/Bridge.png)