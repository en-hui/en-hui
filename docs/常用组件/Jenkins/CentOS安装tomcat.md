# centOS 安装 tomcat9
tomcat9下载地址：https://tomcat.apache.org/download-90.cgi

下载 apache-tomcat-9.0.30.tar.gz ,放至阿里云服务器的/usr/local/tomcat/目录下
- 解压        
  ```tar -zxvf apache-tomcat-9.0.30.tar.gz```
  解压后名称为 apache-tomcat-9.0.30

- 修改名称          
  ```mv apache-tomcat-9.0.30 jenkins-tomcat9```

- 检查            
  进入/usr/local/tomcat/jenkins-tomcat9/bin/下启动tomcat       
  ```sh startup.sh```         
  然后本地访问8080端口        