# lingzhi-event 事件驱动模块

> Spring Event

## 模块简介

`lingzhi-event` 提供事件驱动能力：

- **@EventPublish** - 发布事件
- **@EventListener** - 监听事件
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

### @EventPublish - 发布事件

```java
@Service
public class UserService {

    @EventPublish(eventClass = UserRegisterEvent.class)
    public void register(User user) {
        userMapper.insert(user);
    }
}
```

### @EventListener - 监听事件

```java
@Component
public class UserEventListener {

    @EventListener(eventClass = UserRegisterEvent.class)
    public void handleUserRegister(UserRegisterEvent event) {
        // 发送欢迎邮件
        log.info("用户注册: {}", event.getUsername());
    }
}
```

## 注解属性

### @EventPublish

| 属性 | 说明 |
|------|------|
| eventClass | 事件类 |
| source | 事件源 |
| params | 事件参数，支持 SpEL |

### @EventListener

| 属性 | 说明 | 默认值 |
|------|------|--------|
| eventClass | 监听的事件类 | - |
| phase | 事务阶段：BEFORE_COMMIT/AFTER_COMMIT/AFTER_ROLLBACK | AFTER_COMMIT |

## 事务阶段

| 阶段 | 说明 |
|------|------|
| BEFORE_COMMIT | 事务提交前 |
| AFTER_COMMIT | 事务提交后 |
| AFTER_ROLLBACK | 事务回滚后 |

## 依赖

- lingzhi-core
- spring-context
