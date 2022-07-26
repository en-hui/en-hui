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
