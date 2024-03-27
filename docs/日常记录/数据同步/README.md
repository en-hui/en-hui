# 数据同步专题

## 通用
- 结构变化：一对多、多对一
- 任务启动读取起点（全量初始化、暂停时进度、指定进度、）

## 读取源节点
- jdbc全量读取与cdc增量读取，每种字段类型的输出格式是否统一（输出规范）

## 写入目的节点
- merge操作，针对全字段写入和非全字段写入的方案   
- 主键变更时的处理
- 更新列存在null的处理（where xx is null）
- 流式写入优化，微批执行sql

### 测试用例反例（分组）

> 场景一：    
> 假如表中同时存在 主键、唯一索引   
> 只根据主键分组，存在以下问题   
> 1）插入数据：主键：1，唯一索引：1，普通列：name1    
> 2）插入数据：主键：2，唯一索引：2，普通列：name2    
> 3）插入数据：主键：6，唯一索引：6，普通列：name6    
> 4）删除数据：主键：2    
> 5）插入数据：主键：3，唯一索引：2，普通列：name3    
> 由于1），2），3），5）都是插入，且主键都不同，会分在第一组要执行的sql中，    
> 4）会分在第二个要执行的sql中   
> 本质上将4）5）的顺序颠倒了，导致先插入唯一索引冲突的数据，报错了   
> 

### 更新列非源端主键 场景分析
> Insert事件：before为null，after包括全部列
>
> Delete事件，after为null，before 1-包括全部列；2-仅包括主键。比如pg，只能捕获到主键的before
>
> Update事件，after 1-包括全部列；2-包括被变更的列；before 1-包括全部列；2-仅包括主键。比如pg，只能捕获到主键的before。比如oracle，未开启补全只有被更新的after

> 分组问题：    
> 1、当目的表存在主键和唯一索引时，删除事件如果不能捕获到唯一索引列，那5000 D事件会分成5000组，解决方案：源端捕获级别提高   
> 2、场景一问题，在不能捕获删除事件的唯一索引时，不能保证4-5的顺序，5会分到3的那一组，导致乱序    
> 3、当目的设置逻辑主键，对应源端普通列，且不能采集到，不可用，无可操作空间   

**1、oracle 日志记录现状**   
未开启补全，使用rowid模式：before 和 after只有被更新的列，连主键都没有   
pk：before 和 after包括主键和被更新的列   
all：before 和 after包括全部列   

**2、pg复制槽捕获现状（有主键表，未开启FULL，唯一索引也捕获不到before）**   
主键：【pk2】   
唯一索引：【uk1_col1、uk1_col2】、【uk2_col1】、【uk3_col1】   
默认级别：I before空，after包括全部列；U before包括主键，after包括全部列；D before包括主键，after空
``` 
{"table_name":"public.test_group","op_type":"INSERT",
"columns_name":["pk1","pk2","uk1_col1","uk1_col2","uk2_col1","uk3_col1","col1","col2"],
"columns_type":["integer","integer","integer","integer","character varying","character varying","integer","integer"],
"columns_val":["1","1","1","1","'uk2_col1_1'","'uk3_col1_1'","1","1"],
"old_keys_name":[],"old_keys_type":[],"old_keys_val":[]}


{"table_name":"public.test_group","op_type":"UPDATE",
"columns_name":["pk1","pk2","uk1_col1","uk1_col2","uk2_col1","uk3_col1","col1","col2"],
"columns_type":["integer","integer","integer","integer","character varying","character varying","integer","integer"],
"columns_val":["255072934","4535","4535","4535","'uk2_col1_4535'","'uk3_col1_4535'","4535","4535"],
"old_keys_name":["pk2"],"old_keys_type":["integer"],"old_keys_val":["4535"]}


{"table_name":"public.test_group","op_type":"DELETE",
"columns_name":[],"columns_type":[],"columns_val":[],
"old_keys_name":["pk2"],"old_keys_type":["integer"],"old_keys_val":["4999"]}
```
