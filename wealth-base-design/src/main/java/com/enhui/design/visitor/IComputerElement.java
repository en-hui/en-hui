package com.enhui.design.visitor;

/**
 * 抽象元素-电脑组件
 *
 * @Author 胡恩会
 * @Date 2020/6/26 14:44
 **/
public abstract class IComputerElement {
    public abstract void accept(IComputerVisitor visitor);
}
