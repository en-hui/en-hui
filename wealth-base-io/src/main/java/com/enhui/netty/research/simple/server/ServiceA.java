package com.enhui.netty.research.simple.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * netty服务端，接收读取文件的请求，返回文件内容
 */
public class ServiceA {
    public static final String SERVICE_A_HOST = "127.0.0.1";
    public static final int SERVICE_A_PORT = 9999;

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerAHandler());

            ChannelFuture channelFuture =
                    serverBootstrap.bind(new InetSocketAddress(SERVICE_A_HOST, SERVICE_A_PORT)).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
