# SeaTunnel

## 使用问题
### web和server版本适配问题
web 1.0.0 ：   
https://seatunnel.apache.org/seatunnel_web/1.0.0/deploy

SeaTunnel 2.3.3：   
https://seatunnel.apache.org/docs/2.3.3/start-v2/locally/deployment   
https://seatunnel.apache.org/docs/2.3.3/seatunnel-engine/deployment   

### web 遇到驱动ClassNotFoundException 问题
在web的安装目录下，libs下放入驱动jar包

### 启动任务遇到驱动问题，如： java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver 错误
使用zeta引擎，驱动放在哪个位置？       
/root/seatunnel/install/apache-seatunnel-2.3.3/lib       
参考自：安装目录下的 plugins/README.md     
 
```markdown plugins/README.md
# Introduction of plugins directory

This directory used to store some third party jar package dependency by connector running, such as jdbc drivers.

!!!Attention: If you use Zeta Engine, please add jar to `$SEATUNNEL_HOME/lib/` directory on each node.

## directory structure

The jar dependency  by connector need put in `plugins/${connector name}/lib/` dir.

For example jdbc driver jars need put in `${seatunnel_install_home}/plugins/jdbc/lib/`
```
### mysql-postgres 实时任务配置
```json
{
    "env" : {
        "job.mode" : "STREAMING",
        "job.name" : "SeaTunnel_Job",
        "checkpoint.interval" : "10000"
    },
    "source" : [
        {
            "inverse-sampling.rate" : 1000,
            "catalog" : {
                "factory" : "Mysql"
            },
            "parallelism" : 1,
            "table-names" : [
                "test.sink_test_ddl",
                "test.test_ddl"
            ],
            "chunk-key.even-distribution.factor.lower-bound" : 0.05,
            "database-names" : [
                "test"
            ],
            "password" : "Datapipeline123",
            "sample-sharding.threshold" : 1000,
            "incremental.parallelism" : 1,
            "snapshot.fetch.size" : 1024,
            "connect.max-retries" : 3,
            "base-url" : "jdbc:mysql://localhost:3306/test",
            "startup.mode" : "INITIAL",
            "format" : "DEFAULT",
            "result_table_name" : "Table13647815569600",
            "server-time-zone" : "UTC",
            "plugin_name" : "MySQL-CDC",
            "exactly_once" : "true",
            "connection.pool.size" : 20,
            "snapshot.split.size" : 8096,
            "stop.mode" : "NEVER",
            "chunk-key.even-distribution.factor.upper-bound" : 100,
            "connect.timeout.ms" : 30000,
            "dag-parsing.mode" : "MULTIPLEX",
            "username" : "root"
        }
    ],
    "transform" : [],
    "sink" : [
        {
            "batch_size" : 1000,
            "max_retries" : "3",
            "catalog" : {
                "factory" : "Postgres",
                "username" : "postgres",
                "password" : "123456",
                "base-url" : "jdbc:postgresql://localhost:5455/test",
                "schema" : "public"
            },
            "source_table_name" : "Table13647815569600",
            "max_commit_attempts" : 3,
            "auto_commit" : "true",
            "plugin_name" : "Jdbc",
            "url" : "jdbc:postgresql://localhost:5455/test",
            "is_exactly_once" : "false",
            "database" : "test",
            "password" : "123456",
            "transaction_timeout_sec" : -1,
            "driver" : "org.postgresql.Driver",
            "support_upsert_by_query_primary_key_exist" : "true",
            "database_schema" : "public",
            "connection_check_timeout_sec" : 30,
            "generate_sink_sql" : true,
            "user" : "postgres"
        }
    ]
}
```
