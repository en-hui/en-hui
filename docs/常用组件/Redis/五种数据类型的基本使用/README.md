# 五种数据类型的基本使用

> 使用redic客户端 redis-cli ,要善于使用 help 命令   
> help @组名称   
> help 命令名称   
> 该笔记中命令摘抄自 redis 5.0(阿里-云数据库Redis版-Redis 5.0 社区版)

## String类型命令梳理
> String类型可以分为以下几类：   
> 1.字符串   
> 2.数值   
> 3.bitmaps

字符串命令整理
``` 
APPEND key value
summary: Append a value to a key
since: 2.0.0

GET key
summary: Get the value of a key
since: 1.0.0

GETRANGE key start end
summary: Get a substring of the string stored at a key
since: 2.4.0

GETSET key value
summary: Set the string value of a key and return its old value
since: 1.0.0

MGET key [key ...]
summary: Get the values of all the given keys
since: 1.0.0

MSET key value [key value ...]
summary: Set multiple keys to multiple values
since: 1.0.1

MSETNX key value [key value ...]
summary: Set multiple keys to multiple values, only if none of the keys exist
since: 1.0.1

PSETEX key milliseconds value
summary: Set the value and expiration in milliseconds of a key
since: 2.6.0

SET key value [expiration EX seconds|PX milliseconds] [NX|XX]
summary: Set the string value of a key
since: 1.0.0

SETEX key seconds value
summary: Set the value and expiration of a key
since: 2.0.0

SETNX key value
summary: Set the value of a key, only if the key does not exist
since: 1.0.0

SETRANGE key offset value
summary: Overwrite part of a string at key starting at the specified offset
since: 2.2.0

STRLEN key
summary: Get the length of the value stored in a key
since: 2.2.0
```

数值
``` 
DECR key
summary: Decrement the integer value of a key by one
since: 1.0.0

DECRBY key decrement
summary: Decrement the integer value of a key by the given number
since: 1.0.0

INCR key
summary: Increment the integer value of a key by one
since: 1.0.0

INCRBY key increment
summary: Increment the integer value of a key by the given amount
since: 1.0.0

INCRBYFLOAT key increment
summary: Increment the float value of a key by the given amount
since: 2.6.0
```

bitmap
``` 
BITCOUNT key [start end]
summary: Count set bits in a string
since: 2.6.0

BITFIELD key [GET type offset] [SET type offset value] [INCRBY type offset increment] [OVERFLOW WRAP|SAT|FAIL]
summary: Perform arbitrary bitfield integer operations on strings
since: 3.2.0

BITOP operation destkey key [key ...]
summary: Perform bitwise operations between strings
since: 2.6.0

BITPOS key bit [start] [end]
summary: Find first bit set or clear in a string
since: 2.8.7

GETBIT key offset
summary: Returns the bit value at offset in the string value stored at key
since: 2.2.0

SETBIT key offset value
summary: Sets or clears the bit at offset in the string value stored at key
since: 2.2.0
```

## String类型场景实践
数值
> 抢购、秒杀、详情页、点赞、评论。   
规避并发场景下，对数据库的事务操作，完全由redis内存操作代替
（**一些不重要的数据可以这样操作，对于银行类必须持久化，保证数据可靠性**）
``` 

```

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
BLPOP key [key ...] timeout
summary: Remove and get the first element in a list, or block until one is available
since: 2.0.0

BRPOP key [key ...] timeout
summary: Remove and get the last element in a list, or block until one is available
since: 2.0.0

BRPOPLPUSH source destination timeout
summary: Pop a value from a list, push it to another list and return it; or block until one is available
since: 2.2.0

LINDEX key index
summary: Get an element from a list by its index
since: 1.0.0

LINSERT key BEFORE|AFTER pivot value
summary: Insert an element before or after another element in a list
since: 2.2.0

LLEN key
summary: Get the length of a list
since: 1.0.0

LPOP key
summary: Remove and get the first element in a list
since: 1.0.0

LPUSH key value [value ...]
summary: Prepend one or multiple values to a list
since: 1.0.0

LPUSHX key value
summary: Prepend a value to a list, only if the list exists
since: 2.2.0

LRANGE key start stop
summary: Get a range of elements from a list
since: 1.0.0

LREM key count value
summary: Remove elements from a list
since: 1.0.0

LSET key index value
summary: Set the value of an element in a list by its index
since: 1.0.0

LTRIM key start stop
summary: Trim a list to the specified range
since: 1.0.0

RPOP key
summary: Remove and get the last element in a list
since: 1.0.0

RPOPLPUSH source destination
summary: Remove the last element in a list, prepend it to another list and return it
since: 1.2.0

RPUSH key value [value ...]
summary: Append one or multiple values to a list
since: 1.0.0

RPUSHX key value
summary: Append a value to a list, only if the list exists
since: 2.2.0
```

## hash类型命令梳理
``` 
HDEL key field [field ...]
summary: Delete one or more hash fields
since: 2.0.0

HEXISTS key field
summary: Determine if a hash field exists
since: 2.0.0

HGET key field
summary: Get the value of a hash field
since: 2.0.0

HGETALL key
summary: Get all the fields and values in a hash
since: 2.0.0

HINCRBY key field increment
summary: Increment the integer value of a hash field by the given number
since: 2.0.0

HINCRBYFLOAT key field increment
summary: Increment the float value of a hash field by the given amount
since: 2.6.0

HKEYS key
summary: Get all the fields in a hash
since: 2.0.0

HLEN key
summary: Get the number of fields in a hash
since: 2.0.0

HMGET key field [field ...]
summary: Get the values of all the given hash fields
since: 2.0.0

HMSET key field value [field value ...]
summary: Set multiple hash fields to multiple values
since: 2.0.0

HSCAN key cursor [MATCH pattern] [COUNT count]
summary: Incrementally iterate hash fields and associated values
since: 2.8.0

HSET key field value
summary: Set the string value of a hash field
since: 2.0.0

HSETNX key field value
summary: Set the value of a hash field, only if the field does not exist
since: 2.0.0

HSTRLEN key field
summary: Get the length of the value of a hash field
since: 3.2.0

HVALS key
summary: Get all the values in a hash
since: 2.0.0
```

## set类型命令梳理
``` 
SADD key member [member ...]
summary: Add one or more members to a set
since: 1.0.0

SCARD key
summary: Get the number of members in a set
since: 1.0.0

SDIFF key [key ...]
summary: Subtract multiple sets
since: 1.0.0

SDIFFSTORE destination key [key ...]
summary: Subtract multiple sets and store the resulting set in a key
since: 1.0.0

SINTER key [key ...]
summary: Intersect multiple sets
since: 1.0.0

SINTERSTORE destination key [key ...]
summary: Intersect multiple sets and store the resulting set in a key
since: 1.0.0

SISMEMBER key member
summary: Determine if a given value is a member of a set
since: 1.0.0

SMEMBERS key
summary: Get all the members in a set
since: 1.0.0

SMOVE source destination member
summary: Move a member from one set to another
since: 1.0.0

SPOP key [count]
summary: Remove and return one or multiple random members from a set
since: 1.0.0

SRANDMEMBER key [count]
summary: Get one or multiple random members from a set
since: 1.0.0

SREM key member [member ...]
summary: Remove one or more members from a set
since: 1.0.0

SSCAN key cursor [MATCH pattern] [COUNT count]
summary: Incrementally iterate Set elements
since: 2.8.0

SUNION key [key ...]
summary: Add multiple sets
since: 1.0.0

SUNIONSTORE destination key [key ...]
summary: Add multiple sets and store the resulting set in a key
since: 1.0.0
```

## sorted_set类型命令梳理

``` 
BZPOPMAX key [key ...] timeout
summary: Remove and return the member with the highest score from one or more sorted sets, or block until one is available
since: 5.0.0

BZPOPMIN key [key ...] timeout
summary: Remove and return the member with the lowest score from one or more sorted sets, or block until one is available
since: 5.0.0

ZADD key [NX|XX] [CH] [INCR] score member [score member ...]
summary: Add one or more members to a sorted set, or update its score if it already exists
since: 1.2.0

ZCARD key
summary: Get the number of members in a sorted set
since: 1.2.0

ZCOUNT key min max
summary: Count the members in a sorted set with scores within the given values
since: 2.0.0

ZINCRBY key increment member
summary: Increment the score of a member in a sorted set
since: 1.2.0

ZINTERSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
summary: Intersect multiple sorted sets and store the resulting sorted set in a new key
since: 2.0.0

ZLEXCOUNT key min max
summary: Count the number of members in a sorted set between a given lexicographical range
since: 2.8.9

ZPOPMAX key [count]
summary: Remove and return members with the highest scores in a sorted set
since: 5.0.0

ZPOPMIN key [count]
summary: Remove and return members with the lowest scores in a sorted set
since: 5.0.0

ZRANGE key start stop [WITHSCORES]
summary: Return a range of members in a sorted set, by index
since: 1.2.0

ZRANGEBYLEX key min max [LIMIT offset count]
summary: Return a range of members in a sorted set, by lexicographical range
since: 2.8.9

ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]
summary: Return a range of members in a sorted set, by score
since: 1.0.5

ZRANK key member
summary: Determine the index of a member in a sorted set
since: 2.0.0

ZREM key member [member ...]
summary: Remove one or more members from a sorted set
since: 1.2.0

ZREMRANGEBYLEX key min max
summary: Remove all members in a sorted set between the given lexicographical range
since: 2.8.9

ZREMRANGEBYRANK key start stop
summary: Remove all members in a sorted set within the given indexes
since: 2.0.0

ZREMRANGEBYSCORE key min max
summary: Remove all members in a sorted set within the given scores
since: 1.2.0

ZREVRANGE key start stop [WITHSCORES]
summary: Return a range of members in a sorted set, by index, with scores ordered from high to low
since: 1.2.0

ZREVRANGEBYLEX key max min [LIMIT offset count]
summary: Return a range of members in a sorted set, by lexicographical range, ordered from higher to lower strings.
since: 2.8.9

ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]
summary: Return a range of members in a sorted set, by score, with scores ordered from high to low
since: 2.2.0

ZREVRANK key member
summary: Determine the index of a member in a sorted set, with scores ordered from high to low
since: 2.0.0

ZSCAN key cursor [MATCH pattern] [COUNT count]
summary: Incrementally iterate sorted sets elements and associated scores
since: 2.8.0

ZSCORE key member
summary: Get the score associated with the given member in a sorted set
since: 1.2.0

ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]
summary: Add multiple sorted sets and store the resulting sorted set in a new key
since: 2.0.0
```