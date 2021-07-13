package com.enhui.design.factory.factorymethod.pizzastroe.factory;

import fun.enhui.design.factory.factorymethod.pizzastroe.pizza.Pizza;

public abstract class AbsFactoryMethod {
    /**
     *  定义一个抽象方法，让 工厂子类 去实现
     * @author: HuEnhui
     * @date: 2019/12/20 9:32
     */
    public abstract Pizza createPizza();
}
