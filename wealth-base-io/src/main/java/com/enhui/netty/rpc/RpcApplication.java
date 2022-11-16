package com.enhui.netty.rpc;

import com.enhui.netty.rpc.api.UserApi;
import com.enhui.netty.rpc.framework.handler.ServerRequestHandler;
import com.enhui.netty.rpc.framework.proxy.JdkProxy;
import com.enhui.netty.rpc.model.UserModel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

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
        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture bind = bootstrap.group(boss, boss)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        System.out.println("server accept client port: " + channel.remoteAddress().getPort());
                        channel.pipeline().addLast(new ServerRequestHandler());
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
    public void consumer() {
//        new Thread(()->{
//            provider();
//        }).start();
//        System.out.println("server start");

        UserApi userApi = JdkProxy.proxyGet(UserApi.class);
        UserModel userName = userApi.getByUserName("userName");
        userApi.addUser();
    }
}
