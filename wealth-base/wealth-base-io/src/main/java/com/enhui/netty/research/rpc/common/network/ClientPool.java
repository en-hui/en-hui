package com.enhui.netty.research.rpc.common.network;

import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientPool {
    public NioSocketChannel[] clients;
    public Object[] locks;

    public ClientPool(int size) {
        clients = new NioSocketChannel[size];
        locks = new Object[size];
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new Object();
        }
    }
}
