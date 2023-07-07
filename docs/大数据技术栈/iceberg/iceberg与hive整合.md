# hive 与 iceberg 整合

## 下载整合jar
需要的jar文件：
iceberg-hive-runtime-1.3.0.jar  

放在hive的安装目录下，lib目录中   

下载地址：
https://iceberg.apache.org/releases/

使用方法：
To use Iceberg in Hive 2 or Hive 3, download the Hive runtime JAR and add it to Hive using ADD JAR.
> 连接到hive后，add jar /opt/bigdata/hive/lib/iceberg-hive-runtime-1.3.0.jar   
> 

libfb303-0.9.3.jar

## hive客户端配置hive-site.xml
```
  <property>
    <name>iceberg.engine.hive.enabled</name>
    <value>true</value>
  </property>
```


``` 
# 建表
create table test_iceberg_tbl1(
id int,
name string,
age int
)
partitioned by (dt string)
stored by 'org.apache.iceberg.mr.hive.HiveIcebergStorageHandler';


向表中插入数据：
insert into test_iceberg_tbl1 values(1,"zs",18,"20230707");

查询数据：
select * from test_iceberg_tbl1;


```

/opt/cloudera/parcels/CDH-6.1.1-1.cdh6.1.1.p0.875250/lib/iceberg/iceberg-hive-runtime-1.3.0.jar
