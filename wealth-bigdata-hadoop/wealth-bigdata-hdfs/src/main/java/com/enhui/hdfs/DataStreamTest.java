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
  long srcOffsetGlobal = 0;
  long srcOffsetUnit = 0;
  AtomicInteger sourceDataNum = new AtomicInteger(1);
  LinkedBlockingDeque<byte[]> localQueue = new LinkedBlockingDeque<>(5000);
  FSDataInputStream srcOpenStreamGlobal = null;
  // 1M = 1024 * 1024 = 1048576字节
  ByteBuffer bufferGlobal = ByteBuffer.allocate(1024 * 1024 * 10);
  ByteBuffer bufferUnit = ByteBuffer.allocate(1024 * 1024 * 10);
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
      for (int i = 0; i < 5 * 10000; i++) {
        writer.write("init data——hello world,hello hadoop " + i + "\r\n");
      }
      // 很关键～
      writer.flush();
      fileSystem.copyFromLocalFile(new Path(localPath), srcRemotePath);
      log.info("初始化源文件");
    }
    srcOpenStreamGlobal = fileSystem.open(srcRemotePath);

    // dest 初始化
    if (!fileSystem.exists(destRemotePath)) {
      FSDataOutputStream out = fileSystem.create(destRemotePath);
      // out 和 下面的append 不能同时开启两个写入流，否则会报错AlreadyBeingCreatedException
      out.close();
      log.info("初始化目的文件");
    }
  }

  @After
  public void close() throws IOException {
    if (srcOpenStreamGlobal != null) {
      srcOpenStreamGlobal.close();
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
    // 是否测试：每次都重新打开文件 vs 全局打开一次
    boolean isTest = false;
    // 该线程持续读取源文件，缓存至本地队列
    Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "HDFS--srcReadThread"))
        .scheduleAtFixedRate(
            isTest ? this::dataPipelineProducerTestGlobal : this::dataPipelineProducer,
            100,
            100,
            TimeUnit.MILLISECONDS);
    // 该线程持续将本地队列的数据写到目的文件
    Executors.newSingleThreadExecutor(r -> new Thread(r, "HDFS--destAppendThread"))
        .submit(this::dataPipelineConsumer);

    // 主线程阻塞不退出
    System.in.read();
  }

  /** 生产数据 */
  public void productionData() {
    try {
      FSDataOutputStream srcAppend = fileSystem.append(srcRemotePath);
      int n = 10;
      String msg = "productionData追加写";
      String repeatMsg = getRepeatContent(msg, n, sourceDataNum);
      byte[] bytes = repeatMsg.getBytes(StandardCharsets.UTF_8);
      srcAppend.write(bytes);
      srcAppend.flush();
      srcAppend.close();

      FileStatus srcFileStatus = fileSystem.getFileStatus(srcRemotePath);
      if (n == 1) {
        log.info("源文件追加写「{}」,源文件实际大小：{},内容为：{}", bytes.length, srcFileStatus.getLen(), msg);
      } else {
        log.info("源文件追加写「{}」,源文件实际大小：{}", bytes.length, srcFileStatus.getLen());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** 数据管道的生产者，从hdfs源文件读数据，放进本地队列 */
  public void dataPipelineProducer() {
    try {
      FileStatus fileStatus = fileSystem.getFileStatus(srcRemotePath);
      long len = fileStatus.getLen();
      // 每次都重新打开文件 vs 全局打开一次
      // 每次都重新打开文件
      if (srcOffsetUnit < len) {
        FSDataInputStream srcOpenStreamUnit = fileSystem.open(srcRemotePath);
        // 使用seek 将偏移量调整
        srcOpenStreamUnit.seek(srcOffsetUnit);
        bufferUnit.clear();
        srcOpenStreamUnit.read(bufferUnit);
        srcOpenStreamUnit.close();
        if (bufferUnit.hasArray()) {
          byte[] array;
          if (bufferUnit.hasRemaining()) {
            // 不满的字节数组
            array = new byte[bufferUnit.position()];
            for (int i = 0; i < bufferUnit.position(); i++) {
              array[i] = bufferUnit.get(i);
            }
          } else {
            array = bufferUnit.array();
          }
          if (array.length != 0) {
            localQueue.offer(array);
            // 放进队列后，记录源的进度偏移
            srcOffsetUnit += array.length;
            log.info(
                "局部每次打开，读源问题——源文件大小：{},读取源文件后，新的偏移是：【{}】，本次写入阻塞队列的大小为：{}",
                len,
                srcOffsetUnit,
                array.length);
          } else {
            log.info(
                "局部每次打开，读源问题——源文件大小：{},读到的最新偏移是：【{}】,读到的position大小为：{}",
                len,
                srcOffsetUnit,
                bufferUnit.position());
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 读源文件<br>
   * 每次都重新打开文件 vs 全局打开一次<br>
   * 测试结果--应该每次都重新打开文件，否则源文件的增量read不到
   */
  public void dataPipelineProducerTestGlobal() {
    // 每次都重新打开文件
    dataPipelineProducer();
    try {
      FileStatus fileStatus = fileSystem.getFileStatus(srcRemotePath);
      long len = fileStatus.getLen();
      // 全局打开一次
      if (srcOffsetGlobal < len) {
        srcOpenStreamGlobal.seek(srcOffsetGlobal);
        bufferGlobal.clear();
        srcOpenStreamGlobal.read(bufferGlobal);
        if (bufferGlobal.hasArray()) {
          byte[] array;
          if (bufferGlobal.hasRemaining()) {
            // 不满的字节数组
            array = new byte[bufferGlobal.position()];
            for (int i = 0; i < bufferGlobal.position(); i++) {
              array[i] = bufferGlobal.get(i);
            }
          } else {
            array = bufferGlobal.array();
          }
          if (array.length != 0) {
            srcOffsetGlobal += array.length;
            log.info(
                "全局打开一次，读源文件——源文件大小：{},读取源文件后，新的偏移是：【{}】，本次写入阻塞队列的大小为：{}",
                len,
                srcOffsetGlobal,
                array.length);
          } else {
            log.info(
                "全局打开一次，读源文件——源文件大小：{},读到的最新偏移是：【{}】,读到的position大小为：{}",
                len,
                srcOffsetGlobal,
                bufferGlobal.position());
          }
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
          FSDataOutputStream destAppend = fileSystem.append(destRemotePath);
          // 写入目的
          destAppend.write(peek);
          destAppend.flush();
          destAppend.close();
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

  /**
   * 获取某字符串重复n次的结果<br>
   * 每条一行 \r\n 分割
   *
   * @param msg 单条内容
   * @param n 重复n次
   * @param atomicInteger 后缀数字
   * @return
   */
  public static String getRepeatContent(String msg, int n, AtomicInteger atomicInteger) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
      sb.append(msg).append("——").append(atomicInteger.getAndIncrement()).append("\r\n");
    }
    return sb.toString();
  }

  /**
   * 测试追加写文件<br>
   * 只有close流对象，内容才被真正写入，才能查到文件大小变化
   *
   * @throws IOException
   */
  @Test
  public void appendFile() throws IOException {
    Path path = new Path("/user/root/hello.txt");
    // 场景1：不每次都关闭流对象
    FSDataOutputStream destAppend = fileSystem.append(path);
    System.out.printf("写入前，文件大小为：%s \n", fileSystem.getFileStatus(path).getLen());
    for (int i = 0; i < 3; i++) {
      // 写入目的
      String msg = "你好" + i;
      byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
      destAppend.write(bytes);
      destAppend.flush();
      System.out.printf(
          "不关闭流，写入数据：{%s},大小为：%s,写入后文件大小为：{%s} \n",
          msg, bytes.length, fileSystem.getFileStatus(path).getLen());
    }
    destAppend.close();
    System.out.println("关闭流后，文件大小为：" + fileSystem.getFileStatus(path).getLen());
    // 场景2：每次都关闭流对象
    for (int i = 0; i < 3; i++) {
      destAppend = fileSystem.append(path);
      // 写入目的
      String msg = "你好" + i;
      byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
      destAppend.write(bytes);
      destAppend.flush();
      destAppend.close();
      System.out.printf(
          "关闭流，写入数据：{%s},大小为：%s,写入后文件大小为：{%s} \n",
          msg, bytes.length, fileSystem.getFileStatus(path).getLen());
    }
  }

  @Test
  public void clear() throws IOException {
    fileSystem.delete(srcRemotePath, false);
    fileSystem.delete(destRemotePath, false);
  }
}
