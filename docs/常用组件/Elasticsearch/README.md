# Elasticsearch

## Dev Tools
> 索引名称为 product 为例

- 集群相关
```text
## 查看集群健康状态
GET _cat/health?v
GET _cluster/health?v
```

- 索引相关
```text
## 查看索引列表
GET _cat/indices?v
## 创建索引
PUT /product?pretty
## 查看索引数据
GET /product/_search
## 删除索引
DELETE /product?pretty
## 插入数据
PUT product/_doc/1
{
  "name":"小米手机",
  "desc":"手机中的战斗机",
  "price":"3999",
  "lv":"旗舰机",
  "type":"手机",
  "create_time":"2022-09-04",
  "tags":[
    "性价比",
    "发烧",
    "不卡顿"
    ]
}
## 删除数据
DELETE /product/_doc/2
```

- mapping相关
```text
# dynamic mapping(创建索引时不指定mapping，es会默认根据数据给出类型)
## 查看映射
GET /product/_mapping
## 删除映射
DELETE /product
## 再没有索引的情况下直接插入数据，让es做动态映射
PUT product/_doc/1
{
  "name":"小米手机",
  "desc":"手机中的战斗机",
  "price":"3999",
  "lv":"旗舰机",
  "type":"手机",
  "create_time":"2022-09-04",
  "tags":[
    "性价比",
    "发烧",
    "不卡顿"
    ]
}

# 手动创建mapping
PUT /product
{
  "mappings": {
    "properties": {
      "name":{
        "type":"keyword"
      },
      "desc":{
        "type": "text"
      },
      "price":{
        "type": "long"
      },
      "lv":{
        "type": "text"
      },
      "create_time":{
        "type":"date"
      },
      "tags":{
        "type": "text"
      }
    }
  }
}
```

## 概念
