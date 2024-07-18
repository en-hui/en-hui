#!/bin/bash
while :
do
echo -n -e  '
       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
       #    （如果没有安装docker请先执行步骤1） #
       #                                        #
       #    1  安装docker（已安装请忽略）       #
       #    2  安装nacos           lasted       #
       #    3  安装mysql           8.0          #
       #    4  安装redis           lasted       #
       #    5  安装Skywalking      8.9.0        #
       #    6  安装xxl—job         2.3.0        #
       #    7  安装seata-server    1.4.2        #
       #    8  安装sentinel        lasted       #
       #    9  安装RocketMQ        lasted       #
       #    0  退出                             #
       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

'
  read -p "请选择编号ID: " choice;
  case $choice in
   1)
    echo -n -e "\033[33m安装dokcer中，请稍等\033[0m"

    curl -o /etc/yum.repos.d/docker-ce.repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
    yum -y install docker-ce    > /dev/null
    docker version
    sudo mkdir -p /etc/docker > /dev/null
    echo '
	{
	  "registry-mirrors": ["https://f0b8ihsz.mirror.aliyuncs.com"]
	}'  > /etc/docker/daemon.json


	systemctl daemon-reload > /dev/null
	systemctl restart docker > /dev/null
	systemctl enable docker  > /dev/null


    echo -n -e '\033[5m...安装完成，可以执行后续中间件安装了~\033[0m'


    read -p "   [enter] 继续: " Key  ;;
   2)
    echo "安装nacos中...（拉镜像可能需要一点时间，请稍等）"
    docker run -d --name nacos -p 8848:8848 -e PREFER_HOST_MODE=hostname -e MODE=standalone nacos/nacos-server
    echo -n -e '\033[5m...安装完成，可访问http://ip:8848/nacos/，端口8848，用户名/密码: nacos/nacos \033[0m'
    read -p "按 [enter] 继续: " Key;;


   3)
    echo "安装mysql中...（拉镜像可能需要一点时间，请稍等）"
    docker run -p 3306:3306 --restart always --name  mysql -m 512M -e MYSQL_ROOT_PASSWORD=123456 -d mysql:8.0
    echo -n -e '\033[5m...安装完成，端口3306，用户名/密码:root/123456\033[0m'
    read -p "按 [enter] 继续: " Key;;

   4)
    echo "安装redis中...（拉镜像可能需要一点时间，请稍等）"
    docker run -d --name redis --restart always -p 6379:6379 redis --requirepass "123456" --appendonly yes
    echo -n -e '\033[5m...安装完成 端口6379 ,密码 123456\033[0m'
    read -p "按 [enter] 继续: " Key;;

   5)
    echo "安装skywalking中...（拉镜像可能需要一点时间，请稍等）"
    docker run -d --name=es --restart=always -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.9.0
    docker run --name oap --restart=always -d -e TZ=Asia/Shanghai -p 12800:12800 -p 11800:11800 --link es:es \-e SW_STORAGE=elasticsearch7 -e SW_STORAGE_ES_CLUSTER_NODES=es:9200 apache/skywalking-oap-server:8.9.0
    docker run -d --name skywalking-ui --restart=always -e TZ=Asia/Shanghai -p 8180:8080 --link oap:oap -e SW_OAP_ADDRESS=oap:12800 apache/skywalking-ui:8.9.0
    echo -n -e '\033[5m...安装完成，端口8180\033[0m'
    read -p "按 [enter] 继续: " Key;;


   6)
    echo -e "安装xxl-job需依赖mysql, （拉镜像可能需要一点时间，请稍等），请输入mysql IP(当前服务器IP） : \c"
    read version
        yum install -y mysql wget
	docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://$version:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai --spring.datasource.username=root --spring.datasource.password=123456 --server.port=9900" -p 9900:9900 -v /tmp:/data/applogs --name xxl-job-admin  -d xuxueli/xxl-job-admin:2.3.0
    echo -n -e '\033[5m...安装完成，端口9900，账号/密码：admin/123456\033[0m'
    read -p "按 [enter] 继续: " Key;;
   7)
     echo "安装seata中...（拉镜像可能需要一点时间，请稍等）"
	docker run --name seata-server -p 8091:8091 -d seataio/seata-server:1.4.2
	echo -n -e '\033[5m...seata安装完成 端口8091\033[0m'
	read -p "按 [enter] 继续: " Key;;
   8)
     echo "安装sentinel中...（拉镜像可能需要一点时间，请稍等）"
     docker run --name sentinel-dashboard -d -p 8858:8858 docker.io/bladex/sentinel-dashboard
	echo -n -e '\033[5m...sentinel安装完成 端口:8858，用户名密码：sentinel/sentinel\033[0m'
	read -p "按 [enter] 继续: " Key;;

   9)


echo -e "请输入当前服务器IP : \c"
read mq

echo -e '
brokerClusterName = DefaultCluster
brokerName = broker-a
brokerId = 0
deleteWhen = 04
fileReservedTime = 48
brokerRole = ASYNC_MASTER
flushDiskType = ASYNC_FLUSH
brokerIP1 = 0.0.0.0
diskMaxUsedSpaceRatio=95

' > ${PWD}/broker.conf

sed -i -r "s#0.0.0.0#$mq#"  ${PWD}/broker.conf



#首先安装namesrv
docker run -d \
--restart=always \
--name rmqnamesrv \
-p 9876:9876 \
-e "MAX_POSSIBLE_HEAP=100000000" \
rocketmqinc/rocketmq \
sh mqnamesrv

#Broker配置
docker run -d  \
--restart=always \
--name rmqbroker \
--link rmqnamesrv:namesrv \
-p 10911:10911 \
-p 10909:10909 \
-v  ${PWD}/broker.conf:/opt/rocketmq-4.4.0/conf/broker.conf \
-e "NAMESRV_ADDR=namesrv:9876" \
-e "MAX_POSSIBLE_HEAP=200000000" \
rocketmqinc/rocketmq \
sh mqbroker -c /opt/rocketmq-4.4.0/conf/broker.conf

#安装Console控制台
docker run -d \
--restart=always \
--name rmqadmin \
-e "JAVA_OPTS=-Drocketmq.namesrv.addr=$mq:9876 \
-Dcom.rocketmq.sendMessageWithVIPChannel=false" \
-p 8181:8080 \
pangliang/rocketmq-console-ng

        echo -n -e '\033[5m...sentinel安装完成 请在浏览器访问 IP:8181\033[0m'
        read -p "按 [enter] 继续: " Key;;
   0)
    echo "Bye!"
    exit 0 ;;

   *)
    echo "error"
    read -p "press [enter] key to continue: " Key
    ;;
  esac
done
