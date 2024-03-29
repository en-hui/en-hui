# 分布式事务专题

**单机事务概述**
> db本地事务如何保证：锁，redo，undo   
原子性a：undo log   
一致性c：最终目的   
隔离性i：锁机制   
持久性d：redo log   

**需求及问题**
> 需求案例   
> 在调用第三方支付接口之后，第三方支付接口会回调【支付服务-修改支付状态】，调用【订单服务-修改订单状态】      
> 在此过程中，必须要保证【支付服务和订单服务】保证事务特性。    
> 此案例中，由于【支付服务和订单服务】是分开部署的，所属不同进程，所以无法用单机事务来解决。    
> 从而引出分布式事务解决方案。    

**前置概念**
> 1.CAP理论：[百度百科-CAP定理](https://baike.baidu.com/item/CAP%E5%8E%9F%E5%88%99/5712863?fr=aladdin)   
> CAP原则又称CAP定理，指的是在一个分布式系统中， 
> Consistency（一致性）、 Availability（可用性）、Partition tolerance（分区容错性），三者不可得兼。   
> 一致性（C）：在分布式系统中的所有数据备份，在同一时刻是否同样的值。（等同于所有节点访问同一份最新的数据副本）   
> 可用性（A）：保证每个请求不管成功或者失败都有响应。   
> 分区容忍性（P）：系统中任意信息的丢失或失败不会影响系统的继续运作。      
>     
>
> 2.BASE理论：   
> BASE是Basically Available（基本可用）、Soft state（软状态）和Eventually consistent（最终一致性）三个短语的简写。    
> (1)基本可用：指分布式系统在出现不可预知故障的时候，允许损失部分可用性。   
> (2)弱状态：也称为软状态，和硬状态相对，是指允许系统中的数据存在中间状态，
>   并认为该中间状态的存在不会影响系统的整体可用性，即允许系统在不同节点的数据副本之间进行数据同步的过程存在延时。   
> (3)最终一致性：强调的是系统中所有的数据副本，在经过一段时间的同步后，最终能够达到一个一致的状态。
>   因此，最终一致性的本质是需要系统保证最终数据能够达到一致，而不需要实时保证系统数据的强一致性。   
>
> 3.事务分为刚性事务和柔性事务：   
> 一般分布式事务中，保证AP，舍弃C强一致性，保证最终一致性   
> 刚性事务(实时一致性)：acid     
> 柔性事务(最终一致性)：base理论  

**分布事务方案解决方案**
- [2pc、3pc](夯实基础/分布式事务解决方案/两阶段和三阶段/):两阶段提交模型和三阶段提交模型
- [lcn](夯实基础/分布式事务解决方案/lcn/):基于两阶段提交模型的框架-lcn（Lock(锁定事务单元),Confirm(确认事务),Notify(通知事务)）    
- [消息队列+事件表](夯实基础/分布式事务解决方案/消息队列+事件表/):消息队列+事件表
- [tcc](夯实基础/分布式事务解决方案/tcc/):tcc
- [可靠消息最终一致性](夯实基础/分布式事务解决方案/可靠消息最终一致性/):可靠消息最终一致性
- [最大努力通知](夯实基础/分布式事务解决方案/最大努力通知/):最大努力通知




