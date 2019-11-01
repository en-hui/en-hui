# RabbitMQ

## 基本介绍
-  RabbitMQ     
MQ全称为Message Queue，即消息队列， RabbitMQ是由erlang语言开发，基于AMQP（Advanced Message Queue 高级消息队列协议）协议实现的消息队列，
它是一种应用程序之间的通信方法，消息队列在分布式系统开发中应用非常广泛。   
     
开发中消息队列通常有如下应用场景：   
1. 任务异步处理。   
  将不需要同步处理的并且耗时长的操作由消息队列通知消息接收方进行异步处理。提高了应用程序的响应时间。
2. 应用程序解耦合  
  MQ相当于一个中介，生产方通过MQ与消费方交互，它将应用程序进行解耦合。
  
-  AMQP     
AMQP是一套公开的消息队列协议，最早在2003年被提出，它旨在从协议层定义消息通信数据的标准格式，
 为的就是解决MQ市场上协议不统一的问题。RabbitMQ就是遵循AMQP标准协议开发的MQ服务。 
 
 - JMS（Java消息服务）        
JMS是java提供的一套消息服务API标准，其目的是为所有的java应用程序提供统一的消息通信的标准，
类似java的 jdbc，只要遵循jms标准的应用程序之间都可以进行消息通信。    
它和AMQP有什么不同，jms是java语言专属的消息服务标准，它是在api层定义标准，并且只能用于java应用；而AMQP是在协议层定义的标准，是跨语言的 。

## 工作原理
下图是RabbitMQ的基本结构    
![Alt](./RabbitMQimg/rabbitmq原理.png)   
组成部分说明： 
- Broker:消息队列服务进程，此进程包括两个部分，Exchange和Queue
- Exchange:消息队列交换机，按一定的规则将消息路由转发到某个队列，对消息进行过滤
- Queue：消息队列，存储消息的队列，消息到达队列并转发给指定的消费方
- Producer：消息生产者，即生产方客户端，生产方客户端将消息发送到MQ
- Consumer：消息消费者，即消费方客户端，接收MQ转发的消息

消息发送接收流程：   
-----发送消息-----
- 生产者和Broker建立TCP连接
- 生产者和Broker建立通道
- 生产者通过通道将消息发送给Broker，由Exchange将消息进行转发
- Exchange将消息转发到指定的Queue（队列）

-----接收消息-----
- 消费者和Broker建立TCP连接
- 消费者和Broker建立通道
- 消费者监听指定的Queue（队列）
- 当有消息到达Queue时Broker默认将消息推送给消费者
- 消费者接收到消息

## 入门程序
生产者：
```
import com.rabbitmq.client.*;
/**
 * rabbitmq入门程序-生产者
 * @Author: HuEnhui
 * @Date: 2019/11/1 16:11
 */
public class Producer01 {

    private static final String QUEUE = "helloword";

    public static void main(String[] args) {
        // 通过连接工厂创建新的连接和mq建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("39.105.176.190");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("xuecheng");
        connectionFactory.setPassword("xuecheng");

        // 设置虚拟机，一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("xuecheng");

        Connection connection = null;
        Channel channel = null;
        try {
            // 创建连接
            connection = connectionFactory.newConnection();
            // 创建会话通道,生产者和mq服务所有通信都在channel通道中完成
             channel= connection.createChannel();
            // 声明队列:如果队列在mq中不存在，就创建
            channel.queueDeclare(QUEUE,true,false,false,null);

            String message = "hello,我是消息";
            channel.basicPublish("",QUEUE,null,message.getBytes());
            System.out.println("发送-------------"+message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 先关通道
                channel.close();
                // 后关连接
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
```
消费者：
```
import com.rabbitmq.client.*;
/**
 * rabbitmq入门程序-消费者
 * @Author: HuEnhui
 * @Date: 2019/11/1 16:32
 */
public class Consumer01 {

    private static final String QUEUE = "helloword";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 通过连接工厂创建新的连接和mq建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("39.105.176.190");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("xuecheng");
        connectionFactory.setPassword("xuecheng");

        // 设置虚拟机，一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("xuecheng");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建会话通道
        Channel channel = connection.createChannel();
        // 声明队列:如果队列在mq中不存在，就创建
        channel.queueDeclare(QUEUE,true,false,false,null);

        // 实现消费方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            /**
             *  接收到消息后此方法被调用
             * @author: HuEnhui
             * @date: 2019/11/1 16:47
             * @param consumerTag 消费者标签，用来标识消费者的，可以在监听队列时设置channel.basicConsume
             * @param envelope 信封，通过envelope可以得到交换机等信息
             * @param properties 消息的属性
             * @param body 消息内容
             * @return: void
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 交换机
                String exchange = envelope.getExchange();
                // 消息id，mq在channel中用来标识消息的id，可用于确认消息已接收
                long deliveryTag = envelope.getDeliveryTag();
                // 消息内容
                String message = new String(body,"utf-8");
                System.out.println("接收到-------------"+message);
            }
        };
        
        
        // 监听队列
        channel.basicConsume(QUEUE,true,defaultConsumer);
    }
}
```

