package fun.enhui.design.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

/**
 * @Author 胡恩会
 * @Date 2020/6/22 21:04
 **/
public class Main {
    public static void main(String[] args) {
        Tank tank = new Tank();
        // 保存生成的代理类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        Movable m = (Movable)Proxy.newProxyInstance(Tank.class.getClassLoader(), new Class[]{Movable.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("坦克移动前");
                Object invoke = method.invoke(tank, args);
                System.out.println("坦克移动后");
                return invoke;
            }
        });

        m.move();
    }
}
