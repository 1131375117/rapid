-- 创建系统用户并授权

CREATE USER 'tax_preference'@'%' IDENTIFIED BY '12345678Aa';

GRANT ALL PRIVILEGES ON `tax-preference`.* TO 'tax_preference'@'%';