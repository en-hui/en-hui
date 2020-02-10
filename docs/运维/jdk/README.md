# centOS 安装 JDK8
jdk8下载地址：https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

下载jdk-8u191-linux-x64.tar.gz，放至阿里云服务器的/usr/local/java/目录下        
- 解压  
```tar -zxvf jdk-8u191-linux-x64.tar.gz```
解压后名称为 jdk1.8.0_191

- 修改名称    
```mv jdk1.8.0_191/ jdk1.8```

- 修改环境变量
```vi /etc/profile```
在最后面加四句话
```
JAVA_HOME=/usr/local/java/jdk1.8
PATH=$JAVA_HOME/bin:$PATH
CLASSPATH=.:$JAVA_HOME/jre/lib/ext:$JAVA_HOME/lib/tools.jar
export PATH JAVA_HOME CLASSPATH
```

- 刷新配置
``` source /etc/profile```

- 检查
``` java -version```