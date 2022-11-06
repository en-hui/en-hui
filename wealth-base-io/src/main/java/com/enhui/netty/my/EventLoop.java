package com.enhui.netty.my;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class EventLoop implements Runnable {

    Selector selector = null;
    LinkedBlockingQueue<Channel> queue = new LinkedBlockingQueue<Channel>();
    EventLoopGroup group = null;

    public EventLoop(EventLoopGroup group) throws IOException {
        this.selector = Selector.open();
        this.group = group;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 1. selector.select()    阻塞，可以wakeup()唤醒
//                System.out.printf("%s：before select() , size is %s\n", Thread.currentThread().getName(), selector.keys().size());
                int num = selector.select();
                System.out.printf("%s：after select() , size is %s\n", Thread.currentThread().getName(), selector.keys().size());

                // 2. 处理 selector.selectedKeys()
                if (num > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandle(key);
                        } else if (key.isReadable()) {
                            acceptRead(key);
                        } else if (key.isWritable()) {
                            acceptWrite(key);
                        }
                    }
                }

                // 3. handle other task
                if (!queue.isEmpty()) {
                    Channel channel = queue.take();
                    if (channel instanceof ServerSocketChannel) {
                        ServerSocketChannel server = (ServerSocketChannel) channel;
                        server.register(selector, SelectionKey.OP_ACCEPT);
                    } else if (channel instanceof SocketChannel) {
                        SocketChannel client = (SocketChannel) channel;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                        client.register(selector, SelectionKey.OP_READ, buffer);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void acceptWrite(SelectionKey key) {

    }

    private void acceptRead(SelectionKey key) throws IOException {
        System.out.printf("%s:read handle\n", Thread.currentThread().getName());
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        while (true) {
            int num = client.read(buffer);
            if (num > 0) {
                buffer.flip();
                client.write(buffer);
                buffer.clear();
            } else if (num == 0) {
                break;
            } else if (num == -1) {
                System.out.printf("%s:client:%s is close\n", Thread.currentThread().getName(), client);
                key.cancel();
                break;
            }
        }
    }

    private void acceptHandle(SelectionKey key) throws IOException {
        System.out.printf("%s:accept handle\n", Thread.currentThread().getName());
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        // choose a selector
        group.chooseSelectorAndRegistry(client);

    }
}
