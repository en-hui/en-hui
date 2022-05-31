package com.enhui.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

/**
 * java中的new io <br>
 * os中的nonblocking io
 */
public class SocketNIO {

  @Test
  public void socketNio() throws IOException, InterruptedException {
    LinkedList<SocketChannel> clients = new LinkedList<>();

    ServerSocketChannel server = ServerSocketChannel.open();
    server.bind(new InetSocketAddress(8090));
    System.out.println("server up to  8090");
    // 重点，设置接收请求为非阻塞
    server.configureBlocking(false);

    while (true) {
      SocketChannel client = server.accept();
      // 没有客户端连接，得到的是null（os中的返回值是-1）
      if (client != null) {
        // 重点，设置读取数据为非阻塞
        client.configureBlocking(false);
        System.out.println("client...port:" + client.socket().getPort());
        clients.add(client);
      }

      ByteBuffer buffer = ByteBuffer.allocateDirect(4096); // 可以再堆内，可以再堆外
      // 使用单线程循环处理，串行化，可以考虑优化为多线程处理
      for (SocketChannel c : clients) {
        int num = c.read(buffer);
        if (num > 0) {
          buffer.flip();
          byte[] bytes = new byte[buffer.limit()];
          buffer.get(bytes);

          String data = new String(bytes);
          System.out.println(c.socket().getPort() + ":" + data);
          buffer.clear();
        }
      }
    }
  }
}
