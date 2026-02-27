# lingzhi-xxljob 分布式任务模块

> XXL-Job 客户端封装

## 模块简介

`lingzhi-xxljob` 提供分布式任务能力：

- **XXL-Job** - 分布式任务调度
- **执行器** - 任务执行器
- **JobHandler** - 任务处理器

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-xxljob</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
xxl:
  job:
    admin:
      addresses: http://localhost:8080/xxl-job-admin
    executor:
      appname: lingzhi-executor
      port: 9999
      logpath: /data/xxl-job
    access-token: default_token
```

## 使用方式

### 定义 JobHandler

```java
@Component
public class SampleJobHandler {

    @XxlJob("demoJobHandler")
    public ReturnT<String> execute(String params) {
        XxlJobLogger.log("XXL-JOB, Hello World.");
        return ReturnT.SUCCESS;
    }
}
```

### 任务类型

| 类型 | 说明 |
|------|------|
| BEAN | Bean 模式 |
| GLUE(Java) | GLUE Java |
| GLUE(Shell) | GLUE Shell |
| GLUE(Python) | GLUE Python |

## 依赖

- lingzhi-core
- xxl-job-core
