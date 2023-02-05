# Redis作为数据库&缓存的区别

## 缓存

### 过期
> 根据业务意义或业务特性，可以人为知道数据的有效期，可以使用过期时间进行标记   
> 
> 不会随着访问延长过期时间    
> 修改操作会让过期失效，即不过期   
> expire 设置多少秒过期   
> expireat 可以设置到什么时间过期    
>
> 过期的原理：   
> http://redis.cn/commands/expire.html   
> https://redis.io/commands/expire/    
> 以下为redis.io翻译后的大致含义   
> Redis 密钥有两种过期方式：被动方式和主动方式。
> 
> 当一些客户端试图访问它并且发现密钥超时时，密钥就会被动过期。
> 
> 当然这还不够，因为有过期的密钥将永远不会被再次访问。这些键无论如何都应该过期，因此 Redis 会定期在设置过期的键中随机测试一些键。所有已经过期的密钥都将从密钥空间中删除。
> 
> 具体来说，这是 Redis 每秒执行 10 次的操作：
> 
> 1.从具有关联过期的密钥集中测试 20 个随机密钥。    
> 2.删除所有发现过期的密钥。    
> 3.如果超过 25% 的密钥已过期，请从步骤 1 重新开始。   
> 这是一个简单的概率算法，基本上假设我们的样本代表整个密钥空间，并且我们继续过期，直到可能过期的密钥百分比低于 25%   
> 
> 这意味着在任何给定时刻，使用内存的已过期密钥的最大数量最大等于每秒最大写入操作数除以 4。

### 淘汰策略
> 由于内存是有限的，缓存应该随着用户访问进行变化，淘汰掉冷数据
> 
> 配置：   
> maxmemory : redis进程一共有多少内存（单位是字节）
> 
> maxmemory-policy : 淘汰策略   
> 1. volatile-lru -> Evict using approximated LRU, only keys with an expire set.
> 2. allkeys-lru -> Evict any key using approximated LRU.
> 3. volatile-lfu -> Evict using approximated LFU, only keys with an expire set.
> 4. allkeys-lfu -> Evict any key using approximated LFU.
> 5. volatile-random -> Remove a random key having an expire set.
> 6. allkeys-random -> Remove a random key, any key.
> 7. volatile-ttl -> Remove the key with the nearest expire time (minor TTL)
> 8. noeviction -> Don't evict anything, just return an error on write operations.

### 缓存的常见问题及解决方案
#### 缓存击穿
> 缓存击穿的概念：在高并发场景下，恰好用户请求的数据在缓存中失效（过期、淘汰），导致大批量请求直接访问数据库    
> 关键词：**某个key**的过期或被淘汰，导致并发直接访问到了数据库    
> 
> 解决思路：阻止并发访问数据库   
> 1: get key   
> -> get到结束   
> -> get不到走2   
> 2: setnx   
> 3.1: setnx成功->请求数据库,并将数据放入缓存   
> 4.2: setnx失败->sleep一会，然后在从1开始   
> 
> 相当于使用setnx作为分布式锁，控制所有的并发只有一个请求能够访问数据库，
> 并将数据写到缓存中，等缓存中有数据后，其他的请求就可以直接从缓存拿数据了   
> 
> 对于分布式锁的注意事项（大概思路）：  
> setnx + 过期时间（防止客户端挂了，锁不释放）   
> 过期时间多少合适？太短了可能还没处理完，锁就没了；太长了，假如客户端真挂了，其他客户端一直抢不到锁，需要等锁自动过期消失       
> 最好有个监控（多线程，加个监控线程），监控发现还没完事就给过期时间续上。过期时间适当的短，让续约解决上述问题      

#### 缓存穿透
> 缓存穿透的概念：用户访问数据库中没有的数据，穿过缓存且穿过数据库，基本等同于无效请求
>
> 例如：电商网站卖的品类包括：手机、电脑，但是用户搜索手表品类，
> 肯定搜不到商品，而每次请求都会穿过缓存访问数据库，给数据库造成无用的压力
>
> 解决方案：使用布隆过滤器(概率解决问题)     
> 布隆过滤器的局限：不支持删除，如何避免呢？可以把删除的设置为空，或者换成其他的过滤器：布谷鸟过滤器等   
> 
> 安装module的过程：    
> 1.下载module：https://redis.io/resources/modules/    
> 2.编译module，得到.so文件    
> 3.启动脚本 redis-server --loadmodule /opt/redis-module/redisbloom.so /etc/redis/6379.conf   
> 4.或者修改配置文件，.so的文件要写绝对路径

#### 缓存雪崩
> 缓存雪崩的概念：大量的key同时过期、同时失效，间接造成大量的访问进到数据库  
> 
> 解决思路：   
> 1. 对于时点性无关的业务：随机设置过期时间，让过期时间分散，不在同一时刻过期     
> 2. 时点性相关的业务：比如零点切换利率，零点换商品价格等   
> 2.1: 依赖缓存击穿方案，在第一个请求到来时，将所有零点后的数据刷到缓存中   
> 2.2: 业务层加判断，零点加随机延时，可以让后面的请求能够用到已经刷到缓存的数据，而不是所有的重复请求都到数据库（其实也类似2.1方案）   

## 数据库

### 单机持久化
> 快照、副本：rdb   
> 
> save ： 同步的、阻塞的   
> bgsave ： 异步的、非阻塞的 fork + copy-on-write     
> 父子进程对数据的修改，对方看不到    
> 
> 触发方式：
> 1. 命令行输入 save 或 bgsave
> 2. 配置文件配置（本质是bgsave后台异步处理）   
> save seconds change   
> save 900 1 : 假设900秒发生了1次修改，则触发bgsave    
> save 300 10 : 假设300秒发生了10次修改，则触发bgsave   
> save 60  10000 : 假设60秒发生了10000次修改，则触发bgsave   
> 
> dbfilename dump.db : 持久化文件名称
> 
> dir /var/lib/redis/6379 : 持久化目录

> 日志：aof    
> 如果既开启了rdb，又开了aof，那只会用aof文件进行恢复
> 
> 4.0版本前 -> 重写 -> 删除抵消的命令、合并重复的命令（最终得到的也是一个纯指令的日志文件）       
> 4.0版本后 -> 重写 -> 将老的数据rdb到aof文件中，将增量的以指令的方式append到aof文件（最终得到的是一个混合体，结合了rdb的恢复快和aof的数据全两个优点）   
> 4.0版本后，配置文件中的配置可以看出如上结论：aof-use-rdb-preamble yes   
> 手动触发重写：bgrewriteaof   
> 
> 配置方式：   
> appendonly yes : 开启aof   
> 
> appendfilename "appendonly.aof" : 持久化文件名称   
> 
> auto-aof-rewrite-percentage 100 : 当前aof文件大小相比于上次重写后aof大小的比值    
> auto-aof-rewrite-min-size 64mb : 表示运行AOF重写时文件最小体积   
> 自动触发时机为：aof_current_size > auto-aof-rewrite-minsize && (aof_current_size-aof_base_size) / aof_base_size >= auto-aof-rewritepercentage   
> 
> 类比java中的flush，将buffer数据刷写到pagecache，相当于真正触发io，以下配置相当于控制flush的频率：   
> appendfsync always :  每次操作都触发flush，写到pagecache（最多丢失一条数据）  
> appendfsync everysec : 每秒触发一次flush，写到pagecache（可能丢失一条到一个buffer大小的数据）  
> appendfsync no : 不配置，完全交由操作系统的配置控制什么时候触发flush，写到pagecache（可能丢失一个buffer大小的数据）
