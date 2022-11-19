package com.enhui.netty.rpc.framework.handler;

import com.enhui.netty.rpc.framework.model.RpcHeader;
import com.enhui.netty.rpc.framework.model.RpcMsgPackge;
import com.enhui.netty.rpc.framework.model.RpcRequestContent;
import com.enhui.netty.rpc.framework.model.RpcResponseContent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class DecodeHandler extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        while (byteBuf.readableBytes() >= RpcHeader.headerLen) {
            // header
            byte[] headerBytes = new byte[RpcHeader.headerLen];
            // get 不会移动指针
            byteBuf.getBytes(byteBuf.readerIndex(), headerBytes);
            ByteArrayInputStream headerIn = new ByteArrayInputStream(headerBytes);
            ObjectInputStream headerOin = new ObjectInputStream(headerIn);
            RpcHeader rpcHeader = (RpcHeader) headerOin.readObject();

            if (byteBuf.readableBytes() - RpcHeader.headerLen >= rpcHeader.getDataLen()) {
                // 先移动指针
                byteBuf.readBytes(headerBytes);
                byte[] dataBytes = new byte[(int) rpcHeader.getDataLen()];
                byteBuf.readBytes(dataBytes);
                ByteArrayInputStream dataIn = new ByteArrayInputStream(dataBytes);
                ObjectInputStream dataOin = new ObjectInputStream(dataIn);
                // data 放到 list中
                RpcMsgPackge packge = new RpcMsgPackge();
                packge.setHeader(rpcHeader);
                if (rpcHeader.getFlag() == RpcHeader.server_flag) {
                    RpcResponseContent rpcContent = (RpcResponseContent) dataOin.readObject();
                    packge.setContent(rpcContent);
                    System.out.println("client decode——" + packge);
                } else if (rpcHeader.getFlag() == RpcHeader.client_flag) {
                    RpcRequestContent rpcContent = (RpcRequestContent) dataOin.readObject();
                    packge.setContent(rpcContent);
                    System.out.println("server decode——" + packge);
                }
                list.add(packge);
            } else {
                break;
            }
        }
    }
}
