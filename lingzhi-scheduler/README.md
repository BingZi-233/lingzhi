# lingzhi-scheduler 定时任务模块

> @Scheduled、动态定时任务

## 模块简介

`lingzhi-scheduler` 提供定时任务能力：

- **@Scheduler** - 定时任务注解
- **@Scheduled** - Spring 原生注解

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-scheduler</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用方式

### @Scheduler - 定时任务

```java
@Service
public class ScheduledTask {

    @Scheduler(cron = "0 0 2 * * ?", description = "清理缓存")
    public void cleanCache() {
        // 每天凌晨2点执行
    }
    
    @Scheduler(fixedRate = 5000, description = "同步数据")
    public void syncData() {
        // 每5秒执行一次
    }
}
```

## 注解属性

| 属性 | 说明 | 默认值 |
|------|------|--------|
| cron | Cron 表达式 | - |
| fixedRate | 固定频率(毫秒) | -1 |
| fixedDelay | 固定延迟(毫秒) | -1 |
| initialDelay | 初始延迟(毫秒) | -1 |
| description | 任务描述 | - |
| group | 任务分组 | default |

## Cron 表达式

| 字段 | 范围 | 特殊字符 |
|------|------|----------|
| 秒 | 0-59 | , - * / |
| 分 | 0-59 | , - * / |
| 时 | 0-23 | , - * / |
| 日 | 1-31 | , - * / ? L W |
| 月 | 1-12 | , - * / |
| 周 | 1-7 | , - * / ? L # |

### 示例

```
0 0 10,14,16 * * ?     每天10点、14点、16点
0 0 12 ? * WED        每周三12点
0 0 12 1 * ?          每月1号12点
0 15 10 L * ?         每月最后一天10点15分
0 0 8 ? * MON-FRI    工作日8点
```

## 依赖

- lingzhi-core
- spring-context
