package com.enhui.flink.hudi;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
import org.apache.flink.contrib.streaming.state.PredefinedOptions;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class HudiDemo {

    StreamTableEnvironment tableEnv = null;
    @Before
    public void before() {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置状态后端rocksDB
        EmbeddedRocksDBStateBackend embeddedRocksDBStateBackend = new EmbeddedRocksDBStateBackend(true);
        // idea运行时指定rocksdb存储路径
        embeddedRocksDBStateBackend.setDbStoragePath("file:///Users/huenhui/IdeaProjects/wealth/wealth-bigdata-flink/flink-hudi/src/main/resources/rocksdb");
        embeddedRocksDBStateBackend.setPredefinedOptions(PredefinedOptions.SPINNING_DISK_OPTIMIZED_HIGH_MEM);
        env.setStateBackend(embeddedRocksDBStateBackend);

        System.setProperty("HADOOP_USER_NAME", "root");
        // checkpoint 配置
        env.enableCheckpointing(TimeUnit.SECONDS.toMillis(5), CheckpointingMode.EXACTLY_ONCE);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setCheckpointStorage("hdfs://heh-node02:8020/heh/flink/ckp");
        checkpointConfig.setMinPauseBetweenCheckpoints(TimeUnit.SECONDS.toMillis(2));
        checkpointConfig.setTolerableCheckpointFailureNumber(5);
        checkpointConfig.setCheckpointTimeout(TimeUnit.SECONDS.toMillis(1));
        checkpointConfig.setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        tableEnv = StreamTableEnvironment.create(env);
    }

    @Test
    public void testCreateTable() {
        String tableName = "t2";
        tableEnv.executeSql(String.format("CREATE TABLE %s(\n"+
        " uuid varchar(20),\n" +
        " name varchar(20),\n" +
        " age int \n" +
        " ) WITH ( \n" +
        " 'connector' = 'hudi', \n" +
        " 'path' = 'hdfs://heh-node02:8020/heh/hudi/%s', \n" +
        " 'table.type' = 'MERGE_ON_READ' \n" +
        " )"
        ,tableName,tableName));

        tableEnv.executeSql(String.format("insert into %s values('id1','name1',12)",tableName));

        tableEnv.executeSql(String.format("select * from %s",tableName));
    }


}
