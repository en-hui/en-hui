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