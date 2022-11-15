package com.enhui.netty.rpc.framework.model;

import java.util.concurrent.ConcurrentHashMap;

public class ResponseCallback {
    private static ConcurrentHashMap<Long, Runnable> mapping = new ConcurrentHashMap<>();

    public static void addCallback(Long requestId, Runnable cb) {
        mapping.putIfAbsent(requestId, cb);
    }

    public static void runCallback(Long requestId) {
        Runnable cb = mapping.get(requestId);
        cb.run();
        removeCallback(requestId);
    }

    private static void removeCallback(Long requestId) {
        mapping.remove(requestId);
    }
}
