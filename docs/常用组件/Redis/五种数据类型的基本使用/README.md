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
GET key 【Get the value of a key】

APPEND key value  【Append a value to a key】

GETRANGE key start end 【Get a substring of the string stored at a key】

GETSET key value 【Set the string value of a key and return its old value】

 MGET key [key ...] 【Get the values of all the given keys】

MSET key value [key value ...] 【Set multiple keys to multiple values】

MSETNX key value [key value ...] 【Set multiple keys to multiple values, only if none of the keys exist】

PSETEX key milliseconds value 【Set the value and expiration in milliseconds of a key】

SET key value [expiration EX seconds|PX milliseconds] [NX|XX] 【Set the string value of a key】

SETEX key seconds value 【Set the value and expiration of a key】

SETNX key value 【Set the value of a key, only if the key does not exist】

SETRANGE key offset value 【Overwrite part of a string at key starting at the specified offset】

STRLEN key 【Get the length of the value stored in a key】
```

数值命令整理
``` 
DECR key 【Decrement the integer value of a key by one】

DECRBY key decrement 【Decrement the integer value of a key by the given number】

INCR key 【Increment the integer value of a key by one】

INCRBY key increment 【Increment the integer value of a key by the given amount】

INCRBYFLOAT key increment 【Increment the float value of a key by the given amount】
```

位图命令整理
``` 
BITCOUNT key [start end] 【Count set bits in a string】

BITFIELD key [GET type offset] [SET type offset value] [INCRBY type offset increment] [OVERFLOW WRAP|SAT|FAIL] 【Perform arbitrary bitfield integer operations on strings】

BITOP operation destkey key [key ...] 【Perform bitwise operations between strings】

BITPOS key bit [start] [end] 【Find first bit set or clear in a string】

GETBIT key offset 【Returns the bit value at offset in the string value stored at key】

SETBIT key offset value 【Sets or clears the bit at offset in the string value stored at key】
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

## set类型命令梳理

## sorted_set类型命令梳理

## hash类型命令梳理