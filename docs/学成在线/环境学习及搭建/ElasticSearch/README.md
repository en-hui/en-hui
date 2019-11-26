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
下图是ElasticSearch的索引结构，下边黑色部分是屋里结构，上面黄色部分是逻辑结构，
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
## ElasticSearch Head插件
最简单的方式，可以在谷歌商店搜索 ElasticSearch Head ，然后添加扩展程序       
![Alt](./ElasticSearchimg/esHead插件.png)  

## ElasticSearch 快速入门
### 创建索引库

### 创建映射

### 创建文档

### 搜索文档
