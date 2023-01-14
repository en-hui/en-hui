package com.enhui.netty.research.simple;

import com.enhui.netty.rpc.framework.utils.SerdeUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;

/** netty客户端，发送文件名称，接收文件内容 */
public class ServiceB {

  public static void main(String[] args) throws InterruptedException, IOException {
    Bootstrap bootstrap = new Bootstrap();
    final NioEventLoopGroup group = new NioEventLoopGroup(1);
    ChannelFuture connect =
        bootstrap
            .group(group)
            .channel(NioSocketChannel.class)
            .handler(
                new ChannelInitializer() {
                  @Override
                  protected void initChannel(Channel channel) throws Exception {
                    channel
                        .pipeline()
                        .addLast(new HttpClientCodec()) // io.netty.handler.codec
                        .addLast(new HttpObjectAggregator(1024 * 512))
                        .addLast(new ServiceBInHandler());
                  }
                })
            .connect(new InetSocketAddress(ServiceA.SERVICE_A_HOST, ServiceA.SERVICE_A_PORT));
    Channel client = connect.sync().channel();
    String content = "hello world";
    byte[] data = SerdeUtil.serde(content);
    DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0,
            HttpMethod.POST, "/",
            Unpooled.copiedBuffer(data)
    );
    request.headers().set(HttpHeaderNames.CONTENT_LENGTH, data.length);
    ChannelFuture send = client.writeAndFlush(request);
    send.sync();
    client.closeFuture().sync();
  }

  public static class ServiceBInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      System.out.println("log:: channelRegistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      System.out.println("log:: channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      FullHttpResponse response = (FullHttpResponse) msg;
      System.out.println("log::response: " + response.toString());
      ByteBuf resContent = response.content();
      byte[] data = new byte[resContent.readableBytes()];
      resContent.readBytes(data);
      ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
      System.out.println("log::oin: " + oin.readObject());
    }
  }
}
