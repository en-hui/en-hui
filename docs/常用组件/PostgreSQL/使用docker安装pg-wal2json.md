# 安装pg14.2 包含wal2json插件

``` 
FROM postgres:14.2
 
# Install wal2json extension
RUN apt-get update && \
apt-get install -y postgresql-14-wal2json && \
apt-get clean && \
rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
 
# Expose the PostgreSQL port
EXPOSE 5432
 
# Start the PostgreSQL server
CMD ["postgres", "-c", "shared_preload_libraries=wal2json"]
```

打镜像：    
docker build -t my-postgres .   
启动前，先创建目录：mkdir -p /home/postgresql/pgdata    

启动容器：   
docker run --name postgres-cdc -e POSTGRES_PASSWORD=123456 -p 5432:5432 -v /home/postgresql/pgdata:/var/lib/postgresql/data -d my-postgres


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
