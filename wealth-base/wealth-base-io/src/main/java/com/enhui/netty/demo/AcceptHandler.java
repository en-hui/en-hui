package com.enhui.netty.demo;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;

public class AcceptHandler extends ChannelInboundHandlerAdapter {
    private NioEventLoopGroup loopGroup;
    private ChannelHandler handler;

    public AcceptHandler(NioEventLoopGroup loopGroup, ChannelHandler handler) {
        this.loopGroup = loopGroup;
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server active");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server register");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 框架做了 accept
        NioSocketChannel client = (NioSocketChannel) msg;

        client.pipeline().addLast(handler);  // 先把共享的过桥handler加进来。

        loopGroup.register(client);
    }
}
