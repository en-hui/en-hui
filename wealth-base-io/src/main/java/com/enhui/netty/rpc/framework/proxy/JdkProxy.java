package com.enhui.netty.rpc.framework.proxy;

import com.enhui.netty.rpc.RpcApplication;
import com.enhui.netty.rpc.framework.model.ResponseCallback;
import com.enhui.netty.rpc.framework.model.RpcHeader;
import com.enhui.netty.rpc.framework.model.RpcRequestContent;
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
                    RpcRequestContent content = new RpcRequestContent();
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

                    NioSocketChannel client = ClientFactory.getInstance().getClient(new InetSocketAddress(RpcApplication.host, RpcApplication.port));
                    ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
                    buf.writeBytes(msgHeader);
                    buf.writeBytes(msgBody);
                    ChannelFuture channelFuture = client.writeAndFlush(buf);
                    // io是双向的，这里只能等到out结束，不能等到返回
                    ChannelFuture sync = channelFuture.sync();

                    CountDownLatch countDownLatch = new CountDownLatch(1);

                    ResponseCallback.addCallback(header.getRequestId(), () -> {
                        countDownLatch.countDown();
                    });
                    countDownLatch.wait();
                } catch (Exception e) {
                    System.out.println("proxy error:");
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public static RpcHeader createHeader(byte[] msgBody) {
        RpcHeader header = new RpcHeader();
        header.setRequestId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
        header.setDataLen(msgBody.length);
        return header;
    }
}
