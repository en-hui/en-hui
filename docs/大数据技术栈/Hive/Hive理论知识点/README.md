# Hive 理论知识点

> Hive 基于 Hadoop 使用，使用 HDFS 作为存储层，使用 MapReduce 作为计算层     
> Hive的产生：让非Java编程者，使用sql的方式对hdfs的数据进行MapReduce操作   
> Hive使用关系型数据库存储了Hdfs中文件的元数据
>
> Hive：解释器、编译器、优化器 —— Hive会将sql语句转换成MR代码，进行编译和优化

## Hive架构
> (1)用户接口主要有三个：Cli、Client和WebUI   
> 最常用的是Cli，Cli启动的时候，会同时启动一个Hive副本。    
> Client是Hive的客户端，用户连接至Hive Server。再启动Client模式的时候，需要指定Hive Server所在节点，并且在该节点启动Hive Server。     
> WebUI是通过浏览器访问Hive。   
> 
> (2)Hive将元数据存储在数据库中，如mysql、derby。Hive中的元数据包括表的名称，表的列和分区及其属性，
> 表的属性（是否为外部表等），表的数据所在目录等    
> 
> (3)解释器、编译器、优化器完成HiveSql查询语句从词法分析、语法分析、编译、优化以及查询计划的生成。
> 生成的查询计划存储在HDFS中，并在随后由MapReduce调用执行   
> 
> (4)Hive的数据存储再HDFS中，大部分的查询、计算由MapReduce完成
> （包含*的查询不会生成MR任务,比如：select * from tbl）
> 

## Hive内部表、外部表、分区表
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


> Hive分区partition    
> 必须在表定义时指定对应的分区字段    
> 单分区表，以【分区字段】为文件夹区分
>
> 双分区表，以【分区字段】为文件夹分区，多个分区字段逐级分文件夹
