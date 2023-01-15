package com.enhui.netty.research.simple.server;

import com.enhui.netty.research.simple.common.proto.MsgProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceAReadHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        log.info("服务端收到请求：{}", obj);
        if (obj instanceof MsgProto.FileContentCmd) {
            MsgProto.FileContentCmd msg = (MsgProto.FileContentCmd) obj;
            String content = "file content";
            MsgProto.FileContentResult fileContentResult = MsgProto.FileContentResult.newBuilder().setFileName(msg.getFileName()).setFileContent(content).build();
            byte[] resultBytes = fileContentResult.toByteArray();
            ByteBuf buf = Unpooled.copiedBuffer(resultBytes);
            ctx.writeAndFlush(buf).sync();
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
