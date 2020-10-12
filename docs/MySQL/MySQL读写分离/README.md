# mysql读写分离

## 1、读写分离的介绍
MySQL读写分离基本原理是让master数据库处理写操作，slave数据库处理读操作。
master将写操作的变更同步到各个slave节点。     

​MySQL读写分离能提高系统性能的原因在于：   
​1、物理服务器增加，机器处理能力提升。拿硬件换性能。   
​2、主从只负责各自的读和写，极大程度缓解X锁和S锁争用。   
​3、slave可以配置myiasm引擎，提升查询性能以及节约系统开销。   
​4、master直接写是并发的，slave通过主库发送来的binlog恢复数据是异步。   
​5、slave可以单独设置一些参数来提升其读的性能。   
​6、增加冗余，提高可用性。   

## 2、使用proxy实现读写分离
### 1、硬件配置
```
master 39.105.176.190
slave  47.95.110.77
proxy  192，168.85.14
```

### 2、首先在master和slave上配置主从复制

### 3、进行proxy的相关配置
```
#1、下载mysql-proxy
https://downloads.mysql.com/archives/proxy/#downloads
#2、上传软件到proxy的机器
直接通过xftp进行上传
#3、解压安装包
tar -zxvf mysql-proxy-0.8.5-linux-glibc2.3-x86-64bit.tar.gz
#4、修改解压后的目录
mv mysql-proxy-0.8.5-linux-glibc2.3-x86-64bit mysql-proxy
#5、进入mysql-proxy的目录
cd mysql-proxy
#6、创建目录
mkdir conf
mkdir logs
#7、添加环境变量
#打开/etc/profile文件
vi /etc/profile
#在文件的最后面添加一下命令
export PATH=$PATH:/root/mysql-proxy/bin
#8、执行命令让环境变量生效
source /etc/profile
#9、进入conf目录，创建文件并添加一下内容
vi mysql-proxy.conf
添加内容
[mysql-proxy]
user=root
proxy-address=192.168.85.14:4040
proxy-backend-addresses=192.168.85.11:3306
proxy-read-only-backend-addresses=192.168.85.12:3306
proxy-lua-script=/root/mysql-proxy/share/doc/mysql-proxy/rw-splitting.lua
log-file=/root/mysql-proxy/logs/mysql-proxy.log
log-level=debug
daemon=true
#10、开启mysql-proxy
mysql-proxy --defaults-file=/root/mysql-proxy/conf/mysql-proxy.conf
#11、查看是否安装成功，打开日志文件
cd /root/mysql-proxy/logs
tail -100 mysql-proxy.log
#内容如下：表示安装成功
2019-10-11 21:49:41: (debug) max open file-descriptors = 1024
2019-10-11 21:49:41: (message) proxy listening on port 192.168.85.14:4040
2019-10-11 21:49:41: (message) added read/write backend: 192.168.85.11:3306
2019-10-11 21:49:41: (message) added read-only backend: 192.168.85.12:3306
2019-10-11 21:49:41: (debug) now running as user: root (0/0)
```

### 4、进行连接
```
#mysql的命令行会出现无法连接的情况，所以建议使用客户端
mysql -uroot -p123 -h192.168.85.14 -P 4040
```


## 3、使用amoeba实现读写分离

### 1、什么是amoeba？
Amoeba(变形虫)项目，专注 分布式数据库 proxy 开发。
座落与Client、DB Server(s)之间。对客户端透明。
具有负载均衡、高可用性、sql过滤、读写分离、可路由相关的query到目标数据库、可并发请求多台数据库合并结果。 
  
主要解决：   
- 降低 数据切分带来的复杂多数据库结构
- 提供切分规则并降低 数据切分规则 给应用带来的影响
- 降低db 与客户端的连接数
- 读写分离

### 2、为什么要用Amoeba
目前要实现mysql的主从读写分离，主要有以下几种方案：
1、 通过程序实现，网上很多现成的代码，比较复杂，如果添加从服务器要更改多台服务器的代码。        
2、 通过mysql-proxy来实现，由于mysql-proxy的主从读写分离是通过lua脚本来实现，
目前lua的脚本的开发跟不上节奏，而写没有完美的现成的脚本，因此导致用于生产环境的话风险比较大，
据网上很多人说mysql-proxy的性能不高。      
3、 自己开发接口实现，这种方案门槛高，开发成本高，不是一般的小公司能承担得起。     
4、 利用阿里巴巴的开源项目Amoeba来实现，具有负载均衡、高可用性、sql过滤、读写分离、可路由相关的query到目标数据库，
并且安装配置非常简单。支持国产！

### 3、amoeba安装

#### 1、首先安装jdk，直接使用rpm包安装即可     

#### 2、下载amoeba对应的版本https://sourceforge.net/projects/amoeba/，直接解压即可      

#### 3、配置amoeba的配置文件      

dbServers.xml     
```xml
<?xml version="1.0" encoding="gbk"?>

<!DOCTYPE amoeba:dbServers SYSTEM "dbserver.dtd">
<amoeba:dbServers xmlns:amoeba="http://amoeba.meidusa.com/">

		<!-- 
			Each dbServer needs to be configured into a Pool,
			If you need to configure multiple dbServer with load balancing that can be simplified by the following configu
ration:			 add attribute with name virtual = "true" in dbServer, but the configuration does not allow the element with n
ame factoryConfig			 such as 'multiPool' dbServer   
		-->
		
	<dbServer name="abstractServer" abstractive="true">
		<factoryConfig class="com.meidusa.amoeba.mysql.net.MysqlServerConnectionFactory">
			<property name="connectionManager">${defaultManager}</property>
			<property name="sendBufferSize">64</property>
			<property name="receiveBufferSize">128</property>
				
			<!-- mysql port -->
			<property name="port">3306</property>
			
			<!-- mysql schema -->
			<property name="schema">msb</property>
			
			<!-- mysql user -->
			<property name="user">root</property>
			
			<property name="password">123456</property>
		</factoryConfig>

		<poolConfig class="com.meidusa.toolkit.common.poolable.PoolableObjectPool">
			<property name="maxActive">500</property>
			<property name="maxIdle">500</property>
			<property name="minIdle">1</property>
			<property name="minEvictableIdleTimeMillis">600000</property>
			<property name="timeBetweenEvictionRunsMillis">600000</property>
			<property name="testOnBorrow">true</property>
			<property name="testOnReturn">true</property>
			<property name="testWhileIdle">true</property>
		</poolConfig>
	</dbServer>

	<dbServer name="writedb"  parent="abstractServer">
		<factoryConfig>
			<!-- mysql ip -->
			<property name="ipAddress">192.168.85.11</property>
		</factoryConfig>
	</dbServer>
	
	<dbServer name="slave"  parent="abstractServer">
		<factoryConfig>
			<!-- mysql ip -->
			<property name="ipAddress">192.168.85.12</property>
		</factoryConfig>
	</dbServer>
	<dbServer name="myslave" virtual="true">
		<poolConfig class="com.meidusa.amoeba.server.MultipleServerPool">
			<!-- Load balancing strategy: 1=ROUNDROBIN , 2=WEIGHTBASED , 3=HA-->
			<property name="loadbalance">1</property>
			
			<!-- Separated by commas,such as: server1,server2,server1 -->
			<property name="poolNames">slave</property>
		</poolConfig>
	</dbServer>
</amoeba:dbServers>
```

amoeba.xml     
```xml
<?xml version="1.0" encoding="gbk"?>

<!DOCTYPE amoeba:configuration SYSTEM "amoeba.dtd">
<amoeba:configuration xmlns:amoeba="http://amoeba.meidusa.com/">

	<proxy>
	
		<!-- service class must implements com.meidusa.amoeba.service.Service -->
		<service name="Amoeba for Mysql" class="com.meidusa.amoeba.mysql.server.MySQLService">
			<!-- port -->
			<property name="port">8066</property>
			
			<!-- bind ipAddress -->
			<!-- 
			<property name="ipAddress">127.0.0.1</property>
			 -->
			
			<property name="connectionFactory">
				<bean class="com.meidusa.amoeba.mysql.net.MysqlClientConnectionFactory">
					<property name="sendBufferSize">128</property>
					<property name="receiveBufferSize">64</property>
				</bean>
			</property>
			
			<property name="authenticateProvider">
				<bean class="com.meidusa.amoeba.mysql.server.MysqlClientAuthenticator">
					
					<property name="user">root</property>
					
					<property name="password">123456</property>
					
					<property name="filter">
						<bean class="com.meidusa.toolkit.net.authenticate.server.IPAccessController">
							<property name="ipFile">${amoeba.home}/conf/access_list.conf</property>
						</bean>
					</property>
				</bean>
			</property>
			
		</service>
		
		<runtime class="com.meidusa.amoeba.mysql.context.MysqlRuntimeContext">
			
			<!-- proxy server client process thread size -->
			<property name="executeThreadSize">128</property>
			
			<!-- per connection cache prepared statement size  -->
			<property name="statementCacheSize">500</property>
			
			<!-- default charset -->
			<property name="serverCharset">utf8</property>
			
			<!-- query timeout( default: 60 second , TimeUnit:second) -->
			<property name="queryTimeout">60</property>
		</runtime>
		
	</proxy>
	
	<!-- 
		Each ConnectionManager will start as thread
		manager responsible for the Connection IO read , Death Detection
	-->
	<connectionManagerList>
		<connectionManager name="defaultManager" class="com.meidusa.toolkit.net.MultiConnectionManagerWrapper">
			<property name="subManagerClassName">com.meidusa.toolkit.net.AuthingableConnectionManager</property>
		</connectionManager>
	</connectionManagerList>
	
		<!-- default using file loader -->
	<dbServerLoader class="com.meidusa.amoeba.context.DBServerConfigFileLoader">
		<property name="configFile">${amoeba.home}/conf/dbServers.xml</property>
	</dbServerLoader>
	
	<queryRouter class="com.meidusa.amoeba.mysql.parser.MysqlQueryRouter">
		<property name="ruleLoader">
			<bean class="com.meidusa.amoeba.route.TableRuleFileLoader">
				<property name="ruleFile">${amoeba.home}/conf/rule.xml</property>
				<property name="functionFile">${amoeba.home}/conf/ruleFunctionMap.xml</property>
			</bean>
		</property>
		<property name="sqlFunctionFile">${amoeba.home}/conf/functionMap.xml</property>
		<property name="LRUMapSize">1500</property>
		<property name="defaultPool">writedb</property>
		
		<property name="writePool">writedb</property>
		<property name="readPool">myslave</property>
		<property name="needParse">true</property>
	</queryRouter>
</amoeba:configuration>
```

#### 4、启动amoeba
```
/root/amoeba-mysql-3.0.5-RC/bin/launcher
```

### 4、测试amoeba

```
--测试的sql
--在安装amoeba的服务器上登录mysql
mysql -h192.168.85.13 -uroot -p123 -P8066
--分别在master、slave、amoeba上登录mysql
use msb
select * from user;
--在amoeba上插入数据
insert into user values(2,2);
--在master和slave上分别查看表中的数据
select * from user;
--将master上的mysql服务停止，继续插入数据会发现插入不成功，但是能够查询
--将master上的msyql服务开启，停止slave上的mysql，发现插入成功，但是不能够查询
```