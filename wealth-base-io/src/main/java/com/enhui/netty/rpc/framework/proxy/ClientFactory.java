package com.enhui.netty.rpc.framework.proxy;

import com.enhui.netty.rpc.RpcApplication;
import com.enhui.netty.rpc.framework.handler.ClientResponseHandler;
import com.enhui.netty.rpc.framework.handler.DecodeHandler;
import com.enhui.netty.rpc.framework.model.ResponseCallback;
import com.enhui.netty.rpc.framework.model.RpcHeader;
import com.enhui.netty.rpc.framework.model.RpcRequestContent;
import com.enhui.netty.rpc.framework.utils.SerdeUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ClientFactory {

    int poolSize = 5;
    private final ConcurrentHashMap<InetSocketAddress, ClientPool> poll = new ConcurrentHashMap<>();
    Random random = new Random();
    NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);

    /**
     * 传输层，可以使用不同协议
     * 自定义协议：有状态的：requestId
     * http协议：无状态的
     *
     * @param content
     * @param type:   rpc、http
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static CompletableFuture<Object> transport(RpcRequestContent content, String type) throws IOException, InterruptedException {
        CompletableFuture<Object> cf = new CompletableFuture<>();
        if ("rpc".equals(type)) {
            byte[] msgBody = SerdeUtil.serde(content);
            RpcHeader header = RpcHeader.createHeader(msgBody);
            byte[] msgHeader = SerdeUtil.serde(header);

            NioSocketChannel client = ClientFactory.getInstance().getClient(new InetSocketAddress(RpcApplication.host, RpcApplication.port));
            ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
            buf.writeBytes(msgHeader);
            buf.writeBytes(msgBody);
            ChannelFuture channelFuture = client.writeAndFlush(buf);
            // io是双向的，这里只能等到out结束，不能等到返回
            ChannelFuture sync = channelFuture.sync();
            ResponseCallback.addCallback(header.getRequestId(), cf);
            return cf;
        } else {
            // 1、

            // 2、
            return cf;
        }
    }


    /**
     * 单例的，所以需要线程安全
     * 锁粒度尽可能小，能锁代码块就不锁整个方法
     *
     * @param address
     * @return
     */
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
                        System.out.printf("%s 接收到服务端的响应\n", Thread.currentThread().getName());
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new DecodeHandler());
                        pipeline.addLast(new ClientResponseHandler());
                    }
                }).connect(address);
        return (NioSocketChannel) client.sync().channel();
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
