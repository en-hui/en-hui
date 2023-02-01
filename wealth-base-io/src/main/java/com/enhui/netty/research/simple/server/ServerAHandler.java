package com.enhui.netty.research.simple.server;

import com.enhui.netty.research.simple.proto.MsgProto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ServerAHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel
                .pipeline()
                .addLast(
                        new LoggingHandler(LogLevel.DEBUG),
                        new ProtobufDecoder(MsgProto.DpMessage.getDefaultInstance()),
                        new ServiceAReadHandler()
                ) // io.netty.handler.codec
        ;
    }
}
