# lingzhi-log 日志模块

> 统一日志切面

## 模块简介

`lingzhi-log` 提供日志记录能力：

- **操作日志** - 记录用户操作
- **方法日志** - 方法调用日志
- **日志切面** - AOP 自动记录

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-log</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用方式

### 操作日志

```java
@Log(title = "用户管理", businessType = BusinessType.INSERT)
@PostMapping("/user")
public Result<Void> addUser(@RequestBody User user) {
    return Result.success();
}
```

### 日志注解

| 属性 | 说明 |
|------|------|
| title | 操作模块 |
| businessType | 操作类型 |
| operator | 操作人 |
| isSaveRequestParams | 是否保存请求参数 |
| isSaveResponseParams | 是否保存响应参数 |

### 业务类型

```java
public enum BusinessType {
    OTHER,
    INSERT,
    UPDATE,
    DELETE,
    GRANT,
    EXPORT,
    IMPORT,
    FORCE,
    CLEAN
}
```

## 依赖

- lingzhi-core
- spring-boot-starter-aop
