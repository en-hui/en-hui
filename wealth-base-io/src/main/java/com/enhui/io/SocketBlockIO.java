package com.enhui.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.jupiter.api.Test;

/**
 * 传统的BIO<br>
 * 阻塞io，accept 阻塞；read 阻塞
 */
public class SocketBlockIO {
  /**
   * requested maximum length of the queue of incoming connections <br>
   * 请求的传入连接队列的最大长度
   */
  private final int backlog = 3;

  @Test
  public void socketBio() throws IOException {
    ServerSocket server = new ServerSocket();
    server.bind(new InetSocketAddress(8090), backlog);

    System.out.println("server up to 8090");

    while (true) {
      // 接收客户端的连接（阻塞）
      Socket client = server.accept();
      System.out.printf("客户端地址：%s,端口：%s%n", client.getInetAddress(), client.getPort());

      new Thread(
              () -> {
                // 搞个新线程处理该客户端（每个连接一个新线程）
                try {
                  InputStream in = client.getInputStream();
                  BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                  char[] data = new char[1024];
                  while (true) {
                    // 读取客户端发送的数据，阻塞
                    int num = reader.read(data);
                    if (num > 0) {
                      System.out.println(client + "读取到的数据：" + new String(data));
                    } else if (num == 0) {
                      System.out.println(client + "什么也没读到");
                    } else {
                      System.out.println(client + "客户端断连:" + num);
                      client.close();
                      break;
                    }
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              })
          .start();
    }
  }
}
