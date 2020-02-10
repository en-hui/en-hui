# centOS安装docker

在此学习下docker在centOS系统上的安装

此处参考资料：菜鸟教程

### 首先卸载旧版本
```
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```

### 设置仓库
```puml
# 安装所需的软件包
sudo yum install -y yum-utils \
  device-mapper-persistent-data \
  lvm2
# 设置稳定的仓库
  sudo yum-config-manager \
      --add-repo \
      https://download.docker.com/linux/centos/docker-ce.repo
```

### 安装最新版本 Docker Engine-Community
```puml
sudo yum install docker-ce docker-ce-cli containerd.io
```

### 启动docker
```puml
sudo systemctl start docker
```

### 下载镜像可能会很慢，可以设置国内仓库

- 进入目录/etc/docker，创建文件 daemon.json,并写入内容
```puml
{
"registry-mirrors": ["https://lfxql9az.mirror.aliyuncs.com"]
}
```

- 重启 daemon
```puml
sudo systemctl daemon-reload
```

- 重启docker
```puml
sudo systemctl restart docker
```

然后就可以正常使用docker来完成日常操作了，对于命令不熟悉可以参考菜鸟教程，直接百度搜索 菜鸟教程docker 即可
