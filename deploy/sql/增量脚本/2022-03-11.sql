drop table if exists t_consultation;
CREATE TABLE `t_consultation`
(
    `id`                   bigint(20)   NOT NULL AUTO_INCREMENT,
    `tax_categories_codes` varchar(64)  NOT NULL COMMENT '所属税种码值',
    `tax_categories_names` varchar(256) DEFAULT NULL COMMENT '所属税种名称',
    `tax_practices`        varchar(255) DEFAULT NULL COMMENT '所属税务实务名称',
    `industry_codes`       varchar(64)  DEFAULT NULL COMMENT '适用行业码值',
    `industry_names`       varchar(256) DEFAULT NULL COMMENT '适用行业名称',
    `customer_user_id`     bigint(20)   NOT NULL COMMENT '客户用户id',
    `professor_user_id`    bigint(20)   DEFAULT NULL COMMENT '专家用户id',
    `finish_time`          datetime     DEFAULT NULL COMMENT '解答日期',
    `status`               varchar(256) NOT NULL COMMENT '咨询状态（NOT_REPLY-已答复，\r\nHAVE_REPLY）',
    `create_time`          datetime     NOT NULL,
    `published`            tinyint(4)   NOT NULL COMMENT '是否公开(0-不公开,1-公开)',
    `phone_number`         bigint(20)   NOT NULL COMMENT '手机号',
    PRIMARY KEY (`id`, `tax_categories_codes`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='热门咨询表';