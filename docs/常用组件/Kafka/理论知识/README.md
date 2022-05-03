# Kafka 的理论知识

## 理论基础

### acks 配置
> 配置的含义：数据可靠性的级别（性能 VS 数据可靠性   trade off）   
> 默认值是 **1** , leader 分区持久化成功，返回给生产者后，生产者认为操作成功   

- 0（生产者发出就认为成功）
- 1（生产者发出，收到broker持久化成功的返回，才认为成功）
- -1（分布式环境下，多副本持久化成功，才认为成功——isr的概念）
> -1 时的补充说明：    
> min.insync.replicas——当生产者将 ack 设置为“ all”(或“-1”)时，
> 此配置指定了为了使写操作成功而必须确认写操作的最小副本数量。

## 名词

### ISR、OSR、AR
- ISR（in-sync replicas）
> 保持连通、活跃的副本集合

- OSR（outof-sync replicas）
> 超过阈值时间（默认10秒），没有"心跳"的副本集合

- AR（assigned replicas）
> 面向分区的副本集合，全量副本

AR = ISR + OSR

### LEO、HW、LW

-LEO（LogEndOffset）
> leader 副本的最新offset位置

- HW（High Watermark）高水位
> 当 acks 设置为1时，leader副本和follower副本的数据进度是不一致的（follower一直同步leader的数据，但是没有追平）   
> 在面向消费者可以消费的角度来看，消费者只能消费到ISR中进度最小的那个位置，这个位置叫做高水位   

- LW（Low Watermark）低水位
> kafka可以设置定期删除历史数据，所以低水位表示当前存在数据中，最早的offset那个位置