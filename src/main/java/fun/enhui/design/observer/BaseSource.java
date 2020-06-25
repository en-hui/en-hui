package fun.enhui.design.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象事件源。
 * @Author 胡恩会
 * @Date 2020/6/18 22:01
 **/
public abstract class BaseSource {
    /**
     * 所有监听者
     **/
    List<BaseObserver> observers = new ArrayList<>();
    /**
     * 添加观察者
     * @Author: 胡恩会
     * @Date: 2020/6/25 11:19
    * @param observer:
    * @return: void
     **/
    public void add(BaseObserver observer) {
        this.observers.add(observer);
    }
    /**
     * 移除观察者
     * @Author: 胡恩会
     * @Date: 2020/6/25 11:19
    * @param observer:
    * @return: void
     **/
    public void remove(BaseObserver observer) {
        this.observers.remove(observer);
    }

    /**
     * 唤醒观察者
     * @Author: 胡恩会
     * @Date: 2020/6/25 11:19
    * @return: void
     **/
    public abstract void notifyObserver();
}
