# Jenkins创建war部署任务

复制一份tomcat压缩包，同样解压放在/usr/local/tomcat/目录下，改名为 manage-tomcat9

为tomcat设置 用户权限和用户
打开 /usr/local/tomcat/manage-tomcat9/conf/tomcat-users.xml ,写入以下内容
``` 
<role rolename="manager-hui"/>
<role rolename="manager-script"/>
<role rolename="manager-jmx"/>
<role rolename="manager-status"/>
<user  username="tomcat_user"  password="123456"
roles="manager-hui,manager-script,manager-jmx,manager-status"/>
```


## 新建Jenkins任务
1. 随便起个任务名称，例如：manage      
   选择 构建一个自由风格的软件项目。点击确定

2. 源码管理 部分设置     
   选择Git       
   填写Repository URL为gitee仓库的链接，例如https://gitee.com/huenhui/manage-template       
   添加gitee的登陆账号及密码，并选择此账号

3. 构建 部分设置          
   增加构建步骤，选择 调用顶层Maven目标       
   Maven版本,选择我们自己添加的 Maven3.6         
   目标,填写 clean install

4. 设置完成后，选择保存，并立即构建此任务。

5. 再次进入配置，构建后操作 部分设置        
   (项目打war包的方式)增加构建后操作步骤，选择deploy war/ear to a container。如果没有此选项，安装插件Deploy to container并重启        
   WAR/EAR files(war包的路径)，例如填写 target/manage-0.0.1-SNAPSHOT.war          
   Context path(浏览器访问项目名)，例如填写 manage          
   Containers(配置tomcat)，选择tomcat9.x，       
   -Credentials，添加 tomcat-users.xml 配置中的用户，例如：tomcat_user,123456       
   -Tomcat URL,填写部署项目的tomcat访问链接，例如：http://123.57.241.89:8091

(springboot,打jar包)	    
1.安装插件 publish over ssh       
2.进入系统管理 - 系统配置，配置Publish over SSH部分      
Passphrase(填写远程服务器的登陆密码)：***                  
SSH Servers.Name(随便起一个名字)：阿里云        
SSH Servers.Hostname(远程服务器的ip)：123.57.241.89       
SSH Servers.Username(远程服务器的登陆用户名)：root        
SSH Servers.Remote Directory(设置远程服务器的文件路径)：/usr/local/tomcat                   
3.构建后操作，选择send build artifacts over SSH     
SSH Server.name(选择刚添加的服务器)：              
Transfers.Source files(jenkins工作空间中，构件的路径):例如 target/manage-0.0.1-SNAPSHOT.jar     
Transfers.Remove prefix(要去掉的构件前缀):例如 target/        
Transfers.Remote directory(远程服务器文件路径下的路径): manage-jar
Transfers.Exec command(ssh传输完成后要执行的命令):
```
cd /usr/local/tomcat/manage-jar         
chmod 777 *.sh      
./stop.sh           
./start.sh              
```        
4.创建stop.sh脚本
``` 
#!/bin/bash     
echo "stop SpringBoot manage"       
pid=`ps -ef | grep manage-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{print $2}'`     
echo "旧应用进程id：$pid"     
if [ -n "$pid" ]        
then        
kill -9 $pid        
fi      
```     
5.创建start.sh脚本
```         
#!/bin/bash
#必须要定义一下JAVA_HOME不然下面的java找不到，导致不执行
export JAVA_HOME=/usr/local/java/jdk1.8     
echo ${JAVA_HOME}       
chmod 777 /usr/local/tomcat/manage-jar/manage-0.0.1-SNAPSHOT.jar        
echo "执行...."       
cd /usr/local/tomcat/manage-jar/        
nohup ${JAVA_HOME}/bin/java -jar manage-0.0.1-SNAPSHOT.jar > manage.log &        
echo "启动成功"     
```    

6. 配置钩子函数，以达到git的push操作触发构建         
   此处不做，想用的可以百度下，很简单

7. 关闭跨站请求伪造保护           
   进入Jenkins的全局安全配置，取消勾选 防止跨站点请求伪造