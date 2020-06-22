package fun.enhui.design.proxy.static1;

/**
 * @Author 胡恩会
 * @Date 2020/6/22 20:56
 **/
public class Tank implements Movable {
    @Override
    public void move() {
        System.out.println("坦克移动...");
    }
}
