# centos 安装 docker-compose

> copy 于菜鸟教程

``` 
curl -L https://get.daocloud.io/docker/compose/releases/download/v2.4.1/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
```

将可执行权限应用于二进制文件
``` 
sudo chmod +x /usr/local/bin/docker-compose
```

创建软链
``` 
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

测试是否安装成功
``` 
docker-compose --version
```

## docker compose 使用
> 拿到docker-compose.yml    
> 在yml目录下，执行   
> docker-compose up -d heh-mysql   
> docker-compose up -d heh-redis   
> docker-compose up -d heh-zk   
> docker-compose up -d heh-kafka   