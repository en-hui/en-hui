## 单选题
### SpringCloud
- 1、以下选项中属于微服务相关内容的是？  
  A、Bean   B、Eureka   C、oop   D、MyBatis
- 2、下面那个不属于微服务技术栈？  
  A、服务注册与发现：Eureka、Nacos   B、Servlet/JSP   C、服务熔断：Hystrix   D、服务负载均衡：Ribbon
- 3、以下选项中关于注册中心说法正确的是？  
  A、注册中心可以对微服务系统进行服务治理   B、Gateway可以作为注册中心使用   C、注册中心只能进行服务的注册，服务之间无法相互调用   D、注册中心应用于单体架构项目
- 4、下列关于服务熔断说法正确的是？  
  A、Ribbon可以进行服务熔断和降级   B、服务熔断是一种技术的手段，避免在单体项目中出现方法之间无法调用的问题。   C、服务熔断是指，当下游服务因访问压力过大而响应变慢或失败，上游服务为了保护系统整体的可用性，可以暂时切断对下游服务的调用。   D、注册中心可以实现服务熔断的效果，因为注册中心可以管理微服务中的所有具体服务。
- 5、下列关于网关的说法正确的是？  
  A、我们可以通过网关来实现微服务系统中的服务之间的调用   B、Sentinel是实现网关的组件   C、服务网关 = 路由转发+过滤器   D、服务网关可以指定用户调用具体的业务功能
- 6、以下哪个不是分布式事务的解决方案？  
  A、Seata组件   B、TCC模式   C、二阶段提交协议   D、Ribbon
- 7、以下组件中哪个注册中心组件？  
  A、Seata   B、Hystrix   C、Sentinel   D、Nacos
- 8、Nacos和Eureka的区别以下说法不正确的是？  
  A、Nacos和Eureka都是服务注册中心   B、Nacos和Eureka都提供了注册中心和配置中心功能   C、Nacos包含了Eureka所有功能   D、Nacos和Eureka都提供了可视化页面用于观察服务注册情况
- 9、Eureka和Zookeeper区别，下面说法错误的是？  
  A、Zookeeper在选举期间注册服务瘫痪，虽然服务最终会会恢复，但选举期间可以用   B、Zookeeper采用过半存活原则，Eureka采用自我保护机制解决分区问题   C、Eureka的搭建一般是基于SpringBoot构建的   D、Zookeeper的搭建方式一般是直接使用下载好的内容基于zkServer启动
- 10、服务发现指的是  
  A、服务提供方要注册通告服务地址，服务的调用方要能发现目标服务的过程   B、服务枚举的过程   C、服务定义的过程   D、以上都不对
- 11、以下那一条不是微服务框架封装的关注点  
  A、服务注册、发现、负载均衡和健康检查   B、响应JSON的格式   C、限流和容错   D、负载均衡
- 12、以下选项中那个属于Sentinel的流控模式  
  A、系统调用   B、连接模式   C、链路   D、熔断
- 13、以下选项中那个属于Sentinel的功能  
  A、服务调用   B、节点选择   C、服务管理   D、热点规则
- 14、以下选项中那个属于Sentinel的熔断功能  
  A、异常比例   B、直接流控   C、断路器   D、并发线程数
- 15、下列选项中那个属于Seata的事务模式  
  A、3PC   B、AT   C、2PC   D、oop
### Elasticsearch
- 1、以下关于Elasticsearch描述正确的是（   ）  
  A、ES是一种性能强大的关系型数据库   B、ES是一种基于Java语言开发的分布式数据库   C、ES是一个高性能的搜索引擎，但并不适用于聚合搜索   D、ES支持事务
- 2、下列选项中哪个不是Elasticsearch支持的查询类型？（   ）  
  A、term   B、match   C、equal   D、fuzzy
- 3、以下查询中，不属于聚合查询函数的是（   ）  
  A、term   B、avg   C、cardinality   D、histogram
- 4、在ES中组成Analysis的不包括（   ）  
  A、Token Filter   B、Phrase Filter   C、Character Filter   D、Normalizers
- 5、Elasticsearch的索引生命周期中不包括那个阶段？（   ）  
  A、Cold   B、Warm   C、Freeze   D、Delete   E、Hot
- 6、对于深度分页问题，下列描述正确的是  
  A、可以通过增加主分片数量来提高分页查询性能   B、适当增大max_result_window参数的阈值，可以使分页查询性能更高   C、总数据恒定，单个主分片越大，性能越差   D、应尽量避免使用深度分页
- 7、ES分布式系统使用的是以下那种模式（   ）  
  A、主从模式   B、无主模式   C、分布式哈希   D、以上都不是
- 8、以下那种节点角色不可以通过修改配置来关闭（   ）  
  A、remote_cluster_client node   B、coordinating node   C、data node   D、ingest node
### Redis
- 1、对Redis描述正确的是（）  
  A、关系型数据库   B、Web容器   C、测试工具   D、是一个高性能的 key-value 数据库
- 2、Redis的持久化机制中，描述正确的是（）  
  A、提供了RDB和AOF两种持久化机制   B、Redis不支持持久化   C、Redis提供的五种持久化机制   D、Redis提供了关系表结构
- 3、下列设置Redis密码的方式正确的是（）  
  A、config set requirepass 123456   B、set requirepass 123456   C、hset requirepass 123456   D、add requirepass 123456
- 4、下列关于Redis描述正确的是（）  
  A、Redis 支持Redisson、Jedis、lettuce 等等，官方推荐使用 Redisson。   B、Redis底层用Java代码实现   C、Redis性能和MySQL并驾齐驱   D、Redis提供了12种存储结构
- 5、Redis开启事务的命令为（）  
  A、multi   B、exec   C、discard   D、watch
- 6、Redis取消事务的命令为（）  
  A、multi   B、exec   C、discard   D、watch
- 7、Redis单机版默认有多少个库（）  
  A、16   B、32   C、8   D、4
- 8、Redis什么命令可以查看存储了多少个key（）  
  A、all   B、keys   C、select   D、findAll
- 9、下列关于Redis说法正确的是（）  
  A、使用Redis可以舍弃MySQL   B、Redis支持SQL语句   C、Redis针对很多语言提供了客户端API   D、Redis提供了3种持久化机制
- 10、下列关于Redis说法错误的是（）  
  A、Redis5.x版本单线程处理客户端请求   B、Redis采用了多路复用IO的模型   C、Redis查询的时间复杂度是On   D、Redis支持事务
- 11、下列关于Redis说法错误的是（）  
  A、Redis 是完全开源免费的，遵守 BSD 协议，是一个高性能的 key-value 数据库。   B、Redis 支持数据的持久化，可以将内存中的数据保存在磁盘中   C、Redis 不仅仅支持简单的 key-value 类型的数据，同时还提供 list，set，zset，hash 等数据结构的存储。   D、Redis 不支持数据的备份
- 12、下列属于Redis中Hash结构添加的命令为（）  
  A、set   B、zadd   C、hset   D、add
- 13、下列属于Redis中Set结构命令的为（）  
  A、scard   B、lset   C、zrange   D、type
- 14、下列哪个Redis命令以秒为单位，返回给定 key 的剩余生存时间（）  
  A、ttl   B、pttl   C、sttl   D、secondttl
- 15、下列哪个Redis命令可以返回哈希表中给定域的值（）  
  A、hget   B、get   C、hgetAll   D、hashget
### RabbitMQ
- 1、下列关于RabbitMQ说法错误的是（）  
  A、采用AMQP 高级消息队列协议的一种消息队列技术   B、RabbitMQ基于Java实现   C、RabbitMQ实现了服务之间的高度解耦   D、RabbitMQ最大的特点就是消费并不需
  要确保提供方存在
- 2、下列关于RabbitMQ说法错误的是（）  
  A、RabbitMQ应用于服务间异步通信   B、RabbitMQ可以实现顺序消费   C、RabbitMQ无法实现定时任务   D、RabbitMQ应用于请求削峰
- 3、RabbitMQ基于哪种操作可以实现队列消息的持久化（）  
  A、Confirms机制   B、Return机制   C、手动Ack   D、设置消息的deliveryMode
- 4、下列RabbitMQ说法正确的是（）  
  A、RabbitMQ中的Topic模式可以实现#和*的绑定机制   B、Topic模式的#代表占位符   C、Topic模式的*代表通配符   D、RabbitMQ基于消费者组避免重复消费
- 5、下列RabbitMQ说法正确的是（）  
  A、RabbitMQ可以提升系统的稳定性   B、RabbitMQ会导致系统中服务之间的耦合性过高   C、RabbitMQ基于Erlang语言编写   D、RabbitMQ基于无法保证消息的顺序消费
- 6、下列关于RabbitMQ说法正确的是（）  
  A、消费者直接绑定Exchange消费消息   B、生产者直接发送消息到Queue   C、Queue可以设置消息的生存时间   D、默认RabbitMQ提供了5种Queue
- 7、下列属于RabbitMQ优点的是（）  
  A、降低服务之间的耦合   B、一致性问题   C、系统可用性降低   D、系统复杂性提高
- 8、下列关于RabbitMQ说法正确的是（）  
  A、死信队列可以实现延迟任务   B、direct交换机可以基于header路由   C、rpc模式大大的提升了服务之间的访问效率   D、RabbitMQ提升了系统的稳定性
### SpringBoot
- 1、SpringBoot的核心注解@SpringBootApplication以下不是其包含的注解是  
  A、Configuration   B、CompontScan   C、EnableAutoConfiguration   D、Controller
- 2、关于SpringBoot说法错误的是  
  A、SpringBoot可以独立运行   B、SpringBoot简化了配置   C、SpringBoot默认开启了Actuator   D、SpringBoot实现了自动配置
- 3、以下是错误的配置不同环境的写法的是  
  A、application-dev.properties   B、test-application.properties   C、application-pro.properties   D、application-prod.properties
- 4、关于@Import注解的说法错误的是  
  A、@Import注解是替换了之前的import标签   B、@Import可以导入配置和把某个类型的对象注入到容器中   C、@Import注解也可以动态的注入   D、@Import注解动态注入只有一种方式就是实现了ImportSelector接口
- 5、SpringBoot是哪一年发布的  
  A、2010   B、2014   C、2016   D、2017
- 6、以下哪个不是SpringBoot默认支持自动装配的  
  A、spring-boot-starter-web   B、spring-boot-starter-data-redis   C、spring-boot-starter-security   D、mybatis-spring-boot-starter
- 7、下面关于SpringBoot启动说明错误的是  
  A、SpringBoot项目启动就会加载bootstrap.properties文件   B、SpringBoot项目启动会加载所有的在spring.factories中配置的监听器   C、SpringBoot项目启动的时候会发布相关事件，从而会触发对应的监听器来完成对应的操作   D、SpringBoot项目启动本质上就是Spring的初始化操作
### nginx
- 1、nginx的主配置文件是什么？  
  A、nginx.conf   B、httpd.conf   C、http.conf   D、vhost.conf
- 2、nginx是一种（）中间件？  
  A、消息队列   B、应用服务器   C、数据库   D、缓存
- 3、源码安装完nginx，那么启动nginx的命令为？  
  A、usr/local/nginx/sbin/nginx   B、servicehttpdstart   C、usr/local/apache2/bin/apachect   D、service nginx start
- 4、nginx的优化是哪些？  
  A、隐藏版本信息   B、更改nginx服务的默认用户   C、降权启动nginx   D、以上全对
- 5、nginx可以通过（）来将请求扔给某些语言或框架处理（例如php，python，perl）  
  A、java   B、c#   C、FastCGI   D、C++
### 分布式锁
- 1、以下哪个不是分布式锁的实现方式（）  
  A、使用数据库乐观锁实现   B、使用数据库悲观锁实现   C、使用redis的setnx()、expire()方法，用于分布式锁   D、基于Zookeeper实现分布式锁
- 2、下列关于分布式锁的实际原则的描述中错误的是？  
  A、互斥性，同一时间只有一个线程有锁   B、容错性，即使某一个持有锁的线程异常退出，其他线程课获得锁   C、隔离性，线程只能解自己的锁，不能解其他线程的锁   D、以上的都不正确
- 3、如果获取到分布式锁，服务没执行完挂了，如何应对？  
  A、给锁设置过期时间，防止死锁   B、不予处理   C、下次启动删除锁   D、给服务做集群
### 分布式事物
- 1、下列关于分布式事务管理说法不正确的是（）  
  A、分布式事务映射是指对于一个分布式事务，把分布式事务映射到对应的各个分布式结点上，进而保证分布式事务的正确调度执行   B、分布式事务恢复是指分布式事务执行中出现异常，对每个节点事务进行回滚，保证全局事务的原子性   C、分布式事务是指事务的参与者、支持事务的服务器、资源服务器以及事务管理器分别位于不同的分布式系统的不同节点之上   D、分布式事务管理能解决事务并发控制事物及系统资源的有效利用等问题，但不能保证事务的正确执行及执行结果的有效性
- 2、不属于分布式事物解决方案的是？  
  A、2pc   B、3pc   C、事物消息   D、锁
- 3、TCC的含义不正确的是？  
  A、Try   B、Confirm   C、Cancel   D、Create
- 4、下列哪项描述不是分布式系统的特性  
  A、透明性   B、开放性   C、易用性   D、可扩展性
### Linux基础应用
- 1、Linux操作系统中“~”表示什么含义？  
  A、用户家目录   B、普通目录   C、表示匹配   D、表示文件路径
- 2、Linux操作系统中切换目录的命令为？  
  A、chmod   B、ls   C、cd   D、chown
- 3、Linux操作系中一个文件权限为755，请使用字符表示。  
  A、-rwxrwxrwx   B、-r-xrwx-r-x   C、-rwxr-xr-x   D、r--wr-x-wx
- 4、CentOS操作系统中以下哪个命令用于查看一个文件来自哪个软件包？  
  A、rpm -ql filename   B、rpm -qf filename   C、yum list | grep filename   D、rpm -qi filename
- 5、CentOS操作系统中查找一个未安装的命令属于哪一个软件包的方法是？  
  A、yum list   B、yum makecache   C、yum provides commandname   D、yum -y install commandname
- 6、Linux操作系统中如何查看路由表信息？  
  A、ip address   B、ifconfig -a   C、route -n   D、以上都不是
- 7、Linux操作系统中ssh服务端软件包是什么？  
  A、openssh   B、openssh-clients   C、sshd   D、openssh-server
- 8、Linux操作系统中SCP命令使用的协议及端口为？  
  A、TCP/22   B、TCP/21   C、UDP/22   D、UDP/21
- 9、Linux操作系统中FTP服务端软件为？  
  A、ftp   B、lftp   C、sftp   D、vsftpd
- 10、公司内部web应用集群需要有一台文件共享服务器做后台存储，哪种文件共享方式可行？  
  A、ftp文件共享   B、nfs文件共享   C、samba文件共享   D、以上都可以
- 11、DNS域名管理服务用于为客户端提供域名解析的协议和端口是哪个？  
  A、TCP/53   B、UDP/53   C、TCP/35   D、UDP/23
- 12、Linux操作系统中，用于查看文件系统使用情况的命令为？  
  A、df -Th   B、du -h   C、free -m   D、lsblk
- 13、Linux操作系统中，用于挂载远程主机文件系统到本地主机的命令为?  
  A、mount   B、mnt   C、showmount   D、ab
- 14、Linux操作系统中，在shell中定义变量的方法正确的是？  
  A、A1= 123   B、A1 =123   C、1A=123   D、A1=123
- 15、Linux操作系统中，test命令别名说法正确的是？  
  A、[   B、]   C、[]   D、以上均不正确
- 16、Linux操作系统中用于查看目录层级存储关系的命令为?  
  A、tree   B、ps   C、pstree   D、dd   
## 多选题
### SpringCloud
- 1、以下组件中可以作为微服务注册中心的是？  
  A、Nacos   B、Dubbo   C、Eureka   D、Hystrix
- 2、以下那些组件可以作为微服务流控组件？  
  A、Eureka   B、GateWay   C、Hystrix   D、Sentinel
- 3、以下说法中正确的是？  
  A、微服务是单体应用   B、SpringCloudAlibaba是微服务的一站式解决方案   C、微服务就是把业务拆分成多个小服务来统一为用户提供服务   D、网关不属于微服务的组件
- 4、以下哪些分布式事务模式属于Seata  
  A、AT   B、XA   C、TCC   D、Saga
- 5、以下不属于Hystrix的隔离机制的是  
  A、熔断限流   B、路由转发   C、信号量   D、线程池
- 6、属于Sentinel的功能有？  
  A、实时监控   B、熔断规则   C、限流规则   D、转发规则
- 7、以下关于Nacos的说法错误的是  
  A、Nacos是服务注册中心   B、Nacos不提供服务配置中心   C、Nacos无法做集群部署   D、Nacos可以实现客户端的负载均衡
- 8、以下说法正确的是  
  A、Seata提供了分布式事务的一站式解决方案   B、GateWay能够实现路由和转发的功能   C、在微服务中可以不使用注册中心   D、Nacos和Eureka都提供了注册中心功能和配置中心功能
- 9、关于Nacos集群部署一下说法正确的是？  
  A、Nacos不可以使用集群模式   B、Nacos集群部署需要使用Nginx作为负载均衡转发访问   C、Nacos集群部署只能使用内部数据源   D、Nacos默认启动方式为集群模式
- 10、以下选项中属于Sentinel流控效果的是？  
  A、阈值   B、快速失败   C、预热   D、排队等待
- 11、以下关于Nacos相关概念说法正确的是？  
  A、Nacos作为服务注册中心使用，不可以部署集群   B、Nacos命名空间可以对租户进行更加细粒度的隔离   C、注册到Nacos中的服务都是一个个具体的实例   D、Nacos可以对注册的服务进行健康检查
- 12、以下说法正确的是？  
  A、服务对注册中心发起请求，服务中心接收到请求以后，把服务相关信息进行记录和保存   B、服务的提供者是提供服务的一方，服务的消费者是调用对应服务的一方   C、Nacos注册中心可以直接实现负载均衡   D、微服务架构可以不使用注册中心作
- 13、关于Seata以下说法正确的是？  
  A、Seata提供了分布式事务的一站式解决方案   B、Seata提供了服务注册的功能   C、Seata默认模式为XA   D、Seata的AT模式底层为2PC协议
- 14、以下关于Sentinel的说法错误的是？  
  A、Sentinel承接了阿里巴巴进10年的双十一大促流量核心场景   B、Sentinel可以实现秒杀的场景下控制系统容量在可以承受的范围内   C、Sentinel对代码的侵入性很大   D、Sentinel不能整合进到微服务中，属于单独的一个组件进行使用
- 15、以下关于网关的说法正确的是？  
  A、服务网关相关的组件有Zuul和Gateway   B、网关只能实现路由   C、服务网关 = 路由转发+过滤器   D、服务网关不可以通过服务中心进行整合，因为它凌驾于注册中心之上
### Elasticsearch
- 1、以下哪些是Elasticsearch支持的数据类型（   ）  
  A、keyword   B、nested   C、object   D、text   E、string   F、join
- 2、boolean query支持的子查询有哪几种？  
  A、must   B、filter   C、should   D、must_not
- 3、以下哪些是Elasticsearch支持的节点角色（   ）  
  A、master   B、data   C、ingest   D、ml
- 4、下列属于Ingest pipelines中的Processor的是（   ）  
  A、Terms   B、Set   C、Foreach   D、Trim
- 5、ES的健康值状态包括（   ）  
  A、Red   B、Green   C、Blue   D、Yellow
- 6、在ES中multi_match query支持的查询类型包括（   ）  
  A、best_fields   B、phrase   C、most_fields   D、deep_fields
- 7、在ES中，对文档的操作有哪几种类型？（   ）  
  A、Index   B、Create   C、Update   D、Delete
- 8、以下哪些配置项会触发bootstrap checks？（   ）  
  A、discovery.seed_hosts   B、network.host   C、cluster.name   D、node.name
### Redis
- 1、以下属于Redis的存储结构的是（）  
  A、String   B、Hash   C、List   D、Set   E、SortedSet
- 2、Jedis操作Redis时，下列说法正确的是（）  
  A、Jedis需要设置驱动，连接，编写SQL   B、Jedis连接只需要设置host，port   C、Jedis方法和命令一致   D、Jedis提供了管道操作，批量执行效果更佳
- 3、下列关于Redis描述正确的是（）  
  A、Redis是关系型数据库   B、Redis可以设置连接密码   C、Redis支持持久化   D、Redis支持主从和集群架构
- 4、下列关于Redis描述错误的是（）  
  A、Redis针对不同类型结构提供了不同的命令   B、Redis以性能著称   C、Redis提供了丰富的查询条件   D、Redis可以存储海量数据
- 5、下列关于Redis优势说法正确的是（）  
  A、速度快，因为数据存在内存中，类似于HashMap，HashMap 的优势就是查找和操作的时间复杂度都是 O1)   B、支持丰富数据类型，支持 string，list，set，Zset，hash 等   C、丰富的特性：可用于缓存，消息，按 key 设置过期时间   D、Redis提供了丰富的查询条件，可以基于and连接
- 6、下列关于Redis说法正确的是（）  
  A、Redis 提供两种持久化机制 RDB 和 AOF 机制   B、AOF机制默认开启   C、RDB机制默认开   D、aof 持久化可以配置 appendfsync 属性，有 always，每进行一次命令操作就记录到 aof 文件中一次
- 7、下列关于Redis描述正确的是（）  
  A、get命令是针对hash结构的获取数据   B、hget是针对String结构的操作   C、Zset结构是key，score，member的存储方式   D、Zset可以基于分数范围获取数据
- 8、下列描述Redis淘汰策略正确的是（）  
  A、volatile-lru：从已设置过期时间的数据集（server.db[i].expires）中挑选最近最
  少使用的数据淘汰   B、volatile-ttl：从已设置过期时间的数据集（server.db[i].expires）中挑选将要过
  期的数据淘汰   C、no-enviction（驱逐）：禁止驱逐数据   D、allkeys-random：从数据集（server.db[i].dict）中任意选择数据淘汰
- 9、下列关于Redis集群特点描述错误的是（）  
  A、Redis集群默认有16个库   B、Redis集群默认有16384个Hash槽   C、Redis集群默认有1024个Hash槽   D、Redis集群只有一个数据库
- 10、下列关于Redis说法正确的是（）  
  A、Redis可以基于布隆过滤器来解决缓存穿透问题   B、Java中可以基于@Cache注解同步数据到Redis   C、Redis处理效率极快   D、Redis提供了bitmap位图结构存储数据
- 11、下列关于Redis说法正确的是（）  
  A、Redis性能极高   B、Redis丰富的数据类型   C、Redis 的所有操作都是原子性的   D、Redis 还支持 publish/subscribe   E、Redis支持key 过期
- 12、下列属于key生存时间设置的命令为（）  
  A、expire   B、pexpire   C、pexpireat   D、time
- 13、下列不属于Redis中Zset结构的命令为（）  
  A、sinter   B、spop   C、ltrim   D、rpop
- 14、下列哪些命令属于Redis的事务操作  
  A、multi   B、commit   C、exec   D、rollback
- 15、下列哪个Redis命令属于对库操作的为（）  
  A、select   B、dbflush   C、dbdelete   D、dbinsert
### RabbitMQ
- 1、以下属于RabbitMQ中组件的是（）  
  A、Exchange   B、Queue   C、Channel   D、Group
- 2、关于RabbitMQ说法正确的为（）  
  A、可以实现服务之间的异步通讯   B、可以实现请求的削峰   C、RabbitMQ吞吐量相比Kafka更高   D、RabbitMQ可以基于SQL搜索数据
- 3、下列属于RabbitMQ通讯方式的是（）  
  A、Headers   B、Direct   C、Fanout   D、Topic
- 4、下列不属于Java操作RabbitMQ时核心类的是（）  
  A、Driver   B、Connection   C、Title   D、Channel
- 5、RabbitMQ哪种模式需要依赖RoutingKey路由消息到Queue  
  A、Fanout   B、Direct   C、Topic   D、HelloWord
- 6、下列关于RabbitMQ说法正确的是（）  
  A、将信道设置成 confirm 模式，则所有在信道上发布的消息都会被指派一个唯一的 ID。   B、一旦消息被投递到目的队列后，或者消息被写入磁盘后，信道会发送一个确认给生产者   C、如果 RabbitMQ 发生内部错误从而导致消息丢失，会发送一条 nack消息。   D、如果 RabbitMQ 发生内部错误从而导致消息丢失，会发送一条 ack消息。
- 7、下列属于RabbitMQ缺点的是（）  
  A、降低服务之间的耦合   B、请求削峰   C、系统可用性降低   D、系统复杂性提高
- 8、下列关于RabbitMQ说法正确的是（）  
  A、RabbitMQ可以保证消息的顺序消费   B、RabbitMQ提供了镜像集群实现高可用   C、RabbitMQ底层基于Erlang编写，效率很高   D、RabbitMQ可以与Spring整合
### SpringBoot
- 1、@SpringBootApplication注解是一个组合组件，下面是属于它的有  
  A、Configuration   B、Controller   C、EnableAutoConfiguration   D、CompontScan
- 2、SpringBoot项目的运行方式有  
  A、直接执行main方法   B、用Maven/Gradle插件运行   C、打成jar包，通过java -jar 命令运行   D、打成war包，扔Web容器中，比如Tomcat
### nginx
- 1、在nginx中，哪些是负载均衡策略  
  A、fair   B、指定权重   C、ip_hash   D、url_hash
- 2、nginx主要支持的功能有哪几种？  
  A、静态http服务器   B、反向代理服务器   C、负载均衡   D、虚拟主机   E、FastCGI
- 3、nginx是一个http服务器，可以将服务器上的静态文件（），通过http协议展现给客户端  
  A、图片   B、html   C、java   D、Python
- 4、以下什么功能基于nginx反向代理实现（）？  
  A、负载均衡   B、java   C、python   D、虚拟主机
### 分布式锁
- 1、可以实现分布式锁的组件有哪些?  
  A、mysql   B、redis   C、zookeeper   D、etcd
- 2、redis实现分布式锁需要注意哪些？  
  A、用setnx代替set   B、考虑key的过期时间   C、设置key和key的过期时间是原子操作   D、最后要delete key
- 3、用mysql实现分布式锁，哪些说法是正确的？  
  A、可以利用主键的互斥性实现锁的互斥性   B、可以利用唯一索引的唯一性，来实现锁的互斥性   C、获取锁，还要考虑释放锁，   D、以上都不正确
### 分布式事物
- 1、TCC的含义正确的是？  
  A、Try   B、Confirm   C、Cancel   D、Create
- 2、属于分布式事物解决方案的是？  
  A、2pc   B、3pc   C、事物消息   D、锁
- 3、CAP的解释正确的是？  
  A、一致性   B、可用性   C、分区容错性   D、扩展性
- 4、针对分布式事务，要求提供参与者状态的协议？  
  A、一次封锁协议   B、两段锁协议   C、两阶段提交协议   D、三阶段提交协议
### Linux基础应用
- 1、Linux操作系统中查看文件全部内容的方法有哪些？  
  A、cat   B、less   C、more   D、tail   E、head
- 2、Linux操作系统中，运维“三剑客”是指？  
  A、grep   B、sed   C、awk   D、sort
- 3、Linux操作系统中，shell脚本中的函数定义方法正确的是？  
  A、funcation 函数名() {...}   B、函数名() {...}   C、函数名() fun {...}   D、函数名{...} ()
- 4、源码安装软件包后，使用时提示：xxx command not found，可能的原因是？  
  A、没有安装成功   B、命令输入错误   C、环境变量有问题   D、配置文件错误
- 5、使用ssh工具连接远程主机使用的命令为？  
  A、ssh username@hostip   B、ssh -p port username@hostip   C、ssh -P port username@hostip   D、ssh -L port username@hostip
- 6、Linux操作系统常用的协议分析工具有哪些？  
  A、ping   B、tcpdump   C、wireshark   D、traceroute
- 7、Linux操作系统中设置开机自动挂载硬盘设备的文件为？  
  A、/etc/fstab   B、/etc/rc.d/rc.local   C、/mnt   D、/proc
- 8、关于grep命令使用说法正确的是？  
  A、可以同时搜索多个文件   B、使用正则表达式时，需要配合-E选项   C、搜索多个文件时，文件间使用逗号隔开   D、以上说法均不正确
- 9、使用sed删除文件中的空行和以#号开头的行，以下正确的是？  
  A、sed '/^$|^#/d' 123.txt   B、sed '/^$/d;/^#/d' 123.txt   C、sed -r '/^$|^#/d' 123.txt   D、sed -e '/^$/d -e /^#/d' 123.txt
- 10、使用awk打印出passwd文件中包含root关键字所在行的用户名，以下正确的是？  
  A、awk '/root/{print $1}' passwd   B、awk -F: '/root/{print $1}' passwd   C、awk '{print $1}' passwd   D、awk 'BEGIN{FS=":"}/root/{print $1}' passwd
- 11、CentOS操作系统中用于查看某软件或命令是否安装的命令为？  
  A、rpm -qa | grep softwarename   B、rpm -qi   C、yum provides *bin/commandname   D、yum list | grep commandname   
## 判断题
### SpringCloud
- 1、微服务中，每个服务都可以使用不同的编程语言  
  A、对   B、错
- 2、微服务中，每个服务都可以独立部署  
  A、对   B、错
- 3、微服务必须部署在Docker中  
  A、对   B、错
- 4、在实际使用是 Eureka Server至少部署一台服务器，实现高可用  
  A、对   B、错
- 5、服务注册和发现是微服务的核心概念之一  
  A、对   B、错
- 6、Nacos提供服务端与客户端，服务端是注册中心，客户端完成完成服务的注册过程  
  A、对   B、错
- 7、Spring Cloud Netflix中包含Sentinel组件  
  A、对   B、错
- 8、Sentinel提供对应的控制台，我们可以通过这个控制台来给对应的请求添加流控规则  
  A、对   B、错
- 9、Sentinel可以实时监控对应微服务的流量情况  
  A、对   B、错
- 10、Sentinel可以配合Nacos实现持久化  
  A、对   B、错
- 11、Seata默认模式是AT模式  
  A、对   B、错
- 12、Seata核心概念包括 TC、TM、RM  
  A、对   B、错
- 13、Nacos可以作为Seata的配置中心  
  A、对   B、错
- 14、Seata只支持DB模式  
  A、对   B、错
- 15、SeataAT模式中的undolog用户事务回滚  
  A、对   B、错
### Elasticsearch
- 1、字段值“2022-3-19 13:30:00”的数据在ES中会被自动映射为date类型（   ）  
  A、对   B、错
- 2、倒排索引底层所依赖的数据结构为：B+Trees  
  A、对   B、错
- 3、Elasticsearch支持地理位置搜索（   ）  
  A、对   B、错
- 4、Elasticsearch可以同时做到海量数据查询、查询结果无误差和查询结果近实时（   ）  
  A、对   B、错
- 5、在Master选举的过程中，候选节点不一定可以参与选举  
  A、对   B、错
- 6、当索引创建之后发现字段类型错误，应通过_mapping API修改（   ）  
  A、对   B、错
- 7、图的深度优先算法是基于栈来完成的（   ）  
  A、对   B、错
### Redis
- 1、Redis性能极高、 支持数据的持久化、支持事务，操作都是原子性  
  A、对   B、错
- 2、Redis是一个key-value的非关系型数据库，基于内存存储亦可持久化  
  A、对   B、错
- 3、Redis可以通过auth命令实现密码校验。  
  A、对   B、错
- 4、Redis中的淘汰策略中：volatile-lru：从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰  
  A、对   B、错
- 5、Redis可以基于Zset结构实现延迟队列效果  
  A、对   B、错
- 6、Redis可以解决分布式会话的问题  
  A、对   B、错
- 7、Redis的特点是基于内存存储，高速读写的关系型数据库  
  A、对   B、错
- 8、volatile-random淘汰策略是从已设置过期时间的数据集中任意选择数据淘汰  
  A、对   B、错
- 9、Redis提供了6中内存淘汰策略  
  A、对   B、错
- 10、Redis可以实现分布式锁  
  A、对   B、错
- 11、Redis 有着更为复杂的数据结构并且提供对他们的原子性操作。  
  A、对   B、错
- 12、Redis可以基于主从+哨兵实现高可用  
  A、对   B、错
- 13、sadd命令将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。  
  A、对   B、错
- 14、zadd可以将一个或多个 member 元素及其 score 值加入到有序集 key 当中  
  A、对   B、错
- 15、getset命令将键 key 的值设为 value ， 并返回键 key 在被设置之前的旧值。  
  A、对   B、错
### RabbitMQ
- 1、RabbitMQ的手动Ack机制可以一定程度上避免消息丢失  
  A、对   B、错
- 2、生产者将消息发送到Exchange，Exchange会基于RoutingKey路由到指定的一个或多个Queue  
  A、对   B、错
- 3、RabbitMQ提供了延迟交换机插件实现定时任务  
  A、对   B、错
- 4、RabbitMQ可以基于confirms，return，deliveryMode，ack解决消息可靠性传输问题  
  A、对   B、错
- 5、RabbitMQ提供了丰富的机制实现消息的路由  
  A、对   B、错
- 6、RabbitMQ通过：消息提供方->路由->一至多个队列 的方式发送消息  
  A、对   B、错
- 7、RabbitMQ默认的用户为guest  
  A、对   B、错
### SpringBoot
- 1、SpringBoot项目是基于Spring封装的脚手架  
  A、对   B、错
- 2、SpringBoot是基于约定优于配置的理念开发的  
  A、对   B、错
- 3、SpringBoot是在Spring3.0就有的  
  A、对   B、错
- 4、SpringBoot项目支持xml,properties,yml,yaml这四种类型的属性文件  
  A、对   B、错
- 5、bootstrap.yml属性文件会在application.yml之前加载  
  A、对   B、错
- 6、我们可以用Jetty替换默认的Tomcat  
  A、对   B、错
- 7、我们也可以把一个SpringBoot打包成war包  
  A、对   B、错
- 8、SpringBoot项目需要独立的容器来运行  
  A、对   B、错
- 9、SpringBoot项目整合MyBatis的中在整合的包中解决了DefaultSqlSession的数据安全问题  
  A、对   B、错
- 10、SpringBoot的Actuator可以获取JVM相关的信息  
  A、对   B、错
- 11、SpringBoot中可以默认的Web容器  
  A、对   B、错
- 12、SpringBoot中默认支持的模板引擎是JSP  
  A、对   B、错
### nginx
- 1、nginx 是一个高性能的http和反向代理web服务器，同时也提供了IMAP/POP3/SMTP服务？  
  A、对   B、错
- 2、负载均衡作用可以保证程序更新时始终对外提供服务？  
  A、对   B、错
- 3、虚拟主机的原理是通过http请求投中的host是否匹配server_name来实现  
  A、对   B、错
- 4、nginx直接支持php语言，无需借助其他模块  
  A、对   B、错
- 5、连接高并发的情况下，nginx是apache服务不错的替代品？  
  A、对   B、错
- 6、nginx能够支持高达50000个并发连接数的响应  
  A、对   B、错
- 7、nginx配置检查命令是nginx -s reload  
  A、对   B、错
- 8、nginx是负载均衡组件？  
  A、对   B、错
- 9、nginx可以安装在windows上？  
  A、对   B、错
- 10、nginx平滑重启的命令是nginx -s reload  
  A、对   B、错
### 分布式锁
- 1、CAS可以实现分布式锁吗？  
  A、对   B、错
- 2、syschronized可以实现分布式锁吗？  
  A、对   B、错
- 3、在自己的JVM进程中，可以实现分布式锁吗？  
  A、对   B、错
- 4、用mysql可以实现分布式锁吗？  
  A、对   B、错
- 5、分布式锁，会降低系统的性能吗？  
  A、对   B、错
- 6、在秒杀系统中，很多人抢一个商品，需要锁的是对象是订单吗？  
  A、对   B、错
- 7、红锁是完美的解决方案，能真正实现分布式锁，不会有并发问题？  
  A、对   B、错
### 分布式事物
- 1、同一个服务，连接两个不同的数据库，一个请求中，涉及到两个数据库的写操作，不是分布式事物？  
  A、对   B、错
- 2、两个服务，连接同一个数据库，在一个请求中，调用了两个库进行数据看的写操作，不是分布式事物？  
  A、对   B、错
### Linux基础应用
- 1、Linux操作系统中一切皆文件  
  A、对   B、错
- 2、Linux操作系统主机安全设置主要包含：firewalld及selinux。  
  A、对   B、错
- 3、rpm命令是RedHat 软件包管理器。  
  A、对   B、错   
## 填空题
### SpringCloud
- 1、SpringCloud中有很多组件，其中（）是Netflix提供的注册中心  
  A、Eureka
- 2、SpringCloudAlibaba中用于限流的组件是（）  
  A、Sentinel
- 3、Seata提供的默认事务模式为（）  
  A、AT
- 4、Seata的资源逻辑，可以按微服务的需要，在（）对自行定义事务分组，每组取一个名字。  
  A、客户端
- 5、TCC模式，是指支持把（）的分支事务纳入到全局事务管理器中。  
  A、自定义
- 6、Sentinel的使用可以分为核心库-客户端和（）-Dashboard  
  A、控制台
- 7、Hystrix的隔离机制有线程池和（）组成。  
  A、信号量
- 8、Nacos整合了（）能够提供对客户端的负载均衡访问  
  A、Ribbon
- 9、Eureka通过（）机制完成对服务的健康状态监测  
  A、心跳监测
- 10、Nacos集群模式通过（）完成对Nacos节点调用的负载均衡  
  A、Nginx
- 11、微服务中可以通过（）来进行统一的接收用户请求  
  A、服务网关
- 12、SpringCloudAlibaba中服务的配置管理可以使用（）组件完成  
  A、Nacos
- 13、在使用SpringCloudAlibaba用于服务之间远程调用的常用组件为（）  
  A、OpenFegin
- 14、微服务需要注册到Nacos时，需要用到的注解为（）  
  A、@EnableDiscoveryClient
- 15、使用Seata开启全局事务的注解为（）  
  A、@GlobalTransactional
### Elasticsearch
- 1、Elasticsearch默认的分词器为（   ）  
  A、standard
- 2、倒排表依赖的两种压缩算法为（   ）和（   ）,  
  A、FOR/RBM   B、填Frame Of Reference和RoaringBitmap亦可   C、3   D、难
- 3、为了让符合特定条件的索引都执行同一个映射，ES做法是使用（   ）  
  A、index template   B、回答索引模板亦可   C、3   D、易
- 4、当集群发生意外导致部分节点宕机的时候，为了保证剩余节点的负载均衡，ES使用的分片策略叫（   ）  
  A、Shard Reblance   B、回答分片再平衡亦可   C、3   D、中
- 5、正排索引所依赖的数据结构是（   ）  
  A、doc values   B、填doc_values或者fielddata亦可   C、3   D、中
- 6、备份集群的唯一可靠且受支持的方法是使用（   ）  
  A、snapshot   B、填写快照亦可   C、3   D、难
- 7、ES集群中节点之间是通过什么（   ）协议进行通信的？  
  A、TCP
### Redis
- 1、Redis提供了RDB和（）的持久化方案。  
  A、AOF
- 2、Redis可以通过（）命令设置key的生存时间  
  A、expire
- 3、Redis 集群没有使用一致性 hash,而是引入了哈希槽的概念，Redis 集群有（） 个哈希槽  
  A、16384
- 4、Redis基于（）实现查询key剩余生存时间（返回单位：秒）  
  A、ttl
- 5、一个字符串类型的值能存储最大容量是（）M  
  A、512
- 6、（）持久化机制只有一个文件 dump.rdb，方便持久化。  
  A、RDB
- 7、Redis是基于（）结构存储的非关系型数据库  
  A、key-value
- 8、Redis的（）可以将多次网络 IO 往返的时间缩减为一次  
  A、pipeline
- 9、（）淘汰策略是从数据集中挑选最近最多使用的数据淘汰  
  A、allkeys-lru
- 10、Redis集群无法做到选择数据库，默认只有（）个数据库  
  A、1
- 11、（）持久化机制是间隔一段时间进行持久化，如果持久化之间 redis 发生故障，会发生数据丢失。所以这种方式更适合数据要求不严谨的时候  
  A、RDB
- 12、Redis的（）结构可以存储key-score-member  
  A、zset
- 13、（）命令返回hash结构 key 中，所有的域和值。  
  A、hgetall
- 14、（）命令可以返回哈希表 key 中域的数量  
  A、hlen
- 15、（）命令可以时将多个 field-value (域-值)对设置到哈希表 key 中。  
  A、hmset
### RabbitMQ
- 1、将信道设置成 （）模式，则所有在信道上发布的消息都会被指派一个唯一的 ID。  
  A、confirm
- 2、RabbitMQ可以基于（）队列实现定时任务  
  A、死信
- 3、SpringBoot默认整合RabbitMQ，基于（）类即可实现消费发送  
  A、RabbitTemplate
- 4、RabbitMQ需要开启（）机制才可以实现消息流控  
  A、手动ack
- 5、RabbitMQ可以基于的（）通讯模式可以采用字符串的方式绑定Exchange和Queue  
  A、Direct
- 6、RabbitMQ常用的交换机有fanout，（），topic，headers  
  A、direct
- 7、RabbitMQ服务可以通过构建多个（）隔离业务  
  A、virtual host
### SpringBoot
- 1、在SpringBoot的启动类上我们添加的注解是：()  
  A、@SpringBootApplication
- 2、SpringBoot中用于监控的组件是  
  A、Actuator
- 3、SpringBoot项目中默认静态资源文件放置在  
  A、resources/static
- 4、SpringBoot项目中默认的模板文件放置在  
  A、resources/template
- 5、SpringBoot项目中的自动装配的配置类是配置在  
  A、spring.factories
- 6、SpringBoot中自动装配的核心注解是  
  A、@EnableAutoConfiguration
- 7、SpringBoot默认的Web容器是  
  A、Tomcat
- 8、SpringBoot是在（）年发布1.0版本  
  A、2014
- 9、SpringBoot在在（）年发布是2.0版本  
  A、2018
### nginx
- 1、nginx实现HTTP是在（）层和TCP是在（）层？  
  A、七层，4层
- 2、Nginx中的模块分类及常见核心模块有哪些（）？  
  A、Standard HTTP modules，Optional HTTP modules，Mail modules，Stream modules，3rd party modules
- 3、Nginx负载均衡中常见的算法及原理有（）？  
  A、轮询，加权轮询，ip_hash，least_conn，fail，url_hash
- 4、Nginx负载均衡中常用的server配置参数有哪些（）？  
  A、upstream，proxy_pass，weight，least_conn，fair，url_hash
- 5、Nginx负载均衡中通过（）实现对节点的健康状态检查？  
  A、max_fails，fail_timeout
- 6、Nginx如何实现TCP四层转发，用到的模块是（）？  
  A、stream模块
- 7、Apache实现Tomcat负载均衡的方式主要是配置（）？  
  A、proxy
- 8、描述Nginx的特点是（）？  
  A、内存资源占用少，处理静态网页效率高，一般是企业中间件反向代理的首选，异步处理扛高并发请求。
- 9、描述WEB服务器中的select模型的特点是（）？  
  A、单个进程可以同时处理多个io请求，但是连接上这些请求，其他所有请求就被阻塞了，然后内核就会检查select负责的fd，之后再把它负责到缓冲区。
- 10、Nginx服务器启动脚本的编写思路是（）？  
  A、首先定义pid和定义日志路径，之后创建日志文件，再判断nginx pid文件是否存在，也就是判断nginx关闭还是打开，是打开就mv把日志移动到指定位置，然后再USR1新生成一个日志文件
- 11、worker_processes表示的是（）  
  A、nginx工作进程数量
- 12、worker_cpu_affinity表示的是（）  
  A、cpu个数
- 13、worker_rlimit_nofile表示的是（）  
  A、一个nginx工作进程数量打开文件数量限制
- 14、worker_connections表示的是（）  
  A、每个进程连接最大数量
- 15、客户端想去一个不能去的网站，从而把请求交给别的代理服务器，然后代理服务器帮用户取回来内容。这个叫（）  
  A、正向代理
- 16、用户发送一个请求，然后到了某台服务器，这台服务器、把用户的请求转发给真正用户要去的服务器，这个叫（）  
  A、反向代理
### 分布式锁
- 1、Zookeeper实现分布式锁的原理的关键节点是（）  
  A、顺序节点，临时节点
- 2、分布式锁的实现组件有（）  
  A、数据库、redis，zookeeper
### 分布式事物
- 1、分布式事物是（）？  
  A、将一次大的操作分为很多小的操作，这些小的操作位于各自的服务器上，分布式事物需要保证这些小的操作要么全部成功，要么全部失败。
- 2、分布式事物的解决方案有哪些（）？  
  A、2PC，3PC，TCC，事物消息
- 3、CAP中的C是指（）  
  A、Consistency：一致性
- 4、CAP中的A是指（）  
  A、Availability：可用性
- 5、CAP中的P是指（）  
  A、Partition tolerance：分区容错性
### Linux
- 1、Linux操作系统中一切皆（）  
  A、文件   