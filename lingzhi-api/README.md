# lingzhi-api API 文档模块

> Knife4j/OpenAPI 文档

## 模块简介

`lingzhi-api` 提供 API 文档能力：

- **@IgnoreResponse** - 忽略统一响应包装
- **@ApiVersion** - API 版本控制
- **@ApiDoc** - API 文档扩展
- **Knife4j** - 美观的 API 文档

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-api</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 访问文档

```
http://localhost:8080/doc.html
```

## 注解

### @IgnoreResponse - 忽略统一响应包装

```java
@IgnoreResponse
@GetMapping("/health")
public Health health() {
    return Health.up();
}
```

### @ApiVersion - API 版本控制

```java
@ApiVersion("v2")
@GetMapping("/users")
public Result<List<User>> listUsers() {
    return Result.success(userService.list());
}
```

### @ApiDoc - API 文档扩展

```java
@ApiDoc(
    title = "用户查询",
    description = "根据条件查询用户列表",
    category = "用户管理",
    order = 1
)
@GetMapping("/users")
public Result<List<User>> listUsers() {
    return Result.success(userService.list());
}
```

## 注解说明

### @ApiVersion

| 属性 | 说明 | 默认值 |
|------|------|--------|
| value | API 版本 | v1 |
| description | 版本描述 | - |

### @ApiDoc

| 属性 | 说明 | 默认值 |
|------|------|--------|
| title | 接口标题 | - |
| description | 接口描述 | - |
| category | 接口分类 | - |
| order | 排序 | 0 |
| deprecated | 是否废弃 | false |

## 使用示例

```java
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @ApiOperation("获取用户详情")
    @ApiVersion("v2")
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @ApiOperation("创建用户")
    @ApiDoc(
        title = "创建用户",
        description = "创建一个新用户",
        category = "用户管理"
    )
    @PostMapping
    public Result<Void> createUser(@Valid @RequestBody UserRequest request) {
        return Result.success();
    }
}
```

## 常用配置

```yaml
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
    path: /swagger-ui.html

knife4j:
  enable: true
  setting:
    language: zh_CN
```

## 配置项

```yaml
lingzhi:
  api:
    title: "灵芝 API 文档"
    version: "1.0.0"
    description: "企业级开发脚手架"
    contact:
      name: "开发团队"
      email: "dev@example.com"
```

## 依赖

- lingzhi-core
- spring-boot-starter-web
- knife4j-openapi3-jakarta-spring-boot-starter
