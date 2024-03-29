package com.enhui.netty.research.simple.client;

import com.enhui.netty.research.simple.proto.MsgProto;
import com.enhui.netty.research.simple.server.ServiceA;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * netty客户端，发送文件名称，接收文件内容
 */
public class ServiceB {

    public static void main(String[] args) throws InterruptedException, IOException {
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ServerBHandler());
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(ServiceA.SERVICE_A_HOST, ServiceA.SERVICE_A_PORT));
            Channel client = channelFuture.sync().channel();

            MsgProto.FileContentCmd fileContentCmd = MsgProto.FileContentCmd.newBuilder().setFileName("file1").build();
            MsgProto.DpMessage message = MsgProto.DpMessage.newBuilder()
                    .setMsgType(MsgProto.DpMessage.DpMsgType.FILE_CONTENT_CMD)
                    .setFileContentCmd(fileContentCmd).build();
            byte[] msgBytes = message.toByteArray();
            ByteBuf buf = Unpooled.copiedBuffer(msgBytes);
            client.writeAndFlush(buf).sync();

            client.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
