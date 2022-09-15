# 迁移topic数据
## 运行前需要关注的文件
### startMoveTopic.sh 启动命令
> moveConfig:迁移的配置文件(已配置好路径，保持文件夹结构即可直接使用)   
> topicMappingPath：关闭自动识别时，可以文件中指定要迁移的topic，需准确描述topic名称及序列化方式

### moveConfig.properties 启动需要用到的参数
### topicMapping.csv 手动指定迁移topic
可以选择在【moveConfig.properties】设置参数【autoCheck=true】
如果设置为false，则脚本将会按照【topicMapping.csv】指定的列表进行迁移

### 如何查看脚本运行结束了
查看日志文件，查看输出 /log/moveTopic.log   

## 运行方式
优先选择 dashboard ，前台运行，可直接再控制台看到执行结果，可交互式选择要执行的选项   
> 指定入口(前台执行，日志打印控制台)：./startMoveTopic.sh dashboard      
> 指定入口(后台执行，日志打印文件)：./startMoveTopic.sh lag     
> 指定入口(后台执行，日志打印文件)：./startMoveTopic.sh move
## 容器中没有vi或vim，如何修改配置文件(容器内保证有2G以上内存供程序使用，或者修改启动脚本)
使用docker cp将配置文件拷贝进容器
> docker cp 本机文件  容器名称:容器内路径    
> docker cp moveConfig.properties move-topic:/opt/conf/   

使用sed进行文件编辑
> 添加新行   
> cat -n 文件名称     先查看文件内容    
> sed -i "Na 添加的内容" 文件名称       N代表在哪一行后面加内容，a表示添加
> 比如：sed -i "1a autoCheck=false" conf/moveConfig.properties   表示在第二行添加内容『autoCheck=false』   
> 
> 内容替换    
> sed -i 's/abc/xxx/g' file   abc是要修改的内容，xxx是修改后的内容    
> 比如：sed -i 's/autoCheck=true/autoCheck=false/g' conf/moveConfig.properties   
> 比如：sed -i 's/-Xmx2G -Xms2G/-Xmx1G -Xms1G/g' startMoveTopic.sh

