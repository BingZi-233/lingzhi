# lingzhi-web Web 模块

> 参数校验、分页

## 模块简介

`lingzhi-web` 提供 Web 开发常用能力：

- **参数校验** - @Valid 注解式校验
- **分页** - PageHelper 分页
- **跨域** - CORS 配置

## 快速开始

引入依赖即可：

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-web</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 参数校验

### 定义校验规则

```java
@Data
public class UserRequest {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度3-20")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码至少6位")
    private String password;
    
    @Email(message = "邮箱格式错误")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;
}
```

### 使用校验

```java
@PostMapping("/user")
public Result<Void> createUser(@Valid @RequestBody UserRequest request) {
    // 校验通过才会执行
    userService.create(request);
    return Result.success();
}
```

### 常用注解

| 注解 | 说明 |
|------|------|
| @NotNull | 不能为 null |
| @NotBlank | 不能为空字符串 |
| @NotEmpty | 不能为空集合 |
| @Size | 长度范围 |
| @Min / @Max | 数值范围 |
| @Email | 邮箱格式 |
| @Pattern | 正则表达式 |
| @Valid | 嵌套校验 |

## 分页

### 使用方式

```java
@GetMapping("/users")
public Result<PageResult<User>> listUsers(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
    
    IPage<User> page = userService.page(
        new Page<>(pageNum, pageSize),
        new QueryWrapper<User>()
    );
    
    return Result.success(PageResult.of(page));
}
```

### PageResult

```java
@Data
public class PageResult<T> {
    private List<T> list;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pages;
}
```

## CORS 配置

自动配置跨域，支持：

- 所有接口
- 自定义源
- Cookie 支持

```yaml
lingzhi:
  web:
    cors:
      enabled: true
      allowed-origins: "*"
      allowed-methods: GET,POST,PUT,DELETE
      allowed-headers: "*"
      allow-credentials: false
      max-age: 3600
```

## 依赖

- lingzhi-core
- spring-boot-starter-web
- hibernate-validator
