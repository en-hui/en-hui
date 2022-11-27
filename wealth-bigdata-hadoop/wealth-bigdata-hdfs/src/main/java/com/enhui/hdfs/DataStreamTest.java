package com.enhui.hdfs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStreamBuilder;
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


  @Before
  public void initClient() throws IOException {
    config = new Configuration(true);
    // 客户端与datanode 使用主机名通信
    config.set("dfs.client.use.datanode.hostname", "true");
    // 设置块大小
    config.set("dfs.blocksize", "1m");
    System.setProperty("HADOOP_USER_NAME", "root");
    fileSystem = FileSystem.get(config);
    log.info("=======客户端初始化=======");
  }

  @After
  public void close() throws IOException {
    fileSystem.close();
    log.info("=======客户端关闭=======");
  }

  /** 按块读取，假装增量同步hdfs单文件，然后写到hdfs的另一个文件 */
  @Test
  public void testBlock() throws IOException {
    final String srcRemotePath = "/user/root/big.txt";
    final String destRemotePath = "/user/root/big_dest.txt";
      final Path srcPath = new Path(srcRemotePath);
      final Path destPath = new Path(destRemotePath);
      // 搞个线程，往hdfs源文件写数据（不停的写，稍微慢点写）
    new Thread(
            () -> {
              try {
                boolean first = true;
                while (true) {
                  if (first) {
                    // 只有第一次判断文件是否存在，不存在先本地直接copy过去一个
                    if (!fileSystem.exists(srcPath)) {
                      String localPath = "./data/upload/big.txt";
                      FileWriter writer = new FileWriter(localPath);
                      for (int i = 0; i < 50000; i++) {
                        writer.write("hello world,hello hadoop " + i + "\r\n");
                      }
                      // 很关键～
                      writer.flush();
                      fileSystem.copyFromLocalFile(new Path(localPath), new Path(srcRemotePath));
                    }
                    first = false;
                  }


                }

              } catch (IOException e) {
                e.printStackTrace();
              }
            })
        .start();

    FileStatus fileStatus = fileSystem.getFileStatus(new Path(srcRemotePath));
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
      FSDataInputStream open = fileSystem.open(new Path(srcRemotePath));
      // 使用seek 将偏移量调整
      open.seek(offsetList.get(offsetList.size() - 1));
      log.info("使用seek调整偏移，直接读取最后一个块的数据，读一行：【{}】", open.readLine());
    }
  }
}
