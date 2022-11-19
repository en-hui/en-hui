package com.enhui.netty.rpc.framework.handler;

import com.enhui.netty.rpc.framework.model.ResponseCallback;
import com.enhui.netty.rpc.framework.model.RpcMsgPackge;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcMsgPackge packge = (RpcMsgPackge) msg;
        ResponseCallback.runCallback(packge);
    }
}
