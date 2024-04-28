# Sql Server CT(Change Tracking)

> SQL Server 2016 Enterprise(腾讯云RDS)
>
> DBMS: Microsoft SQL Server (ver. 13.00.6300)     
> Case sensitivity: plain=mixed, delimited=mixed     
> Driver: Microsoft JDBC Driver 12.2 for SQL Server (ver. 12.2.0.0, JDBC4.2)     
> Ping: 24 ms     
> SSL: yes    


```sql
--查看库级别是否启用change tracking：
select d.name,ct.* from sys.change_tracking_databases ct left join sys.databases d on ct.database_id = d.database_id;
    
-- 开启数据库级别的CT
ALTER DATABASE heh_cdc SET CHANGE_TRACKING=on
(
      CHANGE_RETENTION = 2 DAYS ,
      AUTO_CLEANUP = ON
);
      
--禁用库级别change tracking：
alter database db_name set change_trcking = off;


--查看表是否开启CT  is_track_columns_updated_on 列的值为 1，则表示相应的表启用了 Change Tracking
-- all：
SELECT t.name AS table_name, ct.is_track_columns_updated_on
FROM sys.tables t
         LEFT JOIN sys.change_tracking_tables ct ON t.object_id = ct.object_id
WHERE SCHEMA_NAME(t.schema_id) = 'dbo';
-- 单表：
SELECT t.name AS table_name, ct.is_track_columns_updated_on
FROM sys.tables t
         LEFT JOIN sys.change_tracking_tables ct ON t.object_id = ct.object_id
WHERE t.name = 'test_ct';

-- 启用 test_ct 表CT功能:
ALTER TABLE test_ct
    ENABLE CHANGE_TRACKING
    WITH (TRACK_COLUMNS_UPDATED = ON)
    GO

-- 禁用表级别change tracking：
ALTER TABLE test_ct DISABLE CHANGE_TRACKING;
    

-- 查询全增量衔接位点(当前库的最新位点，查询增量时，用消费到的位点做start，用最新位点做end，超过end的作为下一次增量数据处理)
use [heh_cdc] SELECT CHANGE_TRACKING_CURRENT_VERSION();

-- 获取某张表的最低有效版本（与当前消费位点对比，用于检查日志是否被清理-日志清理会导致丢数）
use [heh_cdc] SELECT CHANGE_TRACKING_MIN_VALID_VERSION(object_id('[heh_cdc].[dbo].[test_ct]'));

-- 获取变更记录，【666】为上次处理到的版本位置   
-- SYS_CHANGE_VERSION：此记录的版本（lsn）；
-- SYS_CHANGE_OPERATION：操作类型-I、U、D（主键变更是D+I；同一id被多次操作，最终只会留下一条结果，中间过程被合并了）
-- id：主键值，多主键应该在sql中查多个列
SELECT tcheck.SYS_CHANGE_VERSION, tcheck.SYS_CHANGE_OPERATION, tcheck.[id] 
FROM CHANGETABLE ( CHANGES [heh_cdc].[dbo].[test_ct], 666) AS tcheck;
-- 根据变更的主键反查数据(删除的数据无法反查，所以只有主键在after中)
SELECT * FROM [heh_cdc].[dbo].[test_ct] WITH (NOLOCK) WHERE [id] = '3' OR [id] = '4';
```

``` 
partition: 0  offset: 14  timestamp: 2024-04-28 11:38:22
key: {"id": 6}
value: 
{"after": {"id": 6, "col1": 5, "col2": 5}, 
"before": null, 
"source": {"database": "heh_cdc", "schema": "dbo", "entity": "test_ct", "entity2": "", "partition": 0, "size": 1, "islastone": false, "snapshot": false, "idx": 8, "totalb": 20, "b": 3, "totalc": 8, "total_i_b": 18, "total_i_c": 6, "total_u_b": 0, "total_u_c": 0, "total_d_b": 2, "total_d_c": 2, "total_tran_c": 0, "ts_sec": 1714275495634, "msg_t": "I", "eqr": false, "src_schema_id": 832, "sink_entity_id": 0, "start_time": 0, "collect_ts": 1714275495634, "ver": 0, "custom_param": {}, "src_address": "", "clear_dest_ids": null, "partial_update": null, "par_names": null, "par_type": null, "idempotent_uid": null, "ddl_content": null, "sourceOffsetKey": "", "sourceOffsetValue": "", "lsn": ""}}

partition: 0  offset: 16  timestamp: 2024-04-28 11:39:21
key: {"id": 3}
value: {"after": {"id": 3, "col1": 4, "col2": 4}, 
"before": null, 
"source": {"database": "heh_cdc", "schema": "dbo", "entity": "test_ct", "entity2": "", "partition": 0, "size": 1, "islastone": false, "snapshot": false, "idx": 9, "totalb": 23, "b": 3, "totalc": 9, "total_i_b": 18, "total_i_c": 6, "total_u_b": 3, "total_u_c": 1, "total_d_b": 2, "total_d_c": 2, "total_tran_c": 0, "ts_sec": 1714275556260, "msg_t": "U", "eqr": false, "src_schema_id": 832, "sink_entity_id": 0, "start_time": 0, "collect_ts": 1714275556260, "ver": 0, "custom_param": {}, "src_address": "", "clear_dest_ids": null, "partial_update": null, "par_names": null, "par_type": null, "idempotent_uid": null, "ddl_content": null, "sourceOffsetKey": "", "sourceOffsetValue": "", "lsn": ""}}

partition: 0  offset: 20  timestamp: 2024-04-28 11:42:16
key: {"id": 4}
value: 
{"after": {"id": 4, "col1": null, "col2": null}, 
"before": null, 
"source": {"database": "heh_cdc", "schema": "dbo", "entity": "test_ct", "entity2": "", "partition": 0, "size": 1, "islastone": false, "snapshot": false, "idx": 10, "totalb": 24, "b": 1, "totalc": 10, "total_i_b": 18, "total_i_c": 6, "total_u_b": 3, "total_u_c": 1, "total_d_b": 3, "total_d_c": 3, "total_tran_c": 0, "ts_sec": 1714275736211, "msg_t": "D", "eqr": false, "src_schema_id": 832, "sink_entity_id": 0, "start_time": 0, "collect_ts": 1714275736211, "ver": 0, "custom_param": {}, "src_address": "", "clear_dest_ids": null, "partial_update": null, "par_names": null, "par_type": null, "idempotent_uid": null, "ddl_content": null, "sourceOffsetKey": "", "sourceOffsetValue": "", "lsn": ""}}
```
