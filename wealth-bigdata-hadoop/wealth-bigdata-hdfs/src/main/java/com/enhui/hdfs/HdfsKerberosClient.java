package com.enhui.hdfs;

import com.sun.security.auth.module.Krb5LoginModule;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.LoginException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeysPublic;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.authentication.util.KerberosName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class HdfsKerberosClient {

  FileSystem fileSystem = null;
  Configuration config = null;
  String principal = "hdfs/heh-node01@CMBC";
  String keytab =
      "/Users/huenhui/IdeaProjects/wealth/wealth-bigdata-hadoop/wealth-bigdata-hdfs/src/main/resources/krb5/hdfs.keytab";
  String krb5 =
      "/Users/huenhui/IdeaProjects/wealth/wealth-bigdata-hadoop/wealth-bigdata-hdfs/src/main/resources/krb5/krb5.conf";

  @Before
  public void initClient() throws IOException, InterruptedException, LoginException {
    config = new Configuration(true);
    // 客户端与datanode 使用主机名通信
    config.set("dfs.client.use.datanode.hostname", "true");
    config.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
    // 设置块大小
    config.set("dfs.blocksize", "1m");

    System.setProperty("java.security.krb5.conf", krb5);
    Krb5LoginModule module = new Krb5LoginModule();
    Map<String, String> options = new HashMap<>();
    options.put("refreshKrb5Config", "true");
    options.put("principal", principal);
    options.put("useKeyTab", "true");
    options.put("keyTab", keytab);
    module.initialize(null, null, null, options);
    module.login();

    KerberosName.resetDefaultRealm();
    config.set(CommonConfigurationKeysPublic.HADOOP_SECURITY_AUTHORIZATION, "true");
    config.set(CommonConfigurationKeysPublic.HADOOP_SECURITY_AUTHENTICATION, "kerberos");
    UserGroupInformation.setConfiguration(config);

    final UserGroupInformation ugi =
        UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal, keytab);

    config = ugi.doAs((PrivilegedAction<Configuration>) () -> config);
    fileSystem = ugi.doAs((PrivilegedExceptionAction<FileSystem>) () -> FileSystem.get(config));

    log.info("=======客户端初始化=======");
  }

  @After
  public void close() throws IOException {
    fileSystem.close();
    log.info("=======客户端关闭=======");
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
}
