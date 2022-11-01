package com.enhui.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 多路复用器练习，多线程模式，将accept和数据读写分开线程处理(与v1相比，只是把R\W放进新线程，并使用key.cancel())
 * 本程序为错误案例，不应该用 key.cancel(); // todo: cancel 也是系统调用，这样不好
 * 正确使用多线程，应该将连接分批，分别注册到不同的selector，每个selector一个线程，达到并行
 */
public class SocketNIOSelectorV2 {

    String host = "127.0.0.1";
    int port = 9000;

    @Test
    public void moreThreadSelector() throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(host, port));
        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);

        System.out.printf("服务端启动...监听：%s %s%n", host, port);
        while (true) {
            while (selector.select(500) > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        acceptHandle(key);
                    } else if (key.isReadable()) {
                        key.cancel(); // todo: cancel 也是系统调用，这样不好
                        System.out.println("read handle...");
                        readHandle(key);
                    } else if (key.isWritable()) {
                        key.cancel(); // todo: cancel 也是系统调用，这样不好
                        System.out.println("write handle...");
                        writeHandle(key);
                    }
                }
            }
        }
    }

    private void acceptHandle(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel client = channel.accept();
        client.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocateDirect(8192);
        client.register(key.selector(), SelectionKey.OP_READ, buffer);
        System.out.printf("非阻塞模式，有连接到达：%s%n", client);
    }

    private void readHandle(SelectionKey key) {
        new Thread(() -> {
            try {
                System.out.println("read...");
                SocketChannel client = (SocketChannel) key.channel();
                ByteBuffer buffer = (ByteBuffer) key.attachment();
                buffer.clear();
                while (true) {
                    int num = client.read(buffer);
                    if (num > 0) {
                        client.register(key.selector(), SelectionKey.OP_WRITE, buffer);
                    } else if (num == 0) {
                        break;
                    } else if (num == -1) {
                        System.out.printf("%s：客户端关闭%n", client);
                        client.close();
                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void writeHandle(SelectionKey key) {
        new Thread(() -> {
            try {
                System.out.println("write...");
                SocketChannel client = (SocketChannel) key.channel();
                ByteBuffer buffer = (ByteBuffer) key.attachment();

                buffer.flip();
                while (buffer.hasRemaining()) {
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    System.out.printf("%s：读到的数据写回：%s", client, new String(bytes));
                    buffer.rewind();
                    client.write(buffer);
                }
                buffer.clear();

                // temp
                key.cancel();
                System.out.println("服务端断开连接");
                client.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
