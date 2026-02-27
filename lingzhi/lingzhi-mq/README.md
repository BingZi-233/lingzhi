# lingzhi-mq 消息队列模块

> RabbitMQ、Kafka 封装

## 模块简介

`lingzhi-mq` 提供消息队列能力：

- **RabbitMQ** - 可靠消息
- **Kafka** - 高吞吐消息
- **消息监听** - @RabbitListener

## 快速开始

### 1. 引入依赖

```xml
<!-- RabbitMQ -->
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-mq</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

## 使用方式

### 发送消息

```java
@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void createOrder(Order order) {
        // 发送消息
        rabbitTemplate.convertAndSend(
            "order.exchange",    // 交换机
            "order.create",       // routingKey
            order                 // 消息内容
        );
    }
}
```

### 接收消息

```java
@Component
public class OrderListener {

    @RabbitListener(queues = "order.queue")
    public void handleOrder(Order order) {
        // 处理消息
    }
}
```

### 消息确认

```yaml
spring:
  rabbitmq:
    publisher-confirm-type: correlated
    publisher-returns: true
```

## 依赖

- lingzhi-core
- spring-boot-starter-amqp
