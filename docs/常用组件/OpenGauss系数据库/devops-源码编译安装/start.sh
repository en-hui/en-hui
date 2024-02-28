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
gsql -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE opengauss TO opengauss;"
# 特殊需要的权限
gsql -d postgres -c "ALTER USER opengauss REPLICATION;"
gsql -d postgres -c "grant select on pg_user to opengauss;"

# 跟踪日志文件以保持脚本运行
tail -f ${LOG_HOME}/opengauss.log
