package fun.enhui.design.proxy.static1;

/**
 * @Author 胡恩会
 * @Date 2020/6/22 20:55
 **/
public class LogProxy implements Movable {
    Movable movable;

    public LogProxy(Movable movable) {
        this.movable = movable;
    }

    @Override
    public void move() {
        System.out.println("日志：坦克移动前");
        movable.move();
        System.out.println("日志：坦克移动后");
    }
}
