# lingzhi-cache 缓存模块

> Redis、Spring Cache 抽象

## 模块简介

`lingzhi-cache` 提供缓存相关能力：

- **Redis 集成** - Redisson 客户端
- **Spring Cache** - 注解式缓存
- **多级缓存** - 本地 + Redis

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-cache</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0
```

## 缓存注解

### @Cachable - 缓存读取

```java
@Cachable(key = "'user:' + #id", expire = 3600)
public User getUser(Long id) {
    return userMapper.selectById(id);
}
```

### @CacheEvict - 缓存清除

```java
@CacheEvict(key = "'user:' + #id")
public void deleteUser(Long id) {
    userMapper.deleteById(id);
}
```

### 手动使用

```java
@Autowired
private RedisTemplate<String, Object> redisTemplate;

// 存
redisTemplate.opsForValue().set("key", "value");

// 取
String value = (String) redisTemplate.opsForValue().get("key");

// 过期
redisTemplate.opsForValue().set("key", "value", 10, TimeUnit.MINUTES);

// 删
redisTemplate.delete("key");
```

### 分布式锁

```java
@Autowired
private RedissonClient redissonClient;

public void doWithLock(String lockKey, Runnable task) {
    RLock lock = redissonClient.getLock(lockKey);
    try {
        lock.lock();
        task.run();
    } finally {
        lock.unlock();
    }
}
```

## 配置项

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
          max-wait: -1ms
```

## 依赖

- lingzhi-core
- spring-boot-starter-data-redis
- redisson-spring-boot-starter
