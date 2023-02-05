# Redis

[在线画图工具ProcessOn新人注册](https://www.processon.com/i/5e0d9502e4b02086237ce4f8)       
[Redis思维导图](https://www.processon.com/view/link/611e7e187d9c0834aa5f2157)      
![Alt](http://assets.processon.com/chart_image/601586b1e401fd15813bb667.png)

> redis 安装为服务：    
> https://redis.io/docs/getting-started/installation/install-redis-from-source/      
> 安装官网最后 make install 后的操作    
> 1. cd /opt/redis-stable/utils    
> 2. ./install_server.sh 可能报错，注释脚本部分内容解决：https://blog.csdn.net/xiaoai5324/article/details/118314581    
> 3. 安装完成后，由于设置了开机自启，所以会在/etc/init.d/目录下生成对应服务名称 redis_6379    
> 4. 启动关闭命令：
> 5. service redis_6379 status
> 6. service redis_6379 start 
> 7. service redis_6379 stop
> 
> redis 安装为服务后，默认文件位置：    
> 配置文件：/etc/redis/6379.conf    
> 日志文件：/var/log/redis_6379.log    
> 数据文件：/var/lib/redis/6379    

管道、发布订阅、事务
- [学习Redis前的小科普](常用组件/Redis/科普/)
- [五种数据类型的基本使用](常用组件/Redis/五种数据类型的基本使用/)
- [Redis作为数据库&缓存](常用组件/Redis/Redis作为数据库&缓存/)
- [Redis高可用&集群](常用组件/Redis/Redis高可用&集群/)
