package com.enhui.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

/**
 * c10k :http://www.kegel.com/c10k.html <br>
 * 10k个客户端连接服务器 <br>
 * 本机ip+本机port+服务端ip+服务端port 唯一即可建立socket连接
 */
public class C10kClient {

  @Test
  public void client() throws UnknownHostException {
    LinkedList<SocketChannel> clients = new LinkedList<>();
    String hostAddress = "localhost"; // InetAddress.getLocalHost().getHostAddress();
    InetSocketAddress serverAddr = new InetSocketAddress(hostAddress, 8090);
    // 1w 连接
    for (int i = 10000; i < 20000; i++) {
      try {
        SocketChannel client = SocketChannel.open();
        client.bind(new InetSocketAddress(hostAddress, i));
        client.connect(serverAddr);
        clients.add(client);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    System.out.println("clients: " + clients.size());
  }
}
