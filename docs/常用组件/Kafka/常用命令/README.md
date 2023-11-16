# 常用命令

## topic
```
-- 查所有的topic
kafka-topics --bootstrap-server kafka-debug:9092 --list

-- 创建topic
kafka-topics --bootstrap-server kafka-debug:9092 --create --topic test-topic

-- 创建topic，指定分区数 和 副本数
kafka-topics --bootstrap-server kafka-debug:9092 --create --topic test-topic1 --replication-factor 1 --partitions 1

-- 查看topic详细信息
kafka-topics --bootstrap-server kafka-debug:9092 --topic test-topic --describe

-- 删除topic
kafka-topics --bootstrap-server kafka-debug:9092 --topic test-topic --delete
```

## 消息
``` 
-- 向topic中生产消息
kafka-console-producer --bootstrap-server kafka-debug:9092 --topic test-topic

-- 从最新位置消费topic
kafka-console-consumer --bootstrap-server kafka-debug:9092 --topic test-topic

-- 从最初位置消费topic
kafka-console-consumer --bootstrap-server kafka-debug:9092 --topic test-topic --from-beginning

-- 从最初位置消费topic;打印key
kafka-console-consumer --bootstrap-server kafka-debug:9092 --topic test-topic --from-beginning --property print.key=true

-- 从最初位置消费topic;打印key；打印timestamp
kafka-console-consumer --bootstrap-server kafka-debug:9092 --topic test-topic --from-beginning --property print.key=true --property print.timestamp=true

-- 指定分区和offset消费
kafka-console-consumer --bootstrap-server kafka-debug:9092 --topic test-topic --partition 0 --offset 3
```

## 消费者组
``` 
-- 查所有的消费者组
kafka-consumer-groups --bootstrap-server kafka-debug:9092 --list

-- 查某个组的消费进度
kafka-consumer-groups --describe --bootstrap-server kafka-debug:9092 --group groupName
```
