# lingzhi-scheduler 定时任务模块

> @Scheduled、动态定时任务

## 模块简介

`lingzhi-scheduler` 提供定时任务能力：

- **@Scheduled** - 注解式定时任务
- **动态任务** - 运行时管理任务
- **任务执行器** - 线程池执行

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

### 固定频率

```java
@Scheduled(fixedRate = 5000)  // 每5秒执行一次
public void task1() {
    // 
}
```

### 固定延迟

```java
@Scheduled(fixedDelay = 5000)  // 上次执行完5秒后执行
public void task2() {
    //
}
```

### Cron 表达式

```java
@Scheduled(cron = "0 0 2 * * ?")  // 每天凌晨2点
public void task3() {
    //
}
```

### 初始延迟

```java
@Scheduled(initialDelay = 10000, fixedRate = 5000)  // 首次延迟10秒
public void task4() {
    //
}
```

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
