package com.enhui.netty.rpc.framework.handler;

import com.enhui.netty.rpc.framework.model.RpcHeader;
import com.enhui.netty.rpc.framework.model.RpcMsgPackge;
import com.enhui.netty.rpc.framework.model.RpcRequestContent;
import com.enhui.netty.rpc.framework.model.RpcResponseContent;
import com.enhui.netty.rpc.framework.utils.SerdeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutor;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class ServerRequestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcMsgPackge packge = (RpcMsgPackge) msg;
        String ioThreadName = Thread.currentThread().getName();
        System.out.printf("%s:server read: %s\n",ioThreadName, packge);
        // 1、直接在当前方法处理
        // 2、使用netty当前的eventLoop来处理
//        EventExecutor eventExecutor = ctx.executor();
        // 3、使用netty其他的eventLoop来处理
        // todo，按理说应该用下面这种让线程忙碌程度均衡，
        //  但是使用下面这种，会出现问题：在client是多个的时候，channelRead被调用30次，但下面这部分的打印只有 有限几次
        EventExecutor eventExecutor = ctx.executor().parent().next();
        eventExecutor.execute(() -> {
            try {
                String execThreadName = Thread.currentThread().getName();
                Object reqArg = ((RpcRequestContent) (packge.getContent())).getArgs()[0];
                // 根据请求，调用本地方法，得到返回值
                RpcResponseContent content = new RpcResponseContent(reqArg.toString());
                System.out.printf("ioThread: %s,execThread: %s , param: %s , result: %s\n",
                        ioThreadName, execThreadName, reqArg, content.getResult());
                byte[] msgBody = SerdeUtil.serde(content);
                RpcHeader header = new RpcHeader(RpcHeader.server_flag, packge.getHeader().getRequestId(), msgBody.length);
                byte[] msgHeader = SerdeUtil.serde(header);

                ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
                buf.writeBytes(msgHeader);
                buf.writeBytes(msgBody);
                ChannelFuture channelFuture = ctx.writeAndFlush(buf);
                channelFuture.sync();
                System.out.printf("flush success . ioThread: %s,execThread: %s , param: %s , result: %s\n",
                        ioThreadName, execThreadName, reqArg, content.getResult());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}
