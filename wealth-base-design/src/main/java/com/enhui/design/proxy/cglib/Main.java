package com.enhui.design.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author 胡恩会
 * @Date 2020/6/22 22:06
 **/
public class Main {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        // 设置父类
        enhancer.setSuperclass(Tank.class);
        // 增强方法
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                // 验证父类是被代理类
                System.out.println(o.getClass().getSuperclass().getName());
                System.out.println("坦克移动前");
                Object result = methodProxy.invokeSuper(o, objects);
                System.out.println("坦克移动后");
                return result;
            }
        });

        // 生成代理对象-->为被代理对象的子类
        Tank tank = (Tank)enhancer.create();
        tank.move();
    }
}
