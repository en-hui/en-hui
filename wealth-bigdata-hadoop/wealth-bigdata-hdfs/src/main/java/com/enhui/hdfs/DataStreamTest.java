package com.enhui.hdfs;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class DataStreamTest {
  FileSystem fileSystem = null;
  Configuration config = null;
  final String srcRemotePathStr = "/user/root/big.txt";
  final String destRemotePathStr = "/user/root/big_dest.txt";
  final Path srcRemotePath = new Path(srcRemotePathStr);
  final Path destRemotePath = new Path(destRemotePathStr);
  long srcOffset = 0;
  AtomicInteger sourceDataNum = new AtomicInteger(1);
  LinkedBlockingDeque<byte[]> localQueue = new LinkedBlockingDeque<>(5000);
  FSDataOutputStream srcAppend = null;
  FSDataOutputStream destAppend = null;
  FSDataInputStream srcOpenStream = null;
  ByteBuffer buffer = ByteBuffer.allocate(1024);
  long statisticsWriteBytes = 0;

  @Before
  public void initClient() throws IOException {
    config = new Configuration(true);
    // 客户端与datanode 使用主机名通信
    config.set("dfs.client.use.datanode.hostname", "true");
    // 设置块大小
    config.set("dfs.blocksize", "1m");
    System.setProperty("HADOOP_USER_NAME", "root");
    fileSystem = FileSystem.get(config);

    log.info("=======初始化完毕=======");
  }

  public void initIO() throws IOException {
    // src 初始化
    if (!fileSystem.exists(srcRemotePath)) {
      // 不存在先本地直接copy过去一个
      String localPath = "./data/upload/big.txt";
      FileWriter writer = new FileWriter(localPath);
      for (int i = 0; i < 500; i++) {
        writer.write("hello world,hello hadoop " + i + "\r\n");
      }
      // 很关键～
      writer.flush();
      fileSystem.copyFromLocalFile(new Path(localPath), srcRemotePath);
      log.info("初始化源文件");
    }
    srcAppend = fileSystem.append(srcRemotePath);
    srcOpenStream = fileSystem.open(srcRemotePath);

    // dest 初始化
    if (!fileSystem.exists(destRemotePath)) {
      FSDataOutputStream out = fileSystem.create(destRemotePath);
      // out 和 下面的append 不能同时开启，否则会报错AlreadyBeingCreatedException
      out.close();
      log.info("初始化目的文件");
    }
    destAppend = fileSystem.append(destRemotePath);
  }

  @After
  public void close() throws IOException {
    if (srcAppend != null) {
      srcAppend.close();
    }
    if (destAppend != null) {
      destAppend.close();
    }
    if (fileSystem != null) {
      fileSystem.close();
    }
    log.info("=======结束，客户端关闭=======");
  }

  /**
   * hdfs 文件增量同步，灾备方案调研
   *
   * @throws IOException
   */
  @Test
  public void testDataPipeline() throws IOException {
    initIO();
    // 该线程往源文件持续写数据
    Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "HDFS--srcAppendThread"))
        .scheduleAtFixedRate(this::productionData, 500, 500, TimeUnit.MILLISECONDS);
    // 该线程持续读取源文件，缓存至本地队列
    Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "HDFS--srcReadThread"))
        .scheduleAtFixedRate(this::dataPipelineProducer, 100, 100, TimeUnit.MILLISECONDS);
    // 该线程持续将本地队列的数据写到目的文件
    Executors.newSingleThreadExecutor(r -> new Thread(r, "HDFS--destAppendThread"))
        .submit(this::dataPipelineConsumer);

    // 主线程阻塞不退出
    System.in.read();
  }

  /** 生产数据 */
  public void productionData() {
    try {
      byte[] bytes =
          ("productionData追加写——" + sourceDataNum.getAndIncrement() + "\r\n")
              .getBytes(StandardCharsets.UTF_8);
      srcAppend.write(bytes);
      srcAppend.flush();

      FileStatus srcFileStatus = fileSystem.getFileStatus(srcRemotePath);
      log.info("源文件追加写「{}」,源文件实际大小：{}", bytes.length, srcFileStatus.getLen());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** 数据管道的生产者，从hdfs源文件读数据，放进本地队列 */
  public void dataPipelineProducer() {
    try {
      FileStatus fileStatus = fileSystem.getFileStatus(srcRemotePath);
      long len = fileStatus.getLen();
      if (srcOffset < len) {
        // 使用seek 将偏移量调整
        srcOpenStream.seek(srcOffset);
        buffer.clear();
        srcOpenStream.read(buffer);
        if (buffer.hasArray()) {
          byte[] array;
          if (buffer.hasRemaining()) {
            // 不满的字节数组
            array = new byte[buffer.position()];
            for (int i = 0; i < buffer.position(); i++) {
              array[i] = buffer.get(i);
            }
          } else {
            array = buffer.array();
          }
          localQueue.offer(array);
          // 放进队列后，记录源的进度偏移
          srcOffset += array.length;
          log.info("源文件大小：{},读取源文件后，新的偏移是：【{}】，本次写入阻塞队列的大小为：{}", len, srcOffset, array.length);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** 数据管道的消费者，从本地队列取数据，写进hdfs目的文件 */
  public void dataPipelineConsumer() {
    try {
      while (true) {
        // 取出，不移除
        byte[] peek = localQueue.peek();
        if (peek != null) {
          // 写入目的
          destAppend.write(peek);
          destAppend.flush();
          // 写入完毕，从队列删除
          localQueue.remove();
          statisticsWriteBytes += peek.length;

          // 看下flush后，能不能实时查到大小
          FileStatus destFileStatus = fileSystem.getFileStatus(destRemotePath);
          log.info(
              "向目的写了 {} 字节，本次为：{}，实际目的查到的大小：{}",
              statisticsWriteBytes,
              peek.length,
              destFileStatus.getLen());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void clear() throws IOException {
    fileSystem.delete(srcRemotePath, false);
    fileSystem.delete(destRemotePath, false);
  }
}
