CREATE TABLE `t_wwx_company`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
    `corp_id`           varchar(128) DEFAULT '' COMMENT '企业id',
    `permanent_code`    varchar(512) DEFAULT '' COMMENT '企业永久授权码',
    `corp_name`         varchar(128) DEFAULT '' COMMENT '企业名称',
    `corp_full_name`    varchar(256) DEFAULT '' COMMENT '企业全称',
    `subject_type`      varchar(256) DEFAULT '' COMMENT '企业类型',
    `verified_end_time` varchar(256) DEFAULT '' COMMENT '企业认证到期时间',
    `suite_id`          varchar(128) DEFAULT '-1' COMMENT '授权应用id',
    `create_time`       datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`       datetime     DEFAULT NULL COMMENT '修改时间',
    `deleted`           tinyint(4) NOT NULL COMMENT '是否被删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4 COMMENT ='企业微信三方应用授权公司';


CREATE TABLE `t_channel_user`
(
    `id`               bigint(20)                              NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
    `consumer_user_id` bigint(20)                              NOT NULL COMMENT '消费者用户表ID',
    `channel_name`     varchar(32) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '渠道名称',
    `channel_type`     varchar(32) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '渠道枚举值',
    `open_user_id`     varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '渠道用户ID，由第三方系统提供',
    `extends_field1`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展属性1',
    `extends_field2`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展属性2',
    `note`             varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '说明',
    `create_time`      datetime                                NOT NULL COMMENT '创建时间',
    `update_time`      datetime                                DEFAULT NULL COMMENT '修改时间',
    `deleted`          tinyint(4)                              NOT NULL COMMENT '是否被删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='渠道用户表';