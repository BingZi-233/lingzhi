-- ----------------------------
-- 异步任务表
-- ----------------------------
DROP TABLE IF EXISTS `async_task`;
CREATE TABLE `async_task` (
  `id` bigint NOT NULL COMMENT '任务ID',
  `task_key` varchar(64) NOT NULL COMMENT '任务唯一标识',
  `task_name` varchar(128) NOT NULL COMMENT '任务名称',
  `task_type` varchar(64) NOT NULL COMMENT '任务类型',
  `params` text COMMENT '任务参数（JSON格式）',
  `result` text COMMENT '任务结果（JSON格式）',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '任务状态：0-待执行 1-执行中 2-执行成功 3-执行失败 4-已取消 5-已超时',
  `error_msg` varchar(1024) COMMENT '失败原因',
  `progress` int DEFAULT '0' COMMENT '进度百分比（0-100）',
  `executor_name` varchar(64) COMMENT '线程池名称',
  `expected_duration` bigint COMMENT '预期执行时间（毫秒）',
  `actual_duration` bigint COMMENT '实际执行时间（毫秒）',
  `timeout` bigint DEFAULT '0' COMMENT '超时时间（毫秒），0表示不超时',
  `creator_id` bigint COMMENT '创建者ID',
  `creator_name` varchar(64) COMMENT '创建者名称',
  `start_time` datetime COMMENT '执行开始时间',
  `end_time` datetime COMMENT '执行结束时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_key` (`task_key`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_task_type` (`task_type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='异步任务表';
