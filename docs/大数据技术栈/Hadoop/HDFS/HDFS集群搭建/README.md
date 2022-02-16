# HDFS集群搭建
- 官方教程：https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html

- 设置主机名
```shell
vi /etc/sysconfig/network
    NETWORKING=yes
    # 设置本机主机名,不能包含下划线
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
    <!-- 给出NameNode在哪里启动，ip、端口,尽量不要用localhost -->
    <value>hdfs://node01:9000</value>
  </property>

# https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml  
vi hdfs-site.xml  
  <property>
    <name>dfs.replication</name>
    <!-- 伪分布式，副本数量设置为了1 -->
    <value>1</value>
  </property>
  <property>
    <name>dfs.namenode.name.dir</name>
    <!-- NameNode 数据存放路径 -->
    <value>/var/bigdata/hadoop/local/dfs/name</value>
  </property>
  <property>
    <name>dfs.datanode.data.dir</name>
    <!-- NameNode 数据存放路径 -->
    <value>/var/bigdata/hadoop/local/dfs/data</value>
  </property>
  <property>
    <name>dfs.namenode.secondary.http-address</name>
    <!-- SecondaryNode 在哪里启动,不设置也会在本机启动 -->
    <value>node01:50090</value>
  </property>
  <property>
    <name>dfs.namenode.checkpoint.dir</name>
    <!-- SecondaryNode 数据存放路径 -->
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

使用jps查看角色是否完整
根据配置文件配置的节点，全部角色包括一个NameNode、多个DataNode、一个SecondaryNameNode
```
- 本机配置host，访问node01:50070
- 简单使用
```shell
# hdfs dfs 为前缀，使用起来与操作系统类似
# hdfs dfs 会打印帮助命令
# hdfs 的根目录为 /user 一般使用，创建自己用户的家目录 
hdfs dfs -mkdir -p /user/root
# 默认不写上传目录，默认上传至当前用户家目录（使用操作系统的用户）
hdfs dfs -put hadoop-2.10.1.tar.gz
# -D 设置参数，1.8M文件设置1M的block大小，会分为两个block
hdfs dfs -Ddfs.blocksize=1048576 -put zstd-1.5.1.tar.gz
```

## HDFS完全分布式（多节点，多进程）
- 目标：在四个节点上安装所有角色——NameNode、SecondaryNameNode、DataNode
  - node01:只安装NameNode（对内存使用需求较大，且一个集群只有一个，把整台机器性能都给他自己）
  - node02：安装SecondaryNameNode、DataNode
  - node03：安装DataNode
  - node04：安装DataNode

```shell
# 使用 scp 分发jdk，并配置环境变量
# 设置免密：如果在node01启动，就把node01的公钥给node02、node03、node04
scp ./id_dsa.put node02:/root/.ssh/node01.pub # 在node01执行远程拷贝
cat node01.put >> authorized_keys  # node02 执行，将公钥内容追加到authorized_keys文件
# 修改配置文件；修改后使用 scp 分发 /opt/bigdata/hadoop 文件
scp -r /opt/bigdata/ node02:`pwd`
# 需要修改的配置文件包括：
    # hadoop-env.sh  配置了jdk路径，伪分布式教程改过了，这里不需要改
    # core-site.xml  配置了NameNode在哪里启动，伪分布式教程改过了，这里不需要改
    # hdfs-site.xml 1.修改副本数；2.修改数据路径；3.修改SecondaryNode启动节点
          <property>
            <name>dfs.replication</name>
            <value>2</value>
          </property>
          <property>
            <name>dfs.namenode.name.dir</name>
            <value>/var/bigdata/hadoop/full/dfs/name</value>
          </property>
          <property>
            <name>dfs.datanode.data.dir</name>
            <value>/var/bigdata/hadoop/full/dfs/data</value>
          </property>
          <property>
            <name>dfs.namenode.secondary.http-address</name>
            <value>node02:50090</value>
          </property>
          <property>
            <name>dfs.namenode.checkpoint.dir</name>
            <value>/var/bigdata/hadoop/full/dfs/secondary</value>
          </property>
    # slaves
          node02
          node03
          node04
# 格式化启动（在NameNode节点操作--node01）
hdfs namenode -format
# 启动集群（在NameNode节点操作--ssh会远程启动其他节点角色）
./start-dfs.sh
```


# HDFS HA模式

> HDFS 是主从集群：结构相对简单，主与从协作   
> 主：单点，数据一致好掌握   
> 问题1：单点故障，集群整体不可用    
> 问题2：压力过大，内存受限   
> 
> HDFS在 2.x版本分别提供了解决方案（Hadoop2.x只支持HA的一主一备）    
> 一、单点故障问题：    
>   高可用方案：HA（High Available）——没有SecondaryNode角色了，Standby角色滚动生成FsImage      
>   涉及技术：Zookeeper、JournalNode    
>   多个NameNode，主备切换；Active和Standby状态（Active对外提供服务）     
>   增加JournalNode角色（>3台）,负责同步NameNode的editLog（最终一致性）    
>   增加zkfc角色（与NN同台），通过zookeeper集群协调NN的主从选举和切换（事件回调机制）    
>   DN同时向NNs（所有的）汇报block信息
> 
> 二、压力过大，内存受限问题：   
>   联邦机制：Federation（元数据分片）    
>   多个NameNode，管理不同的元数据     
>   元数据隔离（不同人访问自己的NN）   
>   DN共享，使用目录隔离所属的NN        


- HA搭建（https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/HDFSHighAvailabilityWithQJM.html）
  - node01:NameNode、ZKFC、JournalNode
  - node02:NameNode、ZKFC、JournalNode、DataNode、ZooKeeper
  - node03:JournalNode、DataNode、ZooKeeper
  - node04：DataNode、Zookeeper