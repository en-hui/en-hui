# HBase模块-马士兵教育原笔记

- [HBase介绍](msb/HBase/01%20HBase介绍.md)
- [HBase搭建--Standalone](msb/HBase/02%20HBase搭建--Standalone.md)
- [HBase搭建--Fully-distributed](msb/HBase/03%20HBase搭建--Fully-distributed.md)
- [Protobuf简介](msb/HBase/04%20Protobuf简介.md)
- [HBase基本操作](msb/HBase/05%20HBase基本操作.md)
- [LSM树存储结构](msb/HBase/06%20HBase%20LSM树存储结构.md)
- [HBase数据读取流程](msb/HBase/07%20HBase数据读取流程.md)
- [HBase优化设计](msb/HBase/08%20HBase优化设计.md)


hbase 表设计

``` 
1、人员-角色
	人员有多个角色  角色优先级
	角色有多个人员
	人员 删除添加角色
	角色 可以添加删除人员
	人员 角色 删除添加
	人员编号
	001
	002
	003
	角色编号
	100
	200
	300
---------------------
	一张表存储
	rowkey（pid_rid|rid-pid）
	001_100
	001_200
	
	100_001
	100_00
----------------------
	两张表
人员表
rowkey（pid）			cf1:(属性信息)						cf2:(角色列表)
001					cf1:name=..,cf1:age=..,				cf2:100=10,cf2:200=9
002
003

角色表
rowkey（rid）			cf1:(角色信息)						cf2:（人员列表）
100					cf1:name=班长。。。。				cf2:001=小黑，cf2:002=小宝
200
300	
----------------------
三张表
人员表
rowkey(pid)				cf:(人员信息)
角色表
rowkey(rid)				cf:(角色信息)
人员角色表
rowkey(random_id)       cf：(角色id)				cf2:(人员id)
	

	
	
	
	
2、组织架构 部门-子部门
	查询 顶级部门
	查询 每个部门的所有子部门
	部门 添加、删除子部门
	部门 添加、删除 
	009--CEO
		003--开发部
			002  开发1
			004  开发2
		005--前端
			007 前端1
	一张表
	rowkey				cf：(子部门列表)				cf2:(部门信息)
	0_009				cf:005=..,						cf2:name=..,cf2:..=..,cf2:pid=?
	1_003				cf:002=..,cf:004=..,
	1_002
	1_004
	
	
3、微博表设计
	添加删除关注
	粉丝列表
	写微博
	查看首页，所有关注过的好友发布的最新微博
	查看某个用户发布的所有微博，降序排序
	
	001:小红		小黑
	002：小黑		小白，小红
	003：小白		小红，小黑
	
	关注和关注表：
	rowkey（pid）				cf1:(关注列表)							cf2:(粉丝列表)
	001							cf:002=小黑								cf:002=小黑，cf:003=小白
	002							cf:001=小红，cf:003=小白				cf:001=小红，cf:003=小白
	003							cf:001=小红，cf:002=小黑				cf:002=小黑
	
	微博表
	rowkey(wid)									cf:(微博信息)
	pid_(long.max_value-timestamp)			cf:content=..,cf:title=..,
	
	微博收取表：	
	rowkey：					cf:(所有关注人发布微博的排序)version=10000
								cf:sq=wid3
								cf:sq=wid2
	pid							cf:sq=wid1,

```