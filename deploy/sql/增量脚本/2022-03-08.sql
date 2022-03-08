CREATE TABLE `t_subscribe` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                               `consumer_user_id` bigint(20) NOT NULL COMMENT '消费者用户ID',
                               `subscribe_type` varchar(32) NOT NULL COMMENT '订阅类型',
                               `source_id` bigint(20) NOT NULL COMMENT '数据源ID',
                               `create_time` datetime NOT NULL COMMENT '创建时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8mb4 COMMENT='订阅表';