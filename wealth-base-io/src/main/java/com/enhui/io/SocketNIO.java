package com.enhui.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NIO<br>
 * 非阻塞io，accept 不阻塞；read 不阻塞
 */
public class SocketNIO {

    String host = "127.0.0.1";
    int port = 9000;

    @Test
    public void newScoketNIO() throws IOException {
        List<SocketChannel> clients = new ArrayList<>();
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(host, port));
            System.out.printf("服务端启动...监听：%s:%s%n", host, port);
            // 非阻塞1
            server.configureBlocking(false);
            while (true) {
                SocketChannel client = server.accept();
                // 没有客户端连接，JAVA得到的是null（os中的返回值是-1）
                if (client != null) {
                    System.out.printf("非阻塞模式，有连接到达：%s%n", client);
                    client.configureBlocking(false);
                    clients.add(client);
                }

                // 这里可以放另一个线程，也可以多线程。将accept和读写事件分开
                ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                Iterator<SocketChannel> iterator = clients.iterator();
                while (iterator.hasNext()) {
                    SocketChannel cli = iterator.next();
                    while (true) {
                        // 非阻塞2
                        int n = cli.read(buffer);
                        if (n > 0) {
                            buffer.flip();
                            byte[] bytes = new byte[buffer.limit()];
                            buffer.get(bytes);
                            System.out.printf("%s：读到的数据：%s", cli, new String(bytes));
                            buffer.clear();
                        } else if (n == 0) {
                            break;
                        } else if (n == -1) {
                            System.out.printf("%s：客户端关闭%n", cli);
                            cli.close();
                            iterator.remove();
                            break;
                        }
                    }

                }
            }

        }
    }
}
