package com.enhui.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 操作系统文件io的java api学习
 *
 * <p>文件读写性能(仅限于文件)：on heap(堆内) < off heap(堆外) < maap <br>
 * 堆内堆外：可以理解为jvm的堆在java进程的堆里 堆内：说的是jvm的堆里的字节数组<br>
 * 堆外：说的是jvm的堆外，也就是java进程的堆内<br>
 * maaped映射：是maap调用的一个进程和内核共享的内存区域，且这个内存区域是page cache 到文件的映射<br>
 */
public class OSFileIO {
  String file = "testFile.txt";
  String fullPath;
  byte[] data = "123456789\n".getBytes();

  @BeforeEach
  public void getProjectPath() {
    // 获取到 target 下的目录
    URL resource = this.getClass().getClassLoader().getResource(file);
    if (resource == null) {
      System.out.println("先在对应位置创建文件...");
      return;
    }
    String filePath = resource.getFile();
    // 替换末尾
    fullPath = filePath.replace("/target/classes/", "/src/main/resources/");
    System.out.println(fullPath);
  }

  /**
   * 最基本的file写<br>
   * 每次调用write，都会触发系统调用，用户态、内核态切换，真正写到磁盘（page cache）
   *
   * @throws IOException
   * @throws InterruptedException
   */
  @Test
  public void testBasicFileIO() throws IOException, InterruptedException {
    File file = new File(fullPath);
    FileOutputStream out = new FileOutputStream(file);
    while (true) {
      Thread.sleep(1000);
      out.write(data);
    }
  }

  /**
   * buffer file写 <br>
   * 每次调用write，会先把数据缓存在jvm的一个字节数组缓冲区，默认是8kb<br>
   * 写满才进行flush，用户态、内核态切换，真正写到磁盘（page cache）
   *
   * @throws IOException
   * @throws InterruptedException
   */
  @Test
  public void testBufferFileIO() throws IOException, InterruptedException {
    File file = new File(fullPath);
    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
    while (true) {
      Thread.sleep(1000);
      out.write(data);
    }
  }

  /**
   * 测试文件 NIO
   *
   * @throws IOException
   */
  @Test
  public void testRandomAccessFileWrite() throws IOException, InterruptedException {
    RandomAccessFile raf = new RandomAccessFile(fullPath, "rw");

    // 普通写
    raf.write("hello java\n".getBytes());
    raf.write("hello hadoop\n".getBytes());
    System.out.println("普通写两行：hello java\n hello hadoop------");
    Thread.sleep(5000);

    // 随机写——指定位置，再写
    raf.seek(4);
    raf.write("ooxx".getBytes());
    System.out.println("挪到4位置，写ooxx------");
    Thread.sleep(5000);

    // 堆外映射写 mmap
    FileChannel channel = raf.getChannel();
    // mmap 堆外的内存，和文件映射的
    MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4096);
    map.put("@@@".getBytes()); // 不是系统调用，但是数据会到达 内核的page cache
    // 曾经用 out.write()   这样的系统调用，才能让程序的data进入内核的page cache
    // mmap的内存映射，依然受内核的page cache约束（刷写，丢数据的特征）
    System.out.println("使用 mmap 写 @@@ 三个字符，从0位置开始------");
    // 相当于 flush，刷盘（溢写磁盘）
    // map.force();
    Thread.sleep(5000);

    // channel 和 byteBuffer 一起用，byteBuffer理解参照下面方法：whatByteBuffer
    // byteBuffer 的put
    raf.seek(0);
    ByteBuffer buffer = ByteBuffer.allocateDirect(8192);
    int read = channel.read(buffer); // 等同于buffer.put(),将通道的数据给buffer
    buffer.flip();
    System.out.println(buffer);
    for (int i = 0; i < buffer.limit(); i++) {
      Thread.sleep(200);
      System.out.print((char) buffer.get(i));
    }
  }

  /**
   * 演示ByteBuffer <br>
   * [pos=0 lim=8192 cap=8192]<br>
   * pos（position）：位置，初始值为0<br>
   * lim（limit）：限制，初始值为容量大小<br>
   * cap（capacity）：容量，初始值为容量大小<br>
   */
  @Test
  public void whatByteBuffer() {
    // 在jvm的堆上申请一块内存空间
    ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
    // 在堆外申请一块内存空间
    ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(8192);
    ByteBuffer buffer = byteBuffer;
    // [pos=0 lim=8192 cap=8192]
    System.out.println("初始：" + buffer);

    // [pos=3 lim=8192 cap=8192]
    // 写入了三个字节，所以位置到了3
    buffer.put("123".getBytes());
    System.out.println("put 后：" + buffer);

    // [pos=0 lim=3 cap=8192]
    // 每次要读，就使用flip进行翻转，把pos设为0，limit设为已写位置
    buffer.flip();
    System.out.println("flip 后：" + buffer);

    // [pos=1 lim=3 cap=8192]
    // 获取一个字节，取出，所以pos位置走到1
    buffer.get();
    System.out.println("get 后：" + buffer);

    // [pos=2 lim=8192 cap=8192]
    // 每次要写，就使用compact，把已经读的位置挤压掉，所有内容前移,所以0被取出了，所有前移，pos为2，该写3位置了
    buffer.compact();
    System.out.println("compact 后：" + buffer);

    // [pos=0 lim=8192 cap=8192]
    // 清空内容，所有位置重置回最初样子
    buffer.clear();
    System.out.println("clear 后：" + buffer);
  }
}
