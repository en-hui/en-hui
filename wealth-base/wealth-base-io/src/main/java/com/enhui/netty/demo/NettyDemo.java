package com.enhui.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

/**
 * netty简单使用
 * 先不使用bootstrap脚手架，理解都需要做什么
 * 然后在使用bootstrap脚手架，用简单方式编程
 */
public class NettyDemo {

    @Test
    public void loop() {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);
        loopGroup.execute(() -> {
            try {
                while (true) {
                    System.out.println("hello world001");
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loopGroup.execute(() -> {
            try {
                while (true) {
                    System.out.println("hello world002");
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void nettyClient() throws InterruptedException {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);

        NioSocketChannel client = new NioSocketChannel();
        loopGroup.register(client);

        ChannelFuture connect = client.connect(new InetSocketAddress("127.0.0.1", 9999));
        ChannelFuture sync = connect.sync();

        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world".getBytes());
        ChannelFuture channelFuture = client.writeAndFlush(byteBuf);
        channelFuture.sync();

        client.pipeline().addLast(new InHandler());

        sync.channel().closeFuture().sync();
        System.out.println("client close...");

    }

    /**
     * 标准的netty写法
     */
    @Test
    public void nettyClientApi() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connect = bootstrap.group(new NioEventLoopGroup(1))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new InHandler());
                    }
                }).connect(new InetSocketAddress("127.0.0.1", 9999));
        Channel client = connect.sync().channel();
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world".getBytes());
        ChannelFuture send = client.writeAndFlush(byteBuf);
        send.sync();
        client.closeFuture().sync();
    }

    @Test
    public void nettyServer() throws InterruptedException {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);
        NioServerSocketChannel server = new NioServerSocketChannel();

        loopGroup.register(server);
        server.pipeline().addLast(new AcceptHandler(loopGroup, new ChannelInit() {

            @Override
            public void initChannel(ChannelHandlerContext ctx) {
                ctx.pipeline().addLast(new InHandler());
            }
        }));

        System.out.printf("server start %s %s\n", "127.0.0.1", 9999);
        ChannelFuture bind = server.bind(new InetSocketAddress("127.0.0.1", 9999));
        bind.sync().channel().closeFuture().sync();
        System.out.println("server close...");
    }

    @Test
    public void nettyServerApi() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture bind = serverBootstrap.group(group, group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new InHandler());
                    }
                }).bind(new InetSocketAddress("127.0.0.1", 9999));

        ChannelFuture sync = bind.sync().channel().closeFuture().sync();
    }
}
