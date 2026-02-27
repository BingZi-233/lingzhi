# lingzhi-lock 分布式锁模块

> Redis 分布式锁

## 模块简介

`lingzhi-lock` 提供分布式锁能力：

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

### 注解式

```java
@Service
public class OrderService {

    @Lock(keys = "'order:' + #orderId")
    public void processOrder(Long orderId) {
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
    
    // 带过期时间
    public void doWithLock(String lockKey, long time) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock(time, TimeUnit.SECONDS);
        } finally {
            lock.unlock();
        }
    }
}
```

## 注解属性

| 属性 | 说明 | 默认值 |
|------|------|--------|
| keys | 锁的 key | - |
| expire | 过期时间(秒) | 30 |
| waitTime | 等待时间(秒) | 0 |

## 依赖

- lingzhi-core
- redisson-spring-boot-starter
