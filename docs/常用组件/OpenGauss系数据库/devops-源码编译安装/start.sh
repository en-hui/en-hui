#!/bin/bash

# 启动数据库
gs_ctl start -D ${DATA_HOME} -Z single_node -l ${LOG_HOME}/opengauss.log

# 跟踪日志文件以保持脚本运行
tail -f ${LOG_HOME}/opengauss.log