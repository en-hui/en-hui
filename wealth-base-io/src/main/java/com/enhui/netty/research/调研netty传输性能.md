使用netty实现rpc

两个java进程，进程A使用jdbc查询数据库A，将数据发给进程B，进程B将数据插入到数据库B

场景：

flink的算子间数据交互？相同jvm使用内存传输，同主机和不同主机的跨进程使用rpc传输，为什么能达到很高效？



服务A（source）、服务B（数据转换算子）、服务C（sink）

服务A和服务C分别是netty服务端

服务B是netty客户端
- 向A发起请求，请求读取某文件（可以是list）