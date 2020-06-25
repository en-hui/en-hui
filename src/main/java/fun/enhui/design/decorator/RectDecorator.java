package fun.enhui.design.decorator;

import java.awt.*;

/**
 * 边框装饰
 *
 * @Author 胡恩会
 * @Date 2020/6/25 10:33
 **/
public class RectDecorator extends GameObjectDecorator{
    public RectDecorator(GameObject gameObject) {
        super(gameObject);
    }
    @Override
    public void pagit() {
        System.out.println("边框装饰加在前");
        super.pagit();
        System.out.println("边框装饰加在后");
    }
}
