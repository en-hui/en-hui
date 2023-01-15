package com.enhui.netty.research.simple.server;

import com.enhui.netty.research.simple.common.proto.MsgProto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ServerAHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel
                .pipeline()
                .addLast(
                        new HttpServerCodec(), // Http 服务器编解码器
                        new HttpObjectAggregator(65535), // 内容长度限制
                        new WebSocketServerProtocolHandler("/websocket") // WebSocket 协议处理器, 在这里处理握手、ping、pong 等消息
//                        new RpcMsgDecoder(), // 自定义的消息解码器
//                        new RpcMsgEncoder(), // 自定义的消息编码器
//                        new RpcMsgHandler() // 自定义的消息处理器
                ) // io.netty.handler.codec
        ;
    }
}
