#!/bin/bash
MOVE_ROOT_DIR=`pwd`

if [[ $1 == "lag" ]]; then
  echo "后台运行 move.topic.api.CheckLag，请前往日志文件查看结果"
  nohup java -Xmx2G -Xms2G -XX:+UseG1GC  \
  -DmoveConfig=${MOVE_ROOT_DIR}/conf/moveConfig.properties  \
  -DtopicMappingPath=${MOVE_ROOT_DIR}/conf/topicMapping.csv  \
  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${MOVE_ROOT_DIR}/log/heapdump.bin  \
  -XX:+PrintGCDetails -Xloggc:${MOVE_ROOT_DIR}/log/gc.log  \
  -cp move-topic-1.0.0-jar-with-dependencies.jar move.topic.api.CheckLag >> ${MOVE_ROOT_DIR}/log/moveTopic.log &
elif [[ $1 == "dashboard" ]]; then
  echo "交互式运行 move.topic.api.Dashboard"
  java -Xmx2G -Xms2G -XX:+UseG1GC  \
  -DmoveConfig=${MOVE_ROOT_DIR}/conf/moveConfig.properties  \
  -DtopicMappingPath=${MOVE_ROOT_DIR}/conf/topicMapping.csv  \
  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${MOVE_ROOT_DIR}/log/heapdump.bin  \
  -XX:+PrintGCDetails -Xloggc:${MOVE_ROOT_DIR}/log/gc.log  \
  -cp move-topic-1.0.0-jar-with-dependencies.jar move.topic.api.Dashboard
elif [[ $1 == "move" ]]; then
  echo "后台运行 move.topic.api.MoveTopic，请前往日志文件查看结果"
  nohup java -Xmx2G -Xms2G -XX:+UseG1GC  \
  -DmoveConfig=${MOVE_ROOT_DIR}/conf/moveConfig.properties  \
  -DtopicMappingPath=${MOVE_ROOT_DIR}/conf/topicMapping.csv  \
  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${MOVE_ROOT_DIR}/log/heapdump.bin  \
  -XX:+PrintGCDetails -Xloggc:${MOVE_ROOT_DIR}/log/gc.log  \
  -cp move-topic-1.0.0-jar-with-dependencies.jar move.topic.api.MoveTopic >> ${MOVE_ROOT_DIR}/log/moveTopic.log &
else
  echo "请输入一个执行参数：lag、move或dashboard"
fi