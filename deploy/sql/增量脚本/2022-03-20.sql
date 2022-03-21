SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_api_monitor_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_monitor_info`;
DROP TABLE IF EXISTS `t_api_monitor_info`;
CREATE TABLE `t_api_monitor_info`
(
    `id`             bigint(20)                                                     NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `ak_id`          varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户ak',
    `open_user_id`   varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '公开用户ID',
    `api_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'API名称',
    `request_method` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '请求方式',
    `path`           varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '接口名',
    `request_params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
    `start_time`     datetime(0)                                                    NOT NULL COMMENT '请求开始时间',
    `end_time`       datetime(0)                                                    NOT NULL COMMENT '请求结束时间',
    `invoke_time`    bigint(20)                                                     NOT NULL COMMENT '本次接口调用时长',
    `Invoke_status`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '本次调用成功失败',
    `Invoke_msg`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '调用信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 38
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '调用记录表'
  ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_api_statistics
-- ----------------------------
DROP TABLE IF EXISTS `t_api_user_statistics`;
DROP TABLE IF EXISTS `t_api_statistics`;
CREATE TABLE `t_api_statistics`
(
    `id`                  bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `ak_id`               varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `request_method`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
    `path`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口名',
    `max_time`            bigint(20)                                                    NOT NULL COMMENT '请求耗时最长时间',
    `min_time`            bigint(20)                                                    NOT NULL COMMENT '请求耗时最短时间',
    `total_time`          bigint(20)                                                    NOT NULL COMMENT '接口调用总时长',
    `total_request_count` bigint(20)                                                    NOT NULL COMMENT '用户接口总共调用次数',
    `success_count`       bigint(20)                                                    NOT NULL COMMENT '用户成功次数',
    `error_count`         bigint(20)                                                    NOT NULL COMMENT '请求失败次数',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 34
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'OpenAPI统计信息表'
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

