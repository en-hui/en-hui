package com.enhui.netty.rpc.framework.proxy;

import com.enhui.netty.rpc.framework.handler.ClientResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ClientFactory {

    int poolSize = 1;
    private ConcurrentHashMap<InetSocketAddress, ClientPool> poll = new ConcurrentHashMap<>();
    Random random = new Random();


    /**
     * 单例的，所以需要线程安全
     *
     * @param address
     * @return
     */
    public synchronized NioSocketChannel getClient(InetSocketAddress address) {
        ClientPool clientPool = poll.get(address);
        if (clientPool == null) {
            clientPool = new ClientPool(poolSize);
            poll.putIfAbsent(address, clientPool);
        }
        // 随机
        int i = random.nextInt(poolSize);

        NioSocketChannel client = clientPool.clients[i];
        if (client != null && client.isActive()) {
            return client;
        }
        return clientPool.clients[i] = createClient(address);
    }

    private NioSocketChannel createClient(InetSocketAddress address) {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        NioSocketChannel client = (NioSocketChannel) bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new ClientResponseHandler());
                    }
                }).connect(address).channel();
        return client;
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
