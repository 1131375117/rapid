CREATE TABLE `t_wwx_third_company`
(
    `id`                bigint PRIMARY KEY AUTO_INCREMENT COMMENT 'ID主键',
    `corp_id`           varchar(45)  DEFAULT '' COMMENT '企业id',
    `permanent_code`    varchar(512) DEFAULT '' COMMENT '企业永久授权码',
    `corp_name`         varchar(50)  DEFAULT '' COMMENT '企业名称',
    `corp_full_name`    varchar(100) DEFAULT '' COMMENT '企业全称',
    `subject_type`      varchar(512) DEFAULT '' COMMENT '企业类型',
    `verified_end_time` varchar(512) DEFAULT '' COMMENT '企业认证到期时间',
    `agent_id`          int(10)      DEFAULT '0' COMMENT '授权应用id',
    `status`            tinyint(3)   DEFAULT '0' COMMENT '账户状态，-1为删除，禁用为0 启用为1',
    `create_time`       datetime COMMENT '创建时间',
    `update_time`       datetime COMMENT '修改时间'
) COMMENT '企业微信三方应用授权公司';

CREATE TABLE `t_wwx_third_user`
(
    `id`                bigint PRIMARY KEY AUTO_INCREMENT COMMENT 'ID主键',
    `corp_id`           varchar(45)      DEFAULT '' COMMENT '企业id',
    `user_id`           varchar(100)     DEFAULT '' COMMENT '用户id',
    `name`              varchar(50)      DEFAULT '' COMMENT '部门名称',
    `parent_id`         int(10)          DEFAULT '0' COMMENT '父部门id',
    `position`          varchar(100)     DEFAULT '0' COMMENT '职位',
    `gender`            varchar(10)      DEFAULT '' COMMENT '性别',
    `email`             varchar(100)     DEFAULT '' COMMENT '邮箱',
    `is_leader_in_dept` varchar(10)      DEFAULT '' COMMENT '是否是部门负责人',
    `avatar`            varchar(512)     DEFAULT '' COMMENT '头像',
    `thumb_avatar`      varchar(512)     DEFAULT '' COMMENT '头像缩略图',
    `telephone`         varchar(50)      DEFAULT '' COMMENT '电话',
    `alias`             varchar(50)      DEFAULT '' COMMENT '别名',
    `address`           varchar(100)     DEFAULT '' COMMENT '地址',
    `open_user_id`      varchar(100)     DEFAULT '' COMMENT 'open_user_id',
    `main_department`   int(10)          DEFAULT '0' COMMENT '主部门id',
    `qr_code`           varchar(512)     DEFAULT '' COMMENT '二维码',
    `status`            tinyint(3)       DEFAULT '0' COMMENT '状态，-1为删除，禁用为0 启用为1',
    `create_time`       int(10) unsigned DEFAULT '0' COMMENT '创建时间',
    `update_time`       int(10) unsigned DEFAULT '0' COMMENT '修改时间'
) COMMENT ='企业微信三方应用授权人员';