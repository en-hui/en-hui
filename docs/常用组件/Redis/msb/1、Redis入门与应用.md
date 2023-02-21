# Redis入门与应用

## Redis的技术全景

Redis一个开源的基于键值对（Key-Value）NoSQL数据库。使用ANSI C语言编写、支持网络、基于内存但支持持久化。性能优秀，并提供多种语言的API。

我们要首先理解一点，我们把Redis称为KV数据库，键值对数据库，那就可以把Redis内部的存储视为存在着一个巨大的Map，对Map的操作无非就是get和put，然后通过key操作这个key所对应的value，而这个value的类型可以多种多样，也就是Redis为我们提供的那些数据结构，比如字符串（String）、哈希(Hash)等等。

Redis就这么简单吗？这些年李老师的经历，我发现，很多技术人都有一个误区，那就是，只关注零散的技术点，没有建立起一套完整的知识框架，缺乏系统观，但是系统观其实是至关重要的。从某种程度上说，在解决问题时，拥有了系统观，就意味着你能有依据、有章法地定位和解决问题。

那么，如何高效地形成系统观呢？本质上就是，Redis 的知识都包括什么呢？简单来说，就是“两大维度，三大主线”

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/ba2269e45c1b4a77bf097ff40de3aa8e.png)

### 两大维度

两大维度：应用维度、底层原理维度

我们知道，缓存和集群是Redis 的两大广泛的应用场景。同时Redis 丰富的数据模型，就导致它有很多零碎的应用场景，很多很杂。而且，还有一些问题隐藏得比较深，只有特定的业务场景下（比如亿级访问压力场景）才会出现，所以同时还必须精通Redis的数据结构。

**Redis应用场景**

1.缓存

缓存机制几乎在所有的大型网站都有使用，合理地使用缓存不仅可以加快数据的访问速度，而且能够有效地降低后端数据源的压力。Redis提供了键值过期时间设置,并且也提供了灵活控制最大内存和内存溢出后的淘汰策略。可以这么说,一个合理的缓存设计能够为一个网站的稳定保驾护航。

一般MySQL数据库写的并发是600/s，读的2000/s,对于大型互联网项目的百万并发，根本扛不住，Redis的官方显示Redis能够单台达到10W+/s的并发。

2.排行榜系统

排行榜系统几乎存在于所有的网站，例如按照热度排名的排行榜，按照发布时间的排行榜，按照各种复杂维度计算出的排行榜，Redis提供了列表和有序集合数据结构，合理地使用这些数据结构可以很方便地构建各种排行榜系统。

3.计数器应用

计数器在网站中的作用至关重要，例如视频网站有播放数、电商网站有浏览数，为了保证数据的实时性，每一次播放和浏览都要做加1的操作，如果并发量很大对于传统关系型数据的性能是一种挑战。Redis天然支持计数功能而且计数的性能也非常好,可以说是计数器系统的重要选择。

4.社交网络

赞/踩、粉丝、共同好友/喜好、推送、下拉刷新等是社交网站的必备功能，由于社交网站访问量通常比较大,而且传统的关系型数据不太适合保存这种类型的数据，Redis提供的数据结构可以相对比较容易地实现这些功能。

5.消息队列系统

消息队列系统可以说是一个大型网站的必备基础组件，因为其具有业务解耦、非实时业务削峰等特性。Redis提供了发布订阅功能和阻塞队列的功能，虽然和专业的消息队列比还不够足够强大,但是对于一般的消息队列功能基本可以满足。这个是Redis的作者参考了Kafka做的拓展。

### 三大主线

三大主线：高性能、高可靠和高可扩展

高性能：包括线程模型、数据结构、持久化、网络框架；
高可靠：包括主从复制、哨兵机制；
高可扩：包括数据分片、负载均衡。

因为Redis的应用场景非常多，不同的公司有不同的玩法，但如何不掌握三高这条主线的话，你会遇到以下问题：

1、数据结构的复杂度、跨 CPU 核的访问会导致CPU飙升的问题

2、主从同步和 AOF 的内存竞争，这些会导致内存问题

3、在 SSD 上做快照的性能抖动，这些会导致存储持久化的问题

4、多实例时的异常网络丢包的问题

## Redis的版本选择与安装

在Redis的版本计划中，版本号第二位为奇数，为非稳定版本，如2.7、2.9、3.1；版本号第二为偶数，为稳定版本如2.6、2.8、3.0；一般来说当前奇数版本是下一个稳定版本的开发版本，如2.9是3.0的开发版本。

同时Redis的安装也非常简单，到Redis的官网（[Download | Redis](https://redis.io/download/)），下载对应的版本，简单几个命令安装即可。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/f7ff9554827540b8807bb273a30313a8.png)

### **Redis的linux安装**

```
wget https://download.redis.io/releases/redis-6.2.7.tar.gz
tar xzf redis-6.2.7.tar.gz
cd redis-6.2.7/
make
```

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/7cbc709d5bc4498fa11dc2d8aaa04ddb.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/d56090bdd1fd4e90a1449c6c836d0d05.png)

安装后源码和执行目录会混在一起，为了方便，我做了一次install

```
make install PREFIX=/home/lijin/redis/redis
```

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/e697ede889734f18a1a9623e6b15f0dd.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/7bb695c563e74321b6055e4eac3bee03.png)

因为Redis的安装一般来说对于系统依赖很少，只依赖了Linux系统基本的类库，所以安装很少出问题

**安装常见问题**

如果执行make命令报错：cc 未找到命令，原因是虚拟机系统中缺少gcc，执行下面命令安装gcc：

```
yum -y install gcc automake autoconf libtool make
```

如果执行make命令报错：致命错误:jemalloc/jemalloc.h: 没有那个文件或目录，则需要在make指定分配器为libc。执行下面命令即可正常编译：

```
make MALLOC=libc
```

### Redis的启动

Redis编译完成后，会生成几个可执行文件，这些文件各有各的作用，我们现在先简单了解下，后面的课程会陆续说到和使用这些可执行文件。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/55847140e0b744c382acf8186fe4ffb9.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/210bdc3df1d941cea0d7f6449105310b.png)

一般来说redis-server和redis-cli这些平时用得最多。

Redis有三种方法启动Redis:默认配置、带参数启动、配置文件启动。

#### 默认配置

使用Redis的默认配置来启动，在bin目录下直接输入 ./redis-server

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/471e7f9ca54f4ace8f020d505014d602.png)

可以看到直接使用redis-server启动Redis后，会打印出一些日志，通过日志可以看到一些信息：

当前的Redis版本的是64位的6.2.7，默认端口是6379。Redis建议要使用配置文件来启动。

**因为直接启动无法自定义配置，所以这种方式是不会在生产环境中使用的。**

#### 带参数启动

redis-server加上要修改配置名和值(可以是多对)，没有设置的配置将使用默认配置，例如：如果要用6380作为端口启动Redis，那么可以执行:

./redis-server --port 6380

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/d1096d2f70a444b1a16693a68c9daf5b.png)

这种方式一般我们也用得比较少。

#### 配置文件启动

配置文件是我们启动的最多的模式，配置文件安装目录中有

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/cb5bcbe8f3144cfda3720664fd58b13f.png)

复制过来

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/93c4ba2542e84a028ee31cbc2367dd48.png)

改一下权限

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/95e8ca2a9c284b0aaa33d224cc027bad.png)

通过配置文件来启动

```
./redis-server ../conf/redis.conf
```

注意：这里对配置文件使用了相对路径，绝对路径也是可以的。

同时配置文件的方式可以方便我们改端口，改配置，增加密码等。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/da1a238ae1844d5dac7e386896577d48.png)

打开注释，设置为自己的密码，重启即可

### 操作

Redis服务启动完成后，就可以使用redis-cli连接和操作Redis服务。redis-cli可以使用两种方式连接Redis服务器。

1、单次操作

用redis-cli -hip {host} -p{port} {command}就可以直接得到命令的返回结果，例如:

那么下一次要操作redis，还需要再通过redis-cli。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/0648f2e321764c82a13bc4b1b29c94cb.png)

2、命令行操作

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/d38322174ebb48b89bbb8af2d80b8463.png)

通过redis-cli -h (host}-p {port}的方式连接到Redis服务，之后所有的操作都是通过控制台进行，例如:

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/816e9938ff2d49d9904ff79e413084bf.png)

我们没有写-h参数，那么默认连接127.0.0.1;如果不写-p，那么默认6379端口，也就是说如果-h和-p都没写就是连接127.0.0.1:6379这个 Redis实例。

### 停止

Redis提供了shutdown命令来停止Redis服务，例如我们目前已经启动的Redis服务，可以执行:

```
./redis-cli -p 6379 shutdown
```

redis服务端将会显示：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/a066e0616af6479cb29e109feae91482.png)

除了可以通过shutdown命令关闭Redis服务以外，还可以通过kill进程号的方式关闭掉Redis，但是强烈不建议使用kill -9强制杀死Redis服务，不但不会做持久化操作，还会造成缓冲区等资源不能被优雅关闭，极端情况会造成AOF和复制丢失数据的情况。如果是集群，还容易丢失数据。

同样还可以在命令行中执行shutdown指令

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/caea54b05bd7468b93e5615fc506dafe.png)

shutdown还有一个参数,代表是否在关闭Redis前，生成持久化文件，缺省是save，生成持久化文件，如果是nosave则不生成持久化文件

## Redis全局命令

对于键值数据库而言，基本的数据模型是 key-value 模型，Redis 支持的 value 类型包括了 String、哈希表、列表、集合等，而Memcached支持的 value 类型仅为 String 类型，所以Redis 能够在实际业务场景中得到广泛的应用，就是得益于支持多样化类型的 value。

Redis里面有16个库，但是Redis的分库功能没啥意义（默认就是0号库，尤其是集群操作的时候），我们一般都是默认使用0号库进行操作。

在了解Rediskey-value 模型之前，Redis的有一些全局命令，需要我们提前了解。

**keys命令**

```
keys *
keys L*
```

查看所有键(支持通配符)：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/a38baeb051bd46dab430d3037bedc48b.png)

但是这个命令请慎用，因为keys命令要把所有的key-value对全部拉出去，如果生产环境的键值对特别多的话，会对Redis的性能有很大的影响，推荐使用dbsize。

keys命令会遍历所有键，所以它的时间复杂度是o(n)，当Redis保存了大量键时线上环境禁止使用keys命令。

**dbsize命令**

dbsize命令会返回当前数据库中键的总数。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/21ed59cad4e84610869308640d57718c.png)

dbsize命令在计算键总数时不会遍历所有键,而是直接获取 Redis内置的键总数变量,所以dbsize命令的时间复杂度是O(1)。

**exists**

检查键是否存在，存在返回1，不存在返回0。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/706705c0c9d7418b8b72eb0be7228f85.png)

**del**

删除键，无论值是什么数据结构类型,del命令都可以将其删除。返回删除键个数，删除不存在键返回0。同时del命令可以支持删除多个键。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/2865421882494708bb0f78fd458a3a59.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/5d304a87b9d44633893581107f0d310a.png)

**键过期**

**expire**

Redis支持对键添加过期时间,当超过过期时间后,会自动删除键，时间单位秒。

ttl命令会返回键的剩余过期时间,它有3种返回值:

大于等于0的整数:键剩余的过期时间。

-1:键没设置过期时间。

-2:键不存在

除了expire、ttl命令以外，Redis还提供了expireat、pexpire,pexpireat、pttl、persist等一系列命令。

**expireat key**
timestamp: 键在秒级时间截timestamp后过期。

ttl命令和pttl都可以查询键的剩余过期时间，但是pttl精度更高可以达到毫秒级别，有3种返回值:

大于等于0的整数:键剩余的过期时间(ttl是秒，pttl是毫秒)。

-1:键没有设置过期时间。

-2:键不存在。

**pexpire key**
milliseconds:键在milliseconds毫秒后过期。

**pexpireat key**
milliseconds-timestamp键在毫秒级时间戳timestamp后过期。

**在使用Redis相关过期命令时,需要注意以下几点。**

1)如果expire key 的键不存在,返回结果为0:

2）如果过期时间为负值,键会立即被删除，犹如使用del命令一样:

3 ) persist命令可以将键的过期时间清除:

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/e288cca51dd446a19384a29adcc1faf7.png)

4）对于字符串类型键，执行set命令会去掉过期时间，这个问题很容易在开发中被忽视。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/5b749a4fd43248ce9af261080058ebaf.png)

5 ) Redis不支持二级数据结构(例如哈希、列表)内部元素的过期功能，不能对二级数据结构做过期时间设置。

**type**

返回键的数据结构类型，例如键lijin是字符串类型，返回结果为string。键mylist是列表类型，返回结果为list，键不存在返回none

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/a5cf8aefd7b2414b9e1fb6ad96f24b47.png)

**randomkey**

随机返回一个键，这个很简单，请自行实验。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/41c50a66594e42f98755f4ca70814050.png)

**rename**

键重命名

但是要注意，如果在rename之前,新键已经存在，那么它的值也将被覆盖。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/ec04e5381693497b9d85040317c25d74.png)

为了防止被强行rename，Redis提供了renamenx命令，确保只有newKey不存在时候才被覆盖。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/41644a5ba52048e9a4b2d8b130d211a0.png)

从上面我们可以看出，由于重命名键期间会执行del命令删除旧的键，如果键对应的值比较大，会存在阻塞Redis的可能性。

### 键名的生产实践

Redis没有命令空间，而且也没有对键名有强制要求。但设计合理的键名，有利于防止键冲突和项目的可维护性，比较推荐的方式是使用“业务名:对象名: id : [属性]”作为键名(也可以不是分号)。、

例如MySQL 的数据库名为mall，用户表名为order，那么对应的键可以用"mall:order:1",
"mall:order:1:name"来表示，如果当前Redis 只被一个业务使用，甚至可以去掉“order:”。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/46d96dd72a2943ceba09f109f563c243.png)

在能描述键含义的前提下适当减少键的长度，从而减少由于键过长的内存浪费。

## Redis常用数据结构

Redis提供了一些数据结构供我们往Redis中存取数据，最常用的的有5种，字符串（String）、哈希(Hash)、列表（list）、集合（set）、有序集合（ZSET）。

### 字符串（String）

字符串类型是Redis最基础的数据结构。首先键都是字符串类型，而且其他几种数据结构都是在字符串类型基础上构建的，所以字符串类型能为其他四种数据结构的学习奠定基础。字符串类型的值实际可以是字符串(简单的字符串、复杂的字符串(例如JSON、XML))、数字(整数、浮点数)，甚至是二进制(图片、音频、视频)，但是值最大不能超过512MB。

（虽然Redis是C写的，C里面有字符串&#x3c;本质使用char数组来实现>，但是处于种种考虑，Redis还是自己实现了字符串类型）

#### 操作命令

##### set 设置值

set key value![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/1dab86326fa249cd9d1ab118b48e9c6a.png)

set命令有几个选项:

ex seconds: 为键设置秒级过期时间。

px milliseconds: 为键设置毫秒级过期时间。

nx: 键必须不存在,才可以设置成功，用于添加（分布式锁常用）。

xx: 与nx相反,键必须存在，才可以设置成功,用于更新。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/a37e8717b892401c8aff82af2d280ac9.png)

从执行效果上看，ex参数和expire命令基本一样。还有一个需要特别注意的地方是如果一个字符串已经设置了过期时间，然后你调用了set 方法修改了它，它的过期时间会消失。

而nx和xx执行效果如下

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/03c37295005c4bd19c33f7f7509778bc.png)

除了set选项，Redis 还提供了setex和 setnx两个命令:

setex key
seconds value

setnx key value

setex和 setnx的作用和ex和nx选项是一样的。也就是，setex为键设置秒级过期时间，setnx设置时键必须不存在,才可以设置成功。

setex示例：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/af158144826048b1ac8d2b43551d39a9.png)

setnx示例：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/864f3f2d1a5a43b5a16d3e71cb808352.png)

因为键foo-ex已存在,所以setnx失败,返回结果为0，键foo-ex2不存在，所以setnx成功,返回结果为1。

有什么应用场景吗?以setnx命令为例子，由于Redis的单线程命令处理机制，如果有多个客户端同时执行setnx key value，根据setnx的特性只有一个客户端能设置成功，setnx可以作为分布式锁的一种实现方案。当然分布式锁没有不是只有一个命令就OK了，其中还有很多的东西要注意，我们后面会用单独的章节来讲述基于Redis的分布式锁。

##### get 获取值

如果要获取的键不存在,则返回nil(空):

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/87d74c2cb95349f2a5191929c8eb7735.png)

##### mset 批量设置值

通过mset命令一次性设置4个键值对

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/21b4a825ec3e43898363ea98c322a512.png)

##### mget 批量获取值

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/36e86de36de446f19392c2e0bf8882a7.png)

批量获取了键a、b、c、d的值:

如果有些键不存在,那么它的值为nil(空)，结果是按照传入键的顺序返回。

批量操作命令可以有效提高效率，假如没有mget这样的命令，要执行n次get命令具体耗时如下:

n次 get时间=n次网络时间+n次命令时间

使用mget命令后，要执行n次get命令操作具体耗时如下:

n次get时间=1次网络时间+n次命令时间

Redis可以支撑每秒数万的读写操作，但是这指的是Redis服务端的处理能力，对于客户端来说，一次命令除了命令时间还是有网络时间，假设网络时间为1毫秒，命令时间为0.1毫秒(按照每秒处理1万条命令算)，那么执行1000次 get命令需要1.1秒(1000*1+1000*0.1=1100ms)，1次mget命令的需要0.101秒(1*1+1000*0.1=101ms)。

##### Incr 数字运算

incr命令用于对值做自增操作,返回结果分为三种情况：

值不是整数,返回错误。

值是整数，返回自增后的结果。

键不存在，按照值为0自增,返回结果为1。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/1073c166f7364cf99ebeacd03ba3ca16.png)

除了incr命令，Redis提供了decr(自减)、 incrby(自增指定数字)、decrby(自减指定数字)、incrbyfloat（自增浮点数)，具体效果请同学们自行尝试。

##### append追加指令

append可以向字符串尾部追加值

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/1c0468546f1e499d9f850e3eabffc351.png)

##### strlen 字符串长度

返回字符串长度

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/7772d93625eb4680befbfb328129bbb4.png)

注意：每个中文占3个字节

##### getset 设置并返回原值

getset和set一样会设置值,但是不同的是，它同时会返回键原来的值

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/d568dc6e0a6c4adeae13d8f63a8fd0d8.png)

##### setrange 设置指定位置的字符

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/bdd7efebb340433e807d61f970649218.png)

下标从0开始计算。

##### getrange 截取字符串

getrange 截取字符串中的一部分，形成一个子串，需要指明开始和结束的偏移量，截取的范围是个闭区间。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/7802d85222fc450fbc4497d94acbd1fe.png)

#### 命令的时间复杂度

字符串这些命令中，除了del 、mset、 mget支持多个键的批量操作，时间复杂度和键的个数相关，为O(n)，getrange和字符串长度相关，也是O(n)，其余的命令基本上都是O(1)的时间复杂度，在速度上还是非常快的。

#### 使用场景

字符串类型的使用场景很广泛：

**缓存功能**

Redis 作为缓存层，MySQL作为存储层，绝大部分请求的数据都是从Redis中获取。由于Redis具有支撑高并发的特性,所以缓存通常能起到加速读写和降低后端压力的作用。

**计数**

使用Redis 作为计数的基础工具，它可以实现快速计数、查询缓存的功能,同时数据可以异步落地到其他数据源。

**共享Session**

一个分布式Web服务将用户的Session信息（例如用户登录信息)保存在各自服务器中，这样会造成一个问题，出于负载均衡的考虑，分布式服务会将用户的访问均衡到不同服务器上，用户刷新一次访问可能会发现需要重新登录，这个问题是用户无法容忍的。

为了解决这个问题,可以使用Redis将用户的Session进行集中管理,，在这种模式下只要保证Redis是高可用和扩展性的,每次用户更新或者查询登录信息都直接从Redis中集中获取。

**限速**

比如，很多应用出于安全的考虑,会在每次进行登录时,让用户输入手机验证码,从而确定是否是用户本人。但是为了短信接口不被频繁访问,会限制用户每分钟获取验证码的频率，例如一分钟不能超过5次。一些网站限制一个IP地址不能在一秒钟之内方问超过n次也可以采用类似的思路。

### 哈希(Hash)

Java里提供了HashMap，Redis中也有类似的数据结构，就是哈希类型。但是要注意，哈希类型中的映射关系叫作field-value，注意这里的value是指field对应的值，不是键对应的值。

#### 操作命令

基本上，哈希的操作命令和字符串的操作命令很类似，很多命令在字符串类型的命令前面加上了h字母，代表是操作哈希类型，同时还要指明要操作的field的值。

##### hset设值

hset user:1 name lijin

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/27d0a5b94e5e4a97a22b18f6ffbd370b.png)

如果设置成功会返回1，反之会返回0。此外Redis提供了hsetnx命令，它们的关系就像set和setnx命令一样,只不过作用域由键变为field。

##### hget取值

hget user:1 name

如果键或field不存在，会返回nil。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/fb8b44d85a8048e1a650c3e4c691036a.png)

##### hdel删除field

hdel会删除一个或多个field，返回结果为成功删除field的个数。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/45ed460d37c04b31bbd5122cd964a0e3.png)

##### hlen计算field个数

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/348060ead6c44a7a82817187e00c2f18.png)

##### hmset批量设值

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/dbad743d5eed4f7eb98c39e345ba2687.png)

##### hmget批量取值

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/04c34b6e61004b32835ff377a05a5586.png)

##### hexists判断field是否存在

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/7a182a0b69dc43c7a76f2119eca3910e.png)

若存在返回1，不存在返回0

##### hkeys获取所有field

它返回指定哈希键所有的field

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/7f84f9381fcf48a0ac43af16dbea6b6c.png)

##### hvals获取所有value

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/d4e3825c1bd446da9973b971834c37be.png)

##### hgetall获取所有field与value

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/338a05689b304021aa9b1cd6b601ffd9.png)

在使用hgetall时，如果哈希元素个数比较多，会存在阻塞Redis的可能。如果只需要获取部分field，可以使用hmget，如果一定要获取全部field-value，可以使用hscan命令，该命令会渐进式遍历哈希类型，hscan将在后面的章节介绍。

##### hincrby增加

hincrby和 hincrbyfloat，就像incrby和incrbyfloat命令一样，但是它们的作用域是filed。

##### hstrlen 计算value的字符串长度

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/4c52aa4d0f0c4841afa87011982c2151.png)

#### 命令的时间复杂度

哈希类型的操作命令中，hdel,hmget,hmset的时间复杂度和命令所带的field的个数相关O(k)，hkeys,hgetall,hvals和存储的field的总数相关，O(N)。其余的命令时间复杂度都是O(1)。

#### 使用场景

从前面的操作可以看出，String和Hash的操作非常类似，那为什么要弄一个hash出来存储。

哈希类型比较适宜存放对象类型的数据，我们可以比较下，如果数据库中表记录user为：

| id | name  | age |
| -- | ----- | --- |
| 1  | lijin | 18  |
| 2  | msb   | 20  |

**1、使用String类型**

需要一条条去插入获取。

set user:1:name lijin;

set user:1:age  18;

set user:2:name msb;

set user:2:age  20;

**优点：简单直观，每个键对应一个值**

**缺点：键数过多，占用内存多，用户信息过于分散，不用于生产环境**

**2、将对象序列化存入redis**

set user:1 serialize(userInfo);

**优点：编程简单，若使用序列化合理内存使用率高**

**缺点：序列化与反序列化有一定开销，更新属性时需要把userInfo全取出来进行反序列化，更新后再序列化到redis**

**3、使用hash类型**

hmset user:1 name lijin age 18

hmset user:2 name msb age 20

**优点：简单直观，使用合理可减少内存空间消耗**

**缺点：要控制内部编码格式，不恰当的格式会消耗更多内存**

### 列表（list）

列表( list)类型是用来存储多个有序的字符串，a、b、c、c、b四个元素从左到右组成了一个有序的列表,列表中的每个字符串称为元素(element)，一个列表最多可以存储(2^32-1)个元素(*4294967295*)。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/b2c5e0d8bd6243e59b0c32aa5caa49f1.png)

在Redis 中，可以对列表两端插入( push)和弹出(pop)，还可以获取指定范围的元素列表、获取指定索引下标的元素等。列表是一种比较灵活的数据结构，它可以充当栈和队列的角色，在实际开发上有很多应用场景。

**列表类型有两个特点:**

第一、列表中的元素是有序的，这就意味着可以通过索引下标获取某个元素或者某个范围内的元素列表。

第二、列表中的元素可以是重复的。

#### 操作命令

##### lrange 获取指定范围内的元素列表（不会删除元素）

key start end

索引下标特点：从左到右为0到N-1

lrange 0 -1命令可以从左到右获取列表的所有元素

##### rpush 向右插入

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/f495b4575e994d1f8623beba74dd6fdf.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/33e03ce96b3248ceaac60ccb4fc0b1d8.png)

##### lpush 向左插入

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/302233f9f0284e46ae26930ac8be1cd2.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/abba1d7c8aea4e42a83ca17476c4b9cd.png)

##### linsert 在某个元素前或后插入新元素

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/08d4476bb8e449b7b9efb82f271d69a4.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/c77d0ff5efe44cb8b19dd1d8bac12848.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/ebc063246d934ef19f6611f855273f45.png)

这三个返回结果为命令完成后当前列表的长度，也就是列表中包含的元素个数，同时rpush和lpush都支持同时插入多个元素。

##### lpop 从列表左侧弹出（会删除元素）

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/cf3b7a2f1d85434689734dea9d467c0f.png)r

请注意，弹出来元素就没了。

##### rpop 从列表右侧弹出

rpop将会把列表最右侧的元素d弹出。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/6f55895f739b4a32a1e1c8fde0649031.png)

##### lrem 对指定元素进行删除

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/e533d9a779074c3286d81031ca408450.png)

lrem命令会从列表中找到等于value的元素进行删除，根据count的不同分为三种情况：

count>0，从左到右,删除最多count个元素。

count&#x3c;0，从右到左,删除最多count绝对值个元素。

count=0，删除所有。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/ad2833b4c011453f91928891e8a0e036.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/584b9986d80c46668f3fda91c466e340.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/ba19aa483a884537a687051f80967ebb.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/0480e571bc1e44f79b2e80b376ec288e.png)

返回值是实际删除元素的个数。

##### ltirm 按照索引范围修剪列表

例如想保留列表中第0个到第1个元素

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/8e52841e88f34a05888da5df0a8a4121.png)ls

##### lset修改指定索引下标的元素

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/663c682ce56444e382fc3f1b4da4253c.png)

##### lindex 获取列表指定索引下标的元素

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/f0b6ab8eca224305bcd97a4916c33947.png)l

##### llen 获取列表长度

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/31f34b4b3ec24f2e89d42b958dc3f343.png)

##### blpop和brpop阻塞式弹出元素

blpop和brpop是lpop和rpop的阻塞版本，除此之外还支持多个列表类型，也支持设定阻塞时间，单位秒，如果阻塞时间为0，表示一直阻塞下去。我们以brpop为例说明。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/a83e89040af7495da20b56ef826e6520.png)

A客户端阻塞了（因为没有元素就会阻塞）

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/c11fe0ec650c401cbd69500d99805f85.png)

A客户端一直处于阻塞状态。此时我们从另一个客户端B执行

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/c4c83e7fe2ac4f6385ac04b1ecbcf231.png)

A客户端则输出

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/4b2c7c7b95da4190ab6c772075faec7c.png)

注意：brpop后面如果是多个键，那么brpop会从左至右遍历键，一旦有一个键能弹出元素，客户端立即返回。

#### 使用场景

列表类型可以用于比如：

消息队列，Redis 的 lpush+brpop命令组合即可实现阻塞队列，生产者客户端使用lrpush从列表左侧插入元素，多个消费者客户端使用brpop命令阻塞式的“抢”列表尾部的元素,多个客户端保证了消费的负载均衡和高可用性。

**文章列表**

每个用户有属于自己的文章列表，现需要分页展示文章列表。此时可以考虑使用列表,因为列表不但是有序的,同时支持按照索引范围获取元素。

实现其他数据结构

lpush+lpop =Stack（栈)

lpush +rpop =Queue(队列)

lpsh+ ltrim =Capped Collection（有限集合)

lpush+brpop=Message Queue(消息队列)

### 集合（set）

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/4b28f3a2aa6b4d4f9c5dd09317508102.png)

集合( set）类型也是用来保存多个的字符串元素,但和列表类型不一样的是，集合中不允许有重复元素,并且集合中的元素是无序的,不能通过索引下标获取元素。

一个集合最多可以存储2的32次方-1个元素。Redis除了支持集合内的增删改查，同时还支持多个集合取交集、并集、差集，合理地使用好集合类型,能在实际开发中解决很多实际问题。

#### 集合内操作命令

##### sadd 添加元素

允许添加多个，返回结果为添加成功的元素个数

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/d94f1e8871054f6dad9dad1d245d3dfb.png)

##### srem 删除元素

允许删除多个，返回结果为成功删除元素个数

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/fdcc39ccdf4c4ca1bb26003e65d40ec7.png)

##### scard 计算元素个数

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/292819f68b294180ad037dc590e8c562.png)

##### sismember 判断元素是否在集合中

如果给定元素element在集合内返回1，反之返回0

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/94148d18517a44d99cd6c7716d07f9b1.png)

##### srandmember 随机从集合返回指定个数元素

指定个数如果不写默认为1

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/2b386e0de6df44c685dc69e95bf4e7a2.png)

##### spop 从集合随机弹出元素

同样可以指定个数，如果不写默认为1，注意，既然是弹出，spop命令执行后,元素会从集合中删除,而srandmember不会。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/172580272c8b4413908328fad51e0537.png)

##### smembers 获取所有元素(不会弹出元素)

返回结果是无序的

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/f16f1615505b49a2aa5198a75168c4b2.png)

#### 集合间操作命令

现在有两个集合,它们分别是set1和set2

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/8546a5e8eff348bd8c4879fbc37db67b.png)

##### sinter 求多个集合的交集

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/80dd47f94b06433daa0b1f19be6a684f.png)

##### suinon 求多个集合的并集

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/614e67ffe97646389a781099b6e074fd.png)

##### sdiff 求多个集合的差集

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/4c0e783c37e64c15866bb08ccc0abbf8.png)

##### 将交集、并集、差集的结果保存

```
sinterstore destination key [key ...]
suionstore destination key [key ...]
sdiffstore destination key [key ...]

```

集合间的运算在元素较多的情况下会比较耗时，所以 Redis提供了上面三个命令(原命令+store)将集合间交集、并集、差集的结果保存在destination key中，例如：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/450fcb6c2e2342b39925c1779a4ba369.png)

#### 使用场景

集合类型比较典型的使用场景是标签(tag)。例如一个用户可能对娱乐、体育比较感兴趣，另一个用户可能对历史、新闻比较感兴趣，这些兴趣点就是标签。有了这些数据就可以得到喜欢同一个标签的人，以及用户的共同喜好的标签，这些数据对于用户体验以及增强用户黏度比较重要。

例如一个电子商务的网站会对不同标签的用户做不同类型的推荐，比如对数码产品比较感兴趣的人，在各个页面或者通过邮件的形式给他们推荐最新的数码产品，通常会为网站带来更多的利益。

除此之外，集合还可以通过生成随机数进行比如抽奖活动，以及社交图谱等等。

### 有序集合（ZSET）

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/fe24d4258f4b4dd99ad52a6752a840ae.png)

有序集合相对于哈希、列表、集合来说会有一点点陌生,但既然叫有序集合,那么它和集合必然有着联系,它保留了集合不能有重复成员的特性,但不同的是,有序集合中的元素可以排序。但是它和列表使用索引下标作为排序依据不同的是,它给每个元素设置一个分数( score)作为排序的依据。

有序集合中的元素不能重复，但是score可以重复，就和一个班里的同学学号不能重复,但是考试成绩可以相同。

有序集合提供了获取指定分数和元素范围查询、计算成员排名等功能，合理的利用有序集合，能帮助我们在实际开发中解决很多问题。

#### 集合内操作命令

##### zadd添加成员

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/353cb7b71ca04daab3cf7fb99ceb36f6.png)

返回结果代表成功添加成员的个数

要注意:

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/a8a349c237304f6c94f0a16f5ae3ed24.png)

zadd命令还有四个选项nx、xx、ch、incr 四个选项

nx: member必须不存在，才可以设置成功，用于添加。

xx: member必须存在，才可以设置成功,用于更新。

ch: 返回此次操作后,有序集合元素和分数发生变化的个数

incr: 对score做增加，相当于后面介绍的zincrby

##### zcard 计算成员个数

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/9eb0d86663fd45e6a3032c05c1870339.png)

##### zscore 计算某个成员的分数

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/7da7b741030b4b2b9863d569198f001d.png)

如果成员不存在则返回nil

##### zrank计算成员的排名

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/7395eb62a4fa4e669cf201799aca069e.png)

zrank是从分数从低到高返回排名

zrevrank反之

很明显，排名从0开始计算。

##### zrem 删除成员

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/d4e23637dda8461abba76c303cfbfbad.png)

允许一次删除多个成员。

返回结果为成功删除的个数。

##### zincrby 增加成员的分数

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/b1a0193040334a0f90ddb66bce53fe0c.png)

##### zrange和zrevrange返回指定排名范围的成员

有序集合是按照分值排名的，zrange是从低到高返回,zrevrange反之。如果加上
withscores选项，同时会返回成员的分数

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/d2a8d64fce484b64bf95f0a886fa1f45.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/a17fc90c48dd498f9539f5b834ada3bb.png)

##### zrangebyscore返回指定分数范围的成员

```
zrangebyscore key min max [withscores] [limit offset count]
zrevrangebyscore key max min [withscores][limit offset count]

```

其中zrangebyscore按照分数从低到高返回，zrevrangebyscore反之。例如下面操作从低到高返回200到221分的成员，withscores选项会同时返回每个成员的分数。

同时min和max还支持开区间(小括号）和闭区间(中括号)，-inf和+inf分别代表无限小和无限大:

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/de92b2a82134468ab69bbb8718ccfd1f.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/92edbaef3b2c4c4c9a7ee0daac41ad0a.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/e5dc53cdbff941c9814590973ac96499.png)

##### zcount 返回指定分数范围成员个数

zcount key min max

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/82a1a39c69bf4c8395411312fd276515.png)

##### zremrangebyrank 按升序删除指定排名内的元素

zremrangebyrank key start end

##### zremrangebyscore 删除指定分数范围的成员

zremrangebyscore key min max

#### 集合间操作命令

##### zinterstore 交集

zinterstore![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/0e0688f60b404126895ffbbf4c6ae290.png)

这个命令参数较多，下面分别进行说明

destination:交集计算结果保存到这个键。

numkeys:需要做交集计算键的个数。

key [key ...]:需要做交集计算的键。

weights weight
[weight ...]:每个键的权重，在做交集计算时，每个键中的每个member 会将自己分数乘以这个权重,每个键的权重默认是1。

aggregate sum/
min |max:计算成员交集后，分值可以按照sum(和)、min(最小值)、max(最大值)做汇总,默认值是sum。

不太好理解，我们用一个例子来说明。（算平均分）

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/cfc3967b70cb4dcea41057c3708d8616.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/65f2bee807e1446989277c0fadff86cf.png)

##### zunionstore 并集

该命令的所有参数和zinterstore是一致的，只不过是做并集计算，大家可以自行实验。

#### 使用场景

有序集合比较典型的使用场景就是排行榜系统。例如视频网站需要对用户上传的视频做排行榜，榜单的维度可能是多个方面的:按照时间、按照播放数量、按照获得的赞数。

## Redis高级数据结构

### Bitmaps

现代计算机用二进制(位)作为信息的基础单位，1个字节等于8位，例如“big”字符串是由3个字节组成，但实际在计算机存储时将其用二进制表示,“big”分别对应的ASCII码分别是98、105、103，对应的二进制分别是01100010、01101001和 01100111。

许多开发语言都提供了操作位的功能，合理地使用位能够有效地提高内存使用率和开发效率。Redis提供了Bitmaps这个“数据结构”可以实现对位的操作。把数据结构加上引号主要因为:

Bitmaps本身不是一种数据结构，实际上它就是字符串，但是它可以对字符串的位进行操作。

Bitmaps单独提供了一套命令，所以在Redis中使用Bitmaps和使用字符串的方法不太相同。可以把 Bitmaps想象成一个以位为单位的数组，数组的每个单元只能存储0和1，数组的下标在 Bitmaps 中叫做偏移量。

#### 操作命令

##### setbit 设置值

setbit key offset value

设置键的第 offset 个位的值(从0算起)。

假设现在有20个用户，userid=0,2,4,6,8的用户对网站进行了访问，存储键名为日期。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/faf895f88aa940ea854dba440b8030e7.png)

##### getbit 获取值

getbit key offset

获取键的第 offset位的值(从0开始算)，比如获取userid=8的用户是否在2022（年/这天）访问过,返回0说明没有访问过:

当然offset是不存在的，也会返回0。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/a32b45af412f4ab6a0345447cd7c4ea7.png)

##### bitcount 获取Bitmaps指定范围值为1的个数

bitcount [start] [end]

下面操作计算26号和27号这天的独立访问用户数量

[start]和[end]代表起始和结束字节数

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/6f10ab793dfa4e4bb9cc297344544f17.png)

##### bitop Bitmaps 间的运算

bitop op destkey key [key . ...]

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/085f985a7e5a4e19b4d97cf247e8385b.png)

bitop是一个复合操作，它可以做多个Bitmaps 的 and(交集)or(并集)not(非)xor(异或）操作并将结果保存在destkey中。

##### bitpos 计算Bitmaps中第一个值为targetBit 的偏移量

bitpos key targetBit [start] [end]

计算0815当前访问网站的最小用户id

除此之外，bitops有两个选项[start]和[end]，分别代表起始字节和结束字节。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/6cc67c513009442a9ecf29beed3188af.png)

#### Bitmaps优势

假设网站有1亿用户，每天独立访问的用户有5千万，如果每天用集合类型和 Bitmaps分别存储活跃用户，很明显，假如用户id是Long型，64位，则集合类型占据的空间为64位x50 000 000= 400MB，而Bitmaps则需要1位×100 000 000=12.5MB，可见Bitmaps能节省很多的内存空间。

##### 面试题和场景

1、目前有10亿数量的自然数，乱序排列，需要对其排序。限制条件-在32位机器上面完成，内存限制为 2G。如何完成？

2、如何快速在亿级黑名单中快速定位URL地址是否在黑名单中？(每条URL平均64字节)

3、需要进行用户登陆行为分析，来确定用户的活跃情况？

4、网络爬虫-如何判断URL是否被爬过？

5、快速定位用户属性（黑名单、白名单等）

6、数据存储在磁盘中，如何避免大量的无效IO？

##### 传统数据结构的不足

当然有人会想，我直接将网页URL存入数据库进行查找不就好了，或者建立一个哈希表进行查找不就OK了。

当数据量小的时候，这么思考是对的，

确实可以将值映射到 HashMap 的 Key，然后可以在 O(1) 的时间复杂度内返回结果，效率奇高。但是 HashMap 的实现也有缺点，例如存储容量占比高，考虑到负载因子的存在，通常空间是不能被用满的，举个例子如果一个1000万HashMap，Key=String（长度不超过16字符，且重复性极小），Value=Integer，会占据多少空间呢？1.2个G。实际上，1000万个int型，只需要40M左右空间，占比3%，1000万个Integer，需要161M左右空间，占比13.3%。可见一旦你的值很多例如上亿的时候，那HashMap 占据的内存大小就变得很可观了。

但如果整个网页黑名单系统包含100亿个网页URL，在数据库查找是很费时的，并且如果每个URL空间为64B，那么需要内存为640GB，一般的服务器很难达到这个需求。

#### 布隆过滤器

##### 布隆过滤器简介

**1970 年布隆提出了一种布隆过滤器的算法，用来判断一个元素是否在一个集合中。
这种算法由一个二进制数组和一个 Hash 算法组成。**

本质上布隆过滤器是一种数据结构，比较巧妙的概率型数据结构（probabilistic data structure），特点是高效地插入和查询，可以用来告诉你 “某样东西一定不存在或者可能存在”。

相比于传统的 List、Set、Map 等数据结构，它更高效、占用空间更少，但是缺点是其返回的结果是概率性的，而不是确切的。

实际上，布隆过滤器广泛应用于网页黑名单系统、垃圾邮件过滤系统、爬虫网址判重系统等，Google 著名的分布式数据库 Bigtable 使用了布隆过滤器来查找不存在的行或列，以减少磁盘查找的IO次数，Google Chrome浏览器使用了布隆过滤器加速安全浏览服务。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/801f60ff2d28436faaaab3007dd7d893.png)

##### 布隆过滤器的误判问题

Ø通过hash计算在数组上不一定在集合

Ø本质是hash冲突

Ø通过hash计算不在数组的一定不在集合（误判）

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/d31bbeaf5a8742d2b15ce65bbc5a4537.png)

**优化方案**

增大数组(预估适合值)

增加hash函数

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/84b1457186f44856b6ad56561ba64229.png)

#### [Redis]()中的布隆过滤器

##### Redisson

Maven引入Redisson

```
   <dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson</artifactId>
            <version>3.12.3</version>
        </dependency>
```

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/419fbea26266439f9f1d7b0d333920f3.png)

##### 自行实现

就是利用Redis的bitmaps来实现。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/261c8c065fdc4fdebbd7f343d83d3c6e.png)

##### 单机下无Redis的布隆过滤器

使用Google的Guava的BloomFilter。

Maven引入Guava

```
   <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>30.1.1-jre</version>
        </dependency>
```

### HyperLogLog

#### 介绍

HyperLogLog(Hyper[ˈhaɪpə(r)])并不是一种新的数据结构(实际类型为字符串类型)，而是一种基数算法,通过HyperLogLog可以利用极小的内存空间完成独立总数的统计，数据集可以是IP、Email、ID等。

如果你负责开发维护一个大型的网站，有一天产品经理要网站每个网页每天的 UV 数据，然后让你来开发这个统计模块，你会如何实现？

如果统计 PV 那非常好办，给每个网页一个独立的 Redis 计数器就可以了，这个计数器的 key 后缀加上当天的日期。这样来一个请求，incrby 一次，最终就可以统计出所有的 PV 数据。

但是 UV 不一样，它要去重，同一个用户一天之内的多次访问请求只能计数一次。这就要求每一个网页请求都需要带上用户的 ID，无论是登陆用户还是未登陆用户都需要一个唯一 ID 来标识。

一个简单的方案，那就是为每一个页面一个独立的 set 集合来存储所有当天访问过此页面的用户 ID。当一个请求过来时，我们使用 sadd 将用户 ID 塞进去就可以了。通过 scard 可以取出这个集合的大小，这个数字就是这个页面的 UV 数据。

但是，如果你的页面访问量非常大，比如一个爆款页面几千万的 UV，你需要一个很大的 set集合来统计，这就非常浪费空间。如果这样的页面很多，那所需要的存储空间是惊人的。为这样一个去重功能就耗费这样多的存储空间，值得么？其实需要的数据又不需要太精确，105w 和 106w 这两个数字对于老板们来说并没有多大区别，So，有没有更好的解决方案呢？

这就是HyperLogLog的用武之地，Redis 提供了 HyperLogLog 数据结构就是用来解决这种统计问题的。HyperLogLog 提供不精确的去重计数方案，虽然不精确但是也不是非常不精确，Redis官方给出标准误差是0.81%，这样的精确度已经可以满足上面的UV 统计需求了。

百万级用户访问网站

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/494d8e7c3cbc464db90935208fa20d44.png)

#### 操作命令

HyperLogLog提供了3个命令: pfadd、pfcount、pfmerge。

##### pfadd

pfadd key element [element …]

pfadd用于向HyperLogLog 添加元素,如果添加成功返回1:

pfadd u-9-30 u1 u2 u3 u4 u5 u6 u7 u8

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/c6de0b04ae7b43e69528a24301d7920b.png)

##### pfcount

pfcount key [key …]

pfcount用于计算一个或多个HyperLogLog的独立总数，例如u-9-30 的独立总数为8:

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/a86c5c198cef4717864fd2794eaf3624.png)

如果此时向插入一些用户，用户并且有重复

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/689ddb0df4164a98bb7d078e45af9fda.png)

如果我们继续往里面插入数据，比如插入100万条用户记录。内存增加非常少，但是pfcount 的统计结果会出现误差。

##### pfmerge

pfmerge destkey sourcekey [sourcekey ... ]

pfmerge可以求出多个HyperLogLog的并集并赋值给destkey，请自行测试。

可以看到，HyperLogLog内存占用量小得惊人，但是用如此小空间来估算如此巨大的数据，必然不是100%的正确，其中一定存在误差率。前面说过，Redis官方给出的数字是0.81%的失误率。

#### 原理概述

##### 基本原理

HyperLogLog基于概率论中伯努利试验并结合了极大似然估算方法，并做了分桶优化。

实际上目前还没有发现更好的在大数据场景中准确计算基数的高效算法，因此在不追求绝对准确的情况下，使用概率算法算是一个不错的解决方案。概率算法不直接存储数据集合本身，通过一定的概率统计方法预估值，这种方法可以大大节省内存，同时保证误差控制在一定范围内。目前用于基数计数的概率算法包括:

举个例子来理解HyperLogLog
算法，有一天李瑾老师和马老师玩打赌的游戏。

规则如下: 抛硬币的游戏，每次抛的硬币可能正面，可能反面，没回合一直抛，直到每当抛到正面回合结束。

然后我跟马老师说，抛到正面最长的回合用到了7次，你来猜一猜，我用到了多少个回合做到的？

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/b908ff160cdd4eed858e76b248fd880e.png)

进行了n次实验，比如上图：

第一次试验: 抛了3次才出现正面，此时 k=3，n=1

第二次试验: 抛了2次才出现正面，此时 k=2，n=2

第三次试验: 抛了4次才出现正面，此时 k=4，n=3

…………

第n 次试验：抛了7次才出现正面，此时我们估算，k=7

马老师说大概你抛了128个回合。这个是怎么算的。

k是每回合抛到1所用的次数，我们已知的是最大的k值，可以用kmax表示。由于每次抛硬币的结果只有0和1两种情况，因此，能够推测出kmax在任意回合出现的概率 ，并由kmax结合极大似然估算的方法推测出n的次数n =
2^(k_max) 。概率学把这种问题叫做伯努利实验。

但是问题是，这种本身就是概率的问题，我跟马老师说，我只用到12次，并且有视频为证。

所以这种预估方法存在较大误差，为了改善误差情况，HLL中引入分桶平均的概念。

同样举抛硬币的例子，如果只有一组抛硬币实验，显然根据公式推导得到的实验次数的估计误差较大；如果100个组同时进行抛硬币实验，受运气影响的概率就很低了，每组分别进行多次抛硬币实验，并上报各自实验过程中抛到正面的抛掷次数的最大值，就能根据100组的平均值预估整体的实验次数了。

分桶平均的基本原理是将统计数据划分为m个桶，每个桶分别统计各自的kmax,并能得到各自的基数预估值，最终对这些基数预估值求平均得到整体的基数估计值。LLC中使用几何平均数预估整体的基数值，但是当统计数据量较小时误差较大；HLL在LLC基础上做了改进，**采用调和平均数过滤掉不健康的统计值**。

什么叫调和平均数呢？举个例子

求平均工资：A的是1000/月，B的30000/月。采用平均数的方式就是：
(1000 + 30000) / 2 = 15500

采用调和平均数的方式就是：
2/(1/1000 + 1/30000) ≈ 1935.484

可见调和平均数比平均数的好处就是不容易受到大的数值的影响，比平均数的效果是要更好的。

##### 结合Redis的实现理解原理

现在我们和前面的业务场景进行挂钩：统计网页每天的 UV 数据。

**1.转为比特串**

通过hash函数，将数据转为二进制的比特串，例如输入5，便转为：101。为什么要这样转化呢？

是因为要和抛硬币对应上，比特串中，0 代表了反面，1 代表了正面，如果一个数据最终被转化了 10010000，那么从右往左，从低位往高位看，我们可以认为，首次出现 1 的时候，就是正面。

那么基于上面的估算结论，我们可以通过多次抛硬币实验的最大抛到正面的次数来预估总共进行了多少次实验，同样也就可以根据存入数据中，转化后的出现了 1 的最大的位置 k_max 来估算存入了多少数据。

**2.分桶**

分桶就是分多少轮。抽象到计算机存储中去，就是存储的是一个以单位是比特(bit)，长度为 L 的大数组 S ，将 S 平均分为 m 组，注意这个 m 组，就是对应多少轮，然后每组所占有的比特个数是平均的，设为 P。容易得出下面的关系：

比如有4个桶的话，那么可以截取低2位作为分桶的依据。

比如

10010000   进入0号桶

10010001   进入1号桶

10010010   进入2号桶

10010011   进入3号桶

##### Redis 中的 HyperLogLog 实现

**pfadd**

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/116ef8fb48584cc5910184aaf50092a1.png)

当我们执行这个操作时，lijin这个字符串就会被转化成64个bit的二进制比特串。

0010....0001  64位

然后在Redis中要分到16384个桶中（为什么是这么多桶：第一降低误判，第二，用到了14位二进制：2的14次方=16384）

怎么分？根据得到的比特串的后14位来做判断即可。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/bd71287e85294b14b95e3fcb82243fab.png)

根据上述的规则，我们知道这个数据要分到 1号桶，同时从左往右（低位到高位）计算第1个出现的1的位置，这里是第4位，那么就往这个1号桶插入4的数据（转成二进制）

如果有第二个数据来了，按照上述的规则进行计算。

那么问题来了，如果分到桶的数据有重复了（这里比大小，大的替换小的）：

规则如下，比大小（比出现位置的大小），比如有个数据是最高位才出现1，那么这个位置算出来就是50，50比4大，则进行替换。1号桶的数据就变成了50（二进制是110010）

所以这里可以看到，每个桶的数据一般情况下6位存储即可。

所以我们这里可以推算一下一个key的HyperLogLog只占据多少的存储。

16384*6 /8/1024=12k。并且这里最多可以存储多少数据，因为是64位吗，所以就是2的64次方的数据，这个存储的数据非常非常大的，一般用户用long来定义，最大值也只有这么多。

**pfcount**

进行统计的时候，就是把16384桶，把每个桶的值拿出来，比如取出是 n,那么访问次数就是2的n次方。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/37ea459470614e8fad06c5eab8a009ca.png)

然后把每个桶的值做调和平均数，就可以算出一个算法值。

同时，在具体的算法实现上，HLL还有一个分阶段偏差修正算法。我们就不做更深入的了解了。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1663252342001/50bed8f5a0394a93aa8033ee9f847672.png)

const和m都是Redis里面根据数据做的调和平均数。

### GEO

Redis 3.2版本提供了GEO(地理信息定位)功能，支持存储地理位置信息用来实现诸如附近位置、摇一摇这类依赖于地理位置信息的功能。

地图元素的位置数据使用二维的经纬度表示，经度范围(-180, 180]，纬度范围(-90,
90]，纬度正负以赤道为界，北正南负，经度正负以本初子午线(英国格林尼治天文台) 为界，东正西负。

业界比较通用的地理位置距离排序算法是GeoHash 算法，Redis 也使用GeoHash
算法。GeoHash
算法将二维的经纬度数据映射到一维的整数，这样所有的元素都将在挂载到一条线上，距离靠近的二维坐标映射到一维后的点之间距离也会很接近。当我们想要计算「附近的人时」，首先将目标位置映射到这条线上，然后在这个一维的线上获取附近的点就行了。

在 Redis 里面，经纬度使用 52 位的整数进行编码，放进了 zset 里面，zset 的 value 是元素的 key，score 是 GeoHash 的 52 位整数值。

#### 操作命令

##### 增加地理位置信息

geoadd key longitude latitude member [longitude latitude member ...J

longitude、latitude、member分别是该地理位置的经度、纬度、成员，例如下面有5个城市的经纬度。

城市            经度             纬度             成员

北京            116.28          39.55            beijing

天津            117.12          39.08            tianjin

石家庄        114.29          38.02            shijiazhuang

唐山            118.01          39.38            tangshan

保定            115.29          38.51            baoding

cities:locations是上面5个城市地理位置信息的集合，现向其添加北京的地理位置信息:

geoadd cities :locations 116.28 39.55 beijing

返回结果代表添加成功的个数，如果cities:locations没有包含beijing,那么返回结果为1，如果已经存在则返回0。

如果需要更新地理位置信息，仍然可以使用geoadd命令，虽然返回结果为0。geoadd命令可以同时添加多个地理位置信息:

geoadd cities:locations 117.12 39.08 tianjin 114.29 38.02
shijiazhuang 118.01 39.38 tangshan 115.29 38.51 baoding

##### 获取地理位置信息

geopos key member [member ...]下面操作会获取天津的经维度:

geopos cities:locations tianjin1)1)"117.12000042200088501"

##### 获取两个地理位置的距离。

geodist key member1 member2 [unit]

**其中unit代表返回结果的单位，包含以下四种:**

m (meters)代表米。

km (kilometers)代表公里。

mi (miles)代表英里。

ft(feet)代表尺。

下面操作用于计算天津到北京的距离，并以公里为单位:

geodist cities : locations tianjin beijing km

##### 获取指定位置范围内的地理信息位置集合

```
georadius key longitude latitude radius m|km|ft|mi [withcoord][withdist]
[withhash][COUNT count] [ascldesc] [store key] [storedist key]
georadiusbymember key member radius m|km|ft|mi  [withcoord][withdist]
[withhash] [COUNT count][ascldesc] [store key] [storedist key]

```

georadius和georadiusbymember两个命令的作用是一样的，都是以一个地理位置为中心算出指定半径内的其他地理信息位置，不同的是georadius命令的中心位置给出了具体的经纬度，georadiusbymember只需给出成员即可。其中radius  m | km |ft |mi是必需参数，指定了半径(带单位)。

这两个命令有很多可选参数，如下所示:

withcoord:返回结果中包含经纬度。

withdist:返回结果中包含离中心节点位置的距离。

withhash:返回结果中包含geohash，有关geohash后面介绍。

COUNT count:指定返回结果的数量。

asc l desc:返回结果按照离中心节点的距离做升序或者降序。

store key:将返回结果的地理位置信息保存到指定键。

storedist key:将返回结果离中心节点的距离保存到指定键。

下面操作计算五座城市中,距离北京150公里以内的城市:

georadiusbymember cities:locations beijing 150 km

##### 获取geohash

```
geohash key member [member ...]
```

Redis使用geohash将二维经纬度转换为一维字符串，下面操作会返回beijing的geohash值。

geohash cities: locations beijing

字符串越长,表示的位置更精确，geohash长度为9时,精度在2米左右，geohash长度为8时,精度在20米左右。

两个字符串越相似,它们之间的距离越近,Redis 利用字符串前缀匹配算法实现相关的命令。

geohash编码和经纬度是可以相互转换的。

##### 删除地理位置信息

zrem key member

GEO没有提供删除成员的命令，但是因为GEO的底层实现是zset，所以可以借用zrem命令实现对地理位置信息的删除。
