# HDFS理论知识点

## 存储模型

- 将文件**线性**按**字节**切割成块（block），具有offset、id等信息（按字节切，会把汉字、单词等切坏）
- 文件与文件的block大小可以不一样
- 一个文件除了最后一个block，其他block大小一致（最后一个block可能占不满）
- block的大小应该依据硬件的I/O特效来调整（1.x：默认64M；2.x：默认128M；根据硬盘的读写性能调整）
- block被分散存放在集群的节点中，具有location（块的位置信息）
- block具有副本（replication），没有主从概念，副本不能出现在同一个节点（副本放置策略，会将块放到不同节点）
- 副本是满足可靠性和性能的关键（可靠性：多副本，非单点；性能：多副本，并行计算）
- 文件上传可以指定block大小和副本数，上传后只能修改副本数
- 一次写入多次读取，不支持修改（修改会导致当前修改块后面所有的块offset都需要调整，成本太大，所以舍弃了修改功能）
- 支持追加数据（文件末尾加块，并不会导致offset重新计算）

## 架构设计
> 主从：主从节点都是工作的   
> 主备：主工作，从在主挂掉的时候，顶替主来工作
> 
> 角色信息（进程）：NameNode、DataNode、Client

- HDFS 是一个主从（Master/Slaves ）架构
- 由一个NameNode（主）和一些DataNode（从）组成
- 面向文件包含：文件数据（data）和文件元数据（metadata）
- NameNode负责存储和管理元数据，并维护了一个层次型的文件目录树
- DataNode负责存储文件数据（block块），并提供block的读写
- DataNode与NameNode维持心跳，并汇报自己持有的block块信息
- Client和NameNode交互文件元数据，和DataNode交互文件block数据

## 角色功能

- NameNode
  - 完全基于内存存储文件元数据、目录结构、文件block的映射
  - 需要持久化方案保证数据可靠性
  - 提供**副本放置策略**
- DataNode
  - 基于本地磁盘存储block（以文件的形式）
  - 并保存block的**校验和**数据保证block的可靠性
  - 与NameNode保持心跳，汇报block列表状态
- SecondaryNameNode（只有非Ha模式才有此角色）
  - 在非Ha模式下，SNN一般是独立的节点，周期完成对NameNode的EditLog向FsImage合并，减少EditLog大小，减少NameNode启动时间
  - 根据配置文件设置的时间间隔 fs.checkpoint.period 默认3600秒
  - 根据配置文件设置的editsLog大小 fs.checkpoint.size规定edits文件的最大值 默认64MB

## 元数据持久化

> 通用的数据持久化方案思路：    
> 1.日志（文本文件）：记录实时发生的增删改的操作，append追加至文件末尾    
>   优点：完整性较好   
>   缺点：体积大（1G内存持久化，可能最后日志文件大于1G），加载恢复数据慢
> 
> 2.快照（二进制文件）：间隔（天、小时、分钟、秒），内存全量数据基于某一个时间点做的向磁盘的溢写   
>   优点：体积小（1G内存持久化，快照文件肯定不超过1G），加载恢复数据快    
>   缺点：因为是间隔的，容易丢失一部分数据；全量IO

**HDFS的 NameNode 元数据持久化方案**
- 任何对文件系统元数据产生的修改操作，NameNode都会使用一种称为EditLog的事务日志记录下来
- 使用FsImage存储内存所有的元数据状态
- 使用本地磁盘保存EditLog和FsImage
- EditLog具有完整性，数据丢失少，但恢复速度慢，并有体积膨胀风险
- FsImage具有恢复速度快，体积与内存数据相当，但不能实时保存，数据丢失多
- NameNode使用了FsImage+EditLog整合的方案
    - 滚动将增量的EditLog更新到FsImage，以保证更近时点的FsImage和更小的EditLog体积
    
> FsImage 滚动更新有哪些方案？     
> 方案一：   
>    由NameNode 8点溢写一次，9点溢写一次（每次溢写有大量的IO）   
> 
> 方案二：   
>    NameNode在第一次写FsImage的时候，溢写一次，假设是8点，到9点的时候，EditLog记录了8-9的日志   
>    只需要将8-9的日志的记录，更新到8点的FsImage中，FsImage的数据时点就变成了9点   
> 
> Hdfs使用方案二，并且用 SecondaryNameNode 角色来做合并

## 安全模式

> HDFS搭建时会格式化，格式化操作会产生一个空的FsImage   
> 当NameNode启动时，它从磁盘中读取EditLog和FsImage   
> 将所有EditLog中的事务作用在内存中的FsImage上   
> 并将这个新版本的FsImage从内存中保存到本地磁盘上   
> 然后删除旧的EditLog，因为这个旧的EditLog的事务都已经作用在FsImage上了
> 
> NameNode在持久化时，不会持久化块的位置信息（由DataNode心跳上报自己的块信息）    
> 所以在NameNode启动后，会进入一个称为安全模式的特殊状态   
> 处于安全模式的NameNode是不会进行数据块的复制的    
> NameNode从所有的DataNode接受心跳信号和块状态报告   
> 每当NameNode检测确认某个数据块的副本数量达到这个最小值，那么该数据块就会被认为是副本安全的（safely replicated）   
> 在一定百分比（）的数据块被NameNode检测确认是安全之后（加上一个额外的30秒等待时间），NameNode将退出安全模式状态    
> 接下来他会确定还有哪些数据块的副本没有达到指定数目，并将这些数据块复制到其他DataNode上


## block块的副本放置策略

- 第一个副本：如果客户端与DataNode同节点，直接放置在本机节点；如果是集群外提交，则随机挑选一台磁盘不太满，cpu不太忙的节点
- 第二个副本：放置在与第一个副本不同的机架的节点上
- 第三个副本：与第二个副本相同机架的节点
- 更多副本：随机节点

## 读写流程

- HDFS写流程
  - Client和NameNode连接创建文件元数据
  - NameNode判定元数据是否有效（文件是否存在、权限不足等校验）
  - NameNode触发**副本放置策略**，返回一个有序的DataNode列表
  - Client和DataNode建立**Pipeline**连接
  - Client将块切分成packet（64kb），并使用chunk（512B）+chucksum（4B）填充
  - Client将packet放入发送队列dataqueue中，并向第一个DataNode发送
  - 第一个DataNode收到packet后本地保存并发送第二个DataNode
  - 第二个DataNode收到packet后本地保存并发送第三个DataNode
  - 这一个过程中，上游节点同时发送下一个packet
  - **类比工厂中的流水线，流式其实也是变种的并行**
  - HDFS使用这种传输方式，副本数对于client是透明的（传输时间≈传输一份的时间+一点点）
  - 当block传输完成，DataNode各自向NameNode汇报，同时client继续传输下一个block
  - 所以，client的传输和block的汇报也是并行的
- HDFS读流程
  - 为了降低整体的带宽消耗和读取延时，HDFS会尽量让读取程序读取离他最近的副本
  - 语义：下载一个文件
    - Client和NameNode交互文件元数据获取file block location
    - NameNode会按距离策略排序返回
    - Client尝试下载block并校验数据完整性
  - 语义：下载一个文件其实是获取文件的所有block元数据，那么子集获取某些block应该成立
    - **HDFS支持Client给出文件的offset自定义连接哪些block的DataNode，自定义获取数据**
    - **这个是支持计算层的分治、并行计算的核心**
    

## HDFS的权限说明

> hdfs是一个文件系统（类unix、linux）   
> 
> 有用户的概念    
> 但hdfs没有相关命令和接口去创建用户      
> hdfs的机制：hdfs是信任客户端的     
> 1.默认情况下，使用操作系统提供的用户     
> 2.可以使用 kerberos、LDAP 等第三方用户认证系统进行扩展     
> 
> hdfs有超级用户的概念     
> linux系统中超级用户：root     
> hdfs系统中超级用户：是NameNode进程的启动用户      
> 
> hdfs有权限的概念     
> hdfs的权限是自己控制的，由hdfs的超级用户管理权限     