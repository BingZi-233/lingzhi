# lingzhi-event 事件驱动模块

> Spring Event

## 模块简介

`lingzhi-event` 提供事件驱动能力：

- **ApplicationEvent** - 事件定义
- **@EventListener** - 事件监听
- **TransactionEvent** - 事务事件

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-event</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用方式

### 定义事件

```java
public class UserRegisterEvent extends ApplicationEvent {
    
    private final Long userId;
    private final String username;
    
    public UserRegisterEvent(Object source, Long userId, String username) {
        super(source);
        this.userId = userId;
        this.username = username;
    }
}
```

### 发布事件

```java
@Service
public class UserService {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void register(User user) {
        userMapper.insert(user);
        
        // 发布事件
        publisher.publishEvent(
            new UserRegisterEvent(this, user.getId(), user.getUsername())
        );
    }
}
```

### 监听事件

```java
@Component
public class UserEventListener {

    @EventListener
    public void handleUserRegister(UserRegisterEvent event) {
        // 发送欢迎邮件
        log.info("用户注册: {}", event.getUsername());
    }
}
```

### 事务事件

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void handleAfterCommit(UserRegisterEvent event) {
    // 事务提交后执行
}
```

## 依赖

- lingzhi-core
- spring-context
