package com.enhui.netty.rpc.framework.model;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseCallback {
    private static ConcurrentHashMap<Long, CompletableFuture> mapping = new ConcurrentHashMap<>();

    public static void addCallback(Long requestId, CompletableFuture completableFuture) {
        mapping.putIfAbsent(requestId, completableFuture);
    }

    public static void runCallback(RpcMsgPackge packge) {
        CompletableFuture cf = mapping.get(packge.getHeader().getRequestId());
        cf.complete(((RpcResponseContent) (packge.getContent())).getResult());
        removeCallback(packge.getHeader().getRequestId());
    }

    private static void removeCallback(Long requestId) {
        mapping.remove(requestId);
    }
}
