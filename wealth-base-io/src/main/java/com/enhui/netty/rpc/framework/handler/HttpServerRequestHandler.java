package com.enhui.netty.rpc.framework.handler;

import com.enhui.netty.rpc.framework.model.Dispatcher;
import com.enhui.netty.rpc.framework.model.RpcContent;
import com.enhui.netty.rpc.framework.model.RpcRequestContent;
import com.enhui.netty.rpc.framework.model.RpcResponseContent;
import com.enhui.netty.rpc.framework.utils.SerdeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;

public class HttpServerRequestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //http 协议 ,  这个msg是一个啥：完整的http-request
        FullHttpRequest request = (FullHttpRequest) msg;
        System.out.println(request.toString());
        //这个就是consumer 序列化的MyContent
        ByteBuf content = request.content();
        byte[] data = new byte[content.readableBytes()];
        content.readBytes(data);
        ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
        RpcRequestContent myContent = (RpcRequestContent) oin.readObject();
        String serviceName = myContent.getServiceName();
        String method = myContent.getMethodName();
        Object c = Dispatcher.getDis().get(serviceName);
        Class<?> clazz = c.getClass();
        Object res = null;
        Method m = clazz.getMethod(method, myContent.getParameterTypes());
        res = m.invoke(c, myContent.getArgs());
        RpcResponseContent resContent = new RpcResponseContent(res);
        byte[] contentByte = SerdeUtil.serde(resContent);
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(contentByte));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, contentByte.length);
        //http协议，header+body
        ctx.writeAndFlush(response);
    }
}
