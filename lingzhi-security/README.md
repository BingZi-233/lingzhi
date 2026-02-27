# lingzhi-security 安全模块

> Spring Security + JWT

## 模块简介

`lingzhi-security` 提供安全认证相关能力：

- **JWT 认证** - Token 生成和验证
- **@RequiresPermissions** - 权限注解
- **@RequiresRoles** - 角色注解
- **@Anonymous** - 匿名访问
- **SecurityContext** - 安全上下文

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-security</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
lingzhi:
  security:
    jwt:
      secret: your-secret-key
      expiration: 3600000
```

## 注解

### @RequiresPermissions - 权限校验

```java
@RequiresPermissions("user:add")
@PostMapping("/user")
public Result<Void> addUser() {
    return Result.success();
}

// 多个权限
@RequiresPermissions({"user:add", "user:edit"})
@PostMapping("/user")
public Result<Void> addUser() {
    return Result.success();
}
```

### @RequiresRoles - 角色校验

```java
@RequiresRoles("admin")
@GetMapping("/admin")
public Result<Void> admin() {
    return Result.success();
}

// 全部角色
@RequiresRoles(value = {"admin", "manager"}, mode = Mode.ALL)
@GetMapping("/admin")
public Result<Void> admin() {
    return Result.success();
}

// 任一角色
@RequiresRoles(value = {"admin", "manager"}, mode = Mode.ANY)
@GetMapping("/admin")
public Result<Void> admin() {
    return Result.success();
}
```

### @Anonymous - 匿名访问

```java
@Anonymous
@PostMapping("/login")
public Result<LoginResponse> login() {
    return Result.success();
}
```

## 安全上下文

```java
// 获取当前用户ID
Long userId = SecurityContext.getUserId();

// 获取当前用户名
String username = SecurityContext.getUsername();

// 判断是否已登录
boolean authenticated = SecurityContext.isAuthenticated();
```

## JWT 工具类

```java
@Autowired
private JwtUtils jwtUtils;

// 生成 Token
String token = jwtUtils.generateToken(userId, username);

// 验证 Token
boolean valid = jwtUtils.validateToken(token);

// 获取用户ID
Long userId = jwtUtils.getUserId(token);

// 刷新 Token
String newToken = jwtUtils.refreshToken(token);
```

## 配置项

```yaml
lingzhi:
  security:
    jwt:
      secret: your-256-bit-secret-key
      expiration: 3600000        # 1小时
      header: Authorization
      prefix: Bearer
    anonymous:
      urls: /api/auth/**,/api/public/**
```

## 注解属性

### @RequiresPermissions

| 属性 | 说明 |
|------|------|
| value | 权限标识数组 |
| permissions | 权限表达式数组 |

### @RequiresRoles

| 属性 | 说明 | 默认值 |
|------|------|--------|
| value | 角色数组 | - |
| mode | 验证模式：ALL/ANY | ALL |

## 依赖

- lingzhi-core
- lingzhi-common
- spring-boot-starter-security
- jjwt-api
