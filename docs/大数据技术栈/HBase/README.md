# HBase

## 常用命令
hbase shell 进入命令行

``` 
-- 查看所有命名空间
list_namespace

-- 查看 default 命名空间下的所有表
list_namespace_tables 'default'

-- 扫描全表 default 命名空间下的 test_hbase_pk 表
scan 'default:test_hbase_pk'

-- 列出全部表
list

-- 查看表的描述信息
describe 'test_hbase_pk'
```
