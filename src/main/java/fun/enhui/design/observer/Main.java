package fun.enhui.design.observer;

/**
 * @Author 胡恩会
 * @Date 2020/6/18 21:40
 **/
public class Main {
    public static void main(String[] args) {
        Child child = new Child();
        child.add(new DogObserver());
        child.add(new FatherObserver());
        child.isCry();
    }
}
