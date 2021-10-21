
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(256) NOT NULL COMMENT '用户名称',
  `password` varchar(256) NOT NULL COMMENT '用户密码',
  `name` varchar(256) NOT NULL COMMENT '用户姓名、昵称',
  `user_type` varchar(32) NOT NULL COMMENT '用户类型',
  `role_codes` varchar(1024) DEFAULT NULL COMMENT '角色码值集合（以","分隔）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
);