package com.enhui.netty.research.rpc.common.network;

import com.enhui.netty.research.rpc.common.proto.RpcProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ClientFactory {

    int poolSize = 5;
    public final ConcurrentHashMap<InetSocketAddress, ClientPool> poll = new ConcurrentHashMap<>();
    Random random = new Random();
    NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);

    public NioSocketChannel getClient(InetSocketAddress address) throws InterruptedException {
        if (poll.get(address) == null) {
            synchronized (poll) {
                if (poll.get(address) == null) {
                    poll.putIfAbsent(address, new ClientPool(poolSize));
                }
            }
        }
        ClientPool clientPool = poll.get(address);
        // 随机
        int i = random.nextInt(poolSize);

        if (clientPool.clients[i] == null || !clientPool.clients[i].isActive()) {
            synchronized (clientPool.locks[i]) {
                if (clientPool.clients[i] == null || !clientPool.clients[i].isActive()) {
                    clientPool.clients[i] = createClient(address);
                }
            }
        }
        return clientPool.clients[i];
    }

    private NioSocketChannel createClient(InetSocketAddress address) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture client = bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
//                        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new ProtobufDecoder(RpcProto.DpMessage.getDefaultInstance()));
                        pipeline.addLast(new NettyClientHandler());
                    }
                }).connect(address);
        return (NioSocketChannel) client.sync().channel();
    }

    public void close() {
        for (ClientPool clientPool : poll.values()) {
            for (NioSocketChannel client : clientPool.clients) {
                if (client != null) {
                    client.close();
                }
            }
        }
        loopGroup.shutdownGracefully();
    }
    /**
     * 单例：饿汉式
     */
    private static final ClientFactory INSTANCE = new ClientFactory();

    private ClientFactory() {
    }

    public static ClientFactory getInstance() {
        return INSTANCE;
    }
}
