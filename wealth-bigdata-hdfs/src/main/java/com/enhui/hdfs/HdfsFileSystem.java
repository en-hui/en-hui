package com.enhui.hdfs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HdfsFileSystem {
  // 1.私有构造器
  private HdfsFileSystem() {
    init();
  }
  // 2.创建静态对象并实例化
  private static final HdfsFileSystem INSTANCE = new HdfsFileSystem();
  // 3.向外暴露一个静态的公共方法
  public static HdfsFileSystem getINSTANCE() {
    return INSTANCE;
  }

  private Configuration configuration = null;
  private FileSystem fileSystem = null;

  private void init() {
    // true : 是否加载默认配置文件构造连接（设置false可以上传配置文件自己构造连接其他集群）
    configuration = new Configuration(true);
    // 阿里云搭建的hadoop集群，集群搭建配置的是内网ip，
    // idea中开发需要配置使用主机名连接（否则使用内网ip连接不上DN），且必须本机配置host文件来做ip映射
    configuration.set("dfs.client.use.datanode.hostname", "true");

    // 取core配置的schema获取客户端类型；取环境变量 HADOOP_USER_NAME 当做客户端用户,如果没有取当前系统登录用户
    try {
      // fileSystem = FileSystem.get(URI.create("hdfs://heh-node02:9000"), configuration, "root");
      System.setProperty("HADOOP_USER_NAME", "root");
      fileSystem = FileSystem.get(configuration);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

    public void mkdir() throws IOException {
        Path dir = new Path("test1");
        if (fileSystem.exists(dir)) {
            fileSystem.delete(dir, true);
        }
        fileSystem.mkdirs(dir);
    }

    public void upload() throws IOException {
        BufferedInputStream input =
                new BufferedInputStream(new FileInputStream(new File("./data/upload/hello.txt")));
        Path outFile = new Path("/user/root/hello.txt");
        FSDataOutputStream output = fileSystem.create(outFile);

        IOUtils.copyBytes(input, output, configuration, true);
    }

    public void blocks() throws IOException {
        Path file = new Path("/user/root/zstd-1.5.1.tar.gz");
        FileStatus fileStatus = fileSystem.getFileStatus(file);
        BlockLocation[] fileBlockLocations =
                fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        for (BlockLocation block : fileBlockLocations) {
            // 偏移量    块大小     块所在节点
            //      0,        1048576,   node01
            //      1048576,  899963,    node01
            // 可以实现不同计算节点使用不同块，从而达到计算向数据移动，各自计算各自的块，实现并行计算
            System.out.println(block);
        }

        FSDataInputStream in = fileSystem.open(file);
        System.out.println((char) in.readByte());

        // 使用seek 将偏移量调整
        // 文件1M一块；1048576=1M，即调整到第二个块，
        // 这时候在读，就是跳过第一块，直接读第二块了
        in.seek(1048576);
        System.out.println((char) in.readByte());
    }

    public void close() throws IOException {
        fileSystem.close();
    }
}
