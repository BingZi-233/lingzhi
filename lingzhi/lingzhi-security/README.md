# lingzhi-security 安全模块

> Spring Security + JWT

## 模块简介

`lingzhi-security` 提供安全认证相关能力：

- **JWT 认证** - Token 生成和验证
- **Spring Security** - 权限控制
- **用户认证** - 登录、登出
- **权限注解** - 方法级权限控制

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

## 使用方式

### 登录

```java
@RestController
public class AuthController {

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        // 验证用户名密码
        User user = userService.login(request.getUsername(), request.getPassword());
        
        // 生成 Token
        String token = jwtUtil.generateToken(user.getId());
        
        return Result.success(new LoginResponse(token, user));
    }
}
```

### 获取当前用户

```java
@Service
public class UserService {

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }
}
```

### 权限控制

```java
// 接口级别
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/users")
    public Result<List<User>> listUsers() {
        return Result.success(userService.list());
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/user/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        return Result.success();
    }
}
```

### 注解说明

| 注解 | 说明 |
|------|------|
| @PreAuthorize | 方法执行前权限校验 |
| @PostAuthorize | 方法执行后权限校验 |
| @Secured | 角色校验 |
| @RolesAllowed | 角色校验 |

## JWT Token

### 包含信息

```json
{
  "sub": "1234567890",
  "username": "admin",
  "roles": ["admin", "user"],
  "iat": 1516239022,
  "exp": 1516242622
}
```

### 验证

```java
public boolean validateToken(String token) {
    try {
        Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token);
        return true;
    } catch (JwtException e) {
        return false;
    }
}
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

## 依赖

- lingzhi-core
- spring-boot-starter-security
- jjwt-api
