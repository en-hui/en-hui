# Hadoop 项目

> Hadoop官方网站：https://hadoop.apache.org/    
> 包含了 Hadoop Common、Hadoop Distributed File System（HDFS）、Hadoop YARN、Hadoop MapReduce四个模块   
> HDFS:分布式文件系统     
> MapReduce：分布式计算   
> Yarn：资源管理

## 大数据是什么？核心理念是什么？
海量数据的情况下，如何进行数据存储以及数据计算，就是大数据要解决的问题

数据存储：    
在只考虑单机情况下，我们认为磁盘是可靠的（内存中的数据掉电易失），但磁盘也是会损坏的        
所以为了保障数据可靠性，我们会将磁盘数据做物理机外的备份（即副本）    
**Hdfs解决大数据中数据存储的问题**，他会将文件存储到不同的物理机上，并保存多份副本   

本地计算、并行计算：   
数据可靠的前提下，如何高效利用数据得到各种计算结果?   
**计算向数据移动**，计算程序相对于海量数据来说，是很小的。   
将计算程序移动到存储数据的物理节点上，然后运行程序对本地数据进行计算，并最终汇合结果   
这样就避免了海量数据的网络io，且利用多台物理机的性能，实现并行计算   

资源管理：    
数据是多副本存储的，计算时只需利用其中一个节点即可   
当多个计算任务同时运行，就需要知道各节点的资源情况（cpu、内存），以确定计算程序移动到哪台较为空闲的物理机器去运行   

## HDFS

- [HDFS理论知识点](大数据技术栈/Hadoop/HDFS/HDFS理论知识点/)
- [HDFS集群搭建](大数据技术栈/Hadoop/HDFS/HDFS集群搭建/)
- [HDFS客户端](大数据技术栈/Hadoop/HDFS/HDFS客户端/)

## 资源管理 Yarn

- [MapReduce的资源管理是如何演进到Yarn的](大数据技术栈/Hadoop/Yarn/Yarn理论知识点/)
- [Yarn环境搭建](大数据技术栈/Hadoop/Yarn/Yarn环境搭建/)

## MapReduce

- [MapReduce理论](大数据技术栈/Hadoop/MapReduce/MapReduce理论/)
- [MapReduce代码开发及任务提交方式](大数据技术栈/Hadoop/MapReduce/MapReduce代码开发及任务提交方式/)
- [MapReduce源码探索](大数据技术栈/Hadoop/MapReduce/MapReduce源码探索/)