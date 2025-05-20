# 安装pg14.2 包含wal2json插件

``` 
FROM postgres:14.2

# Install wal2json and decoderbufs extensions
RUN apt-get update && \
    apt-get install -y postgresql-14-wal2json && \
    # Install decoderbufs
    apt-get install -y postgresql-14-decoderbufs && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Expose the PostgreSQL port
EXPOSE 5432

# Start the PostgreSQL server with both extensions
CMD ["postgres", "-c", "shared_preload_libraries=wal2json,decoderbufs"]
```

打镜像：    
docker build -t postgres-wal2json .   
启动前，先创建目录：mkdir -p ./pgdata    

启动容器：   
docker run --name postgres-cdc -e POSTGRES_PASSWORD=123456 -p 5455:5432 -v ./pgdata:/var/lib/postgresql/data -d postgres-wal2json


修改/home/postgresql/pgdata目录下的配置文件，逻辑复制相关的配置   
``` 
postgres.conf
 
wal_level = logical
max_wal_senders = 10                    
max_replication_slots = 10
 

pg_hba.conf
 
host    all             all             0.0.0.0/0               md5
host    replication     all             0.0.0.0/0               md5
```

修改两个配置文件后，重启容器：docker restart postgres-cdc

进入容器：docker exec -it postgres-cdc bash

进入容器后，可以进行pg命令行：psql -U postgres

# 使用pg-vector插件
docker run --name pgvector -e POSTGRES_PASSWORD=123456 -p 5456:5432 -v ./pgdata:/var/lib/postgresql/data -d pgvector/pgvector:pg17

```Dockerfile
FROM pgvector/pgvector:pg17

RUN apt-get update && \
    apt-get install -y postgresql-17-wal2json && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

EXPOSE 5432

CMD ["postgres", "-c", "shared_preload_libraries=wal2json"]

# 添加初始化脚本（如果pgvector需要显式启用）
COPY init.sql /docker-entrypoint-initdb.d/

CMD ["postgres","-c", "shared_preload_libraries=vector,wal2json"]
```

```init.sql
CREATE EXTENSION IF NOT EXISTS vector;
```
