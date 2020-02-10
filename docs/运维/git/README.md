# centOS安装git2.9.5

git下载地址：https://mirrors.edge.kernel.org/pub/software/scm/git/

下载git-2.9.5.tar.gz,放至阿里云服务器的/usr/local/git/目录下

- 安装编译git所需资源
```
yum install -y curl-devel expat-devel gettext-devel openssl-devel zlib-devel
yum install -y gcc perl-ExtUtils-MakeMaker
```

- 删除已有的git
``` 
yum remove git
```

- 将下载好的 git-2.9.5.tar.gz 解压
``` 
tar -zxvf git-2.9.5.tar.gz
```
解压后的文件名称为 git-2.9.5

- 编译安装
``` 
cd git-2.9.5
make prefix=/usr/local/git all
make prefix=/usr/local/git install

vim /etc/profile
-- 最后加上
GIT_HOME=/usr/local/git
exprot PATH=$PATH:$GIT_HOME/bin
-- 生效
source /etc/profile
```

- 检查
``` git --version ```