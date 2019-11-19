# SpringCloud

## Eureka环境
### Eureka服务端工程（注册中心）
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
  port: ${PORT:50101}  # 如果没有传入参数，则用50101端口 传入参数的方式：设置VM options环境变量：-DPORT=50101 -DEUREKA_SERVER=http://localhost:50102/eureka/ -DEUREKA_DOMAIN=eureka01 
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
### Eureka客户端工程（需要注册到中心的工程）
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
## 远程调用Feign和Ribbon
### Ribbon客户端负载均衡器（调用其他服务的工程）
> - 导入依赖
```xml
<dependencys>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
    </dependency>
</dependencys>
```
> - 添加配置
```yml
ribbon:
  MaxAutoRetries: 2 #最大重试次数，当Eureka中可以找到服务，但是服务连不上时将会重试
  MaxAutoRetriesNextServer: 3 #切换实例的重试次数
  OkToRetryOnAllOperations: false  #对所有操作请求都进行重试，如果是get则可以，如果是post，put等操作没有实现幂等的情况下是很危险的,所以设置为false
  ConnectTimeout: 5000  #请求连接的超时时间
  ReadTimeout: 6000 #请求处理的超时时间
```
> - 启动类配置RestTemplate(用于测试ribbon)
```
    @Bean
    @LoadBalanced // 开启客户端负载均衡  RibbonLoadBalancerClient 拦截器的 execute 方法获取服务ip和端口
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
```
> - 测试程序
```java
package com.xuecheng.manage_course.dao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
/**
 * ribbon环境测试
 * @Author: HuEnhui
 * @Date: 2019/11/19 20:49
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {
    @Autowired
    private RestTemplate restTemplate;
    /**
     *  测试ribbon
     */
    @Test
    public void testRibbon() {
        //确定要获取的服务名
        String serviceId = "XC-SERVICE-MANAGE-CMS";
        for (int i = 0; i < 10; i++) {
            //ribbon客户端从eurekaServer中获取服务列表,根据服务名获取服务列表      RibbonLoadBalancerClient 拦截器的 execute 方法获取服务ip和端口
            ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://" + serviceId + "/cms/page/get/5a754adf6abb500ad05688d9", Map.class);
            Map body = forEntity.getBody();
            System.out.println(body);
        }
    }
}
```
### Feign
使用Feign的注意：     
1.feignClient 接口中，有参数在@PathVariable和@RequestParam中添加("")     
2.feignClient返回值为自定义类的对象，必须有无参构造函数      
> - 导入依赖
```xml
        <!--<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-ribbon</artifactId>
        </dependency>-->
        <!--下面这个包含了上面这个ribbon依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```
> - 创建一个 feignClient 接口
```java
package com.xuecheng.manage_course.client;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(value = "XC-SERVICE-MANAGE-CMS")   // 指定远程调用的服务名
public interface CmsPageClient {
    /**
     *  根据页面id查询页面信息，远程调用cms请求数据
     * @author: HuEnhui
     * @date: 2019/11/19 21:11
     * @param id
     * @return:
     */
    @GetMapping("/cms/page/get/{id}") //标识远程调用的http方法类型为get，和地址
    public CmsPage findCmsPageById(@PathVariable("id") String id);
}
```
> - 测试方法
```java
package com.xuecheng.manage_course.dao;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.client.CmsPageClient;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
/**
 * Feign环境测试
 * @Author: HuEnhui
 * @Date: 2019/11/19 20:49
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFeign {
    @Autowired
    private CmsPageClient cmsPageClient; // 接口代理对象，由Feign生成代理对象
    /**
     *  测试Feign
     */
    @Test
    public void testFeign() {
        // 发起远程调用
        CmsPage cmsPage = cmsPageClient.findCmsPageById("5a754adf6abb500ad05688d9");
        System.out.println(cmsPage);
    }
}
```
> - 启动类开启开启FeignClient
```
//开启FeignClient
@EnableFeignClients  
```
