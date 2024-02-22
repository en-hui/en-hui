
``` yaml
version: '3'
services:
  zk-debug:
    image: confluentinc/cp-zookeeper:7.2.0
    container_name: zk-debug
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      JVMFLAGS: "-Xms300m -Xmx300m"

  kafka-debug: # https://docs.confluent.io/platform/current/installation/docker/config-reference.html
    image: confluentinc/cp-kafka:7.2.0
    container_name: kafka-debug
    depends_on:
      - zk-debug
    ports:
      - "9092:9092"
     # - "5005:5005"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zk-debug:2181/kafka
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-debug:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_JMX_PORT: 9999
      KAFKA_HEAP_OPTS: "-Xms300m -Xmx300m"
     # KAFKA_JVM_PERFORMANCE_OPTS: "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

  kafka-debug1:
    image: confluentinc/cp-kafka:7.2.0
    container_name: kafka-debug1
    depends_on:
      - zk-debug
    ports:
      - "9093:9093"
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ZOOKEEPER_CONNECT: zk-debug:2181/kafka
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-debug1:9093
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_HEAP_OPTS: "-Xms300m -Xmx300m"
  kafka-debug2:
    image: confluentinc/cp-kafka:7.2.0
    container_name: kafka-debug2
    depends_on:
      - zk-debug
    ports:
      - "9094:9094"
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ZOOKEEPER_CONNECT: zk-debug:2181/kafka
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-debug2:9094
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_HEAP_OPTS: "-Xms300m -Xmx300m"
```
