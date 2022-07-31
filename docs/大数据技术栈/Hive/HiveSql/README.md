# HiveSql

> 官方文档：https://cwiki.apache.org/confluence/display/Hive/LanguageManual

样例数据：
```
1,小明,lol-book-movie,beijing:haidian-shanghai:pudong
2,小明,lol-book-movie,beijing:haidian-shanghai:pudong
3,小明,lol-book-movie,beijing:haidian-shanghai:pudong
4,小明,lol-book-movie,beijing:haidian-shanghai:pudong
5,小明,lol-book-movie,beijing:haidian-shanghai:pudong

6,小明,lol-book-movie,beijing:haidian-shanghai:pudong
7,小明,lol-book-movie,beijing:haidian-shanghai:pudong
8,小明,lol-book-movie,beijing:haidian-shanghai:pudong
9,小明,lol-book-movie,beijing:haidian-shanghai:pudong
10,小明,lol-book-movie,beijing:haidian-shanghai:pudong
```

## DDL
> https://cwiki.apache.org/confluence/display/Hive/LanguageManual+DDL   

### 建表语句
> 内部表：hive在默认情况下创建的是内部表
>
> 外部表：指定external关键字，并指定location存储目录即可创建外部表
> 
> 区别：   
> 1.内部表创建的时候，数据存储在hive的默认目录中；外部表创建的时候需要指定external关键字，同时需要指定location存储目录   
> 2.删除内部表，数据和元数据都会被删除；删除外部表，只会删除元数据，不会删除数据   
> 应用场景：   
> 内部表：先创建表，在添加数据    
> 外部表：可以先创建表再添加数据；也可以先添加数据再创建表   

- 不同格式建表样例
``` csv
CREATE EXTERNAL TABLE `heh`.`csvtable`(
  `id` INT, 
  `col1` STRING, 
  `col2` STRING)
PARTITIONED BY ( 
  `dt` STRING)
ROW FORMAT SERDE 
  'org.apache.hadoop.hive.serde2.OpenCSVSerde' 
WITH SERDEPROPERTIES ( 
  'quoteChar'='\"', 
  'escapeChar'='\\', 
  'separatorChar'=',') 
STORED AS INPUTFORMAT 
  'org.apache.hadoop.mapred.TextInputFormat' 
OUTPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION
  '/user/heh/csvtable'
```
``` orc 
CREATE EXTERNAL TABLE `heh`.`orctable`(
  `id` INT, 
  `col1` STRING, 
  `col2` STRING)
PARTITIONED BY ( 
  `dt` STRING)
ROW FORMAT SERDE 
  'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
STORED AS INPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'
LOCATION
  '/user/heh/orctable'
```
``` parquet
CREATE EXTERNAL TABLE `heh`.`parquettable`(
  `id` INT, 
  `col1` STRING, 
  `col2` STRING)
PARTITIONED BY ( 
  `dt` STRING)
ROW FORMAT SERDE 
  'org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe' 
WITH SERDEPROPERTIES ( 
  'parquet.compression'='SNAPPY') 
STORED AS INPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat' 
OUTPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat'
LOCATION
  '/user/heh/parquettable'
```
``` avro
CREATE EXTERNAL TABLE `heh`.`avrotable`(
  `id` INT, 
  `col1` STRING, 
  `col2` STRING)
PARTITIONED BY ( 
  `dt` STRING)
ROW FORMAT SERDE 
  'org.apache.hadoop.hive.serde2.avro.AvroSerDe' 
STORED AS INPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' 
OUTPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
LOCATION
  '/user/heh/avrotable'
TBLPROPERTIES (
  'avro.schema.literal'='{"type":"record","name":"parquetRecord","fields":[{"name":"id","type":["int","null"]},{"name":"col1","type":["string","null"]},{"name":"col2","type":["string","null"]}]}')
```

- 内部表

```hiveql
-- 用【,】分割字段，用【-】分割数组，用【:】分割map
create table person_in(
    id int,
    name string,
    likes array<string>,
    address map<string,string>
)
row format delimited 
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':';
```

- 外部表

```hiveql
-- 用【,】分割字段，用【-】分割数组，用【:】分割map,
-- /data 是hdfs中一个目录
create external table person_out(
    id int,
    name string,
    likes array<string>,
    address map<string,string>
)
row format delimited 
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':'
location '/data';
```

- 分区表
> Hive分区partition    
> 必须在表定义时指定对应的分区字段    
> 单分区表，按天分区，再表结构中存在id,name,dt三列    
> 以dt为文件夹区分
>
> 双分区表，按天和小时分区，再表结构中新增了dt和hour两列    
> 先以dt为文件夹，再以hour子文件夹区分  

1.单分区建表语句
```hiveql
create table single_partition(id int,name string) partitioned by (dt string);
```
2.双分区建表语句
```hiveql
create table double_partition(id int,name string) partitioned by (dt string,hour string);
```

### 修复分区
```hiveql
-- 当hdfs目录中存在数据，并且符合分区的格式，此时创建外部表的时候，一定要修复分区才能查到结果
msck repair table partition_external_table;
```

## DML
> https://cwiki.apache.org/confluence/display/Hive/LanguageManual+DML
### 新增数据：Loading files into tables

新增语句：
```hiveql
-- 完整语法
LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)]

-- 将本地文件拷贝到hive表对应的hdfs相应目录，无需走mr任务，所以比较快
load data local inpath '/root/data/data.txt' into table person;

-- 将hdfs文件移动到hive表对应的hdfs相应目录，无需走mr任务，所以比较快
load data inpath '/user/root/data/data.txt' into table person;

-- 分区表插入数据(需要给所有的分区列都指定值)
load data local inpath '/root/data/partition001' into table double_partition partition(dt='1',hour='18');
```

## Hive函数
> https://cwiki.apache.org/confluence/display/Hive/LanguageManual+UDF

> UDF（用户自定义函数）：输入一个值，输出也是一个值    
> UDAF(聚合函数)：多个输入，一个输出   
> UDTF：一个输入，多个输出    
> 
> 自定义函数：    
> https://cwiki.apache.org/confluence/display/Hive/HivePlugins    
> 1.定义一个类，实现UDF,并重写方法     
> 2.将工程打成jar包，上传到hive所在服务器     
> 3.将jar包添加到hive的classpath（hive命令行）：add jar my_jar.jar;       
> 4.创建一个hive函数（hive命令行）：create function heh_tm as 'com.enhui.MyFunction';     
> 5.使用函数：select heh_tm(name) from person;     
> 
> 上述方法是临时函数，关闭hive，重新连接就不生效了     
> 
> 1.创建一个hdfs目录存放jar：hdfs dfs -mkdir /jar     
> 2.将jar上传到hdfs中:hdfs dfs -put my_jar.jar /jar     
> 3.创建一个hive函数（hive命令行）：create function heh_tm2 as 'com.enhui.MyFunction' using jar 'hdfs://heh-node02/jar/my_jar.jar';     
> 4.使用函数：select heh_tm2(name) from person;      
> 关闭hive，重新连接，失效了会自动去hdfs重新加载   