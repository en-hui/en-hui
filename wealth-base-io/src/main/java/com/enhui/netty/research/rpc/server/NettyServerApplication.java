package com.enhui.netty.research.rpc.server;

import com.enhui.netty.research.rpc.common.proto.RpcProto;
import com.enhui.netty.research.rpc.server.api.impl.DiskFileService;
import com.enhui.netty.research.rpc.server.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyServerApplication {
    public static DiskFileService diskFileService = new DiskFileService();
    public static final String SERVICE_A_HOST = "127.0.0.1";
    public static final int SERVICE_A_PORT = 9999;

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(2);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
//                    pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                    pipeline.addLast(new ProtobufDecoder(RpcProto.DpMessage.getDefaultInstance()));
                    pipeline.addLast(new NettyServerHandler());
                }
            });

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(SERVICE_A_HOST, SERVICE_A_PORT)).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
