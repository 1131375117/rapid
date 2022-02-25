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
    PRIMARY KEY (`id`, `tax_categories_codes`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4 COMMENT ='热门咨询表';

CREATE TABLE `t_consultation_content`
(
    `id`              bigint(20)  NOT NULL AUTO_INCREMENT,
    `consultation_id` bigint(20)  NOT NULL,
    `content_type`    varchar(64) NOT NULL COMMENT '咨询内容类型(question,answer)',
    `content`         text COMMENT '文本内容',
    `image_uris`      varchar(512) DEFAULT NULL COMMENT '咨询图片',
    `sort`            int(4)      NOT NULL COMMENT '排序字段',
    `create_time`     datetime    NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 24
  DEFAULT CHARSET = utf8mb4 COMMENT ='热门咨询表';


CREATE TABLE `t_api_access_key`
(
    `id`                bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
    `consumer_user_id`  bigint(20)   NOT NULL COMMENT '消费者用户id',
    `access_key_id`     varchar(128) NOT NULL COMMENT 'accessKeyId',
    `access_key_secret` varchar(128) NOT NULL COMMENT 'accessKeySecret',
    `enable`            tinyint(4)   NOT NULL COMMENT '是否可用',
    `create_time`       datetime     NOT NULL COMMENT '创建时间',
    `last_use_time`     datetime DEFAULT NULL COMMENT '最后时间时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4 COMMENT ='API访问密钥';

INSERT INTO t_api_access_key
VALUES (1, -1, 'LTAI5tFPbwtd5UNZW9jBWBCL', 'QS9bGktBytwMdrcW5iLyVyBA0lXeOP', 1, CURRENT_TIMESTAMP, null);