package com.enhui.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO，多路复用器练习，单线程模式，accept和数据读写在一个线程
 */
public class SocketNIOSelectorV1 {

    String host = "127.0.0.1";
    int port = 9000;

    @Test
    public void singleThreadSelector() throws IOException {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(host, port));


            /**
             * 对应linux的多路复用器（select、poll、epoll）
             * 在linux中优先选择epoll，可以用 -D 修改<br>
             * -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider<br>
             * -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.PollSelectorProvider<br>
             * 如果使用的epoll，会在这一步完成 epoll_create
             */
            Selector selector = Selector.open();
            /**
             * 假设有 fd3、fd4两个fd（代表两个网络连接）<br>
             * java的register() 方法对应内核的方法：<br>
             * select、poll：将fd放进jvm开辟的集合中<br>
             * epoll：epoll_ctl(),将fd放进内核中的空间 <br>
             */
            server.register(selector, SelectionKey.OP_ACCEPT);

            System.out.printf("服务端启动...监听：%s %s%n", host, port);
            while (true) {
                /**
                 * java的select()方法对应内核的方法：<br>
                 * select、poll：内核的select(fds) 或者 poll(fds),将jvm中的fd作为参数<br>
                 * epoll：内核的epoll_wait()<br>
                 */
                while (selector.select(500) > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandle(key);
                        } else if (key.isReadable()) {
                            System.out.println("read handle...");
                            readHandle(key);
                        } else if (key.isWritable()) {
                            System.out.println("write handle...");
                            writeHandle(key);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeHandle(SelectionKey key) throws Exception {
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
    }

    private void readHandle(SelectionKey key) throws IOException {
        System.out.println("read...");
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read = 0;
        while (true) {
            read = client.read(buffer);
            if (read > 0) {
                client.register(key.selector(), SelectionKey.OP_WRITE, buffer);
            } else if (read == 0) {
                break;
            } else {
                System.out.printf("%s：客户端关闭%n", client);
                // 客户端断连会返回-1
                client.close();
                break;
            }
        }
    }

    private void acceptHandle(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        /** 接收到请求，得到新的fd */
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocateDirect(8192);
        Selector selector = key.selector();

        /**
         * select、poll：将fd放进jvm开辟的集合中<br>
         * epoll：epoll_ctl(),将fd放进内核中的空间 <br>
         */
        client.register(selector, SelectionKey.OP_READ, buffer);
        System.out.printf("非阻塞模式，有连接到达：%s%n", client);
    }
}
