## 日常问题

- 开发机服务启动了，浏览器不能访问页面，本地telnet等都无法连接，可以ping通

> 问题查看：sysctl -p | grep net.ipv4.ip_forward     
> 问题分析：得到的结果如果是0，修改为1     
> 问题解决：sysctl 直接回车，查看帮助，看到-w是修改（临时修改，还需修改配置文件）   
> sysctl -w net.ipv4.ip_forward=1   
> 修改配置文件 /etc/sysctl.conf


## 生产环境问题排查

innodb_locks表在8.0.13版本中由 performance_schema.data_locks表所代替，   
innodb_lock_waits表则由 performance_schema.data_lock_waits表代替

> 报错：com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: 
> Lock wait timeout exceeded; try restarting transaction\n;
>  
> -- 查看数据库当前的进程    
> show processlist;   或者sql：select * from information_schema.processlist where Info is not null;      
> -- 当前运行的所有事务    
> select * from information_schema.INNODB_LOCKS;     
> -- 当前出现的锁   
> select * from information_schema.INNODB_LOCK_waits;    
> -- 锁等待的对应关系    
> select * from information_schema.INNODB_TRX;    
> -- 查询产生锁的具体sql    
> select a.trx_id 事务id,a.trx_mysql_thread_id 事务线程id,a.trx_query 事务sql 
> from INFORMATION_SCHEMA.INNODB_LOCKS b,INFORMATION_SCHEMA.innodb_trx a
> where b.lock_trx_id=a.trx_id;    
> 
> -- 杀掉死锁线程 innodb_trx表的trx_requested_lock_id   
> kill {thread_id};
> 

### 操作系统层面

### JVM层面

### 工具