package com.enhui.design.observer;

import java.util.EventListener;

/**
 * 抽象观察者
 *
 * @Author 胡恩会
 * @Date 2020/6/18 21:33
 **/
public interface BaseObserver extends EventListener {
    /**
     * 处理事件
     *
     * @param event:
     * @Author: 胡恩会
     * @Date: 2020/6/18 21:37
     * @return: void
     **/
    void actionOnEvent(ChildActionEvent event);
}
