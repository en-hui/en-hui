package com.enhui.design.observer;

/**
 * 观察者-父亲
 *
 * @Author 胡恩会
 * @Date 2020/6/18 22:30
 **/
public class FatherObserver implements BaseObserver {
    @Override
    public void actionOnEvent(ChildActionEvent event) {
        Object source = event.getSource();
        if (event.childSleep){
            System.out.println(source + "孩子睡了，爸爸开始玩游戏");
        }
        if (event.childCry){
            System.out.println(source + "孩子哭了，爸爸来哄孩子");
        }
    }
}
