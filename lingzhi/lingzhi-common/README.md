# lingzhi-common 公共配置模块

> 统一响应、异常处理、全局拦截器

## 模块简介

`lingzhi-common` 提供 Web 应用的公共配置：

- **统一响应** - `Result<T>` 统一返回格式
- **全局异常** - `GlobalExceptionHandler` 统一异常处理
- **请求拦截** - `TraceInterceptor` 请求链路追踪
- **Jackson 配置** - 日期、数字格式化

## 快速开始

引入依赖即可自动配置：

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 统一响应

### Result 结构

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "requestId": "xxx",
  "success": true
}
```

### 使用方式

```java
@RestController
public class UserController {

    // 成功响应（无数据）
    public Result<Void> success() {
        return Result.success();
    }

    // 成功响应（带数据）
    public Result<User> getUser(Long id) {
        return Result.success(user);
    }

    // 成功响应（带消息）
    public Result<User> createUser(User user) {
        return Result.success("创建成功", user);
    }

    // 失败响应
    public Result<Void> error() {
        return Result.error("操作失败");
    }

    // 失败响应（带错误码）
    public Result<Void> error(int code, String message) {
        return Result.error(code, message);
    }
}
```

## 全局异常处理

自动处理以下异常：

| 异常类型 | HTTP 状态码 | 响应码 |
|----------|-------------|--------|
| ServiceException | 400 | 1000 |
| IllegalArgumentException | 400 | 400 |
| ResourceNotFoundException | 404 | 404 |
| UnauthorizedException | 401 | 401 |
| ForbiddenException | 403 | 403 |
| Exception | 500 | 500 |

### 自定义异常

```java
// 业务异常
throw new ServiceException("业务处理失败");

// 带错误码
throw new ServiceException(1001, "用户名已存在");

// 资源不存在
throw new ResourceNotFoundException("用户不存在");
```

## 请求追踪

自动为每个请求生成 `requestId`，并通过响应头返回：

```
X-Request-Id: abc123456789
```

## Jackson 配置

自动配置：

- 日期格式：`yyyy-MM-dd HH:mm:ss`
- 时区：JVM 默认时区
- 数字类型：不过滤 `NaN` 和 `Infinity`

## 配置项

```yaml
lingzhi:
  common:
    jackson:
      date-format: "yyyy-MM-dd HH:mm:ss"
      timezone: "Asia/Shanghai"
```

## 依赖

- lingzhi-core
- spring-boot-starter-web
