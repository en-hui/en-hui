# lcn
> lcn是基于两阶段提交模型实现的分布式事务框架   
> Lock(锁定事务单元),Confirm(确认事务),Notify(通知事务)   
> 额外需要的技术：Redis、tx-manager服务      

github文档   
https://www.codingapi.com/docs/txlcn-principle-control/

官方原理图   
![Alt](../../img/分布式事务-lcn官方原理图.jpg)    

**核心步骤**    

    1.创建事务组
    是指在事务发起方开始执行业务代码之前先调用TxManager创建事务组对象，然后拿到事务标示GroupId的过程。

    2.加入事务组
    添加事务组是指参与方在执行完业务方法以后，将该模块的事务信息通知给TxManager的操作。

    3、通知事务组
    是指在发起方执行完业务代码以后，将发起方执行结果状态通知给TxManager,TxManager将根据事务最终状态和事务组的信息来通知相应的参与模块提交或回滚事务，并返回结果给事务发起方。

## 案例实现
> 3个库：lcn-order、lcn-pay、tx-manager(com.codingapi.txlcn txlcn-tm jar包中有sql脚本)   
> 1个缓存：redis   
> 3个项目：lcn-tm、lcn-order、lcn-pay    
> 见SpringCloud-Taxi项目   