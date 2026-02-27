# lingzhi-starter 一键引入模块

> 引入所有常用模块

## 模块简介

`lingzhi-starter` 一键引入所有常用模块，开箱即用。

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
lingzhi:
  enabled: true

spring:
  datasource:
    dynamic:
      primary: master
      datasource:
        master:
          url: jdbc:mysql://localhost:3306/test
          username: root
          password: root
  data:
    redis:
      host: localhost
      port: 6379
```

## 包含模块

| 模块 | 说明 |
|------|------|
| lingzhi-core | 核心基础 |
| lingzhi-common | 公共配置 |
| lingzhi-async | 异步任务 |
| lingzhi-scheduler | 定时任务 |
| lingzhi-db | 数据库 |
| lingzhi-cache | 缓存 |
| lingzhi-lock | 分布式锁 |
| lingzhi-mq | 消息队列 |
| lingzhi-event | 事件驱动 |
| lingzhi-web | Web |
| lingzhi-api | API 文档 |
| lingzhi-security | 安全 |
| lingzhi-log | 日志 |
| lingzhi-monitor | 监控 |

## 可选模块

以下模块需要单独引入：

```xml
<!-- 文件处理 -->
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-file</artifactId>
</dependency>

<!-- Excel -->
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-excel</artifactId>
</dependency>

<!-- 验证码 -->
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-captcha</artifactId>
</dependency>

<!-- OAuth2 -->
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-oauth2</artifactId>
</dependency>

<!-- 代码生成 -->
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-generator</artifactId>
</dependency>

<!-- 分布式任务 -->
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-xxljob</artifactId>
</dependency>
```

## 默认配置

引入 `lingzhi-starter` 后，以下功能默认开启：

- MyBatis Plus
- 动态数据源
- Redis
- JWT
- 操作日志
- API 文档
- 监控端点

如需关闭某个功能：

```yaml
lingzhi:
  async:
    enabled: false
  log:
    enabled: false
```

## 依赖

所有子模块依赖
