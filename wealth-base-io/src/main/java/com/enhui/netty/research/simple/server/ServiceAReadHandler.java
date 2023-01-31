package com.enhui.netty.research.simple.server;

import com.enhui.netty.research.simple.common.proto.MsgProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceAReadHandler extends SimpleChannelInboundHandler<MsgProto.DpMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgProto.DpMessage message) throws Exception {
        log.info("服务端收到请求：{}", message);
        if (message.getMsgType() == MsgProto.DpMessage.DpMsgType.FILE_CONTENT_CMD) {
            MsgProto.FileContentCmd msg = message.getFileContentCmd();
            String content = "file content";
            MsgProto.FileContentResult fileContentResult = MsgProto.FileContentResult.newBuilder()
                    .setFileName(msg.getFileName()).setFileContent(content).build();
            MsgProto.DpMessage msgResult = MsgProto.DpMessage.newBuilder()
                    .setMsgType(MsgProto.DpMessage.DpMsgType.FILE_CONTENT_RESULT)
                    .setFileContentResult(fileContentResult).build();
            byte[] resultBytes = msgResult.toByteArray();
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
