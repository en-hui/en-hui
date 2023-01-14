package com.enhui.netty.research.simple;

import com.enhui.netty.rpc.framework.utils.SerdeUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;

/** netty服务端，接收读取文件的请求，返回文件内容 */
public class ServiceA {
  public static final String SERVICE_A_HOST = "127.0.0.1";
  public static final int SERVICE_A_PORT = 9999;

  public static void main(String[] args) throws InterruptedException {
    NioEventLoopGroup group = new NioEventLoopGroup(1);
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    ChannelFuture bind =
        serverBootstrap
            .group(group, group)
            .channel(NioServerSocketChannel.class)
            .childHandler(
                new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel
                        .pipeline()
                        .addLast(new HttpServerCodec()) // io.netty.handler.codec
                        .addLast(new HttpObjectAggregator(1024 * 512))
                        .addLast(new ServiceAInHandler());
                  }
                })
            .bind(new InetSocketAddress(SERVICE_A_HOST, SERVICE_A_PORT));

    ChannelFuture sync = bind.sync().channel().closeFuture().sync();
  }

  public static class ServiceAInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      // http 协议 ,  这个msg是一个啥：完整的http-request
      FullHttpRequest request = (FullHttpRequest) msg;
      System.out.println("log::request: " + request.toString());
      // 这个就是consumer 序列化的MyContent
      ByteBuf content = request.content();
      byte[] data = new byte[content.readableBytes()];
      content.readBytes(data);
      ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
      System.out.println(oin.readObject());

      String resContent = "文件内容";
      byte[] contentByte = SerdeUtil.serde(resContent);
      DefaultFullHttpResponse response =
          new DefaultFullHttpResponse(
              HttpVersion.HTTP_1_0, HttpResponseStatus.OK, Unpooled.copiedBuffer(contentByte));
      response.headers().set(HttpHeaderNames.CONTENT_LENGTH, contentByte.length);
      // http协议，header+body
      ctx.writeAndFlush(response);
    }
  }
}
