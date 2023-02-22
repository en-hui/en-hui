# Hive 安装
> 官方文档：https://cwiki.apache.org/confluence/display/Hive/AdminManual+Installation     
> hive下载：https://www.apache.org/dyn/closer.cgi/hive/     

## Hive Metastore Administration
> Hive Metastore 有三种方式    
> 将 metadata 存储在内存数据库（基本不用）    
> 将 metadata 存储在第三方关系型数据库（MySQL、Oracle）    
> 将 metadata 抽取成一个服务（Thrift远程调用）  
> 
> 在安装过程中，并没有配置hadoop的节点信息，为什么可以访问到hadoop   
> 因为hadoop配置了环境变量    
> hive的默认配置会从环境变量取信息$HADOOP_HOME

首先安装Mysql用于存储元数据    
元数据管理安装：https://cwiki.apache.org/confluence/display/Hive/AdminManual+Metastore+Administration
### 远程元数据数据库（Remote Metastore Database）
- 解压hive安装包
  - tar -zxvf hive-2.3.9
- 修改配置文件：hive-site.xml
  - cd conf/
  - cp hive-default.xml.template hive-site.xml
  - vim hive-site.xml(删除默认的配置，重新添加下面的内容)
  - 删除从当前到最后-1行  :.,$-1d
- 添加环境变量
    - vim /etc/profile
    - export HIVE_HOME=/opt/bigdata/
    - source /etc/profile
- 上传mysql驱动包（最好与mysql版本一致），放在hive的lib目录下
- 初始化hive数据库表
  - schematool -dbType mysql -initSchema
- 连接hive并使用
  - hive

```hive-site.xml 
<configuration>
    <property>
        <!-- hive默认在hdfs的存储位置 -->
        <name>hive.metastore.warehouse.dir</name>
        <value>/user/hive/warehouse</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionURL</name>
        <value>jdbc:mysql://node01:3306/hive?createDatabaseIfNotExist=true</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionDriverName</name>
        <value>com.mysql.cj.jdbc.Driver</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionUserName</name>
        <value>hive</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionPassword</name>
        <value>123456</value>
    </property>
</configuration>
```

### 远程元数据服务（Remote Metastore Server）

> 在服务端启动MetaStoreServer，将hive与关系型数据库进行解耦   
> 使用Thrift进行远程服务调用    
> 元数据服务，不光可以让hive用，还可以提供给其他组件使用（Spark Sql、Impala）

- 在node01、node02分别解压hive、配置环境变量    
- 修改配置见下面内容   
- 在服务端（node01）上传mysql驱动包（最好与mysql版本一致），放在hive的lib目录下
- 在服务端初始化：schematool -dbType mysql -initSchema
- 启动服务端（node01）阻塞式窗口：hive --service metastore
- 后台启动 metastore 服务器端，默认使用 9083 端口: nohup hive --service metastore &
- 客户端连接（node02）：hive

node01:服务端
```hive-site.xml 
<configuration>
    <property>
        <!-- hive默认在hdfs的存储位置 -->
        <name>hive.metastore.warehouse.dir</name>
        <value>/user/hive_remote/warehouse</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionURL</name>
        <value>jdbc:mysql://node01:3306/hive_remote?createDatabaseIfNotExist=true</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionDriverName</name>
        <value>com.mysql.cj.jdbc.Driver</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionUserName</name>
        <value>hive</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionPassword</name>
        <value>123456</value>
    </property>
</configuration>
```

node02:客户端
```hive-site.xml 
<configuration>
    <property>
        <!-- hive默认在hdfs的存储位置 -->
        <name>hive.metastore.warehouse.dir</name>
        <value>/user/hive_remote/warehouse</value>
    </property>
        <!-- 服务端metaStoreServer 默认端口9083 -->
        <name>hive.metastore.uris</name>
        <value>thrift://node01:9083</value>
    </property>
</configuration>
```

## HiveServer2 and beenline(提供jdbc方式连接，主要用于查询功能)

node01节点配置:同上，配置了mysql的信息     
node02节点配置：同上，配置了node01的元数据服务的信息   

### hiveserver2的搭建使用
1. 独立hiveserver2模式
   1. 将现有的所有hive的服务停止，不需要修改任何服务，在node01机器上执行hiveserver2或者hive --service hiveserver2的命令，开始启动hiveserver2的服务，hiveserver2的服务也是一个阻塞式窗口，当开启服务后，会开启一个10000的端口，对外提供服务。
   2. 在任意一台包含beeline脚本的虚拟机中执行beeline的命令进行连接
2. 共享metastore server的hiveserver2模式搭建
   1. 在node01上执行hive --service metastore启动元数据服务
   2. 在node02上执行hiveserver2或者hive --service hiveserver2两个命令其中一个都可以
   3. 在任意一台包含beeline脚本的虚拟机中执行beeline的命令进行连接

**企业中常用的模式**   
在node01启动metastore server   
在node02启动hiveserver2     
node02即可以直接使用hive连接，做【增删改查】操作    
又可以使用beeline连接，做查询操作

在hadoop的配置文件core-site.xml添加配置   

```
  <!-- 给什么用户超级管理员权限，就写什么用户，这里写的root-->
  <property>
    <name>hadoop.proxyuser.root.groups</name>
    <value>*</value>
  </property>
  <property>
    <name>hadoop.proxyuser.root.hosts</name>
    <value>*</value>
  </property>
-- 刷新配置
hdfs dfsadmin -refreshSuperUserGroupsConfiguration
```

### hiveserver2的访问方式

假设是再node01启动了hiveserver2,node01提供了10000端口提供服务   

1. beeline的访问方式    
(1）beeline -u jdbc:hive2://node01:10000/default   
(2）beeline进入到beeline的命令行      
beeline> !connect jdbc:hive2://node01:10000/default root 密码(随便写也可以)   
注意：   
1、使用beeline方式登录的时候，默认的用户名和密码是不验证的，也就是说随便写用户名和密码即可    
2、使用第一种beeline的方式访问的时候，用户名和密码可以不输入。   
有可能会报 /tmp 没有权限的错误：hdfs dfs -chmod 777 /tmp    
3、使用第二种beeline方式访问的时候，必须输入用户名和密码，用户名和密码是什么无所谓    
!close 退出到beeline，还可以使用 !connect 连接    
!quit 直接退出beeline

2. jdbc的访问方式

```xml
    <dependencies>
        <dependency>
            <groupId>org.apache.hive</groupId>
            <artifactId>hive-jdbc</artifactId>
            <version>1.2.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>3.3.1</version>
        </dependency>
    </dependencies>
```

```java
package com.enhui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcClient {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // heh-node02 在host文件配置了 hiveserver2 的ip地址
        Connection conn = DriverManager.getConnection("jdbc:hive2://heh-node02:10000/default", "root", "root");
        Statement stmt = conn.createStatement();
        String sql = "select * from person limit 5";
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + "-" + resultSet.getString("name"));
        }

    }
}
```


## 验证安装
- 简单操作，查看是否安装正确
  - 查看表：show tables;
  - 查看数据库：show databases;
  - 创建表：create table tbl(id int,age int);
  - 查看表：show tables;
  - 查询表：select * from tbl;
  - 查看表信息：desc tbl;
  - 查看表信息：desc formatted tbl;
  - 插入数据：insert into tbl values(1,1);
  - 查看数据：select * from tbl;
  - 查看hdfs的文件 hdfs dfs -cat /user/hive/warehouse/tbl/*
    - 下载hdfs的文件 hdfs dfs -get /user/hive/warehouse/tbl/*
    - 查看带分隔符的文件内容：cat -A 000000_0
    - hive默认的分隔符：^A  表示ascii码，手动输入：control+v  control+a
