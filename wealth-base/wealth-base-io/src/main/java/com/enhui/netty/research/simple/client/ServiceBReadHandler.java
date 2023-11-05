package com.enhui.netty.research.simple.client;

import com.enhui.netty.research.simple.proto.MsgProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceBReadHandler extends SimpleChannelInboundHandler<MsgProto.DpMessage> {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("log:: channelRegistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("log:: channelActive");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgProto.DpMessage message) throws Exception {
        if (message.getMsgType() == MsgProto.DpMessage.DpMsgType.FILE_CONTENT_RESULT) {
            MsgProto.FileContentResult msg = message.getFileContentResult();
            log.info("客户端得到响应：{}", msg);
        } else {
            log.info("暂不支持的类型");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}