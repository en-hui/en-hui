# Sky Walking 链路追踪

## 链路追踪服务搭建
前提：安装docker 和 docker compose

部署完成后，在浏览器中访问 http://localhost:8080 查看 SkyWalking UI。

### 单机环境
``` 
version: '3'
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.10.0
    container_name: elasticsearch
    environment:
      - "discovery.type=single-node"
    ports:
      - "9200:9200"
      - "9300:9300"
    volumes:
      - /data/es:/usr/share/elasticsearch/data

  skywalking-oap:
    image: apache/skywalking-oap-server:8.4.0-es7
    container_name: skywalking-oap
    depends_on:
      - elasticsearch
    links:
      - elasticsearch
    environment:
      - SW_STORAGE=elasticsearch
      - SW_STORAGE_ES_CLUSTER_NODES=elasticsearch:9200
    ports:
      - "11800:11800"
      - "12800:12800"

  skywalking-ui:
    image: apache/skywalking-ui:8.4.0
    container_name: skywalking-ui
    depends_on:
      - skywalking-oap
    links:
      - skywalking-oap
    ports:
      - "8080:8080"

```

### 高可用环境：
三台es，两台oap   
SkyWalking OAP 集群：这里有两个 SkyWalking OAP 节点。   
请注意，为了简化配置，这里使用的是 standalone 集群模式。在生产环境中，您可能需要使用更复杂的集群配置，例如使用 Zookeeper    

``` 
version: '3'
services:
  elasticsearch1:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.10.0
    container_name: elasticsearch1
    environment:
      - cluster.name=docker-cluster
      - node.name=elasticsearch1
      - discovery.seed_hosts=elasticsearch2,elasticsearch3
      - cluster.initial_master_nodes=elasticsearch1,elasticsearch2,elasticsearch3
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - /data/es1:/usr/share/elasticsearch/data
    ports:
      - "9200:9200"

  elasticsearch2:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.10.0
    container_name: elasticsearch2
    environment:
      - cluster.name=docker-cluster
      - node.name=elasticsearch2
      - discovery.seed_hosts=elasticsearch1,elasticsearch3
      - cluster.initial_master_nodes=elasticsearch1,elasticsearch2,elasticsearch3
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - /data/es2:/usr/share/elasticsearch/data

  elasticsearch3:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.10.0
    container_name: elasticsearch3
    environment:
      - cluster.name=docker-cluster
      - node.name=elasticsearch3
      - discovery.seed_hosts=elasticsearch1,elasticsearch2
      - cluster.initial_master_nodes=elasticsearch1,elasticsearch2,elasticsearch3
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - /data/es3:/usr/share/elasticsearch/data

  skywalking-oap1:
    image: apache/skywalking-oap-server:8.4.0-es7
    container_name: skywalking-oap1
    depends_on:
      - elasticsearch1
    environment:
      - SW_STORAGE=elasticsearch
      - SW_STORAGE_ES_CLUSTER_NODES=elasticsearch1:9200,elasticsearch2:9200,elasticsearch3:9200
      - SW_CLUSTER=standalone
    ports:
      - "11800:11800"
      - "12800:12800"

  skywalking-oap2:
    image: apache/skywalking-oap-server:8.4.0-es7
    container_name: skywalking-oap2
    depends_on:
      - elasticsearch1
    environment:
      - SW_STORAGE=elasticsearch
      - SW_STORAGE_ES_CLUSTER_NODES=elasticsearch1:9200,elasticsearch2:9200,elasticsearch3:9200
      - SW_CLUSTER=standalone

  skywalking-ui:
    image: apache/skywalking-ui:8.4.0
    container_name: skywalking-ui
    depends_on:
      - skywalking-oap1
    links:
      - skywalking-oap1:skywalking-oap
    ports:
      - "8080:8080"
```

## 应用服务接入