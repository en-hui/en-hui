package com.enhui.netty.my;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class EventLoopGroup {

    EventLoop[] eventLoops = null;
    ServerSocketChannel server;
    AtomicInteger incr = new AtomicInteger(0);
    EventLoopGroup workerGroup = this;


    public EventLoopGroup(int num, String namePre) throws IOException {
        eventLoops = new EventLoop[num];
        for (int i = 0; i < num; i++) {
            eventLoops[i] = new EventLoop(this);

            new Thread(eventLoops[i], namePre + i).start();
        }

    }

    public void bind(int port) throws IOException {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(port));

        chooseSelectorAndRegistry(server);
    }

    public void chooseSelectorAndRegistry(Channel channel) throws ClosedChannelException {
        System.out.printf("%s: choose selector\n", Thread.currentThread().getName());
        EventLoop eventLoop = null;
        if (channel instanceof ServerSocketChannel) {
            eventLoop = chooseSelector();
        } else if (channel instanceof SocketChannel) {
            eventLoop = chooseWorkerSelector();
        } else {
            throw new RuntimeException("error");
        }

        // 因为在select()阻塞了，所以要想办法 什么时候wakeup
//        channel.register(eventLoop.selector, SelectionKey.OP_ACCEPT);
//        eventLoop.selector.wakeup();

        // registry 前后分别尝试wakeup，都不行，所以使用队列
        eventLoop.queue.add(channel);
        // 通过打断阻塞，让对应的线程去自己在打断后完成注册selector
        eventLoop.selector.wakeup();
    }

    private EventLoop chooseSelector() {
        int i = incr.incrementAndGet() % eventLoops.length;
        return eventLoops[i];
    }

    private EventLoop chooseWorkerSelector() {
        int i = incr.incrementAndGet() % workerGroup.eventLoops.length;
        return workerGroup.eventLoops[i];
    }

    public void setWorker(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }
}
