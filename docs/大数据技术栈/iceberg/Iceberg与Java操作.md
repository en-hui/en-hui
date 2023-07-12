# iceberg是什么

## 官方表述：

Iceberg is a high-performance format for huge analytic tables. Iceberg brings the reliability and simplicity of SQL tables to big data, while making it possible for engines like Spark, Trino, Flink, Presto, Hive and Impala to safely work with the same tables, at the same time.

Iceberg 是用于大型分析表的高性能格式。Iceberg将SQL表的可靠性和简单性带到大数据中，同时使Spark，Trino，Flink，Presto，Hive和Impala等引擎可以同时安全地使用相同的表。

## 如何理解：

他是一种表格式，将数据和元数据都存储在HDFS（也可以是S3等存储），目录格式如下：
``` 
    iceberg格式表名称
        data：
            xxxxx.parquet：data files
        metadata
            xxxx.metadata.json：元数据信息
            xxxx.avro：Manifest file
            snap-xxxx.avro：Snapshot = Manifest list
        temp
```

## 术语解释：

- data files（数据文件）：数据文件是Apache Iceberg表真实存储数据的文件，一般是在表的数据存储目录的data目录下，如果我们的文件格式选择的是parquet,那么文件是以“.parquet”结尾，Iceberg每次更新会产生多个数据文件(data files)。


- snapshot（表快照）：快照代表一张表在某个时刻的状态。每个快照里面会列出表在某个时刻的所有 data files 列表。data files是存储在不同的manifest files里面，manifest files是存储在一个Manifest list文件里面，而一个Manifest list文件代表一个快照。


- Manifest list（清单列表）：manifest list是一个元数据文件，它列出构建表快照（Snapshot）的清单（Manifest file）。这个元数据文件中存储的是Manifest file列表，每个Manifest file占据一行。每行中存储了Manifest file的路径、其存储的数据文件（data files）的分区范围，增加了几个数文件、删除了几个数据文件等信息，这些信息可以用来在查询时提供过滤，加快速度。


- Manifest file（清单文件）：Manifest file也是一个元数据文件，它列出组成快照（snapshot）的数据文件（data files）的列表信息。每行都是每个数据文件的详细描述，包括数据文件的状态、文件路径、分区信息、列级别的统计信息（比如每列的最大最小值、空值数等）、文件的大小以及文件里面数据行数等信息。其中列级别的统计信息可以在扫描表数据时过滤掉不必要的文件。Manifest file是以avro格式进行存储的，以“.avro”后缀结尾。

## 类似SQL的操作应该如何使用

### 获取Catalog和table两个对象，来做接下来的操作

``` 
    Configuration conf = new Configuration();
    System.setProperty("HADOOP_USER_NAME", "hdfs");
    conf.set("dfs.client.use.datanode.hostname", "true");
    String warehousePath = "hdfs://cdh1:8020/user/heh/iceberg";
    HadoopCatalog catalog = new HadoopCatalog(conf, warehousePath);

    String namespace = "icebergdb";
    String tableName = "iceberg_nopar_tbl1";
    TableIdentifier name = TableIdentifier.of(namespace, tableName);


  public static Table createOrLoadTable(
      HadoopCatalog catalog, TableIdentifier name, boolean isPartition) {
    Table table = null;
    // 创建或加载现有的Iceberg表
    if (!catalog.tableExists(name)) {
      System.out.println("创建新表,是否有分区:" + isPartition);
      // 创建Iceberg表的schema
      Schema schema =
          new Schema(
              Types.NestedField.required(1, "id", Types.IntegerType.get()),
              Types.NestedField.required(2, "name", Types.StringType.get()),
              Types.NestedField.required(3, "loc", Types.StringType.get()));
      PartitionSpec spec = null;
      if (isPartition) {
        spec = PartitionSpec.builderFor(schema).identity("loc").build();
      } else {
        // 没有分区
        spec = PartitionSpec.unpartitioned();
      }
      // 指定iceberg表数据格式化为parquet
      ImmutableMap<String, String> props =
          ImmutableMap.of(TableProperties.DEFAULT_FILE_FORMAT, FileFormat.PARQUET.name());
      table = catalog.createTable(name, schema, spec, props);
    } else {
      System.out.println("加载已有表");
      table = catalog.loadTable(name);
    }
    return table;
  }

```



### 创建表

``` 
String namespace = "icebergdb";
String tableName = "iceberg_nopar_tbl1";
TableIdentifier name = TableIdentifier.of(namespace, tableName);

Schema schema =
new Schema(
Types.NestedField.required(1, "id", Types.IntegerType.get()),
Types.NestedField.required(2, "name", Types.StringType.get()),
Types.NestedField.required(3, "loc", Types.StringType.get()));

PartitionSpec spec = PartitionSpec.unpartitioned();

ImmutableMap<String, String> props =
ImmutableMap.of(TableProperties.DEFAULT_FILE_FORMAT, FileFormat.PARQUET.name());

Table table = catalog.createTable(name, schema, spec, props);
```

### 删除表

```
String namespace = "icebergdb";
String tableName = "iceberg_nopar_tbl1";
TableIdentifier name = TableIdentifier.of(namespace, tableName);

catalog.dropTable(name);
```
### 清空表

### 插入数据

### 更新数据

### 删除数据

### 新增列
```
table.updateSchema().addColumn("add_column", Types.IntegerType.get()).commit();
```

### 重命名列
```
table.updateSchema().renameColumn("add_column","operate_column").commit();
```

### 修改列
```
table.updateSchema().updateColumn("operate_column",Types.LongType.get()).commit();
```

### 删除列
```
table.updateSchema().deleteColumn("operate_column").commit();
```
