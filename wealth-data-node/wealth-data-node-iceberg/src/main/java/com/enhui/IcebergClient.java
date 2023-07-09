package com.enhui;

import com.google.common.collect.ImmutableMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.FileFormat;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.TableProperties;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.types.Types;

public class IcebergClient {

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        System.setProperty("HADOOP_USER_NAME", "hdfs");
        conf.set("dfs.client.use.datanode.hostname", "true");
        String warehousePath = "hdfs://cdh1:8020/user/heh/iceberg";
        HadoopCatalog catalog = new HadoopCatalog(conf, warehousePath);

        TableIdentifier name = TableIdentifier.of("icebergdb", "iceberg_test_tbl1");
        // 创建Iceberg表的schema
        Schema schema = new Schema(
                Types.NestedField.required(1, "id", Types.IntegerType.get()),
                Types.NestedField.required(2, "name", Types.StringType.get()),
                Types.NestedField.required(3,"loc",Types.StringType.get())
        );

        // 没有分区
        PartitionSpec spec = PartitionSpec.unpartitioned();
        PartitionSpec.builderFor(schema).identity("loc").build();
        // 指定iceberg表数据格式化为parquet
        ImmutableMap<String, String> props = ImmutableMap.of(TableProperties.DEFAULT_FILE_FORMAT, FileFormat.PARQUET.name());

        Table table = null;
        // 创建或加载现有的Iceberg表
        if (!catalog.tableExists(name)) {
            System.out.println("创建新表");
            table = catalog.createTable(name, schema, spec,props);
        } else {
            System.out.println("加载已有表");
            table = catalog.loadTable(name);
        }


    }
}
