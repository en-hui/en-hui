package com.enhui.design.memento;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理者
 * @Author 胡恩会
 * @Date 2020/6/27 16:17
 **/
public class Caretaker {
    private Map<String,Memento> mementoMap = new HashMap<>();
    public void add(String name,Memento memento){
        mementoMap.put(name,memento);
    }
    public Memento get(String name){
        return mementoMap.get(name);
    }
}
