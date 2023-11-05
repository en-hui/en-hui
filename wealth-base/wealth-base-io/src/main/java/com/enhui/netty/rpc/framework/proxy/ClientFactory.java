package com.enhui.netty.rpc.framework.proxy;

import com.enhui.netty.rpc.RpcApplication;
import com.enhui.netty.rpc.framework.handler.ClientResponseHandler;
import com.enhui.netty.rpc.framework.handler.DecodeHandler;
import com.enhui.netty.rpc.framework.model.ResponseCallback;
import com.enhui.netty.rpc.framework.model.RpcContent;
import com.enhui.netty.rpc.framework.model.RpcHeader;
import com.enhui.netty.rpc.framework.model.RpcRequestContent;
import com.enhui.netty.rpc.framework.model.RpcResponseContent;
import com.enhui.netty.rpc.framework.utils.SerdeUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
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
        } else {
            nettyTS(content, cf);
        }
        return cf;
    }

    private static void nettyTS(RpcContent content, CompletableFuture<Object> res) throws IOException, InterruptedException {
        //每个请求对应一个连接
        //1，通过netty建立io 建立连接
        NioEventLoopGroup group = new NioEventLoopGroup(1);//定义到外面
        Bootstrap bs = new Bootstrap();
        Bootstrap client = bs.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 512))
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        //3，接收   预埋的回调，根据netty对socket io 事件的响应
                                        //客户端的msg是啥：完整的http-response
                                        FullHttpResponse response = (FullHttpResponse) msg;
                                        System.out.println(response.toString());
                                        ByteBuf resContent = response.content();
                                        byte[] data = new byte[resContent.readableBytes()];
                                        resContent.readBytes(data);
                                        ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
                                        RpcResponseContent o = (RpcResponseContent) oin.readObject();
                                        res.complete(o.getResult());
                                    }
                                });
                    }
                });
        //未来连接后，收到数据的处理handler
        ChannelFuture syncFuture = client.connect(RpcApplication.host, RpcApplication.port).sync();
        //2，发送
        Channel clientChannel = syncFuture.channel();
        byte[] data = SerdeUtil.serde(content);
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0,
                HttpMethod.POST, "/",
                Unpooled.copiedBuffer(data)
        );
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, data.length);
        clientChannel.writeAndFlush(request).sync();//作为client 向server端发送：http  request
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
