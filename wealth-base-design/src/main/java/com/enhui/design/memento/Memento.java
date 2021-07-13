package com.enhui.design.memento;

import lombok.Getter;
import lombok.Setter;

/**
 * 备忘录-其实就是发起人的副本对象
 *
 * @Author 胡恩会
 * @Date 2020/6/27 16:14
 **/
@Getter
@Setter
public class Memento {
    private String gameName;
    private int checkpoint;
    private int life;
}
