package fun.enhui.design.memento;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
