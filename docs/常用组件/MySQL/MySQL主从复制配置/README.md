# mysql主从复制安装配置

## 1、基础设置准备
```
#操作系统：
centos6.5
#mysql版本：
5.7
#两台虚拟机：
node1:39.105.201.14（主）
node2:39.105.176.190（从）
```

## 2、安装mysql数据库
[切换yum源参考](../../../大数据技术栈/Hive/msb-notes/linux切换yum源.md)   
[安装参考](../../../大数据技术栈/Hive/msb-notes/linux安装mysql步骤.md)

## 3、在两台数据库中分别创建数据库
```sql
--注意两台必须全部执行
create database copy;
```

## 4、在主（node1）服务器进行如下配置：
```
#修改配置文件，执行以下命令打开mysql配置文件
vi /etc/my.cnf
#在mysqld模块中添加如下配置信息

#二进制文件名称
log-bin=master-bin 
#二进制日志格式，有row、statement、mixed三种格式，
#row指的是把改变的内容复制过去，而不是把命令在从服务器上执行一遍，
#statement指的是在主服务器上执行的SQL语句，在从服务器上执行同样的语句。
#mixed指的是默认采用基于语句的复制，一旦发现基于语句的无法精确的复制时，就会采用基于行的复制。
#MySQL默认采用基于语句的复制，效率比较高。
binlog-format=ROW  
#要求各个服务器的id必须不一样
server-id=1		   
#同步的数据库名称
binlog-do-db=copy   
```

## 5、配置从服务器登录主服务器的账号授权
mysql -u root -p 登录mysql，执行下面命令
```
set global validate_password_policy=0;
set global validate_password_length=1;
grant replication slave on *.* to 'root'@'%' identified by '123456';
flush privileges;
```
命令解释   
```
--授权操作
-- 设置密码校验级别为最低，只校验密码长度，只要长度跟validate_password_length一样即可
set global validate_password_policy=0;
-- 设置校验密码长度
set global validate_password_length=1;
-- GRANT：赋权命令
-- *.*：当前用户对所有数据库和表的相应操作权限
-- ‘root’@’%’：权限赋给root用户，所有ip都能连接
-- IDENTIFIED BY ‘123456’：连接时输入密码，密码为123456
grant replication slave on *.* to 'root'@'%' identified by '123456';
--刷新权限
flush privileges;
```

## 6、从服务器的配置
```
#修改配置文件，执行以下命令打开mysql配置文件
vi /etc/my.cnf
#在mysqld模块中添加如下配置信息

log-bin=master-bin	#二进制文件的名称
binlog-format=ROW	#二进制文件的格式
server-id=2			#服务器的id
```

## 7、重启主服务器的mysqld服务
```
#重启mysql服务
service mysqld restart
#登录mysql数据库
mysql -uroot -p
#查看master的状态
show master status；
```

## 8、重启从服务器并进行相关配置
```
#重启mysql服务
service mysqld restart
#登录mysql
mysql -uroot -p
#连接主服务器 注意修改ip 用户名 密码 端口号 主服务器【show master status】查出来的【File和Position】
change master to master_host='39.105.201.14',master_user='root',master_password='123456',master_port=3306,master_log_file='master-bin.000001',master_log_pos=154;
#启动slave
start slave
#查看slave的状态 (注意没有分号)
show slave status\G
```

## 9、此时可以在主服务器进行相关的数据添加删除工作，在从服务器看相关的状态