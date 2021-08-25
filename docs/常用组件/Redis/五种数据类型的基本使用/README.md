# 五种数据类型的基本使用

> 使用redic客户端 redis-cli ,要善于使用 help 命令   
> help @组名称   
> help 命令名称   
> 该笔记中命令全量摘抄自 redis 5.0(阿里-云数据库Redis版-Redis 5.0 社区版)   
> 如实际使用遇到疑问，当前文档不能解惑，可以结合help和中文官网【命令】来了解具体用法

## String类型命令梳理
> String类型可以分为以下几类：   
> 1.字符串   
> 2.数值   
> 3.bitmaps

字符串命令整理
``` 
SET key value [expiration EX seconds|PX milliseconds] [NX|XX]
summary: Set the string value of a key
since: 1.0.0
描述：给key设置一个值，如果key已存在值，则做覆盖动作；set成功后，之前设置的过期时间将失效
EX seconds – 设置键key的过期时间，单位是秒
PX milliseconds – 设置键key的过期时间，单位是毫秒
NX – 只有键key不存在的时候才会设置key的值
XX – 只有键key存在的时候才会设置key的值

PSETEX key milliseconds value
summary: Set the value and expiration in milliseconds of a key
since: 2.6.0
描述：设置key的值，并设置过期时间，单位为毫秒（等同于 set px）

SETEX key seconds value
summary: Set the value and expiration of a key
since: 2.0.0
描述：设置key的值，并设置过期时间，单位为秒（等同于 set ex）

SETNX key value
summary: Set the value of a key, only if the key does not exist
since: 1.0.0
描述：设置key的值，只有key不存在时才设置成功（等同于 set nx）

MSETNX key value [key value ...]
summary: Set multiple keys to multiple values, only if none of the keys exist
since: 1.0.1
描述：设置多个值，只有key不存在才设置成功（原子性操作，一个不成功都不成功）

MSET key value [key value ...]
summary: Set multiple keys to multiple values
since: 1.0.1
描述：一次性设置多个值

MGET key [key ...]
summary: Get the values of all the given keys
since: 1.0.0
描述：一次性查询多个key

GET key
summary: Get the value of a key
since: 1.0.0
描述：获取键key的值

APPEND key value
summary: Append a value to a key
since: 2.0.0
描述：在键key的值后面追加字符串

SETRANGE key offset value
summary: Overwrite part of a string at key starting at the specified offset
since: 2.2.0
描述：根据偏移量覆盖key的值

GETRANGE key start end
summary: Get a substring of the string stored at a key
since: 2.4.0
描述：根据索引位置（支持正负索引）获取键key的值

GETSET key value
summary: Set the string value of a key and return its old value
since: 1.0.0
描述：查询键key的旧值并设置新值

STRLEN key
summary: Get the length of the value stored in a key
since: 2.2.0
描述：查询key的长度（相当于length）
```

数值
``` 
INCR key
summary: Increment the integer value of a key by one
since: 1.0.0
描述：给key的值 ++（原值必须是整数）

INCRBY key increment
summary: Increment the integer value of a key by the given amount
since: 1.0.0
描述：给key的值 + 指定整数（原值必须是整数）

INCRBYFLOAT key increment
summary: Increment the float value of a key by the given amount
since: 2.6.0
描述：给键key的值 + 指定浮点数（没有减法方法，所以减法就加负数）

DECR key
summary: Decrement the integer value of a key by one
since: 1.0.0
描述：给键key的值 --（原值必须是整数）

DECRBY key decrement
summary: Decrement the integer value of a key by the given number
since: 1.0.0
描述：给key的值 - 指定整数
```

bitmap
> 每8个bit为一字节，从下标为0开始   
> 比如：ascii码中 二进制 0010 0001 表示 !
> 所以如下：   
> setbit k1 2 1 和 setbit k1 7 1  ==> get k1 可以得到 !   
> 同样：set k2 !  ==> getbit k2 2 是1，getbit k2 7是1
``` 
SETBIT key offset value
summary: Sets or clears the bit at offset in the string value stored at key
since: 2.2.0
描述：给key的某个偏移量设置值（value 只能是 0 和 1）（偏移量指的是二进制位的偏移）

GETBIT key offset
summary: Returns the bit value at offset in the string value stored at key
since: 2.2.0
描述：查询key在某位置上的二进制位是 0 还是 1（偏移量指的是二进制位的偏移）

BITCOUNT key [start end]
summary: Count set bits in a string
since: 2.6.0
描述：查询key的二进制位上，有多少个1,(start 和 end 为字符串的索引下标-支持正负索引)

BITOP operation destkey key [key ...]
summary: Perform bitwise operations between strings
since: 2.6.0
描述：对一个或多个key进行 位操作，并把结果保存到 destkey()
BITOP 命令支持 AND 、 OR 、 NOT 、 XOR 四种位操作
AND：对多个二进制字符串进行【逻辑与--bit同一位置，全1则1】操作 
OR：对多个二进制字符串进行【逻辑或--bit同一位置，有1则1】操作 
NOT：对单个二进制字符串进行【逻辑非--1变0,0变1】操作 
XOR：对多个二进制字符串进行【逻辑异或--bit同一位置，相同为0，不同为1】操作 

BITPOS key bit [start] [end]
summary: Find first bit set or clear in a string
since: 2.8.7
描述：找出字符串中，第一次出现该bit的二进制位的位置;bit为0或1，start和end代表字符串索引下标
例子：
setbit k1 2 1, setbit k1 7 1, setbit k1 8 1
bitpos k1 0 0 -1 : 从整个字符串中找到第一个0的位置，返回值为 0
bitpos k1 1 0 -1 : 从整个字符串中找到第一个1的位置，返回值为 2
bitpos k1 1 1 1 : 从第一个字节找第一个1的位置，返回值为8 （字节下标也从0开始）
bitpos k1 0 1 1 : 从第一个字节找第一个0的位置，返回值为9


BITFIELD key [GET type offset] [SET type offset value] [INCRBY type offset increment] [OVERFLOW WRAP|SAT|FAIL]
summary: Perform arbitrary bitfield integer operations on strings
since: 3.2.0

```

## String类型场景实践
数值
> 抢购、秒杀、详情页、点赞、评论。   
规避并发场景下，对数据库的事务操作，完全由redis内存操作代替
（**一些不重要的数据可以这样操作，对于银行类金额相关数据必须持久化，保证数据可靠性**）


bitmap 
```  
1.问题：统计用户登录天数，且时间窗口随机
分析：一年365或366天，按400估算，一个字节8位，400/8=50字节。不到50字节就可以表示一个人一年的登录情况
实操：
# huenhui 这个用户在第1天、第8天、第365天登录
setbit huenhui 0 1
setbit huenhui 7 1
setbit huenhui 364 1

# huenhui这个key的长度（查出来的是字节的长度，不是二进制位的长度）
strlen huenhui

# huenhui 这个用户在最后两周内，登录了几天
一个字节八位，最后两周14天，最后两个字节16（问题，如果刚好时间窗口在字节中间怎么办？）
bitcount huenhui -2 -1

2.场景：京东618活动，登录就送礼物，假设京东共2亿注册用户（活跃+僵尸号），问需要准备多少礼物（僵尸号可能618不登录）
方案一：按2亿准备礼物，假设一个礼物10rmb
方案二：按活跃用户膨胀30%准备礼物，假设活跃用户1亿，可以省 7000w*10 rmb。关键在于找出活跃用户
问题：活跃用户统计，且时间窗口随机
分析：用时间做key，把用户id映射到二进制位的位置
实操：
# huenhui 用户id是1;liuhe 用户id是5,在 20210101、20210102 这天登录了
setbit 20210101 1 1
setbit 20210101 5 1
setbit 20210102 1 1

# 20210101、20210102 一共有几个人登录(一天多个人登录也算1，有则为1，无则为0，所以用或)，把结果放到result这个key中
bitop or result 20210101 20210102
# 查看结果
bitcount result
```


## list类型命令梳理
``` 
LPUSH key value [value ...]
summary: Prepend one or multiple values to a list
since: 1.0.0
描述：从链表左边插入元素

RPUSH key value [value ...]
summary: Append one or multiple values to a list
since: 1.0.0
描述：从链表右边插入元素

LPUSHX key value
summary: Prepend a value to a list, only if the list exists
since: 2.2.0
描述：只有链表存在时，才插入一个元素（从左插入）

RPUSHX key value
summary: Append a value to a list, only if the list exists
since: 2.2.0
描述：只有链表存在时，才插入一个元素（从右插入）

LINSERT key BEFORE|AFTER pivot value
summary: Insert an element before or after another element in a list
since: 2.2.0
描述：在某个元素前面或后面插入一个元素（从左边看，第一个符合的元素）

LRANGE key start stop
summary: Get a range of elements from a list
since: 1.0.0
描述：根据索引范围查看链表

LINDEX key index
summary: Get an element from a list by its index
since: 1.0.0
描述：根据下标获取元素

LLEN key
summary: Get the length of a list
since: 1.0.0
描述：得到链表的长度

LPOP key
summary: Remove and get the first element in a list
since: 1.0.0
描述：从链表左边取出一个元素（弹出，取完就删了）

RPOP key
summary: Remove and get the last element in a list
since: 1.0.0
描述：从链表右边取出一个元素（弹出，取完就删了）

LREM key count value
summary: Remove elements from a list
since: 1.0.0
描述：从左边看，删除n个指定value（n给大了也没事）

LSET key index value
summary: Set the value of an element in a list by its index
since: 1.0.0
描述：给下标位置设置新值（根据下标更新）

LTRIM key start stop
summary: Trim a list to the specified range
since: 1.0.0
描述：根据开始和结束下标为界限，删除两边的元素

RPOPLPUSH source destination
summary: Remove the last element in a list, prepend it to another list and return it
since: 1.2.0
描述：从源list右边弹出一个元素，添加到目标list的左边。并返回这个元素

BLPOP key [key ...] timeout
summary: Remove and get the first element in a list, or block until one is available
since: 2.0.0
描述：从链表左侧弹出一个元素，如果链表为空，则阻塞（可设置阻塞时间，单位为秒）

BRPOP key [key ...] timeout
summary: Remove and get the last element in a list, or block until one is available
since: 2.0.0
描述：从链表右侧弹出一个元素，如果链表为空，则阻塞（可设置阻塞时间，单位为秒）

BRPOPLPUSH source destination timeout
summary: Pop a value from a list, push it to another list and return it; or block until one is available
since: 2.2.0
描述：从源list右边弹出一个元素，添加到目标list的左边。并返回这个元素。如果源链表为空，则阻塞（可设置阻塞时间，单位为秒）
```

## hash类型命令梳理
``` 
HSET key field value
summary: Set the string value of a hash field
since: 2.0.0
描述：设置一个key的一个字段

HSETNX key field value
summary: Set the value of a hash field, only if the field does not exist
since: 2.0.0
描述：设置一个key的一个字段（只有这个字段不存在才设置成功）

HEXISTS key field
summary: Determine if a hash field exists
since: 2.0.0
描述：判断某个key的某个字段是否存在（存在返回1，不存在返回0）

HGET key field
summary: Get the value of a hash field
since: 2.0.0
描述：获取某个key的某个字段的值

HMSET key field value [field value ...]
summary: Set multiple hash fields to multiple values
since: 2.0.0
描述：一次性设置多个字段的值

HMGET key field [field ...]
summary: Get the values of all the given hash fields
since: 2.0.0
描述：一次性获取多个字段的值

HGETALL key
summary: Get all the fields and values in a hash
since: 2.0.0
描述：获取某个key的全部字段（返回全部field value）

HLEN key
summary: Get the number of fields in a hash
since: 2.0.0
描述：查看某个key有几个字段

HKEYS key
summary: Get all the fields in a hash
since: 2.0.0
描述：查看某个key的所有字段

HVALS key
summary: Get all the values in a hash
since: 2.0.0
描述：查看某个key的所有字段的值

HSTRLEN key field
summary: Get the length of the value of a hash field
since: 3.2.0
描述：获取某key的某个字段的长度

HINCRBY key field increment
summary: Increment the integer value of a hash field by the given number
since: 2.0.0
描述：给key的某个字段 + 整数 （该字段必须为整数值）

HINCRBYFLOAT key field increment
summary: Increment the float value of a hash field by the given amount
since: 2.6.0
描述：给key的某个字段 + 浮点数 （该字段必须为数值）

HDEL key field [field ...]
summary: Delete one or more hash fields
since: 2.0.0
描述：删除某key的一个或多个字段

HSCAN key cursor [MATCH pattern] [COUNT count]
summary: Incrementally iterate hash fields and associated values
since: 2.8.0
描述：参考 scan http://redis.cn/commands/scan.html
用迭代的方式查询key，传一个游标过去，可以指定筛选条件，可以指定返回个数。返回值包含一个游标和一个数组，如果游标为0，则表示迭代结束
cursor表示游标，必须正确使用游标：即第一次必须传0，后面每次必须传上次返回的游标
MATCH选项：指定筛选规则，例如 hscan k1 n* 表示筛选以n开头的字段
COUNT选项：
实验过程中，COUNT选项没生效 ==> ??

```

## set类型命令梳理
``` 
SADD key member [member ...]
summary: Add one or more members to a set
since: 1.0.0
描述：添加一个或多个元素到集合中（重复的只会设置一个）

SMEMBERS key
summary: Get all the members in a set
since: 1.0.0
描述：获取集合中所有元素

SCARD key
summary: Get the number of members in a set
since: 1.0.0
描述：获取集合中元素个数

SISMEMBER key member
summary: Determine if a given value is a member of a set
since: 1.0.0
描述：判断元素是否在集合中（存在返回1，不存在返回0）

SREM key member [member ...]
summary: Remove one or more members from a set
since: 1.0.0
描述：删除指定元素

SPOP key [count]
summary: Remove and return one or multiple random members from a set
since: 1.0.0
描述：弹出一个或多个随机元素

SRANDMEMBER key [count]
summary: Get one or multiple random members from a set
since: 1.0.0
描述：获取一个或多个随机元素

SMOVE source destination member
summary: Move a member from one set to another
since: 1.0.0
描述：把一个元素从源集合移动到目标集合

SINTER key [key ...]
summary: Intersect multiple sets
since: 1.0.0
描述：获取集合的交集

SINTERSTORE destination key [key ...]
summary: Intersect multiple sets and store the resulting set in a key
since: 1.0.0
描述：获取集合的交集，并存储到目标key中

SUNION key [key ...]
summary: Add multiple sets
since: 1.0.0
描述：获取集合的并集

SUNIONSTORE destination key [key ...]
summary: Add multiple sets and store the resulting set in a key
since: 1.0.0
描述：获取集合的并集，并存储到目标key中

SDIFF key [key ...]
summary: Subtract multiple sets
since: 1.0.0
描述：获取集合的差集（跳转key的位置，可以实现左差右差）

SDIFFSTORE destination key [key ...]
summary: Subtract multiple sets and store the resulting set in a key
since: 1.0.0
描述：获取集合的差集，并存储到目标key中（跳转key的位置，可以实现左差右差）

SSCAN key cursor [MATCH pattern] [COUNT count]
summary: Incrementally iterate Set elements
since: 2.8.0
描述：参考 scan http://redis.cn/commands/scan.html
```

## sorted_set类型命令梳理
> 有序集合：物理内存左小右大，所以正向命令或排序都是分值小的在前，rev反向命令是分值大的在前   
> 
> lex：基于字典序的命令，多数命令适用于分值相同的情况   
> rank：基于排名的命令   
> score：基于分值的命令
``` 
ZADD key [NX|XX] [CH] [INCR] score member [score member ...]
summary: Add one or more members to a sorted set, or update its score if it already exists
since: 1.2.0
描述：
XX: 仅仅更新存在的成员，不添加新成员。
NX: 不更新存在的成员。只添加新成员。
CH: 修改返回值为发生变化的成员总数，原始是返回新添加成员的总数 (CH 是 changed 的意思)。更改的元素是新添加的成员，已经存在的成员更新分数。 所以在命令中指定的成员有相同的分数将不被计算在内。注：在通常情况下，ZADD返回值只计算新添加成员的数量。
INCR: 当ZADD指定这个选项时，成员的操作就等同ZINCRBY命令，对成员的分数进行递增操作。

ZCARD key
summary: Get the number of members in a sorted set
since: 1.2.0
描述：获取有序集合中元素个数

ZCOUNT key min max
summary: Count the members in a sorted set with scores within the given values
since: 2.0.0
描述：根据【最大、最小分值】获取符合条件的元素个数
min、max 为分值，默认闭区间，"("符号描述开区间：(5 8 表示 5 < score <= 8
min和max可以是-inf和+inf

ZSCORE key member
summary: Get the score associated with the given member in a sorted set
since: 1.2.0
描述：获取key中元素的分值

ZRANGE key start stop [WITHSCORES]
summary: Return a range of members in a sorted set, by index
since: 1.2.0
描述：根据元素【索引开始、结束下标】查询元素，返回数据 分值从小到大（物理存储）
WITHSCORES: 该选项可以指定展示元素时，将分值一并输出

ZREVRANGE key start stop [WITHSCORES]
summary: Return a range of members in a sorted set, by index, with scores ordered from high to low
since: 1.2.0
描述：根据元素【索引开始、结束下标】查询元素，返回数据 分值从大到小（物理存储--反转）

ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]
summary: Return a range of members in a sorted set, by score
since: 1.0.5
描述：根据【最大、最小分值】查询元素，返回数据 分值从小到大（物理存储）
min、max 为分值，默认闭区间，"("符号描述开区间：(5 8 表示 5 < score <= 8
min和max可以是-inf和+inf

ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]
summary: Return a range of members in a sorted set, by score, with scores ordered from high to low
since: 2.2.0
描述：根据【最大、最小分值】查询元素，返回数据 分值从大到小（物理存储--反转）
min、max 为分值，默认闭区间，"("符号描述开区间：(5 8 表示 5 < score <= 8
min和max可以是-inf和+inf

ZRANGEBYLEX key min max [LIMIT offset count]
summary: Return a range of members in a sorted set, by lexicographical range
since: 2.8.9
描述：返回指定成员区间内的成员，按成员字典正序排序, 分数必须相同
比较复杂，查看官网更准确 http://redis.cn/commands/zrangebylex.html

ZREVRANGEBYLEX key max min [LIMIT offset count]
summary: Return a range of members in a sorted set, by lexicographical range, ordered from higher to lower strings.
since: 2.8.9
描述：返回指定成员区间内的成员，按成员字典倒序排序, 分数必须相同
比较复杂，查看官网更准确 http://redis.cn/commands/zrevrangebylex.html

ZLEXCOUNT key min max
summary: Count the number of members in a sorted set between a given lexicographical range
since: 2.8.9
描述：计算有序集合中指定成员之间的成员数量
ZLEXCOUNT k1 - +   最小分值元素与最大分值元素之间有多少个元素
ZLEXCOUNT k1 [m1 [m2 元素m1与m2之间有多少元素（分值之间，必须m1小，m2大，否则返回0）
- 成员名称前需要加 [ 符号作为开头, [ 符号与成员之间不能有空格
- 可以使用 - 和 + 表示得分最小值和最大值
- min 和 max 不能反, max 放前面 min放后面会导致返回结果为0
- 计算成员之间的成员数量时,参数 min 和 max 的位置也计算在内。
- min 和 max 参数的含义与 zrangebylex 命令中所描述的相同

ZINCRBY key increment member
summary: Increment the score of a member in a sorted set
since: 1.2.0
描述：将key中某一元素的分值做一个数

ZPOPMAX key [count]
summary: Remove and return members with the highest scores in a sorted set
since: 5.0.0
描述：弹出key中最大分值的几个元素

ZPOPMIN key [count]
summary: Remove and return members with the lowest scores in a sorted set
since: 5.0.0
描述：弹出key中最小分值的几个元素

BZPOPMAX key [key ...] timeout
summary: Remove and return the member with the highest score from one or more sorted sets, or block until one is available
since: 5.0.0
描述：阻塞弹出key中最大分值的一个元素，可同时操作多个key（可设置阻塞时间，单位为秒）

BZPOPMIN key [key ...] timeout
summary: Remove and return the member with the lowest score from one or more sorted sets, or block until one is available
since: 5.0.0
描述：阻塞弹出key中最小分值的一个元素，可同时操作多个key（可设置阻塞时间，单位为秒）

ZRANK key member
summary: Determine the index of a member in a sorted set
since: 2.0.0
描述：查看key中某元素的排名，分值从小到大（物理存储），分值最小的排名为0

ZREVRANK key member
summary: Determine the index of a member in a sorted set, with scores ordered from high to low
since: 2.0.0
描述：查看key中某元素的排名，分值从大到小（物理存储--反转），分值最大的排名为0

ZREM key member [member ...]
summary: Remove one or more members from a sorted set
since: 1.2.0
描述：删除key中指定元素

ZREMRANGEBYLEX key min max
summary: Remove all members in a sorted set between the given lexicographical range
since: 2.8.9
描述：删除名称按字典由低到高排序成员之间所有成员。适用于分数一致的有序集合，待删除的有序集合中,分数最好相同,否则删除结果会不正常
查看：http://redis.cn/commands/zremrangebylex.html

ZREMRANGEBYRANK key start stop
summary: Remove all members in a sorted set within the given indexes
since: 2.0.0
描述：根据排名删除元素，0为分数最小的（即排名第一），可以使用负数（-1为分数最高的，即排名倒数第一）

ZREMRANGEBYSCORE key min max
summary: Remove all members in a sorted set within the given scores
since: 1.2.0
描述：根据分值删除元素

ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
summary: Add multiple sorted sets and store the resulting sorted set in a new key
since: 2.0.0
描述：计算给定 numkeys 个有序集合的并集，把结果放到目标key中，默认求不同key相同元素的分值的和为新key的元素分值
WEIGHTS、AGGREGATE 选项可参考下面的交集部分实例

ZINTERSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
summary: Intersect multiple sorted sets and store the resulting sorted set in a new key
since: 2.0.0
描述：计算给定 numKeys 个有序集合的交集，把结果放到目标key中，默认求不同key相同元素的分值的和为新key的元素分值
ZADD k1 1 a （聚合前*2==为2）
ZADD k1 3 b （聚合前*2==为6）
ZADD k2 2 a （聚合前*3==为6）
ZADD k2 1 b （聚合前*3==为3）
ZADD k2 4 c （聚合前*3==为12）
ZINTERSTORE resultKey 2 k1 k2 WEIGHTS 2 3 AGGREGATE MIN
ZRANGE resultKey 0 -1 WITHSCORES
结果为：resultKey中有两个元素，a是2，b是3
WEIGHTS: 每个给定有序集的所有成员的score值在传递给聚合函数之前都要先乘以该因子，默认不给是1
AGGREGATE: 可以指定并集的结果集的聚合方式,默认不给是sum，将所有集合中某个成员的score值之和作为结果集中该成员的score值

ZSCAN key cursor [MATCH pattern] [COUNT count]
summary: Incrementally iterate sorted sets elements and associated scores
since: 2.8.0
描述：参考scan http://redis.cn/commands/scan.html
```