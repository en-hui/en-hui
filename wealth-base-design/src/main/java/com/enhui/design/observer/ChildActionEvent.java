package com.enhui.design.observer;

import java.util.EventObject;

/**
 * 儿童活动事件
 *
 * @Author 胡恩会
 * @Date 2020/6/18 21:29
 **/
public class ChildActionEvent extends EventObject {

    public boolean childCry;
    public boolean childSleep;

    public ChildActionEvent(Object source,boolean childCry,boolean childSleep) {
        super(source);
        this.childCry = childCry;
        this.childSleep = childSleep;
    }
}
