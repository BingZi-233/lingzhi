# lingzhi-lock 分布式锁模块

> Redis 分布式锁

## 模块简介

`lingzhi-lock` 提供分布式锁能力：

- **@Lock 注解** - 分布式锁注解
- **Redis 锁** - 基于 Redisson
- **可重入** - 同一线程可重复获取
- **自动续期** - 防止提前过期

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-lock</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用方式

### @Lock 注解

```java
@Service
public class OrderService {

    @Lock(key = "'order:' + #orderId")
    public void processOrder(Long orderId) {
        // 业务逻辑
    }
    
    // 等待5秒获取锁，持有30秒
    @Lock(key = "'order:' + #orderId", waitTime = 5, leaseTime = 30)
    public void processOrderWithWait(Long orderId) {
        // 业务逻辑
    }
}
```

### 手动使用

```java
@Service
public class LockService {

    @Autowired
    private RedissonClient redissonClient;

    public void doWithLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        
        try {
            lock.lock();
            // 业务逻辑
        } finally {
            lock.unlock();
        }
    }
}
```

## 注解属性

| 属性 | 说明 | 默认值 |
|------|------|--------|
| key | 锁的 key，支持 SpEL | - |
| prefix | 锁前缀 | lock |
| waitTime | 等待时间(秒) | 0 |
| leaseTime | 持有锁时间(秒) | 30 |

## 依赖

- lingzhi-core
- redisson-spring-boot-starter
