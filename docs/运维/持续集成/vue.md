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
4.保存，此时已完成从git拉取代码，将代码放到目录/usr/local/tomcat/manage-vue下

### nginx安装及配置

