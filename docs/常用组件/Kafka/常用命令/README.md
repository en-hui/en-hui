# 常用命令

## schema_registry常用接口（avro序列化器时使用）
```text
-- 列出所有主题
curl http://schema_registry:8081/subjects

-- 列出指定主题下所有已注册的 Schema 版本
curl http://schema_registry:8081/subjects/{subject-name}/versions
eg:
curl http://schema_registry:8081/subjects/v2_dptask_54.PY_AUTO.AA1.1426-key/versions
curl http://schema_registry:8081/subjects/v2_dptask_54.PY_AUTO.AA1.1426-value/versions

-- 获取指定主题和指定版本的Schema定义
curl http://schema_registry:8081/subjects/{subject-name}/versions/{version-number}
eg:
curl http://schema_registry:8081/subjects/v2_dptask_54.PY_AUTO.AA1.1426-key/versions/1

-- 获取指定主题的最新版本 Schema 定义
curl http://schema_registry:8081/subjects/{subject-name}/versions/latest
eg:
curl http://schema_registry:8081/subjects/v2_dptask_54.PY_AUTO.AA1.1426-key/versions/latest

-- 根据全局唯一的 Schema ID 获取 Schema 定义（Schema定义中的id就是schema_id）
curl http://schema_registry:8081/schemas/ids/{schema-id}
eg:
curl http://schema_registry:8081/schemas/ids/1
```

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
