# HDFS集群搭建
- 官方教程：https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html

- 设置主机名
```shell
vi /etc/sysconfig/network
    NETWORKING=yes
    # 设置本机主机名
    HOSTNAME=node01
```
- 修改host文件
```shell
vi /etc/hosts
    # 阿里云主机，配置时使用外网ip有问题，启动时NameNode和SecondaryNode会失败
    ip1 node01
    ip2 node02
```
- 关闭防火墙
```shell
service iptables stop  # 关闭防火墙
chkconfig iptables off # 设置开机关闭
```
- 关闭selinux
```shell
vi /etc/selinux/config
  SELINUX=disabled
```
- 时间同步
```shell
yum install ntp -y
vi /etc/ntp.conf
    server ntp1.aliyun.com  # 配置从阿里时间同步
service ntpd start  # 启动
chkconfig ntpd on  # 设置开机启动
```
- 安装jdk
```shell
# 设置环境变量
vi /etc/profile
    export JAVA_HOME=/usr/java
    export PATH=$PATH:$JAVA_HOME/bin
```
- ssh免密
```shell
ssh localhost  # （会被动生成/root/.ssh文件，不要自己创建）检查是否需要密码才能连接本机
# 如果不能，需要以下配置
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa # 生成公钥私钥
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys # 公钥追加到相应文件，想免密登录谁，就把公钥给谁
chmod 0600 ~/.ssh/authorized_keys # 修改权限
```

## HDFS独立模式（单节点，单进程）
暂无实践

## HDFS伪分布式（单节点，多进程）
- 目标：在一个节点上安装所有角色——NameNode、SecondaryNameNode、DataNode
- 部署路径：/opt/bigdata/

```shell
# 去官网下载hadoop安装包
https://www.apache.org/dyn/closer.cgi/hadoop/common/  
# 下载 2.10.1 版本
https://dlcdn.apache.org/hadoop/common/hadoop-2.10.1/hadoop-2.10.1.tar.gz

# 文件放在 /opt/bigdata 下并解压
tar xf hadoop-2.10.1.tar.gz

# 配置环境变量
vi /etc/profile
    # 加到文件结尾（jdk之前设置过的）
    export JAVA_HOME=/usr/java
    export HADOOP_HOME=/opt/bigdata/hadoop-2.10.1
    export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
# 环境变量生效
source /etc/profile

# 配置hadoop
cd $HADOOP_HOME/etc/hadoop
vi hadoop-env.sh
  # 必须给hadoop配置JAVA_HOME,不然ssh连接后找不到$JAVA_HOME
  export JAVA_HOME=/usr/java

# https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/core-default.xml  
vi core-site.xml
  <property>
    <name>fs.defaultFS</name>
    # 给出NameNode在哪里启动，ip、端口   尽量不要用localhost
    <value>hdfs://node01:9000</value>
  </property>

# https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml  
vi hdfs-site.xml  
  <property>
    <name>dfs.replication</name>
    # 伪分布式，副本数量设置为了1
    <value>1</value>
  </property>
  <property>
    <name>dfs.namenode.name.dir</name>
    # NameNode 数据存放路径
    <value>/var/bigdata/hadoop/local/dfs/name</value>
  </property>
  <property>
    <name>dfs.datanode.data.dir</name>
    # NameNode 数据存放路径
    <value>/var/bigdata/hadoop/local/dfs/data</value>
  </property>
  <property>
    <name>dfs.namenode.secondary.http-address</name>
    # SecondaryNode 在哪里启动,不设置也会在本机启动
    <value>node01:50090</value>
  </property>
  <property>
    <name>dfs.namenode.checkpoint.dir</name>
    # SecondaryNode 数据存放路径
    <value>/var/bigdata/hadoop/local/dfs/secondary</value>
  </property>
vi slaves
  # 配置DataNode在哪里启动
  node01
```

- 初始化**格式化操作，不要重复操作**
```shell
# 创建目录并初始化一个空的FsImage
hdfs namenode -format
```
- 启动集群
```shell
cd /opt/bigdata/hadoop-2.10.1/sbin
# 第一次执行，DataNode和SecondaryNode会初始化自己的数据目录
./start-dfs.sh
```
- 本机配置host，访问node01:50070
- 简单使用
```shell
# hdfs dfs 为前缀，使用起来与操作系统类似
# hdfs dfs 会打印帮助命令
# hdfs 的根目录为 /user 一般使用，创建自己用户的家目录 
hdfs dfs -mkdir -p /user/root
```
## HDFS完全分布式（多节点，多进程）