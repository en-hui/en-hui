package com.enhui.netty.rpc;

import com.enhui.netty.rpc.api.UserApi;
import com.enhui.netty.rpc.framework.handler.DecodeHandler;
import com.enhui.netty.rpc.framework.handler.ServerRequestHandler;
import com.enhui.netty.rpc.framework.proxy.JdkProxy;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RpcApplication {

    public static String host = "127.0.0.1";
    public static int port = 9090;

    /**
     * 服务提供者
     */
    @Test
    public void provider() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(3);
        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture bind = bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        System.out.printf("%s 接收到客户端 %s 的请求\n", Thread.currentThread().getName(), channel.remoteAddress().getPort());
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new DecodeHandler());
                        pipeline.addLast(new ServerRequestHandler());
                    }
                }).bind(new InetSocketAddress(host, port));
        try {
            bind.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 服务消费者
     */
    @Test
    public void consumer() throws IOException {
        UserApi userApi = JdkProxy.proxyGet(UserApi.class);
        for (int i = 0; i < 30; i++) {
            int finalI = i;
            new Thread(() -> {
                String param = "userName" + finalI;
                System.out.println(param + "--" + userApi.getByUserName(param));
            }).start();
        }
        System.in.read();
    }
}
