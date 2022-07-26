## 日常问题

- 开发机服务启动了，浏览器不能访问页面，本地telnet等都无法连接，可以ping通

> 问题查看：sysctl -p | grep net.ipv4.ip_forward     
> 问题分析：得到的结果如果是0，修改为1     
> 问题解决：sysctl 直接回车，查看帮助，看到-w是修改（临时修改，还需修改配置文件）   
> sysctl -w net.ipv4.ip_forward=1   
> 修改配置文件 /etc/sysctl.conf


## 生产环境问题排查

### 操作系统层面

### JVM层面

### 工具