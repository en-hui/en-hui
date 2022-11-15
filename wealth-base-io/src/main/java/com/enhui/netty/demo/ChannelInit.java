package com.enhui.netty.demo;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public abstract class ChannelInit extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("过桥handler，必须是Sharable的，注册有意义的handler");
        initChannel(ctx);
//        ctx.channel().pipeline().addLast(new InHandler());
        ctx.pipeline().remove(this);
    }

    public abstract void initChannel(ChannelHandlerContext ctx);


}
