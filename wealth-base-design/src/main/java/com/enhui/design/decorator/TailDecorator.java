package com.enhui.design.decorator;

/**
 * 拖尾装饰
 *
 * @Author 胡恩会
 * @Date 2020/6/25 10:33
 **/
public class TailDecorator extends GameObjectDecorator {
    public TailDecorator(GameObject gameObject) {
        super(gameObject);
    }
    @Override
    public void pagit() {
        super.pagit();
        System.out.println("拖尾装饰加在后面");
    }
}
