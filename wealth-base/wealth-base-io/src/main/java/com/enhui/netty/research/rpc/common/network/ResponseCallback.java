package com.enhui.netty.research.rpc.common.network;

import com.enhui.netty.research.rpc.common.proto.RpcProto;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ResponseCallback {
    private static ConcurrentHashMap<Long, CompletableFuture> mapping = new ConcurrentHashMap<>();

    public static void addCallback(Long requestId, CompletableFuture completableFuture) {
        mapping.put(requestId, completableFuture);
    }

    public static void runCallback(RpcProto.DpMessage message) {
        long requestId = message.getRequestId();
        if (message.getMsgType() == RpcProto.DpMessage.DpMsgType.FILE_CONTENT_RESULT) {
            RpcProto.FileContentResult msg = message.getFileContentResult();
            CompletableFuture cf = mapping.get(requestId);
            while (cf == null) {
                log.error("当前mapping容量：{},mapping:{},error:{}", mapping.size(), mapping, message);
                cf = mapping.get(requestId);
                if (cf != null) {
                    log.info("恢复成功");
                }
            }
            cf.complete(msg.getFileContent());
        }
        removeCallback(requestId);
    }

    private static void removeCallback(Long requestId) {
        mapping.remove(requestId);
    }
}
