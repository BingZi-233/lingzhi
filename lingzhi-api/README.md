# lingzhi-api API 文档模块

> Knife4j/OpenAPI 文档

## 模块简介

`lingzhi-api` 提供 API 文档能力：

- **Knife4j** - 美观的 API 文档
- **OpenAPI 3.0** - 标准 API 规范
- **Swagger UI** - 在线调试

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

## 使用方式

### 注解说明

```java
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @ApiOperation("获取用户详情")
    @ApiParam(name = "id", type = "Long", required = true, example = "1")
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @ApiOperation("创建用户")
    @PostMapping
    public Result<Void> createUser(@Valid @RequestBody UserRequest request) {
        return Result.success();
    }
}
```

### 请求/响应注解

| 注解 | 说明 |
|------|------|
| @Api | Controller 描述 |
| @ApiOperation | 方法描述 |
| @ApiParam | 参数描述 |
| @ApiModel | 实体类描述 |
| @ApiModelProperty | 字段描述 |
| @ApiImplicitParams | 多个参数 |
| @ApiResponses | 响应列表 |

### 常用配置

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
