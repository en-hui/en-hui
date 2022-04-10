# Kafka Connect 自定义开发

### pom依赖
```
        <dependency>
            <groupId>org.apache.kafka</groupId>
            <artifactId>connect-api</artifactId>
            <version>2.3.0</version>
        </dependency>
```

### source
> 定义connector和task分别继承 SourceConnector、SourceTask

```java
public class FileSourceConnector extends SourceConnector {
    
}
```
```java
public class FileSourceTask extends SourceTask {
    
}
```

### sink

```java
public class FileSinkConnector extends SinkConnector {
    
}
```
```java
public class FileSinkTask extends SinkTask {
    
}
```
### 启动
> 单机模式启动（kafka安装目录下）：    
> ./bin/connect-standalone.sh ./config/connect-standalone.properties ./config/connect-file-source.properties ./config/connect-console-sink.properties
> 
> 
> Idea 中启动（kafka源码）：   
> org.apache.kafka.connect.cli 包下 ConnectStandalone 类有main方法，直接启动这个    
> 启动参数(program arguments，两个及以上就行)：   
> 单机启动配置文件：/Users/huenhui/Desktop/kafkaconnect/connect-standalone.properties    
> 源配置文件：/Users/huenhui/Desktop/kafkaconnect/connect-file-source.properties    
> 目的配置文件：/Users/huenhui/Desktop/kafkaconnect/connect-file-sink.properties    
