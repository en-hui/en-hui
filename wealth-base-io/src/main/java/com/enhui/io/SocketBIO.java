package com.enhui.io;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 传统的BIO<br>
 * 阻塞io，accept 阻塞；read 阻塞
 */
public class SocketBIO {

    String host = "127.0.0.1";
    int port = 9000;

    /**
     * java.net 包，旧api
     *
     * @throws IOException
     */
    @Test
    public void oldSocketBio() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            System.out.printf("服务端启动...监听：%s:%s%n", host, port);

            while (true) {
                // 阻塞1
                Socket client = serverSocket.accept();
                System.out.printf("阻塞模式，有连接到达进行接收...客户端：%s%n", client);
                new Thread(() -> {
                    try {
                        InputStream inputStream = client.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        char[] buffer = new char[1024];
                        while (true) {
                            // 阻塞2
                            int n = reader.read(buffer);
                            if (n > 0) {
                                System.out.printf("%s：读到的数据：%s", client, new String(buffer, 0, n));
                            } else if (n == -1) {
                                client.close();
                                System.out.printf("%s：客户端关闭%n", client);
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    /**
     * new io的阻塞模型
     * java.nio.channels 包，新api
     *
     * @throws IOException
     */
    @Test
    public void newSocketBio() throws IOException {
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(host, port));
            System.out.printf("服务端启动...监听：%s:%s%n", host, port);

            while (true) {
                // 阻塞1
                SocketChannel client = server.accept();
                client.configureBlocking(true);
                System.out.printf("阻塞模式，有连接到达进行接收...客户端：%s%n", client);
                new Thread(() -> {
                    try {
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                        while (true) {
                            // 阻塞2
                            int n = client.read(buffer);
                            if (n > 0) {
                                buffer.flip();
                                byte[] bytes = new byte[buffer.limit()];
                                buffer.get(bytes);

                                String data = new String(bytes);
                                buffer.clear();
                                System.out.printf("%s：读到的数据：%s", client, data);
                            } else if (n == -1) {
                                client.close();
                                System.out.printf("%s：客户端关闭%n", client);
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
