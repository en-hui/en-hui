package com.enhui;

import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hadoop.HadoopCatalog;

public class IcebergRead {

  public static void main(String[] args) {
    Configuration conf = new Configuration();
    System.setProperty("HADOOP_USER_NAME", "hdfs");
    conf.set("dfs.client.use.datanode.hostname", "true");
    String warehousePath = "hdfs://cdh1:8020/user/heh/iceberg";
    HadoopCatalog catalog = new HadoopCatalog(conf, warehousePath);

    String namespace = "icebergdb";
    String tableName = "flink_iceberg_tbl";
    TableIdentifier name = TableIdentifier.of(namespace, tableName);

    Table table = catalog.loadTable(name);
    IcebergClient.selectAndPrint(table);
  }
}
