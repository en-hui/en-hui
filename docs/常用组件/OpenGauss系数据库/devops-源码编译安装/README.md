# 容器内编译安装opengauss

> 参考文档：   
> https://docs-opengauss.osinfra.cn/zh/docs/5.1.0/docs/CompilationGuide/%E7%89%88%E6%9C%AC%E7%BC%96%E8%AF%91.html


## 准备工作

### 1、opengauss-server
> 克隆openGauss的代码仓库,branchname为分支名称，如：5.0.0    
> git clone https://gitee.com/opengauss/openGauss-server.git openGauss-server -b 5.0.0   

### 2、openGauss-third_party
> 直接下载基于centos_x86编译好的二进制文件(如下为5.0.0对应文件)    
> wget https://opengauss.obs.cn-south-1.myhuaweicloud.com/5.0.0/binarylibs/openGauss-third_party_binarylibs_Centos7.6_x86_64.tar.gz     
> tar -zxvf openGauss-third_party_binarylibs_Centos7.6_x86_64.tar.gz    
> mv openGauss-third_party_binarylibs_Centos7.6_x86_64 binarylibs    

### 3、openGauss-OM
> git clone https://gitee.com/opengauss/openGauss-OM.git -b 5.0.0


做完以上准备工作，使用Dockerfile打镜像
docker build -t opengauss:5.0.0 .


