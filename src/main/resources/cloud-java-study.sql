-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.22 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for demo
CREATE DATABASE IF NOT EXISTS `demo` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `demo`;

-- Dumping structure for table demo.copy1
CREATE TABLE IF NOT EXISTS `copy1` (
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.copy1: ~4 rows (approximately)
/*!40000 ALTER TABLE `copy1` DISABLE KEYS */;
INSERT INTO `copy1` (`name`) VALUES
	('1'),
	('2'),
	('3'),
	('4');
/*!40000 ALTER TABLE `copy1` ENABLE KEYS */;

-- Dumping structure for table demo.copy2
CREATE TABLE IF NOT EXISTS `copy2` (
  `name` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.copy2: ~0 rows (approximately)
/*!40000 ALTER TABLE `copy2` DISABLE KEYS */;
/*!40000 ALTER TABLE `copy2` ENABLE KEYS */;

-- Dumping structure for table demo.tb_dictionary
CREATE TABLE IF NOT EXISTS `tb_dictionary` (
  `id` varchar(50) NOT NULL,
  `key` varchar(50) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.tb_dictionary: ~4 rows (approximately)
/*!40000 ALTER TABLE `tb_dictionary` DISABLE KEYS */;
INSERT INTO `tb_dictionary` (`id`, `key`, `value`, `description`) VALUES
	('1', 'sex', '男', NULL),
	('2', 'sex', '女', NULL),
	('3', 'city', '武汉', NULL),
	('4', 'city', '成都', NULL);
/*!40000 ALTER TABLE `tb_dictionary` ENABLE KEYS */;

-- Dumping structure for table demo.tb_menu
CREATE TABLE IF NOT EXISTS `tb_menu` (
  `id` varchar(50) NOT NULL,
  `parent_id` varchar(50) DEFAULT NULL,
  `menu_type` int(11) DEFAULT NULL COMMENT '0菜单 1功能',
  `menu_code` varchar(255) DEFAULT NULL COMMENT 'url对应的数字编号',
  `url` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.tb_menu: ~7 rows (approximately)
/*!40000 ALTER TABLE `tb_menu` DISABLE KEYS */;
INSERT INTO `tb_menu` (`id`, `parent_id`, `menu_type`, `menu_code`, `url`, `description`) VALUES
	('0', '0', 0, '0', '', NULL),
	('1', '0', 1, 'PROJ_GET', '/project/query/{userId}', '查询用户分享项目'),
	('2', '0', 1, 'PROJ_GETJ', '/project/querybyjson', '查询用户分享项目'),
	('3', '0', 1, 'PROJ_GETALL', '/project/queryall', '查询所有项目'),
	('4', '0', 1, 'PROJ_DEL', '/project/del/{id}', '删除项目'),
	('5', '0', 1, 'PROJ_GETPAGES', '/project/querypages', '分页查询项目'),
	('6', '0', 1, 'PROJ_ADD', '/project/add', '创建项目');
/*!40000 ALTER TABLE `tb_menu` ENABLE KEYS */;

-- Dumping structure for table demo.tb_model
CREATE TABLE IF NOT EXISTS `tb_model` (
  `id` varchar(50) NOT NULL,
  `project_id` varchar(50) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longtitude` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.tb_model: ~8 rows (approximately)
/*!40000 ALTER TABLE `tb_model` DISABLE KEYS */;
INSERT INTO `tb_model` (`id`, `project_id`, `latitude`, `longtitude`) VALUES
	('1', '1', '5', '5'),
	('2', '2', '6', '6'),
	('3', '3', '7', '7'),
	('4', '4', '4', '4'),
	('5', '5', '5', '5'),
	('6', '6', '6', '6'),
	('7', '7', '7', '7'),
	('8', '8', '8', '8');
/*!40000 ALTER TABLE `tb_model` ENABLE KEYS */;

-- Dumping structure for table demo.tb_project
CREATE TABLE IF NOT EXISTS `tb_project` (
  `id` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `is_public` int(10) DEFAULT NULL COMMENT '1=公开 2=私有 3=共享',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.tb_project: ~9 rows (approximately)
/*!40000 ALTER TABLE `tb_project` DISABLE KEYS */;
INSERT INTO `tb_project` (`id`, `name`, `user_id`, `is_public`) VALUES
	('1', '1', '1', 1),
	('2', '2', '2', 1),
	('3', '3', '3', 2),
	('4', '4', '4', 2),
	('5', '5', '5', 2),
	('6', '6', '6', 2),
	('7', '7', '7', 1),
	('8', '8', '8', 1),
	('9', '9', '9', 2);
/*!40000 ALTER TABLE `tb_project` ENABLE KEYS */;

-- Dumping structure for table demo.tb_role
CREATE TABLE IF NOT EXISTS `tb_role` (
  `id` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.tb_role: ~3 rows (approximately)
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
INSERT INTO `tb_role` (`id`, `name`, `type`, `description`) VALUES
	('0', '管理员', '0', NULL),
	('1', '高级会员', '1', NULL),
	('2', '会员', '2', NULL);
/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;

-- Dumping structure for table demo.tb_role_menu
CREATE TABLE IF NOT EXISTS `tb_role_menu` (
  `id` varchar(50) NOT NULL,
  `role_id` varchar(50) DEFAULT NULL,
  `menu_id` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.tb_role_menu: ~6 rows (approximately)
/*!40000 ALTER TABLE `tb_role_menu` DISABLE KEYS */;
INSERT INTO `tb_role_menu` (`id`, `role_id`, `menu_id`, `description`) VALUES
	('1', '1', '1', NULL),
	('2', '1', '2', NULL),
	('3', '1', '3', NULL),
	('4', '2', '4', NULL),
	('5', '2', '5', NULL),
	('6', '2', '6', NULL);
/*!40000 ALTER TABLE `tb_role_menu` ENABLE KEYS */;

-- Dumping structure for table demo.tb_stu
CREATE TABLE IF NOT EXISTS `tb_stu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `address` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.tb_stu: ~0 rows (approximately)
/*!40000 ALTER TABLE `tb_stu` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_stu` ENABLE KEYS */;

-- Dumping structure for table demo.tb_user
CREATE TABLE IF NOT EXISTS `tb_user` (
  `id` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.tb_user: ~2 rows (approximately)
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` (`id`, `name`, `password`, `email`, `address`, `status`, `description`) VALUES
	('1', 'bob', '123456', NULL, '', NULL, NULL),
	('2', 'admin', '123456', NULL, '', NULL, NULL),
	('3', 'string', 'string', NULL, NULL, NULL, NULL),
	('4', 'zz', 'zz', NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;

-- Dumping structure for table demo.tb_user_role
CREATE TABLE IF NOT EXISTS `tb_user_role` (
  `id` varchar(50) NOT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `role_id` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.tb_user_role: ~2 rows (approximately)
/*!40000 ALTER TABLE `tb_user_role` DISABLE KEYS */;
INSERT INTO `tb_user_role` (`id`, `user_id`, `role_id`, `description`) VALUES
	('1', '1', '1', NULL),
	('2', '2', '0', NULL);
/*!40000 ALTER TABLE `tb_user_role` ENABLE KEYS */;

-- Dumping structure for table demo.tb_user_share_project
CREATE TABLE IF NOT EXISTS `tb_user_share_project` (
  `id` varchar(50) NOT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `project_id` varchar(50) DEFAULT NULL,
  `share_user_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demo.tb_user_share_project: ~3 rows (approximately)
/*!40000 ALTER TABLE `tb_user_share_project` DISABLE KEYS */;
INSERT INTO `tb_user_share_project` (`id`, `user_id`, `project_id`, `share_user_id`) VALUES
	('1', '1', '1', '2'),
	('2', '3', '3', '2'),
	('3', '4', '4', '2');
/*!40000 ALTER TABLE `tb_user_share_project` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
