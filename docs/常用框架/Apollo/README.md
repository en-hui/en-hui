# Apollo 配置中心

> 角色介绍:
> portal:配置的UI（配置管理系统）
> admin service:对配置的增删改查（配置管理系统）
> config service：与客户端建立连接（集成了eureka）

## 服务端环境搭建
1. 下载jar包（3个）
https://github.com/ctripcorp/apollo/releases/tag/v1.8.1

2. 下载sql，并创建库表
https://github.com/nobodyiam/apollo-build-scripts/tree/master/sql

3. config service 配置修改
application-github.properties
```properties
# DataSource
spring.datasource.url = jdbc:mysql://fill-in-the-correct-server:3306/ApolloConfigDB?characterEncoding=utf8&serverTimezone=Asia/Shanghai
spring.datasource.username = FillInCorrectUser
spring.datasource.password = FillInCorrectPassword
```
4. 启动config服务
java -jar apollo-configservice-1.8.1.jar

5. admin service 配置修改
application-github.properties
```properties
# DataSource
spring.datasource.url = jdbc:mysql://fill-in-the-correct-server:3306/ApolloConfigDB?characterEncoding=utf8&serverTimezone=Asia/Shanghai
spring.datasource.username = FillInCorrectUser
spring.datasource.password = FillInCorrectPassword
```

6. 启动admin服务
java -jar apollo-adminservice-1.8.1.jar

7. portal 配置修改
application-github.properties
```properties
# DataSource
spring.datasource.url = jdbc:mysql://fill-in-the-correct-server:3306/ApolloPortalDB?characterEncoding=utf8&serverTimezone=Asia/Shanghai
spring.datasource.username = FillInCorrectUser
spring.datasource.password = FillInCorrectPassword
```
apollo-env.properties
```properties
# 不同环境的config
local.meta=http://localhost:8080
dev.meta=http://localhost:8080
#fat.meta=http://fill-in-fat-meta-server:8080
#uat.meta=http://fill-in-uat-meta-server:8080
#lpt.meta=${lpt_meta}
#pro.meta=http://fill-in-pro-meta-server:8080
```

8. 启动 portal 服务
java -jar apollo-portal-1.8.1.jar