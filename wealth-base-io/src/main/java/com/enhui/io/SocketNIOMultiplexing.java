package com.enhui.io;

import org.junit.jupiter.api.Test;

/**
 * 多路复用器 ,os中 select、poll、epoll 都是多路复用器。select、poll是一个阶段、epoll更高级<br>
 * select、poll：每次在系统调用时，将所有的fd作为参数传递，内核中遍历所有的fd，查询状态，返回有状态的fd<br>
 * select有个限制，fd参数最大1024，超过1024就要调用多次select，poll没有这个限制<br>
 * 问题：<br>
 * 1.每次调用都传所有的fd<br>
 * 2.每次调用，内核中都要遍历所有的fd来查询状态
 * epoll：再内核开辟一块空间，只需要把fd传递一次，存在这块内存空间，内核对中断进行扩展，将有状态的fd提前放到一个链表中，程序调用时，直接返回链表<br>
 *
 * <p>网络连接socket再os中，就是fd文件描述符<br>
 * 多路复用器：操作系统提供的系统调用，返回有状态的fd（可读写的）<br>
 * 场景：服务端接收到10k个网络连接，要读写网络连接中的数据<br>
 * 未用多路复用器时：程序调用10k次read（用户空间遍历），无数据就返回null（read是系统调用，对于无数据的调用，很多余） <br>
 * 用了多路复用器时：程序先调用1次多路复用器，得到有数据的连接，再对有数据的连接调用read（避免无用的系统调用）<br>
 *
 * <p>select（man 2 select）：包含一个系统调用<br>
 * select： 接收10k个fd，在内核中遍历一次，查询io状态，将有状态的fd返回。有一个最大限制，一次只能询问1024的fd（10k就要多次调用）<br>
 *
 * <p>poll（man 2 poll）：包含一个系统调用<br>
 * poll：接收10k个fd，在内核中遍历一次，查询io状态，将有状态的fd返回。没有最大1024的限制（10k只需1次调用）<br>
 *
 * <p>epoll（man epoll）：包含三个系统调用<br>
 * epoll_create：内核中开辟一块空间，存放fd（使用了红黑树结构）<br>
 * epoll_ctl：将fd新增、修改、删除等操作，到内核中的这块内存空间<br>
 * epoll_wait：调用后，直接返回有状态的fd<br>
 */
public class SocketNIOMultiplexing {

  @Test
  public void testPoll() {}

  @Test
  public void testEPoll() {}
}
