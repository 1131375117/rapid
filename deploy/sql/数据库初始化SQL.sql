-- 创建系统用户并授权

CREATE USER 'tax_preference'@'%' IDENTIFIED BY 'tax_preference';

GRANT SELECT, INSERT, UPDATE, DELETE ON `tax\_preference`.* TO 'tax_preference'@'%';


INSERT INTO `t_user` VALUES ('1', 'admin', 'c06bd8424a3a2e717ffae750e0bd2fed', '管理员', 'PRODUCER_USER', 'ADMIN', '0', '2021-10-18 17:03:59', '0');
INSERT INTO `t_role` VALUES ('1', '系统管理员', 'ADMIN', '*', '系统的超级管理员、拥有最高权限、默认可操作所有功能模块');
