package com.lingzhi.core.util;

import cn.hutool.core.util.StrUtil;

import java.util.UUID;

/**
 * ID生成工具
 */
public final class IdUtils {

    private IdUtils() {}

    /**
     * 获取UUID (无下划线)
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取带下划线的UUID
     */
    public static String uuidWithDash() {
        return UUID.randomUUID().toString();
    }

    /**
     * 简单UUID (8位)
     */
    public static String simpleUuid() {
        return uuid().substring(0, 8);
    }

    /**
     * 雪花ID (需要配置 yml 的 worker-id 和 datacenter-id)
     * 使用默认配置
     */
    public static long snowflakeId() {
        return SnowflakeIdWorker.getInstance().nextId();
    }

    /**
     * 雪花ID (字符串)
     */
    public static String snowflakeIdStr() {
        return String.valueOf(snowflakeId());
    }

    /**
     * 简单雪花ID (15位数字)
     */
    public static String simpleSnowflakeId() {
        return String.valueOf(snowflakeId() % 1000000000000L);
    }

    /**
     * 雪花ID生成器 (单例)
     */
    private static class SnowflakeIdWorker {
        private static final SnowflakeId INSTANCE = new SnowflakeIdWorker.SnowflakeId(1, 1);

        public static SnowflakeId getInstance() {
            return INSTANCE;
        }

        public static class SnowflakeId {
            private final long twepoch = 1704067200000L; // 2024-01-01 00:00:00
            private final long workerIdBits = 5L;
            private final long datacenterIdBits = 5L;
            private final long sequenceBits = 12L;

            private final long maxWorkerId = ~(-1L << workerIdBits);
            private final long maxDatacenterId = ~(-1L << datacenterIdBits);

            private final long workerIdShift = sequenceBits;
            private final long datacenterIdShift = sequenceBits + workerIdBits;
            private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
            private final long sequenceMask = ~(-1L << sequenceBits);

            private long workerId;
            private long datacenterId;
            private long sequence = 0L;
            private long lastTimestamp = -1L;

            public SnowflakeId(long workerId, long datacenterId) {
                if (workerId > maxWorkerId || workerId < 0) {
                    throw new IllegalArgumentException("worker Id can't be greater than " + maxWorkerId + " or less than 0");
                }
                if (datacenterId > maxDatacenterId || datacenterId < 0) {
                    throw new IllegalArgumentException("datacenter Id can't be greater than " + maxDatacenterId + " or less of 0");
                }
                this.workerId = workerId;
                this.datacenterId = datacenterId;
            }

            public synchronized long nextId() {
                long timestamp = timeGen();

                if (timestamp < lastTimestamp) {
                    throw new RuntimeException("Clock moved backwards. Refusing to generate id");
                }

                if (lastTimestamp == timestamp) {
                    sequence = (sequence + 1) & sequenceMask;
                    if (sequence == 0) {
                        timestamp = tilNextMillis(lastTimestamp);
                    }
                } else {
                    sequence = 0L;
                }

                lastTimestamp = timestamp;

                return ((timestamp - twepoch) << timestampLeftShift)
                        | (datacenterId << datacenterIdShift)
                        | (workerId << workerIdShift)
                        | sequence;
            }

            private long tilNextMillis(long lastTimestamp) {
                long timestamp = timeGen();
                while (timestamp <= lastTimestamp) {
                    timestamp = timeGen();
                }
                return timestamp;
            }

            private long timeGen() {
                return System.currentTimeMillis();
            }
        }
    }
}
