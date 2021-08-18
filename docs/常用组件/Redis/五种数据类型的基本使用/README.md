# 五种数据类型的基本使用

> 使用redic客户端 redis-cli ,要善于使用 help 命令   
> help @组名称   
> help 命令名称   
> 该笔记中命令摘抄自 redis 5.0

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

## list类型命令梳理

## set类型命令梳理

## sorted_set类型命令梳理

## hash类型命令梳理