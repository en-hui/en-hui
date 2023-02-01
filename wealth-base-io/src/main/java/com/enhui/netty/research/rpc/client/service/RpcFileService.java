package com.enhui.netty.research.rpc.client.service;

import com.enhui.netty.research.rpc.common.api.FileSource;
import com.enhui.netty.research.rpc.common.network.ClientFactory;
import com.enhui.netty.research.rpc.common.network.ResponseCallback;
import com.enhui.netty.research.rpc.common.proto.RpcProto;
import com.enhui.netty.research.rpc.server.NettyServerApplication;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class RpcFileService implements FileSource {

    @Override
    public List<String> snapshotTable(String tableName) throws ExecutionException, InterruptedException {
        log.info("rpc 模式，调用远程方法");
        long requestId = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        CompletableFuture<String> cf = new CompletableFuture<>();
        NioSocketChannel client = ClientFactory.getInstance().getClient(new InetSocketAddress(NettyServerApplication.SERVICE_A_HOST, NettyServerApplication.SERVICE_A_PORT));
        RpcProto.DpMessage message = RpcProto.DpMessage.newBuilder().setRequestId(requestId)
                .setMsgType(RpcProto.DpMessage.DpMsgType.FILE_CONTENT_CMD)
                .setFileContentCmd(RpcProto.FileContentCmd.newBuilder().setFileName(tableName).build()).build();
        ChannelFuture channelFuture = client.writeAndFlush(message);
        ResponseCallback.addCallback(requestId, cf);
        log.info("requestId:{},clientId:{} 请求消息已发出，等待响应", requestId, client.id());
        channelFuture.sync();
        String content = cf.get();
        List<String> lines = Arrays.asList(content.split("\r\n"));
        log.info("requestId:{},clientId:{} rpc 模式，远程调用结束", requestId, client.id());
        return lines;
    }

}
