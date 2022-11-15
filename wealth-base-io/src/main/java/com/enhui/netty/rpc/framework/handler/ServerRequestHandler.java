package com.enhui.netty.rpc.framework.handler;

import com.enhui.netty.rpc.framework.model.ResponseCallback;
import com.enhui.netty.rpc.framework.model.RpcHeader;
import com.enhui.netty.rpc.framework.model.RpcRequestContent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class ServerRequestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        ByteBuf copyBuf = buf.copy();

        if (buf.readableBytes() >= RpcHeader.headerLen) {
            // header
            byte[] headerBytes = new byte[RpcHeader.headerLen];
            buf.readBytes(headerBytes);
            ByteArrayInputStream headerIn = new ByteArrayInputStream(headerBytes);
            ObjectInputStream headerOin = new ObjectInputStream(headerIn);
            RpcHeader rpcHeader = (RpcHeader)headerOin.readObject();
            System.out.println("server handler requestId: " + rpcHeader.getRequestId());

            if (buf.readableBytes() > rpcHeader.getDataLen()) {
                byte[] dataBytes = new byte[(int) rpcHeader.getDataLen()];
                buf.readBytes(dataBytes);
                ByteArrayInputStream dataIn = new ByteArrayInputStream(dataBytes);
                ObjectInputStream dataOin = new ObjectInputStream(dataIn);
                RpcRequestContent rpcData = (RpcRequestContent)dataOin.readObject();
            }
        }
        ChannelFuture channelFuture = ctx.writeAndFlush(copyBuf);
        channelFuture.sync();
    }
}
