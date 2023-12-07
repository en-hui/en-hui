## jstat使用

```
使用 jstat 或 jstat -help 可以查看帮助
使用 jstat -options 可以查看所有options

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
