## 持续化部署vue项目（使用nginx方式访问）

### 插件安装及配置检查
检查是否安装 NodeJS Plugin            
检查是否在 系统管理—全局工具配置—NodeJS中配置，名称任意，例如node10.16,选择自动安装，版本选择NodeJS 10.16.0,然后保存             

### 新建任务
工程名称：随便起，例如 manage-vue          
选择 自由风格             
点击确定            

### 任务配置
1.源码管理，选择git，进行配置。前面写过了      
2.构建环境，选择Provide Node & npm bin/ folder to PATH，选择刚才添加的 node10.16，其他默认            
3.构建，选择执行shell,写入以下内容
``` 
npm install 
rm -rf ./dist/*
npm run build
rm -rf /usr/local/tomcat/manage-vue/*
cp -rf ./dist/* /usr/local/tomcat/manage-vue
```  
4.保存。此时已完成从git拉取代码，将代码放到目录/usr/local/tomcat/manage-vue下

### nginx安装及配置

### nginx安装
yum安装nginx非常简单，一条命令即可。这种方式安装，配置文件在/etc/nginx目录下。   
``` 
# 安装 nginx      
sudo yum -y install nginx       
# 卸载 nginx      
sudo yum remove nginx       
```

其他操作命令      
``` 
# 设置开机启动        
sudo systemctl enable nginx         
# 启动nginx服务     
sudo service nginx start        
# 停止nginx服务     
sudo service nginx stop         
# 重启nginx服务     
sudo service nginx restart      
# 重新加载配置，一般是在修改过nginx配置文件时使用。       
sudo service nginx reload       
```

#### nginx配置文件
1.修改第一行的用户，改为root，否则访问时会出现403错误。
``` user root;```

2.添加server块
``` 
    server {
        listen          9080;
        server_name     123.57.241.89;

        location / {
                # manage-vue的打包路径，包括 index.html 和 static文件夹 
                root    /usr/local/tomcat/manage-vue/;
                # 重定向，内部文件的指向（照写）
                try_files $uri $uri/    /index.html;
        }

        # 当请求跨域时，配置端口转发
        location /api {
                # http://123.57.241.89:9081/。最后这个/会替换掉/api，即最终请求会去掉/api
                proxy_pass      http://123.57.241.89:9081/;
        }
    }
```
