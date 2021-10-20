-- 创建系统用户并授权

CREATE USER 'tax_preference'@'%' IDENTIFIED BY 'tax_preference';

GRANT SELECT, INSERT, UPDATE, DELETE ON `tax\_preference`.* TO 'tax_preference'@'%';
