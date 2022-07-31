-- 创建hive表，使用两个test的orc文件测试hive读取时是否区分大小写（建表时不可使用不同大小写的相同字段）
CREATE EXTERNAL TABLE `heh`.`orctable`(
  `id` INT,
  `age` BIGINT,
  `name` STRING)
ROW FORMAT SERDE
  'org.apache.hadoop.hive.ql.io.orc.OrcSerde'
STORED AS INPUTFORMAT
  'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'
OUTPUTFORMAT
  'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'
LOCATION
  '/user/heh/0731/heh1'