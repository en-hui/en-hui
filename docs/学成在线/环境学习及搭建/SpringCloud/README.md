# SpringCloud

## Eureka环境
### Eureka服务端工程
> - Eureka的父工程管理SpringCloud版本
```xml
<dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.SR1</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
</dependencyManagement>
```
> - Eureka服务工程添加Eureka Server依赖
```xml
    <dependencies>
        <!--Eureka服务的依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>
```
> - Eureka服务工程启动类加注解
```
@EnableEurekaServer
```
> - Eureka服务工程配置文件
```yml
server:
  port: ${PORT:50101}
  # 设置VM options环境变量：-DPORT=50101 -DEUREKA_SERVER=http://localhost:50102/eureka/ -DEUREKA_DOMAIN=eureka01
  # ${PORT:50101}   如果没有传入参数，则用50101端口
spring:
  application:
    name: xc-govern-center
eureka:
  client:
    registerWithEureka: true # 服务注册，是否将自己注册到Eureka中
    fetchRegistry: true  # 服务发现，是否从Eureka中获取注册信息
    serviceUrl:  # Eureka客户端与Eureka服务端的交互地址，高可用状态配置对方的地址，单机状态配置自己（如果不配置则默认本机8761端口）
      defaultZone: ${EUREKA_SERVER:http://localhost:50102/eureka/}
  server:
    enable-self-preservation: false #是否开启自我保护模式
    eviction-interval-timer-in-ms: 60000 # 服务注册表清理间隔（单位毫秒，默认是60*1000）
  instance:
    hostname: ${EUREKA_DOMAIN:eureka01}
```
### Eureka客户端工程
> - 客户端工程添加Eureka Client依赖
```xml
        <!--Eureka 客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
```
> - 客户端工程启动类添加注解
```
// 从EurekaServer发现服务
@EnableDiscoveryClient 
```
> - 客户端工程配置文件
```yml
eureka:
  client:
    registerWithEureka: true # 服务注册开关
    fetchRegistry: true # 服务发现开关
    serviceUrl: # eureka服务端的地址，多个用逗号隔开
      defaultZone: ${EUREKA_SERVER:http://localhost:50101/eureka/,http://localhost:50102/eureka/}
  instance:
    prefer-ip-address: true # 将自己的ip注册到Eureka服务
    ip-address: ${IP_ADDRESS:127.0.0.1}
    instance-id: ${spring.application.name}:${server.port} # 指定实例id
```
