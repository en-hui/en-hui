# 使用JaCoCo代理分析测试覆盖情况

## 第一步：下载JaCoCo代理jar文件
访问官网(https://www.jacoco.org/jacoco/)下载【jacocoagent.jar】、【jacococli.jar】

## 启动命令附加JaCoCo代理
```bash
java -javaagent:/path/to/jacocoagent.jar=destfile=/path/to/jacoco.exec,includes=com.yourpackage.*,output=file -jar your-app.jar

-- 实际例子(添加一段JVM_OPTIONS)
 -javaagent:/opt/jacoco/jacocoagent.jar=destfile=/opt/logs/jacoco.exec,includes=com.datapipeline.*,output=file
```
- 关键参数：
  - destfile：覆盖率数据输出路径（默认为 jacoco.exec）。
  - includes：要检测的包（支持通配符，如 com.yourpackage.*）。
  - output：输出方式（file 写入文件，tcpserver 或 tcpclient 用于远程监控）。

## 执行测试操作（自动化&手工）

## 停止应用并生成报告
关闭应用，生成 .exec 文件   
使用 【jacococli.jar】 生成报告
```bash 
java -jar /path/to/jacococli.jar report /path/to/jacoco.exec \
  --classfiles /path/to/your-app-classes/ \
  --sourcefiles /path/to/your-app-sources/ \
  --html /path/to/report/html \
  --csv /path/to/report/coverage.csv \
  --xml /path/to/report/coverage.xml

-- 实际例子  
java -jar jacococli.jar report jacoco.exec --classfiles /root/code/connectors/connector-newpostgres/base/connector-newpostgres/source/target/newpostgres-base-source-0.34.1.jar --sourcefiles /root/code/connectors/connector-newpostgres/base/connector-newpostgres/source/src/main/java/ --html ./html
```
- 参数说明
  - --classfiles：编译后的 .class 文件目录（如 target/classes）。
  - --sourcefiles：源代码目录（如 src/main/java）。
  - 输出格式支持 HTML、CSV、XML。