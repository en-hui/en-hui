package com.enhui.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class HBaseDemo {

    Configuration conf = null;
    Connection conn = null;
    Admin admin = null;

    @Before
    public void init() throws IOException {
        // 配置对象
        conf = HBaseConfiguration.create();
        conf.set("|hbase.zookeeper.quorum","heh-node02");
        // 获取连接
        conn = ConnectionFactory.createConnection(conf);

        admin = conn.getAdmin();
    }

    /**
     * 创建表
     * @throws IOException
     */
    @Test
    public void createTable() throws IOException {
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(TableName.valueOf("phone"));
        ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder("cf".getBytes());
        tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptorBuilder.build());

        admin.createTable(tableDescriptorBuilder.build());
    }

    public void destory() {
        try {
            admin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
