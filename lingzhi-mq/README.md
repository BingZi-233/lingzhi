# lingzhi-mq 消息队列模块

> RabbitMQ、Kafka

## 模块简介

`lingzhi-mq` 提供消息队列能力：

- **@MqProducer** - 消息生产者
- **@MqConsumer** - 消息消费者
- **RabbitMQ** - 可靠消息

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

## 注解

### @MqProducer - 消息生产者

```java
@Service
public class OrderService {

    @MqProducer(topic = "order-created", tag = "new")
    public void sendOrderCreated(Order order) {
        // 消息自动发送
    }
}
```

### @MqConsumer - 消息消费者

```java
@Component
public class OrderListener {

    @MqConsumer(topic = "order-created", tag = "new", group = "order-group")
    public void handleOrderCreated(Message msg) {
        // 处理消息
    }
}
```

## 注解属性

### @MqProducer

| 属性 | 说明 | 默认值 |
|------|------|--------|
| topic | 消息 topic | - |
| tag | 消息 tag | - |
| keys | 消息 keys | - |
| delayLevel | 延迟等级：1-18 | 0 |

### @MqConsumer

| 属性 | 说明 | 默认值 |
|------|------|--------|
| topic | 消息 topic | - |
| tag | 消息 tag，* 表示所有 | * |
| group | 消费者组 | - |
| consumeThreadMin | 并发数 | 1 |
| consumeThreadMax | 最大并发数 | 1 |

## 延迟等级

| 等级 | 延迟 |
|------|------|
| 1 | 1秒 |
| 2 | 5秒 |
| 3 | 10秒 |
| 4 | 30秒 |
| 5 | 1分钟 |
| ... | ... |

## 依赖

- lingzhi-core
- spring-boot-starter-amqp
