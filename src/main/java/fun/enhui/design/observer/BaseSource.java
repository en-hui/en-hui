package fun.enhui.design.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 胡恩会
 * @Date 2020/6/18 22:01
 **/
public abstract class BaseSource {
    List<BaseObserver> observers = new ArrayList<>();

    public void add(BaseObserver observer) {
        this.observers.add(observer);
    }

    public void remove(BaseObserver observer) {
        this.observers.remove(observer);
    }

    public abstract void notifyObserver();
}
