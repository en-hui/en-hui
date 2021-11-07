# ZooKeeper

> 官网：http://zookeeper.apache.org/   
> ZooKeeper 是分布式协调服务，它公开了一组简单的原语   
> 
> ZooKeeper 数据保存在内存中，这意味着 ZooKeeper 可以实现高吞吐量和低延迟   
> 
> ZooKeeper 数据结构类似于文件系统，在 ZooKeeper 中称为 znodes   
> 一个znode尽量存储不到1MB的数据   
> znode分为:持久节点 和 临时节点
> 
> ZooKeeper 基于主从复制集群，主可以读写，从只负责读   
> 
> Zookeeper 是高可用的，意味着他可以从【不可用状态】很快的恢复到【可用状态】（官方描述：200ms）   
> 不可用状态：指主挂掉   
> 可用状态：指从剩余机器中，重新选出一个主


- [zk安装及cli使用](常用组件/ZooKeeper/zk安装及cli使用/)   
- 
paxos介绍: https://www.douban.com/note/208430424/
