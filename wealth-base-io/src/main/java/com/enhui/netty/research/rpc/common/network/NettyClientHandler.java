package com.enhui.netty.research.rpc.common.network;

import com.enhui.netty.research.rpc.common.proto.RpcProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcProto.DpMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProto.DpMessage message) throws Exception {
        long requestId = message.getRequestId();
        if (message.getMsgType() == RpcProto.DpMessage.DpMsgType.FILE_CONTENT_RESULT) {
            log.info("{}:客户端得到响应", requestId);
            ResponseCallback.runCallback(message);
        } else {
            log.info("暂不支持的类型");
        }
    }
}
