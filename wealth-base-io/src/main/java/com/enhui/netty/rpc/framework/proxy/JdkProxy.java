package com.enhui.netty.rpc.framework.proxy;

import com.enhui.netty.rpc.RpcApplication;
import com.enhui.netty.rpc.framework.model.ResponseCallback;
import com.enhui.netty.rpc.framework.model.RpcHeader;
import com.enhui.netty.rpc.framework.model.RpcRequestContent;
import com.enhui.netty.rpc.framework.utils.SerdeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class JdkProxy {

    /**
     * 动态代理实现
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T proxyGet(Class<T> clazz) {
        Class<?>[] methodInfo = {clazz};
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), methodInfo, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                try {
                    RpcRequestContent content =
                            new RpcRequestContent(clazz.getName(), method.getName(), method.getParameterTypes(), args);

                    CompletableFuture<Object> cf = ClientFactory.transport(content, "rpc");
                    return cf.get();
                } catch (Exception e) {
                    System.out.println("proxy error:");
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
