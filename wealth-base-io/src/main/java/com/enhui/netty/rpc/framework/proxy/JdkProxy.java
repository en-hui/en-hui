package com.enhui.netty.rpc.framework.proxy;

import com.enhui.netty.rpc.framework.model.RpcContent;
import com.enhui.netty.rpc.framework.model.RpcHeader;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class JdkProxy {

  /**
   * 动态代理实现
   *
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T proxyGet(Class<T> clazz) {

    return (T)
        Proxy.newProxyInstance(
            clazz.getClassLoader(),
            clazz.getInterfaces(),
            new InvocationHandler() {
              @Override
              public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcContent content = new RpcContent();
                content.setArgs(args);
                content.setServiceName(clazz.getName());
                content.setMethodName(method.getName());
                content.setParameterTypes(method.getParameterTypes());

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream oout = new ObjectOutputStream(out);
                oout.writeObject(content);
                byte[] msgBody = out.toByteArray();

                RpcHeader header = createHeader(msgBody);
                out.reset();
                oout = new ObjectOutputStream(out);
                oout.writeObject(header);
                byte[] msgHeader = out.toByteArray();

                return null;
              }
            });
  }

  private static RpcHeader createHeader(byte[] msgBody) {
    RpcHeader header = new RpcHeader();
    header.setRequestId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    header.setDataLen(msgBody.length);
    return header;
  }
}
