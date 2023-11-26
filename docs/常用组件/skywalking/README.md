# Sky Walking 链路追踪

## 链路追踪服务搭建
前提：安装docker 和 docker compose

部署完成后，在浏览器中访问 http://localhost:8080 查看 SkyWalking UI。

### 单机环境（使用h2）
``` 
version: '3'
services:
  skywalking-oap:
    image: apache/skywalking-oap-server:9.6.0
    container_name: skywalking-oap
    environment:
      - SW_STORAGE=h2
      - "JAVA_OPTS=-Xmx512m -Xms512m"
    ports:
      - "11800:11800"
      - "12800:12800"

  skywalking-ui:
    image: apache/skywalking-ui:9.6.0
    container_name: skywalking-ui
    depends_on:
      - skywalking-oap
    ports:
      - "8080:8080"
    environment:
      - SW_OAP_ADDRESS=http://skywalking-oap:12800

```
### 单机环境（使用mysql）
1、需要自己下载mysql驱动，放到对应目录，并设置目录挂载    
2、下载驱动: wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-8.2.0.tar.gz    
3、解压: tar -zxvf mysql-connector-j-8.2.0.tar.gz    
4、将jar移动: mv mysql-connector-j-8.2.0/mysql-connector-j-8.2.0.jar /data/skywalking/ext-libs/

``` 
version: '3'
services:
  mysql:
    image: mysql:5.7
    container_name: skywalking-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: skywalking
      MYSQL_USER: skywalking
      MYSQL_PASSWORD: skywalking
    ports:
      - "3306:3306"
    volumes:
      - /data/skywalking/mysql:/var/lib/mysql

  skywalking-oap:
    image: apache/skywalking-oap-server:9.6.0
    container_name: skywalking-oap
    depends_on:
      - mysql
    links:
      - mysql
    environment:
      - SW_STORAGE=mysql
      - SW_JDBC_URL=jdbc:mysql://mysql:3306/skywalking
      - SW_DATA_SOURCE_USER=skywalking
      - SW_DATA_SOURCE_PASSWORD=skywalking
      - "JAVA_OPTS=-Xmx512m -Xms512m"
    ports:
      - "11800:11800"
      - "12800:12800"
    volumes:
      - /data/skywalking/ext-libs:/skywalking/ext-libs
      
  skywalking-ui:
    image: apache/skywalking-ui:9.6.0
    container_name: skywalking-ui
    depends_on:
      - skywalking-oap
    ports:
      - "8080:8080"
    environment:
      - SW_OAP_ADDRESS=http://skywalking-oap:12800
```
### 单机环境（使用es）
``` 
version: '3'
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.10.0
    container_name: skywalking-es
    environment:
      - "discovery.type=single-node"
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ports:
      - "9200:9200"
      - "9300:9300"
    volumes:
      - /data/skywalking/es:/usr/share/elasticsearch/data

  skywalking-oap:
    image: apache/skywalking-oap-server:9.6.0
    container_name: skywalking-oap
    depends_on:
      - elasticsearch
    links:
      - elasticsearch
    environment:
      - SW_STORAGE=elasticsearch
      - SW_STORAGE_ES_CLUSTER_NODES=elasticsearch:9200
      - "JAVA_OPTS=-Xmx512m -Xms512m"
    ports:
      - "11800:11800"
      - "12800:12800"

  skywalking-ui:
    image: apache/skywalking-ui:9.6.0
    container_name: skywalking-ui
    depends_on:
      - skywalking-oap
    links:
      - skywalking-oap
    ports:
      - "8080:8080"
    environment:
      - SW_OAP_ADDRESS=http://skywalking-oap:12800
```

## 应用服务接入

> agent下载：https://skywalking.apache.org/downloads/#Agents   
> 
> 例如：https://www.apache.org/dyn/closer.cgi/skywalking/java-agent/9.0.0/apache-skywalking-java-agent-9.0.0.tgz
> 
> 下载后解压，根目录有skywalking-agent.jar，还有几个目录   
> 
> 常用的都在plugins目录    
> 如果plugins中没有的时候，需要将optional-plugins中对应的jar放入plugins    
> 比如gateway    
>  
> 设置host映射
> extra_hosts:
> - "heh-node02:10.0.24.17"

``` 
# skywalking-agent.jar 的路径
-javaagent:/root/sykwalking/skywalking-agent/skywalking-agent.jar
# 在skywalking中显示的服务名称
-DSW_AGENT_NAME=test-skywalking-service-test01
# skywalking的地址
-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=heh-node02:11800


# 完整使用
-javaagent:/root/sykwalking/skywalking-agent/skywalking-agent.jar
-DSW_AGENT_NAME=test-skywalking-service-test01
-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=heh-node02:11800
```


