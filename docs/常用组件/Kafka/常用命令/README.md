# 常用命令

``` 
-- 查所有的消费者组
kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --list
-- 查某个组的消费进度
kafka-consumer-groups.sh --describe --bootstrap-server 127.0.0.1:9092 --group heh-group
```