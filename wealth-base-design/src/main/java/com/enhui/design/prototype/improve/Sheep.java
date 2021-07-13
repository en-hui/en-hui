package com.enhui.design.prototype.improve;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 实现Cloneable接口，重写clone方法实现克隆
 *
 * @Author: 胡恩会
 * @Date: 2020/6/27 15:24
 **/
@Data
@AllArgsConstructor
public class Sheep implements Cloneable {
    private String name;
    private int age;
    private String color;

    /**
     * 重写clone
     *
     * @author: HuEnhui
     * @date: 2019/12/22 18:46
     */
    @Override
    protected Object clone() {
        Sheep sheep = null;
        try {
            sheep = (Sheep) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return sheep;
    }
}
