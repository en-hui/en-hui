package com.enhui;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.FileFormat;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.io.CloseableIterable;
import org.apache.iceberg.types.Type;
import org.apache.iceberg.types.Types;

public class IcebergClient {

  // hive

  //  add jar
  // /opt/cloudera/parcels/CDH-6.1.1-1.cdh6.1.1.p0.875250/lib/iceberg/iceberg-hive-runtime-1.3.0.jar

  //  create table iceberg_nopar_tbl1(
  //  id int,
  //  name string,
  //  loc string)
  //  stored by 'org.apache.iceberg.mr.hive.HiveIcebergStorageHandler'
  //  LOCATION 'hdfs://cdh1:8020/user/heh/iceberg/icebergdb/iceberg_nopar_tbl1'
  //  TBLPROPERTIES('iceberg.catalog'='location_based_table');

  //  select * from iceberg_test_tbl1;
  public static void main(String[] args) throws IOException {
    Configuration conf = new Configuration();
    System.setProperty("HADOOP_USER_NAME", "hdfs");
    conf.set("dfs.client.use.datanode.hostname", "true");
    String warehousePath = "hdfs://cdh1:8020/user/heh/iceberg";
    HadoopCatalog catalog = new HadoopCatalog(conf, warehousePath);

    String namespace = "icebergdb";
    String tableName = "iceberg_nopar_tbl1";
    TableIdentifier name = TableIdentifier.of(namespace, tableName);

    // 删除表
    System.out.println("删除表：" + tableName);
    catalog.dropTable(name);

    // 创建或加载表
    Table table = createOrLoadTable(catalog, name, false);
    // 数据增删
//    table.newAppend().appendFile()

    // 查询数据
    selectAndPrint(table);

    // 添加列，新增数据，查看
    table.updateSchema().addColumn("add_column", Types.IntegerType.get()).commit();
    System.out.println("添加列");
    selectAndPrint(table);

    // 重命名列，新增数据，查看
    table.updateSchema().renameColumn("add_column","operate_column").commit();
    System.out.println("重命名列");
    selectAndPrint(table);

    // 更新列，新增数据，查看
    table.updateSchema().updateColumn("operate_column",Types.LongType.get()).commit();
    System.out.println("更新列");
    selectAndPrint(table);

    // 删除列，新增数据，查看
    table.updateSchema().deleteColumn("operate_column").commit();
    System.out.println("删除列");
    selectAndPrint(table);


  }

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

  public static void selectAndPrint(Table table) {
    System.out.println("schema：" + table.schema());
    CloseableIterable<Record> result = IcebergGenerics.read(table).build();
    for (Record record : result) {
      System.out.println("查询到的数据：" + record);
    }
    System.out.println();
  }

}
