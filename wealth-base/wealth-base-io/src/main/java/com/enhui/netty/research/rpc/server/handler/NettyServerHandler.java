package com.enhui.netty.research.rpc.server.handler;

import com.enhui.netty.research.rpc.common.proto.RpcProto;
import com.enhui.netty.research.rpc.common.util.FileUtil;
import com.enhui.netty.research.rpc.server.NettyServerApplication;
import com.enhui.netty.research.simple.proto.MsgProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcProto.DpMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProto.DpMessage message) throws Exception {
        log.info("服务端收到请求：{}", message);
        if (message.getMsgType() == RpcProto.DpMessage.DpMsgType.FILE_CONTENT_CMD) {
            RpcProto.FileContentCmd msg = message.getFileContentCmd();
            List<String> content = NettyServerApplication.diskFileService.snapshotTable(msg.getFileName());
            String contentStr = String.join("\r\n", content);
            log.info("服务端调用本地方法结束");
            RpcProto.FileContentResult fileContentResult = RpcProto.FileContentResult.newBuilder()
                    .setFileName(msg.getFileName())
                    .setFileContent(contentStr).build();
            RpcProto.DpMessage msgResult = RpcProto.DpMessage.newBuilder()
                    .setRequestId(message.getRequestId())
                    .setMsgType(RpcProto.DpMessage.DpMsgType.FILE_CONTENT_RESULT)
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
