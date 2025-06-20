# 使用docker安装主备的postgres


```bash
-- 启动集群, 初始化
docker-compose up -d

-- 停止集群,修改master的配置文件 pg_hba.conf和postgresql.conf
docker-compose down

-- 重新启动集群,将使用新的配置启动
docker-compose up -d

-- 进入备库开启复制
docker exec -it pg-slave bash   
rm -rf $PGDATA/* && pg_basebackup -h pg-master -U postgres -D $PGDATA -Fp -Xs -P -R

-- 检查主库复制状态
docker exec pg-master psql -U postgres -c \
  "SELECT client_addr, state, sync_state FROM pg_stat_replication;"
  
 
-- 测试数据同步
docker exec pg-master psql -U postgres -d postgres -c \
  "CREATE TABLE test (id SERIAL, data TEXT); INSERT INTO test (data) VALUES ('sync_test');"
docker exec pg-slave psql -U postgres -d postgres -c "SELECT * FROM test;" 
```
