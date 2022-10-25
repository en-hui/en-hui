package com.enhui.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用：初级的多线程模型<br>
 * 将读写操作扔给新的线程处理，使用了.cancel()，这是系统调用<br>
 * 此版本就是为了知道多线程的问题：accept和R/W 异步的时候会有重复进入handlerR/W 的问题
 */
public class SocketNIOMultiplexingV2 {
  private ServerSocketChannel server = null;
  // 对应linux的多路复用器（select、poll、epoll）
  private Selector selector = null;
  int port = 8090;

  public void initServer() {
    try {
      server = ServerSocketChannel.open();
      server.configureBlocking(false);
      server.bind(new InetSocketAddress(port));

      /**
       * 在linux中优先选择epoll，可以用 -D 修改<br>
       * -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider<br>
       * -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.PollSelectorProvider<br>
       * 如果使用的epoll，会在这一步完成 epoll_create
       */
      selector = Selector.open();
      /**
       * 假设有 fd3、fd4两个fd（代表两个网络连接）<br>
       * java的register() 方法对应内核的方法：<br>
       * select、poll：将fd放进jvm开辟的集合中<br>
       * epoll：epoll_ctl(),将fd放进内核中的空间 <br>
       */
      server.register(selector, SelectionKey.OP_ACCEPT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void start() {
    initServer();
    System.out.println("服务器启动了");
    try {
      while (true) {
        /**
         * java的select()方法对应内核的方法：<br>
         * select、poll：内核的select(fds) 或者 poll(fds),将jvm中的fd作为参数<br>
         * epoll：内核的epoll_wait()<br>
         */
        while (selector.select(500) > 0) {
          Set<SelectionKey> selectionKeys = selector.selectedKeys();
          Iterator<SelectionKey> iterator = selectionKeys.iterator();
          while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isAcceptable()) {
              acceptHandler(key);
            } else if (key.isReadable()) {
              // 单线程处理业务，如果查缓存，查库，计算，保存等特别耗时，那其他人的请求就会有很大延时
              // 所以要将 io 和 计算解耦，
              key.cancel(); // cancel 也是系统调用，这样不好
              readHandler(key);
            } else if (key.isWritable()) {
              key.cancel();
              writeHandle(key);
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void acceptHandler(SelectionKey key) {
      try {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        /** 接收到请求，得到新的fd */
        SocketChannel client = channel.accept();
        client.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocateDirect(8192);
        /**
         * select、poll：将fd放进jvm开辟的集合中<br>
         * epoll：epoll_ctl(),将fd放进内核中的空间 <br>
         */
        client.register(selector, SelectionKey.OP_READ, buffer);
        System.out.println("--------------------------------------");
        System.out.println("处理连接请求，新客户端：" + client.getRemoteAddress());
        System.out.println("--------------------------------------");
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  private void readHandler(SelectionKey key) {
    new Thread(()->{
      System.out.println("read....");
      SocketChannel client = (SocketChannel) key.channel();
      ByteBuffer buffer = (ByteBuffer) key.attachment();
      buffer.clear();
      int read = 0;
      try {
        while (true) {
          read = client.read(buffer);
          if (read > 0) {
            client.register(key.selector(),SelectionKey.OP_WRITE,buffer);
          } else if (read == 0) {
            break;
          } else {
            System.out.println("客户端断开连接");
            // 客户端断连会返回-1
            client.close();
            break;
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();
  }

  public void writeHandle(SelectionKey key) {
    new Thread(()->{
      System.out.println("write....");
      SocketChannel client = (SocketChannel)key.channel();
      ByteBuffer buffer = (ByteBuffer)key.attachment();
      buffer.flip();
      while (buffer.hasRemaining()) {
        try{
          client.write(buffer);
        }catch (IOException e) {
          e.printStackTrace();
        }
      }
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      buffer.clear();
      key.cancel();
      try {
        System.out.println("服务端断开连接");
        client.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();

  }
}
