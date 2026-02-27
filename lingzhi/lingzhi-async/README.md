# lingzhi-async 异步任务模块

> 异步任务执行框架，支持耗时任务管理

## 模块简介

`lingzhi-async` 提供完善的异步任务解决方案，特别适用于耗时任务的场景：

- **@LingzhiAsyncTask 注解** - 一键将同步方法改为异步执行
- **任务管理** - 任务创建、查询、取消、删除
- **线程池管理** - 可配置的线程池，支持多线程池
- **超时控制** - 任务执行超时自动处理
- **结果存储** - 任务结果自动持久化

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-async</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
lingzhi:
  async:
    enabled: true
    thread-pool:
      core-pool-size: 8
      max-pool-size: 16
      queue-capacity: 200
      thread-name-prefix: lingzhi-async-
      keep-alive-time: 60
      rejected-policy: CALLER_RUNS
```

### 3. 创建数据库表

执行 `src/main/resources/sql/async_task.sql` 创建任务表：

```sql
CREATE TABLE `async_task` (
  `id` bigint NOT NULL COMMENT '任务ID',
  `task_key` varchar(64) NOT NULL COMMENT '任务唯一标识',
  `task_name` varchar(128) NOT NULL COMMENT '任务名称',
  `task_type` varchar(64) NOT NULL COMMENT '任务类型',
  `params` text COMMENT '任务参数（JSON格式）',
  `result` text COMMENT '任务结果（JSON格式）',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '任务状态',
  `error_msg` varchar(1024) COMMENT '失败原因',
  `progress` int DEFAULT '0' COMMENT '进度百分比',
  `executor_name` varchar(64) COMMENT '线程池名称',
  `timeout` bigint DEFAULT '0' COMMENT '超时时间（毫秒）',
  `creator_id` bigint COMMENT '创建者ID',
  `creator_name` varchar(64) COMMENT '创建者名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_key` (`task_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

## 使用方式

### 方式一：@LingzhiAsyncTask 注解（推荐）

在 Controller 方法上加注解，自动将同步方法改为异步执行：

```java
@Slf4j
@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private AsyncTaskService asyncTaskService;

    /**
     * 耗时端点 - 加注解自动变异步
     */
    @LingzhiAsyncTask(
        taskType = "EXPORT_REPORT",
        taskName = "导出报表: #request.reportType",
        timeout = 300000  // 5分钟超时
    )
    @PostMapping("/export")
    public String exportReport(@RequestBody ExportRequest request) {
        // 耗时操作，返回值会自动存储为任务结果
        return doExport(request);
    }

    // 任务管理端点
    @GetMapping("/task/{taskKey}")
    public Result<TaskDetailResponse> getTask(@PathVariable String taskKey) {
        return Result.success(asyncTaskService.getTaskDetail(taskKey));
    }

    @PostMapping("/task/{taskKey}/cancel")
    public Result<Void> cancelTask(@PathVariable String taskKey) {
        boolean success = asyncTaskService.cancelTask(taskKey, getCurrentUserId());
        return success ? Result.success() : Result.error("取消失败");
    }

    private String doExport(ExportRequest request) {
        // 模拟耗时操作
        try { Thread.sleep(5000); } catch (InterruptedException e) {}
        return "/downloads/report_" + System.currentTimeMillis() + ".xlsx";
    }
}
```

**效果：**
- 调用 `POST /api/export/export` → 立即返回 `taskKey`
- 方法在后台线程执行
- 通过 `GET /api/export/task/{taskKey}` 查询状态

### 方式二：AsyncTaskSupport 抽象类

继承 `AsyncTaskSupport` 获得任务管理能力：

```java
@Slf4j
@RestController
@RequestMapping("/api/export")
public class ExportController extends AsyncTaskSupport {

    @Autowired
    public ExportController(AsyncTaskService asyncTaskService) {
        super(asyncTaskService);
    }

    // 自动执行模式
    @PostMapping("/auto")
    public Result<String> exportAuto(@RequestBody ExportRequest request) {
        String taskKey = executeTask(
            "EXPORT_REPORT",
            "导出报表",
            request,
            () -> doExport(request)
        );
        return Result.success(taskKey);
    }

    // 手动控制模式（可更新进度）
    @PostMapping("/manual")
    public Result<String> exportManual(@RequestBody ExportRequest request) {
        String taskKey = submitTask("EXPORT_REPORT", "导出报表", request);
        startTask(taskKey, () -> {
            updateProgress(taskKey, 50);
            updateResult(taskKey, doExport(request));
            return null;
        });
        return Result.success(taskKey);
    }
}
```

### 方式三：直接使用 AsyncTaskService

```java
@Autowired
private AsyncTaskService asyncTaskService;

// 创建任务
TaskCreateRequest request = new TaskCreateRequest();
request.setTaskType("EXPORT");
request.setTaskName("导出报表");
request.setParams(params);
request.setTimeout(300000L);

AsyncTask task = asyncTaskService.createAndExecuteTask(request, 
    () -> doExport(), 
    userId, 
    userName
);

// 查询任务
TaskDetailResponse task = asyncTaskService.getTaskDetail(taskKey);

// 取消任务
asyncTaskService.cancelTask(taskKey, userId);

// 删除任务
asyncTaskService.deleteTask(taskKey);
```

## 任务状态

| 状态码 | 状态名 | 说明 |
|--------|--------|------|
| 0 | PENDING | 待执行 |
| 1 | RUNNING | 执行中 |
| 2 | SUCCESS | 执行成功 |
| 3 | FAILED | 执行失败 |
| 4 | CANCELLED | 已取消 |
| 5 | TIMEOUT | 已超时 |

## 注解属性

| 属性 | 说明 | 默认值 |
|------|------|--------|
| `taskType` | 任务类型（必填） | - |
| `taskName` | 任务名称，支持 SpEL 表达式 | 方法名 |
| `timeout` | 超时时间（毫秒），0=不超时 | 0 |
| `expectedDuration` | 预期执行时间，用于进度估算 | 0 |
| `executorName` | 线程池名称 | 默认线程池 |

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /async/task | 创建任务 |
| GET | /async/task/{taskKey} | 查询任务详情 |
| GET | /async/task/list | 查询任务列表 |
| POST | /async/task/{taskKey}/cancel | 取消任务 |
| DELETE | /async/task/{taskKey} | 删除任务 |
| GET | /async/task/{taskKey}/result | 获取任务结果 |

## 配置项

```yaml
lingzhi:
  async:
    enabled: true                    # 是否启用
    thread-pool:
      core-pool-size: 8              # 核心线程数
      max-pool-size: 16              # 最大线程数
      queue-capacity: 200            # 队列容量
      thread-name-prefix: "lingzhi-async-"  # 线程名前缀
      keep-alive-time: 60            # 空闲线程存活时间(秒)
      allow-core-thread-time-out: false  # 允许核心线程超时
      rejected-policy: CALLER_RUNS   # 拒绝策略
      wait-for-tasks-to-complete-on-shutdown: true  # 关闭时等待任务完成
      await-termination-seconds: 60  # 等待终止时间(秒)
```

## 拒绝策略

| 策略 | 说明 |
|------|------|
| CALLER_RUNS | 由调用线程执行 |
| ABORT_POLICY | 抛出 RejectedExecutionException |
| DISCARD_OLDEST_POLICY | 丢弃最老的任务 |
| DISCARD_POLICY | 丢弃新任务 |
| LOG_DISCARD_POLICY | 丢弃并记录日志 |

## 进阶使用

### 自定义线程池

```yaml
lingzhi:
  async:
    thread-pool:
      # 默认线程池配置
      core-pool-size: 8
      max-pool-size: 16
```

在业务中使用指定线程池：

```java
@LingzhiAsyncTask(
    taskType = "EXPORT",
    executorName = "customExecutor"  // 指定线程池
)
@PostMapping("/export")
public String export() { ... }
```

### 任务进度更新

在耗时任务内部更新进度：

```java
@LingzhiAsyncTask(taskType = "BATCH")
@PostMapping("/batch")
public String batchProcess(@RequestBody BatchRequest request) {
    // 方式1: 使用 AOP 切面自动更新
    // 方式2: 注入 AsyncTaskService 手动更新
    asyncTaskService.updateProgress(taskKey, 50);
    // ... 处理一半
    asyncTaskService.updateProgress(taskKey, 100);
    return result;
}
```

## 依赖

- lingzhi-core
- lingzhi-db (MyBatis Plus)
- lingzhi-common
- spring-boot-starter-aop
- spring-context
