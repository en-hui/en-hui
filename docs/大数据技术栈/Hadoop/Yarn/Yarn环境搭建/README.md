# Yarn 环境搭建

在hadoop——hdfs环境搭建好的情况下

## 修改配置文件

/opt/bigdata/hadoop-2.10.1/etc/hadoop 目录下,修改 mapped-site.xml、yarn-site.xml

```shell
cp mapred-site.xml.template mapped-site.xml
vim mapped-site.xml

<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <!-- mapreduce on yarn -->
        <value>yarn</value>
    </property>
    <property>
        <name>mapreduce.application.classpath</name>
        <value>/opt/bigdata/hadoop-2.10.1/share/hadoop/mapreduce/*:/opt/bigdata/hadoop-2.10.1/share/hadoop/mapreduce/lib/*</value>
    </property>
</configuration>
    
    
vim yarn-site.xml

<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_HOME,PATH,LANG,TZ,HADOOP_MAPRED_HOME</value>
    </property>
</configuration>

```

## 启动进程

/opt/bigdata/hadoop-2.10.1/sbin 目录下
```shell
./start-yarn.sh 

使用jps查看角色是否完整
会在主节点启动一个ResourceManager进程
会在每个datanode启动一个NodeManager进程
```