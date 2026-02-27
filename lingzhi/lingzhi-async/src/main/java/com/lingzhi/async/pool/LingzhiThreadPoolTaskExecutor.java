package com.lingzhi.async.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 灵芝线程池任务执行器
 * 支持任务执行回调、监控、优雅关闭等
 */
@Slf4j
public class LingzhiThreadPoolTaskExecutor extends ThreadPoolExecutor {

    /**
     * 线程池名称
     */
    private final String poolName;

    public LingzhiThreadPoolTaskExecutor(int corePoolSize, int maximumPoolSize,
                                          long keepAliveTime, TimeUnit unit,
                                          BlockingQueue<Runnable> workQueue,
                                          ThreadFactory threadFactory,
                                          RejectedExecutionHandler handler,
                                          String poolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.poolName = poolName;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            log.error("线程池 [{}] 执行任务失败", poolName, t);
        }
    }

    /**
     * 获取线程池信息
     */
    public String getPoolInfo() {
        return String.format("[%s] 活跃: %d, 完成: %d, 任务: %d, 队列: %d",
                poolName,
                getActiveCount(),
                getCompletedTaskCount(),
                getTaskCount(),
                getQueue().size());
    }
}
