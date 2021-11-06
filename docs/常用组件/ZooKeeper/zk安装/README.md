# ZooKeeper安装

> 官方下载地址：http://zookeeper.apache.org/releases.html   
> 选择最新稳定版下载（Apache ZooKeeper 3.7.0 是我们当前的版本，3.6.3 是我们最新的稳定版本。）     
> https://www.apache.org/dyn/closer.lua/zookeeper/zookeeper-3.6.3/apache-zookeeper-3.6.3-bin.tar.gz   

## 四个节点的集群安装
> 保证：已安装jdk，已配置jdk环境变量   
> 安装目录：/usr/local/zk/    
> 数据目录：/var/zk/   
> apache-zookeeper-3.6.3-bin.tar.gz    

``` 
# 切换目录并上传安装文件
cd /usr/local/zk/

# 解压
tar xf apache-zookeeper-3.6.3-bin.tar.gz

# 复制一个配置文件
cd /conf
cp zoo_sample.cfg zoo.cfg

# 修改配置文件
vim zoo.cfg
【
dataDir=/var/zk/
# 文件末尾写入所有节点（根据节点列表计算过半数；2888用于给客户端提供服务；3888用于节点间通信，选举leader）
server.1=node01:2888:3888
server.2=node02:2888:3888
server.3=node03:2888:3888
server.4=node04:2888:3888
# 需要修改hosts文件
vim /etc/hosts
# 文件末尾 新增  ip 域名（别名）
39.100.158.215 node01
106.14.158.60 node02
】

# 创建 dataDir 配置的目录
cd /var
mkdir zk

# dataDir 目录下，创建 myid 文件，并写入 唯一id值
cd /var/zk/
vim myid
写入1，保存退出

# 分发至其他机器（重复操作所有节点）
cd /usr/local/
scp -r ./zk/ node02:`pwd`
输入密码验证，等待传输完成 

去节点2，创建dataDir，并创建myid，写入唯一id值（id值和配置文件中的一致。server.后面的数字）
```