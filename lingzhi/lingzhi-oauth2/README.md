# lingzhi-oauth2 OAuth2 模块

> OAuth2 客户端/服务端

## 模块简介

`lingzhi-oauth2` 提供 OAuth2 能力：

- **授权码模式** - 标准 OAuth2
- **客户端模式** - 服务间调用
- **第三方登录** - GitHub、微信等

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-oauth2</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: xxx
            client-secret: xxx
        provider:
          github:
            authorization-uri: https://github.com/login/oauth/authorize
            token-uri: https://github.com/login/oauth/access_token
```

## 使用方式

### 授权登录

```java
@GetMapping("/login/oauth2/code/github")
public void oauth2Login() {
    // OAuth2 登录
}
```

### 获取用户信息

```java
@Principal OAuth2User principal = ...;
String name = principal.getName();
```

## 依赖

- lingzhi-security
- spring-boot-starter-oauth2-client
- spring-boot-starter-oauth2-resource-server
