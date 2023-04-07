# kubernetes

https://www.cncf.io/projects/kubernetes/


k8s是主从架构，主节点称为master节点、从节点称为worker节点

## 集群节点组件

### Master节点组件
master节点是集群管理中心，它的组件可以在集群内任意节点运行，但是为了方便管理所以会在一台主机上运行Master所有组件，**并且不在此主机上运行用户组件**

Master组件包括
- kube-apiserver ：用于暴露kubernetes API，任何对资源请求/调用操作都是通过kube-apiserver提供的接口进行   
- kebe-controller-manager ：控制器管理器，用于对控制器进行管理，他们是集群中处理常规任务对后台线程   
- kube-scheduler ：监视新创建没有分配到Node对Pod，为Pod选择一个Node运行   
- ETCD ：是kebernetes提供默认对存储系统，保证所有集群数据   

### Worker节点组件
worker节点用于运行以及维护Pod，管理volume（CVI）和网络（CNI），维护pod及service等信息

Worker组件包括
- kubelet ：负责维护容器等生命周期（创建pod、销毁pod），同时也负责volume和网络等管理   
- kube-proxy ：
  - 通过在主机上维护网络规则并执行连接转发来实现service（iptables/ipvs）   
  - 随时与apiserver通信，把service或pod改变提交给apiserver，保存至etcd中，负责service实现，从内部pod至service和从外部node到service访问   
- Container Runtime
  - 容器运行时
  - 负责镜像管理以及pod和容器到真正运行
  - 支持docker/containerd/rkt/pouch/kata等多种运行时

### Add-ons
Add-ons使功能更丰富，但没有这些不影响实际使用，做扩展用的

- coredns/kube-dns ：负责为整个集群提供DNS服务
- Ingress Controller ： 为服务提供集群外部访问   
- Heapster/Metries-server ：提供集群资源监控（监控容器可以使用prometheus）  
- Dashboard ：提供集群GUI
- Federation ：提供跨可用区的集群   
- Fluentd-elasticsearch ：提供集群日志采集，存储与查询    


## 安装

- kubeadm
  - 文档：https://kubernetes.io/zh-cn/docs/setup/production-environment/tools/kubeadm/
- minikube
- 二进制部署方式 
- 国内第三方部署工具 
  - rke
  - kubekey
  - kubeasz : https://github.com/easzlab/kubeasz
  
### 使用 kubeadm 部署集群（1.21）
``` 
kubeadm init 初始化
kubeadm join 添加worker节点到k8s集群
kubeadm upgrade 更新k8s版本
kubeadm reset 重置k8s集群
```

#### 环境准备
查看操作系统版本：cat /etc/redhat-release    



- 设置主机名
```shell
vi /etc/hostname   或者 hostnamectl set-hostname master01
    # 设置本机主机名,不能包含下划线，重启依然生效（hostname node01直接生效，两个一起改就可以了）
    master01
```
- 修改host文件
```shell
vi /etc/hosts

    ip1 node01
    ip2 node02
```
- 关闭防火墙
```shell
systemctl stop firewalld
systemctl disable firewalld
firewall-cmd --state
```
- 关闭selinux
```shell
vi /etc/selinux/config
  SELINUX=disabled
```
- 时间同步
```shell
yum install ntp -y
vi /etc/ntp.conf
    server ntp1.aliyun.com  # 配置从阿里时间同步
service ntpd start  # 启动
chkconfig ntpd on  # 设置开机启动
```
- 配置内核转发及网桥过滤
``` 
vim /etc/sysctl.d/k8s.conf
  net.bridge.bridge-nf-call-ip6tables = 1
  net.bridge.bridge-nf-call-iptables = 1
  net.ipv4.ip_forward = 1
  vm.swappiness = 0
  
# 加载br_netfilter模块
modprobe br_netfilter

# 加载网桥过滤及内核转发配置文件
sysctl -p /etc/sysctl.d/k8s.conf
```
- 安装ipset及ipvsadm
``` 
# 安装ipset及ipvsadm
yum -y install ipset ipvsadm

# 配置ipvsadm模块加载方式 
cat > /etc/sysconfig/modules/ipvs.modules <<EOF
#!/bin/bash
modprobe -- ip_vs
modprobe -- ip_vs_rr
modprobe -- ip_vs_wrr
modprobe -- ip_vs_sh
modprobe -- nf_conntrack
EOF

# 授权、运行、检查是否加载
chmod 755 /etc/sysconfig/modules/ipvs.modules && bash /etc/sysconfig/modules/ipvs.modules && lsmod | grep -e ip_vs -e nf_conntrack
```
- 关闭SWAP分区
``` 
# 修改配置文件,注释（需要重启）
vim /etc/fstab
  #/dev/mapper/centos-swap swap swap defaults 0 0
# 命令修改，当前有效，重启后失效
swapoff -a

free -h 可以看到swap分区占用
              total        used        free      shared  buff/cache   available
Mem:            14G        1.5G        8.4G        668K        5.0G         13G
Swap:            0B          0B          0B
```

- docker准备
``` 
# 获取yum源-阿里镜像
wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo

# 查看版本
yum list docker-ce.x86_64 --showduplicates | sort -r

# 安装docker
yum -y install --setopt=obsoletes=0 docker-ce-23.0.3-1.el7 

# 开机自启
systemctl enable docker;systemctl start docker

# 修改cgroup方式
vim /etc/docker/daemon.json
{
  "exec-opts":["native.cgroupdriver=systemd"]
}

# 重启docker
systemctl restart docker
```

#### 集群部署

kubeadm(1.21.0) : 所有节点都要安装 : 初始化集群、管理集群等    
kubelet(1.21.0) : 所有节点都要安装 : 用于接收api-server命令，对pod生命周期进行管理    
kubectl(1.21.0) : 使用命令的节点   : 集群应用命令行管理工具   

- 准备yum源(阿里云YUM源)
``` 
vim /etc/yum.repos.d/k8s.repo

[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg

yum repolist 然后按y
```
- 集群软件安装
``` 
# 查看指定版本
yum list kubeadm.x86_64 --showduplicates | sort -r
yum list kubelet.x86_64 --showduplicates | sort -r
yum list kubectl.x86_64 --showduplicates | sort -r

# 安装指定版本
yum -y install --setopt=obsoletes=0 kubeadm-1.21.0-0  kubelet-1.21.0-0 kubectl-1.21.0-0
```
- 配置kublet
``` 
# 为了实现docker使用的cgroupdriver与kubelet使用的cgroup的一致性，建议修改如下文件内容。
vim /etc/sysconfig/kubelet
  KUBELET_EXTRA_ARGS="--cgroup-driver=systemd"

# 设置kubelet为开机自启动即可，由于没有生成配置文件，集群初始化后自动启动
systemctl enable kubelet
```
- 下载镜像
``` 
# 查看需要下载的镜像
# kubeadm config images list --kubernetes-version=v1.21.0
k8s.gcr.io/kube-apiserver:v1.21.0
k8s.gcr.io/kube-controller-manager:v1.21.0
k8s.gcr.io/kube-scheduler:v1.21.0
k8s.gcr.io/kube-proxy:v1.21.0
k8s.gcr.io/pause:3.4.1
k8s.gcr.io/etcd:3.4.13-0
k8s.gcr.io/coredns/coredns:v1.8.0

# 使用脚本的方式把上面这些镜像都下载下来,下载下来是一个tar包，可以在有vpn的地方下载下来，然后上传，然后使用docker load来加载:docker load < k8s-1-21-0.tar
# vim image_download.sh
#!/bin/bash
images_list='
k8s.gcr.io/kube-apiserver:v1.21.0
k8s.gcr.io/kube-controller-manager:v1.21.0
k8s.gcr.io/kube-scheduler:v1.21.0
k8s.gcr.io/kube-proxy:v1.21.0
k8s.gcr.io/pause:3.4.1
k8s.gcr.io/etcd:3.4.13-0
k8s.gcr.io/coredns/coredns:v1.8.0'

for i in $images_list
do
        docker pull $i
done

docker save -o k8s-1-21-0.tar $images_list

sh image_download.sh
```

#### 集群初始化
kubeadm init --kubernetes-version=v1.21.0 --apiserver-advertise-address=192.168.10.11