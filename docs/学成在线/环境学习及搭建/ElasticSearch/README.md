# ElasticSearch

## 介绍
ElasticSearch是一个基于Lucene的搜索服务器。
它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。
Elasticsearch是用Java语言开发的，并作为Apache许可条款下的开放源码发布，是一种流行的企业级搜索引擎。
ElasticSearch用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。
根据DB-Engines的排名显示，Elasticsearch是最受欢迎的企业搜索引擎，其次是Apache Solr，也是基于Lucene。

官方网址:[https://www.elastic.co/cn/products/elasticsearch](https://www.elastic.co/cn/products/elasticsearch)       
Github:[https://github.com/elastic/elasticsearch](https://github.com/elastic/elasticsearch)

总结：
1. ElasticSearch是一个基于Lucene的高扩展的分布式搜索服务器，支持开箱即用。
2. ElasticSearch隐藏了Lucene的复杂性，对外提供Restful接口来操作索引、搜索。

优点：
1. 扩展性好，可部署上百台服务器集群，处理PB级数据。
2. 近实时的去索引数据，搜索数据

## 原理了解
下图是ElasticSearch的索引结构，下边黑色部分是物理结构，上面黄色部分是逻辑结构，
逻辑结构也是为了更好的去描述ElasticSearch的工作原理及去使用物理结构中的索引文件。     
![Alt](./ElasticSearchimg/es原理图.png)        

逻辑结构部分是一个倒排索引表：
1. 将要搜索的文档内容分词，所有不重复的词组成分词列表
2. 将搜索的文档最终以document方式存储起来
3. 每个词和document都有关联

## 在Windows环境安装 ElasticSearch7.4.2
Elasticsearch是使用Java构建的，并且在每个发行版中都包含来自JDK维护者（GPLv2 + CE）的捆绑版本的 OpenJDK。
捆绑的JVM是推荐的JVM，位于Elasticsearch主目录的jdk目录内。        
要使用自己的Java版本，请设置JAVA_HOME环境变量。如果必须使用与捆绑的JVM不同的Java版本，
则建议使用受支持 的Java LTS版本。如果使用已知的Java错误版本，Elasticsearch将拒绝启动。
使用您自己的JVM时，可以删除捆绑的JVM目录。        

ElasticSearch7.4.2要求Java环境为jdk11。       
从官网下载windows环境的zip压缩包，解压即可             
下载地址:[https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.4.2-windows-x86_64.zip](https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.4.2-windows-x86_64.zip)   

配置文件(zip方式安装，配置文件在ES目录下的config中)：       
- elasticsearch.yml ： 用于配置Elasticsearch运行参数       
- jvm.options ： 用于配置Elasticsearch JVM设置       
- log4j2.properties： 用于配置Elasticsearch日志      

启动：直接双击bin目录下的[elasticsearch.bat] 脚本文件，然后浏览器访问9200端口
## 使用docker安装ES7.4.2
```
# 拉取镜像
docker pull elasticsearch:7.4.2

# 运行容器，注意设置内存，默认分配jvm空间大小为2g
docker run -e ES_JAVA_OPTS="-Xms128m -Xmx128m" -d -p 9200:9200 -p 9300:9300 --name docker_es elasticsearch:7.4.2

# 运行失败，查看日志命令：docker logs -f -t --tail 100 容器名称  

# 报错信息： max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
# 解决方案：  使用命令：sudo sysctl -w vm.max_map_count=262144   设置后可用命令查看：sysctl -a
docker run -d -e ES_JAVA_POTS="-Xms128m -Xmx128m"  -e "discovery.type=single-node" -p 9200:9200 -p 9300:9300 --name docker_es elasticsearch:7.4.2
```
## ElasticSearch Head插件
最简单的方式，可以在谷歌商店搜索 ElasticSearch Head ，然后添加扩展程序       
![Alt](./ElasticSearchimg/esHead插件.png)  

## ElasticSearch 快速入门
### restful
- GET: 获取资源
- POST： 新建资源
- PUT：在服务器更新资源（向客户端提供改变后的所有资源）
- DELETE：删除资源
### 概念理解
1. Elasticsearch是面向文档型数据库，一条数据在这里就是一个文档，用JSON作为文档序列化的格式    
2. 将Elasticsearch和关系型数据术语对照表           
关系数据库     ⇒ 数据库 ⇒ 表    ⇒ 行    ⇒ 列(Columns)      
Elasticsearch  ⇒ 索引库(Index)   ⇒ 类型(type)  ⇒ 文档(Docments)  ⇒ 字段(Fields)  
注意：ES7.x版本已经移除type这个概念    
3. ES官方建议，在一个索引库中只存储相同类型的文档。
4. 分片和副本        
副本是分片的副本。分片有主分片和副本分片之分。（主从）     
一个index数据在物理上被分布在多个主分片上，每个主分片只存放部分数据。    
每个主分片可以有多个副本，叫副本分片，是主分片的复制。

### 创建索引库（理解为创建数据库）
ES的索引库是一个逻辑概念，它包括了分词列表及文档列表，同一个索引库中存储了相同类型的文档。     
 
>  可以使用head插件创建       
>  也可以使用 postman 工具创建  
put http://localhost:9200/索引名称
例子：put http://localhost:9200/test_index
```$xslt
{
    "settings":{
        "index":{
            "number_of_shards":1,
            "number_of_replicas":0
        }       
    }
}

```
number_of_shards：设置分片的数量，在集群中通常设置多个分片，表示一个索引库将拆分成多片分别存储不同的结点，
提高了ES的处理能力和高可用性，入门程序使用单机环境，这里设置为1。      
number_of_replicas：设置副本的数量，设置副本是为了提高ES的高可靠性，单机环境设置为0.
### 创建映射（理解为表结构）
在索引中每个文档都包括了一个或多个field，创建映射就是向索引库中创建field的过程
> 使用 postman 工具
post http://localhost:9200/索引库名称/_mapping
例子：http://localhost:9200/test_index/_mapping
包含三个字段  name description studymodel
```
{
    "properties": {
        "name": {
            "type": "text"
        },
        "description": {
            "type": "text"
        },
        "studymodel": {
            "type": "keyword"
        }
    }
}

```


### 创建文档（理解为添加一条数据）
> 使用 postman 工具 
post http://localhost:9200/索引库名称/默认类型名称/文档编号
例子：http://localhost:9200/test_index/_doc/1
```
{
	"name":"胡",
	"description":"学习",
	"studymodel":"java"
}

```
### 搜索文档（理解为查询数据）
> 使用 postman 工具 
get http://localhost:9200/索引库名称/默认类型名称/文档编号
例子：http://localhost:9200/test_index/_doc/1