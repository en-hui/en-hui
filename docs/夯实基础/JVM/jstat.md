## jstat使用

使用 jstat 或 jstat -help 可以查看帮助

- option:参数选项
- -t: 可以在打印的列加上Timestamp列，用于显示系统运行的时间
- -h: 可以在周期性数据数据的时候，可以在指定输出多少行以后输出一次表头
- vmid: Virtual Machine ID（ 进程的 pid）
- interval: 执行每次的间隔时间，单位为毫秒
- count: 用于指定输出多少次记录，缺省则会一直打印

使用 jstat -options 可以查看所有options, option有如下选项：
```
-class : 显示ClassLoad的相关信息
-compiler : 显示JIT编译的相关信息
-gc : 显示和gc相关的堆信息
-gccapacity : 显示各个代的容量以及使用情况
-gccause : 显示垃圾回收的相关信息（通-gcutil）,同时显示最后一次或当前正在发生的垃圾回收的诱因
-gcmetacapacity : 显示metaspace的大小
-gcnew : 显示新生代信息
-gcnewcapacity : 显示新生代大小和使用情况
-gcold : 显示老年代和永久代的信息
-gcoldcapacity : 显示老年代的大小
-gcutil : 显示垃圾收集信息
-printcompilation : 输出JIT编译的方法信息
```
