-- 用户及权限
create user 'huenhui'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON *.* TO 'huenhui'@'%' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* to 'root'@'%' IDENTIFIED by '123456' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- 创建数据库
create database if not exists heh_test_db;

-- 创建数据表
CREATE TABLE IF NOT EXISTS `heh_test_db`.`heh_test_table` (
    `id` INT(11) auto_increment NOT NULL,
    `name` VARCHAR(40) NOT NULL,
    `age` INT(11) NOT NULL,
    `status` TINYINT(4) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
    ) ENGINE= InnoDB DEFAULT CHARSET=utf8;

-- 初始化数据
INSERT INTO `heh_test_db`.`heh_test_table` VALUES (1, '胡恩会', 25, 1);
