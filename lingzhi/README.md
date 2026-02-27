# 灵芝 (Lingzhi) - Spring Boot 脚手架

> Spring Boot 3.5.x 企业级开发脚手架

## 项目简介

灵芝是一个基于 Spring Boot 3.5.x 的企业级开发脚手架，提供了丰富的模块封装，让开发更高效。

**技术栈：**
- Spring Boot 3.5.11
- Java 17+
- MyBatis Plus 3.5.7
- Redis (Redisson)
- RabbitMQ / Kafka
- XXL-Job
- Spring Security + JWT
- Knife4j API 文档

## 模块列表

| 模块 | 说明 |
|------|------|
| lingzhi-core | 核心基础 - 常量、工具类、异常、枚举 |
| lingzhi-common | 公共配置 - 统一响应、异常处理、全局拦截器 |
| lingzhi-async | 异步任务 - @Async、线程池、Future、CompletableFuture |
| lingzhi-scheduler | 定时任务 - @Scheduled、动态定时任务 |
| lingzhi-xxljob | 分布式任务 - XXL-Job 客户端封装 |
| lingzhi-db | 数据库 - MyBatis Plus、分布式ID、乐观锁 |
| lingzhi-cache | 缓存 - Redis、Spring Cache 抽象 |
| lingzhi-lock | 分布式锁 - Redis 锁 |
| lingzhi-mq | 消息队列 - RabbitMQ、Kafka 封装 |
| lingzhi-event | 事件驱动 - Spring Event |
| lingzhi-web | Web - 参数校验、分页 |
| lingzhi-api | API 文档 - Knife4j/OpenAPI |
| lingzhi-security | 安全 - Spring Security + JWT |
| lingzhi-oauth2 | OAuth2 客户端/服务端 |
| lingzhi-file | 文件处理 - 本地存储、MinIO、阿里云OSS |
| lingzhi-excel | Excel - EasyExcel 封装 |
| lingzhi-captcha | 验证码 |
| lingzhi-log | 日志 - 统一日志切面 |
| lingzhi-monitor | 监控 - Actuator、Prometheus |
| lingzhi-generator | 代码生成器 |
| lingzhi-starter | 一键引入所有常用模块 |

## 快速开始

### 1. 引入 starter

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置 application.yml

```yaml
lingzhi:
  enabled: true
  async:
    enabled: true
    thread-pool:
      core-pool-size: 10
      max-pool-size: 50
      queue-capacity: 200
```

### 3. 使用异步任务

```java
@Service
public class DemoService {

    @Autowired
    private AsyncService asyncService;

    public void demo() {
        // 异步执行无返回值
        asyncService.execute(() -> {
            System.out.println("异步任务执行中...");
        });

        // 异步执行有返回值
        Future<String> future = asyncService.submit(() -> {
            return "Hello Async";
        });

        // 使用 CompletableFuture
        asyncService.supplyAsync(() -> "Hello")
            .thenApply(s -> s + " World")
            .thenAccept(System.out::println);
    }
}
```

### 4. 使用 @LingzhiAsync 注解

```java
@Service
public class AsyncTaskService {

    @LingzhiAsync(executor = "lingzhiAsyncExecutor", timeout = 30)
    public String asyncMethod() {
        // 异步执行的方法
        return "result";
    }
}
```

## 模块详细介绍

### lingzhi-async 异步任务模块

提供完善的异步任务解决方案：

- **自定义线程池** - 可配置的线程池参数
- **多种执行方式** - Runnable、Callable、CompletableFuture
- **超时控制** - 任务执行超时设置
- **批量任务** - 批量提交异步任务
- **优雅关闭** - 等待任务完成后再关闭

配置项：

```yaml
lingzhi:
  async:
    enabled: true
    thread-pool:
      core-pool-size: 10        # 核心线程数
      max-pool-size: 50         # 最大线程数
      queue-capacity: 200       # 队列容量
      thread-name-prefix: "lingzhi-async-"
      keep-alive-time: 60       # 空闲线程存活时间(秒)
      rejected-policy: CALLER_RUNS  # 拒绝策略
```

## 模块文档

每个模块都有独立的 README 文档：

| 模块 | 说明 | 文档 |
|------|------|------|
| lingzhi-core | 核心基础 - 常量、工具类、异常 | [README](lingzhi-core/README.md) |
| lingzhi-common | 公共配置 - 统一响应、异常处理 | [README](lingzhi-common/README.md) |
| lingzhi-async | 异步任务 - @LingzhiAsyncTask 注解 | [README](lingzhi-async/README.md) |
| lingzhi-scheduler | 定时任务 - @Scheduled | [README](lingzhi-scheduler/README.md) |
| lingzhi-xxljob | 分布式任务 - XXL-Job | [README](lingzhi-xxljob/README.md) |
| lingzhi-db | 数据库 - MyBatis Plus | [README](lingzhi-db/README.md) |
| lingzhi-cache | 缓存 - Redis | [README](lingzhi-cache/README.md) |
| lingzhi-lock | 分布式锁 - Redis 锁 | [README](lingzhi-lock/README.md) |
| lingzhi-mq | 消息队列 - RabbitMQ/Kafka | [README](lingzhi-mq/README.md) |
| lingzhi-event | 事件驱动 - Spring Event | [README](lingzhi-event/README.md) |
| lingzhi-web | Web - 参数校验、分页 | [README](lingzhi-web/README.md) |
| lingzhi-api | API 文档 - Knife4j/OpenAPI | [README](lingzhi-api/README.md) |
| lingzhi-security | 安全 - Spring Security + JWT | [README](lingzhi-security/README.md) |
| lingzhi-oauth2 | OAuth2 - 第三方登录 | [README](lingzhi-oauth2/README.md) |
| lingzhi-file | 文件处理 - 本地/OSS/MinIO | [README](lingzhi-file/README.md) |
| lingzhi-excel | Excel - EasyExcel | [README](lingzhi-excel/README.md) |
| lingzhi-captcha | 验证码 | [README](lingzhi-captcha/README.md) |
| lingzhi-log | 日志 - 操作日志切面 | [README](lingzhi-log/README.md) |
| lingzhi-monitor | 监控 - Actuator/Prometheus | [README](lingzhi-monitor/README.md) |
| lingzhi-generator | 代码生成器 | [README](lingzhi-generator/README.md) |
| lingzhi-starter | 一键引入所有常用模块 | [README](lingzhi-starter/README.md) |

## 项目结构

```
lingzhi/
├── pom.xml                      # 父 POM
├── lingzhi-core/                # 核心基础模块
├── lingzhi-common/              # 公共配置模块
├── lingzhi-async/               # 异步任务模块 ⭐
├── lingzhi-scheduler/           # 定时任务模块
├── lingzhi-xxljob/              # 分布式任务模块
├── lingzhi-db/                  # 数据库模块
├── lingzhi-cache/               # 缓存模块
├── lingzhi-lock/                # 分布式锁模块
├── lingzhi-mq/                  # 消息队列模块
├── lingzhi-event/               # 事件驱动模块
├── lingzhi-web/                 # Web 模块
├── lingzhi-api/                 # API 文档模块
├── lingzhi-security/            # 安全模块
├── lingzhi-oauth2/              # OAuth2 模块
├── lingzhi-file/                # 文件处理模块
├── lingzhi-excel/               # Excel 模块
├── lingzhi-captcha/             # 验证码模块
├── lingzhi-log/                 # 日志模块
├── lingzhi-monitor/             # 监控模块
├── lingzhi-generator/           # 代码生成模块
└── lingzhi-starter/             # Starter 入口
```

## 文档

- 📖 [项目主 README](README.md) - 快速开始、模块列表
- 📄 [模块文档](#模块文档) - 21 个模块的详细使用说明

---

## 许可证

MIT License
