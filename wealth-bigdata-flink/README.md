```txt
* flink编程模版：流和批的api不同，但建议直接使用流的api   
* 1、获取flink的执行环境 env  
* 2、加载数据 -- source
* 3、对加载的数据转换 -- transformation
* 4、对结果进行保存或打印 -- sink
* 5、触发程序的提交运行 -- env.execute() --> 只有流的api需要

获取env的三种写法：
// 1.准备flink环境：如果在本地启动就会创建本地env；如果在集群中启动就会创建集群env
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
// 2.指定并行度创建本地env
LocalStreamEnvironment localEnv = StreamExecutionEnvironment.createLocalEnvironment(5);
// 3.指定远程JobManagerIp和RPC端口，及运行程序所在jar包及其依赖包
StreamExecutionEnvironment remoteEnv = StreamExecutionEnvironment.createRemoteEnvironment("hostname", 6021, 5, "application.jar");

// 流式api可以指定模式,可以在代码中指定，也可以在提交任务时指定：flink run -Dexecution.runtime-mode=BATCH xxx.jar
env.setRuntimeMode(RuntimeExecutionMode.BATCH);

Unable to make field private final byte[] java.lang.String.value accessible: module java.base does not "opens java.lang" to unnamed module
报错说明jdk版本太高，换为jdk1.8
```

```text
# flink-example 是官网初始化命令创建出来的

mvn archetype:generate \
    -DarchetypeGroupId=org.apache.flink \
    -DarchetypeArtifactId=flink-walkthrough-datastream-java \
    -DarchetypeVersion=1.15.2 \
    -DgroupId=com.enhui \
    -DartifactId=flink-example \
    -Dversion=0.1 \
    -Dpackage=com.enhui.flink.example \
    -DinteractiveMode=false
```

> 在 IDE 中运行该项目可能会遇到 java.langNoClassDefFoundError 的异常。这很可能是因为运行所需要的 Flink 的依赖库没有默认被全部加载到类路径（classpath）里。
> 
> IntelliJ IDE：前往 运行 > 编辑配置 > 修改选项 > 选中 将带有 “provided” 范围的依赖项添加到类路径。这样的话，运行配置将会包含所有在 IDE 中运行所必须的类。

