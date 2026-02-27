# lingzhi-monitor 监控模块

> Actuator、Prometheus

## 模块简介

`lingzhi-monitor` 提供监控能力：

- **Actuator** - Spring Boot 端点
- **Prometheus** - 指标收集
- **Grafana** - 可视化

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-monitor</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
```

## 端点

| 端点 | 说明 |
|------|------|
| /actuator/health | 健康检查 |
| /actuator/info | 应用信息 |
| /actuator/metrics | 指标 |
| /actuator/env | 环境变量 |
| /actuator/beans | Bean 列表 |
| /actuator/httptrace | HTTP 追踪 |

## 依赖

- lingzhi-core
- spring-boot-starter-actuator
- micrometer-registry-prometheus
