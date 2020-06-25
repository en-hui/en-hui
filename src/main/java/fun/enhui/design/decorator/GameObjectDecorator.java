package fun.enhui.design.decorator;

import java.awt.*;

/**
 * 抽象装饰者
 *
 * @Author 胡恩会
 * @Date 2020/6/25 10:44
 **/
public class GameObjectDecorator extends GameObject {
    GameObject gameObject;

    public GameObjectDecorator(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public void pagit() {
        gameObject.pagit();
    }
}
