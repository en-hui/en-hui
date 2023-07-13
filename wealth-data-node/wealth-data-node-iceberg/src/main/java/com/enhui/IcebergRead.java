package com.enhui;

import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.io.CloseableIterable;

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
        System.out.println("schema：" + table.schema());
        CloseableIterable<Record> result = IcebergGenerics.read(table).build();
        for (Record record : result) {
            System.out.println("查询到的数据：" + record);
        }
        System.out.println();
    }
}
