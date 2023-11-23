# Sky Walking 链路追踪

## 链路追踪服务搭建
前提：安装docker 和 docker compose

部署完成后，在浏览器中访问 http://localhost:8080 查看 SkyWalking UI。

### 单机环境（使用h2）
``` 
version: '3'
services:
  skywalking-oap:
    image: apache/skywalking-oap-server:9.2.0
    container_name: skywalking-oap
    environment:
      - SW_STORAGE=h2
      - "JAVA_OPTS=-Xmx512m -Xms512m"
    ports:
      - "11800:11800"
      - "12800:12800"

  skywalking-ui:
    image: apache/skywalking-ui
    container_name: skywalking-ui
    depends_on:
      - skywalking-oap
    ports:
      - "8080:8080"

```
### 单机环境（使用mysql）
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
      - /data/mysql:/var/lib/mysql

  skywalking-oap:
    image: apache/skywalking-oap-server:9.2.0
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

  skywalking-ui:
    image: apache/skywalking-ui
    container_name: skywalking-ui
    depends_on:
      - skywalking-oap
    ports:
      - "8080:8080"
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
      - /data/es:/usr/share/elasticsearch/data

  skywalking-oap:
    image: apache/skywalking-oap-server:9.2.0
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
    image: apache/skywalking-ui
    container_name: skywalking-ui
    depends_on:
      - skywalking-oap
    links:
      - skywalking-oap
    ports:
      - "8080:8080"

```

## 应用服务接入