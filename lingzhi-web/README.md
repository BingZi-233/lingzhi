# lingzhi-web Web 模块

> 参数校验、分页、CORS、基础 Controller

## 模块简介

`lingzhi-web` 提供 Web 开发常用能力：

- **参数校验** - @Valid 注解式校验
- **分页** - 通用分页请求/响应
- **CORS** - 跨域配置
- **基础 Controller** - 通用 CRUD

## 快速开始

引入依赖即可：

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-web</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 分页

### 分页请求

```java
@PostMapping("/list")
public Result<PageResult<User>> list(@RequestBody PageRequest request) {
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
    wrapper.like(User::getUsername, request.getKeyword());
    
    return page(request, wrapper);
}
```

### 响应结构

```json
{
  "code": 200,
  "data": {
    "list": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10,
    "hasPrevious": false,
    "hasNext": true
  }
}
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
    
    @Min(value = 18, message = "年龄最小18岁")
    @Max(value = 100, message = "年龄最大100岁")
    private Integer age;
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
| @DecimalMin / @DecimalMax | BigDecimal 范围 |

## 基础 Controller

```java
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserService, User> {

    @Override
    protected UserService getService() {
        return userService;
    }

    @PostMapping
    public Result<Void> add(@RequestBody User user) {
        return super.add(user);
    }

    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        return super.update(user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return super.get(id);
    }

    @PostMapping("/page")
    public Result<PageResult<User>> page(@RequestBody PageRequest request) {
        return super.page(request);
    }
}
```

## CORS 配置

```yaml
lingzhi:
  web:
    cors:
      enabled: true
```

## 配置项

```yaml
lingzhi:
  web:
    cors:
      enabled: true                    # 是否启用
    validation:
      enabled: true                   # 是否启用参数校验
```

## 依赖

- lingzhi-core
- lingzhi-db
- spring-boot-starter-web
- hibernate-validator
