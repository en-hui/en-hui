# Sql Server CDC

```sql
-- 创建数据库
create database heh_cdc
go
exec sp_addextendedproperty 'MS_Description', '测试cdc'
go

-- 查看数据库是否开启CDC
SELECT is_cdc_enabled FROM SYS.databases where name = 'heh_cdc';

-- 开启数据库级别的CDC
USE [heh_cdc]
EXECUTE sys.sp_cdc_enable_db;
    
--  查看该库所有开启了CDC的表
EXEC sys.sp_cdc_help_change_data_capture;

-- 查看该库的[dbo].[DP_28985]表是否开启了CDC，如果有记录，则为成功开启
EXEC sys.sp_cdc_help_change_data_capture 'dbo', 'DP_28985';


-- 开启表级别CDC
EXEC sys.sp_cdc_enable_table
     @source_schema= 'dbo',
     @source_name = 'DP_28985', -- 需要开启CDC的表名
     @capture_instance = 'DP_28985_instance', -- 为CDC实例起的名
     @role_name = null;

-- 查询全增量衔接位点
use dp_test;
SELECT sys.fn_cdc_map_time_to_lsn ('largest less than or equal',CURRENT_TIMESTAMP);

```
