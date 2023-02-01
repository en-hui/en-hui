package com.enhui.netty.research.simple.client;

import com.enhui.netty.research.simple.proto.MsgProto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ServerBHandler extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    socketChannel
        .pipeline()
        .addLast(new LoggingHandler(LogLevel.DEBUG))
        .addLast(new ProtobufEncoder())
        .addLast(new ProtobufDecoder(MsgProto.DpMessage.getDefaultInstance()))
        .addLast(new ServiceBReadHandler());
  }
}
