# lingzhi-monitor 监控模块

> Actuator、Prometheus、限流、幂等、脱敏

## 模块简介

`lingzhi-monitor` 提供监控和安全相关能力：

- **Actuator** - Spring Boot 端点
- **Prometheus** - 指标收集
- **@RateLimiter** - 接口限流
- **@Idempotent** - 接口幂等
- **@Sensitive** - 数据脱敏

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-monitor</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
```

## 注解

### @RateLimiter - 限流

```java
@RateLimiter(key = "api:user:list", permitsPerSecond = 10)
@GetMapping("/users")
public Result<List<User>> listUsers() {
    return Result.success(userService.list());
}
```

### @Idempotent - 幂等

```java
@Idempotent(key = "'order:' + #request.orderNo", expire = 30)
@PostMapping("/createOrder")
public Result<Void> createOrder(@RequestBody OrderRequest request) {
    return Result.success();
}
```

### @Sensitive - 数据脱敏

```java
public class User {
    @Sensitive(SensitiveType.NAME)
    private String name;
    
    @Sensitive(SensitiveType.PHONE)
    private String phone;
    
    @Sensitive(SensitiveType.EMAIL)
    private String email;
    
    @Sensitive(SensitiveType.ID_CARD)
    private String idCard;
}
```

## 端点

| 端点 | 说明 |
|------|------|
| /actuator/health | 健康检查 |
| /actuator/info | 应用信息 |
| /actuator/metrics | 指标 |
| /actuator/env | 环境变量 |
| /actuator/beans | Bean 列表 |
| /actuator/httptrace | HTTP 追踪 |

## 注解属性

### @RateLimiter

| 属性 | 说明 | 默认值 |
|------|------|--------|
| key | 限流 key | - |
| permitsPerSecond | 每秒请求数 | 10 |
| timeout | 获取令牌超时时间(毫秒) | 0 |
| message | 限流提示消息 | 请求过于频繁 |

### @Idempotent

| 属性 | 说明 | 默认值 |
|------|------|--------|
| key | 幂等 key，支持 SpEL | - |
| expire | 过期时间(秒) | 60 |
| message | 错误消息 | 请求重复 |

### @Sensitive

| 属性 | 说明 |
|------|------|
| type | 脱敏类型 |
| prefixLen | 自定义前缀长度 |
| suffixLen | 自定义后缀长度 |

## 脱敏类型

| 类型 | 示例 |
|------|------|
| NAME | 张*丰 |
| PHONE | 138****1234 |
| EMAIL | a***@example.com |
| ID_CARD | 3201***********1234 |
| BANK_CARD | 6228***********1234 |
| ADDRESS | 江苏省南京市*** |

## 依赖

- lingzhi-core
- spring-boot-starter-actuator
- micrometer-registry-prometheus
