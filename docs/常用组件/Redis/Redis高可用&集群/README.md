# Redis高可用&集群
> 单机的问题   
> 1、单点故障     
> 2、容量有限    
> 3、压力
> 
> 主备：需要基于主从复制   
> 可以解决单点故障，主挂了，从节点切换角色，成为主顶上   
> 基于主备，可以让主支持读写，从支持读，可以一定程度缓解读压力   
> 
> AKF原则   
> x轴：所有节点都是全量的、镜像的（主从复制）   
> y轴：根据业务、功能划分（类似分库，例如：用户在节点1，商品在节点2）   
> z轴：相同功能根据数据范围划分（类似分表，例如：用户id1～100w在表1，100w～以上在表2）

## 主从复制
> 异步方式同步，可能丢数据   

http://redis.cn/topics/replication.html    
https://redis.io/docs/management/replication/

## HA高可用（哨兵）
> 哨兵-监控主节点并实现当主挂了，能够在从节点中在选出一个主恢复集群提供服务

http://redis.cn/topics/sentinel.html   
https://redis.io/docs/management/sentinel/   


## 分片

如何将数据分区：
http://redis.cn/topics/partitioning.html   
Redis Cluster：   
http://redis.cn/topics/cluster-tutorial.html    
http://redis.cn/topics/cluster-spec.html    

英文官网：
https://redis.io/docs/reference/cluster-spec/    

