# lingzhi-xxljob 分布式任务模块

> XXL-Job 客户端封装

## 模块简介

`lingzhi-xxljob` 提供分布式任务能力：

- **@XxlJobHandler** - XXL-Job 任务注解
- **XXL-Job** - 分布式任务调度

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

### @XxlJobHandler - 定义任务

```java
@Component
public class SampleJobHandler {

    @XxlJobHandler("demoJob")
    public ReturnT<String> execute(String params) {
        XxlJobLogger.log("XXL-JOB, Hello World.");
        return ReturnT.SUCCESS;
    }
    
    @XxlJobHandler(
        value = "customJob",
        description = "自定义任务",
        init = "init",
        destroy = "destroy"
    )
    public ReturnT<String> customJob(String params) {
        return ReturnT.SUCCESS;
    }
    
    public void init() {
        // 任务初始化
    }
    
    public void destroy() {
        // 任务销毁
    }
}
```

## 注解属性

| 属性 | 说明 | 默认值 |
|------|------|--------|
| value | 任务名称 | - |
| description | 任务描述 | - |
| init | 初始化方法 | - |
| destroy | 销毁方法 | - |

## 返回值

```java
// 成功
return ReturnT.SUCCESS;

// 失败
return ReturnT.FAIL;
```

## 任务类型

| 类型 | 说明 |
|------|------|
| BEAN | Bean 模式 |
| GLUE(Java) | GLUE Java |
| GLUE(Shell) | GLUE Shell |
| GLUE(Python) | GLUE Python |

## 依赖

- lingzhi-core
- xxl-job-core
