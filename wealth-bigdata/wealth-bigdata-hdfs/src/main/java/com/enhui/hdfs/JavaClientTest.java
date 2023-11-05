package com.enhui.hdfs;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class JavaClientTest {

  FileSystem fileSystem = null;
  Configuration configuration = null;

  @Before
  public void initClient() throws IOException, InterruptedException {
    // 不加载配置文件
    configuration = new Configuration(false);
    // 客户端与datanode 使用主机名通信
    configuration.set("dfs.client.use.datanode.hostname", "true");
    // 设置块大小
    configuration.set("dfs.blocksize", "1m");
    System.setProperty("HADOOP_USER_NAME", "root");
    fileSystem = FileSystem.get(URI.create("hdfs://heh-node02:8020"), configuration, "root");
    //    fileSystem = FileSystem.get(config);
    log.info("=======客户端初始化=======");
  }

  @After
  public void close() throws IOException {
    fileSystem.close();
    log.info("=======客户端关闭=======");
  }

  /** 创建文件夹 */
  @Test
  public void mkdir() throws IOException {
    String path = "/user/root";
    if (fileSystem.exists(new Path(path))) {
      log.info("【{}】文件夹已存在", path);
    } else {
      boolean result = fileSystem.mkdirs(new Path(path));
      log.info("【{}】文件夹创建结果：{}", path, result);
    }
  }

  /** 展示指定路径下的文件及文件夹 */
  @Test
  public void ls() throws IOException {
    String path = "/user/root";
    FileStatus[] files = fileSystem.listStatus(new Path(path));
    log.info("【{}】路径下包含文件如下：", path);
    for (FileStatus file : files) {
      log.info(
          "    {} {} {} {} {}",
          file.getPermission().toString(),
          file.getOwner(),
          file.getGroup(),
          LocalDateTime.ofEpochSecond(file.getModificationTime() / 1000, 0, ZoneOffset.ofHours(8)),
          file.getPath().toUri().getPath());
    }
  }

  @Test
  public void copyWithContent() throws IOException {
    String remotePath = "/user/root/putWithContent.txt";
    String req = "hello hadoop 1   hello world";
    String res = "";

    BufferedInputStream input =
        new BufferedInputStream(new ByteArrayInputStream(req.getBytes(StandardCharsets.UTF_8)));
    Path outFile = new Path(remotePath);
    FSDataOutputStream output = fileSystem.create(outFile, true);
    IOUtils.copyBytes(input, output, configuration, true);
    log.info("io形式将字符串内容 【{}】 上传至文件 {}", req, remotePath);

    FileStatus[] files = fileSystem.listStatus(new Path("/user/root/"));
    for (FileStatus file : files) {
      if (remotePath.equals(file.getPath().toUri().getPath())) {
        FSDataInputStream open = fileSystem.open(new Path(remotePath));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copyBytes(open, outputStream, configuration, true);
        res = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        log.info("打开文件并读取内容: 【{}】", res);
      }
    }
  }

  /** 文件上传 */
  @Test
  public void put() throws IOException {
    String localPath = "./data/upload/hello.txt";
    String remotePath = "/user/root/hello.txt";

    fileSystem.copyFromLocalFile(new Path(localPath), new Path(remotePath));
    log.info("本地文件 【{}】 上传至 【{}】", localPath, remotePath);
  }

  /** 文件下载 */
  @Test
  public void get() throws IOException {
    String localPath = "./data/download/hello.txt";
    String remotePath = "/user/root/hello.txt";

    fileSystem.copyToLocalFile(new Path(remotePath), new Path(localPath));
    log.info("【{}】 下载至 【{}】", remotePath, localPath);
  }

  /** 对于多个块的操作 */
  @Test
  public void putMultipleBlock() throws IOException {
    String localPath = "./data/upload/big.txt";
    FileWriter writer = new FileWriter(localPath);
    // 写入完成后,会分为多个块
    for (int i = 0; i < 50000; i++) {
      writer.write("hello world,hello hadoop " + i + "\r\n");
    }
    // 很关键～
    writer.flush();
    String remotePath = "/user/root/big.txt";
    fileSystem.copyFromLocalFile(new Path(localPath), new Path(remotePath));
    log.info("本地文件 【{}】 上传至 【{}】", localPath, remotePath);

    FileStatus fileStatus = fileSystem.getFileStatus(new Path(remotePath));
    BlockLocation[] fileBlockLocations =
        fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
    List<Long> offsetList = new ArrayList<>();
    for (BlockLocation fileBlockLocation : fileBlockLocations) {
      log.info(
          "偏移量：{}，块大小：{}，节点位置：{}",
          fileBlockLocation.getOffset(),
          fileBlockLocation.getLength(),
          fileBlockLocation.getHosts());
      offsetList.add(fileBlockLocation.getOffset());
    }
    if (offsetList.size() > 1) {
      FSDataInputStream open = fileSystem.open(new Path(remotePath));
      // 使用seek 将偏移量调整
      open.seek(offsetList.get(offsetList.size() - 1));
      log.info("使用seek调整偏移，直接读取最后一个块的数据，读一行：【{}】", open.readLine());
    }
  }
}
