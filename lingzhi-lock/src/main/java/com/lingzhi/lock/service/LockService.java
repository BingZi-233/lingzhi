package com.lingzhi.lock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {

    private final RedissonClient redissonClient;

    /**
     * 获取锁
     */
    public RLock lock(String lockKey) {
        return redissonClient.getLock(lockKey);
    }

    /**
     * 尝试获取锁（立即返回）
     */
    public boolean tryLock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            return lock.tryLock();
        } catch (Exception e) {
            log.error("获取锁失败: {}", lockKey, e);
            return false;
        }
    }

    /**
     * 尝试获取锁（带等待时间）
     */
    public boolean tryLock(String lockKey, long waitTime) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            return lock.tryLock(waitTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("获取锁失败: {}", lockKey, e);
            return false;
        }
    }

    /**
     * 尝试获取锁（带等待时间和过期时间）
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (Exception e) {
            log.error("获取锁失败: {}", lockKey, e);
            return false;
        }
    }

    /**
     * 释放锁
     */
    public void unlock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Exception e) {
            log.error("释放锁失败: {}", lockKey, e);
        }
    }

    /**
     * 释放锁（指定锁对象）
     */
    public void unlock(RLock lock) {
        try {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Exception e) {
            log.error("释放锁失败", e);
        }
    }

    /**
     * 执行带锁的操作
     */
    public <T> T execute(String lockKey, Supplier<T> task) {
        return execute(lockKey, 0, 30, TimeUnit.SECONDS, task);
    }

    /**
     * 执行带锁的操作（带等待时间）
     */
    public <T> T execute(String lockKey, long waitTime, long leaseTime, TimeUnit unit, Supplier<T> task) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean acquired = lock.tryLock(waitTime, leaseTime, unit);
            if (!acquired) {
                throw new RuntimeException("获取锁失败: " + lockKey);
            }
            return task.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取锁被中断: " + lockKey, e);
        } finally {
            unlock(lock);
        }
    }
}
