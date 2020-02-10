# centOS 安装 maven3.6.3
maven3.6.3下载地址：https://maven.apache.org/download.cgi

下载 apache-maven-3.6.3-bin.tar.gz ，放至阿里云服务器的/usr/local/maven/目录下        
- 解压  
```tar -zxvf apache-maven-3.6.3-bin.tar.gz```
解压后名称为 apache-maven-3.6.3

- 修改名称    
```mv apache-maven-3.6.3 maven3.6```

- 配置环境变量
```vi /etc/profile```
在最后面加
```
MAVEN_HOME=/usr/local/maven/maven3.6
export PATH=${MAVEN_HOME}/bin:${PATH}
```

- 刷新配置
``` source /etc/profile```

- 检查
``` mvn –v ```

- 更换阿里源
```
vim /usr/local/maven/maven3.6/conf/settings.xml
```
找到<mirror></mirror>标签对，改为
``` 
<mirror>
     <id>alimaven</id>
     <name>aliyun maven</name>
     <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
     <mirrorOf>central</mirrorOf>
</mirror> 
```

- 指定下载路径
创建文件夹repository
```
<localRepository>/usr/local/maven/repository</localRepository>
```

- 指定jdk版本
``` 
<profile>    
     <id>jdk-1.8</id>    
     <activation>    
       <activeByDefault>true</activeByDefault>    
       <jdk>1.8</jdk>    
     </activation>    
       <properties>    
         <maven.compiler.source>1.8</maven.compiler.source>    
         <maven.compiler.target>1.8</maven.compiler.target>    
         <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>    
       </properties>    
</profile>
```