#!/bin/bash

# postgresql.conf配置文件
echo "wal_level = logical" >> ${DATA_HOME}/postgresql.conf
echo "listen_addresses = '*'" >> ${DATA_HOME}/postgresql.conf

# pg_hba.conf配置文件
echo "host  all all 0.0.0.0/0 sha256" >> ${DATA_HOME}/pg_hba.conf
echo "host  replication all 0.0.0.0/0 sha256" >> ${DATA_HOME}/pg_hba.conf

# 启动数据库
gs_ctl start -D ${DATA_HOME} -Z single_node -l ${LOG_HOME}/opengauss.log

# 创建新的数据库和用户
gsql -d postgres -c "CREATE DATABASE opengauss;"
gsql -d postgres -c "CREATE USER opengauss WITH PASSWORD 'openGauss@123';"
gsql -d postgres -c "GRANT ALL PRIVILEGES TO opengauss;"
# 特殊需要的权限
gsql -d postgres -c "ALTER USER opengauss REPLICATION;"
gsql -d postgres -c "grant select on pg_user to opengauss;"


# 初始化数据
gsql -h localhost -p 5432 -d opengauss -U opengauss -W openGauss@123 -c "select * from pg_create_logical_replication_slot('slot_name', 'mppdb_decoding');"
gsql -h localhost -p 5432 -d opengauss -U opengauss -W openGauss@123 -c "create table public.small_table1 (column_1 int,id serial constraint small_table_pk1 primary key);"
gsql -h localhost -p 5432 -d opengauss -U opengauss -W openGauss@123 -c "INSERT INTO public.small_table (column_1, id) VALUES (1, 1);"

# 跟踪日志文件以保持脚本运行
tail -f ${LOG_HOME}/opengauss.log
