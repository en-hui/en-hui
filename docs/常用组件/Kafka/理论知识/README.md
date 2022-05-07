# Kafka 的理论知识

## 理论基础

### kafka存储数据的文件介绍
> topic是逻辑的，partition是物理的。    
> 假设 topic：heh01   三个分区    三个副本     
> 在数据目录，对于topic：heh01 需要关注三个文件{heh01-0  heh01-1  heh01-2}   
> 以一个目录下的文件列表举例子(heh01-0)：
> 
> 00000000000000000000.log ：数据文件，存储了数据     
> 查看文件时，需要加上参数才能把真实数据打印出来：（kafka-dump-log --files 00000000000000000000.log --print-data-log）     
> baseOffset: 3004 lastOffset: 3004 count: 1 baseSequence: -1 lastSequence: -1 producerId: -1 producerEpoch: -1 partitionLeaderEpoch: 0 isTransactional: false isControl: false position: 245176 CreateTime: 1651934082083 size: 82 magic: 2 compresscodec: NONE crc: 604145200 isvalid: true | offset: 3004 CreateTime: 1651934082083 keysize: 9 valuesize: 5 sequence: -1 headerKeys: [] key: item-2996 payload: val-2      
> baseOffset: 3005 lastOffset: 3005 count: 1 baseSequence: -1 lastSequence: -1 producerId: -1 producerEpoch: -1 partitionLeaderEpoch: 0 isTransactional: false isControl: false position: 245258 CreateTime: 1651934082100 size: 82 magic: 2 compresscodec: NONE crc: 4100308093 isvalid: true | offset: 3005 CreateTime: 1651934082100 keysize: 9 valuesize: 5 sequence: -1 headerKeys: [] key: item-2999 payload: val-2          
> 
> 00000000000000000000.index ：索引文件，存储了offset和对应的position  
> offset: 2924 position: 238616   
> offset: 2974 position: 242716
> 
> 00000000000000000000.timeindex ：时间戳索引文件，存储了时间戳和对应的offset   
> timestamp: 1651934080154 offset: 2924   
> timestamp: 1651934081535 offset: 2974   
> 
> 文件查看：使用 kafka-dump-log --files 00000000000000000000.index

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