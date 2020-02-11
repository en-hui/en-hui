# centOS安装Jenkins

## 环境准备
此处使用部署的方式安装Jenkins,即官网下载Jenkins的war包，然后部署在tomcat下               
jdk8下载地址：https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html               
tomcat下载地址：https://tomcat.apache.org/download-90.cgi            
Jenkins下载地址：https://jenkins.io/zh/download/         

- [centOS安装java8](运维/jdk/README.md)
- [centOS安装tomcat9](运维/tomcat/README.md)

## 项目部署及启动
将下载好的 jenkins.war 放到安装好的 tomcat 目录/usr/local/tomcat/jenkins-tomcat9/webapps/目录下         
改名为ROOT.war，然后启动tomcat。         
浏览器访问 8080 端口，按提示找到网页登陆密码     

关闭tomcat，修改配置文件/usr/local/tomcat/jenkins-tomcat9/conf/server.xml ,配置编码
``` 
<Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" URIEncoding="UTF-8"/>

```  
