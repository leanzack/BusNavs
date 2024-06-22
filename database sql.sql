-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               11.3.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for busnavs
CREATE DATABASE IF NOT EXISTS `busnavs` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `busnavs`;

-- Dumping structure for table busnavs.driver
CREATE TABLE IF NOT EXISTS `driver` (
  `driver_id` int(11) NOT NULL AUTO_INCREMENT,
  `driver_name` varchar(50) DEFAULT NULL,
  `fare` decimal(20,6) DEFAULT NULL,
  `route_name` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`driver_id`),
  UNIQUE KEY `driver_name` (`driver_name`)
) ENGINE=InnoDB AUTO_INCREMENT=334 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table busnavs.driver: ~2 rows (approximately)
DELETE FROM `driver`;
INSERT INTO `driver` (`driver_id`, `driver_name`, `fare`, `route_name`) VALUES
	(111, 'Toril-Driver', 536.000000, 'SM, SM, SM, SM, SM, Outs, SW-Works'),
	(222, 'Sawmill-Driver', 14.000000, 'Outs'),
	(333, 'Just-Driver', NULL, NULL);

-- Dumping structure for table busnavs.passenger
CREATE TABLE IF NOT EXISTS `passenger` (
  `passenger_id` int(11) NOT NULL AUTO_INCREMENT,
  `passenger_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`passenger_id`),
  UNIQUE KEY `passenger_name` (`passenger_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table busnavs.passenger: ~3 rows (approximately)
DELETE FROM `passenger`;
INSERT INTO `passenger` (`passenger_id`, `passenger_name`) VALUES
	(3, 'lean'),
	(2, 'Paninsoro'),
	(4, 'pupa');

-- Dumping structure for table busnavs.routes
CREATE TABLE IF NOT EXISTS `routes` (
  `route_id` int(3) NOT NULL AUTO_INCREMENT,
  `route_name` varchar(50) DEFAULT NULL,
  `driver_name` varchar(50) DEFAULT NULL,
  `fare` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`route_id`),
  UNIQUE KEY `route_name` (`route_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table busnavs.routes: ~4 rows (approximately)
DELETE FROM `routes`;
INSERT INTO `routes` (`route_id`, `route_name`, `driver_name`, `fare`) VALUES
	(6, 'UM', NULL, 30.20),
	(7, 'GMALL', NULL, 12.33),
	(8, 'SM', NULL, 100.00),
	(9, 'SW-Works', NULL, 22.00),
	(10, 'Outs', NULL, 14.00);

-- Dumping structure for table busnavs.selectedroutes
CREATE TABLE IF NOT EXISTS `selectedroutes` (
  `driver_name` varchar(255) NOT NULL,
  `selected_route` varchar(255) NOT NULL,
  `fare` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`driver_name`,`selected_route`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table busnavs.selectedroutes: ~7 rows (approximately)
DELETE FROM `selectedroutes`;
INSERT INTO `selectedroutes` (`driver_name`, `selected_route`, `fare`) VALUES
	('Sawmill-Driver', 'GMALL', 12.33),
	('Sawmill-Driver', 'Outs', 3311.00),
	('Toril-Driver', 'GMALL', 12.33),
	('Toril-Driver', 'Outs', 2233.00),
	('Toril-Driver', 'SM', 100.00),
	('Toril-Driver', 'SW-Works', 22.00),
	('Toril-Driver', 'UM', 30.20);

-- Dumping structure for table busnavs.ticket
CREATE TABLE IF NOT EXISTS `ticket` (
  `ticket_id` int(11) NOT NULL AUTO_INCREMENT,
  `fare` decimal(10,2) NOT NULL DEFAULT 0.00,
  `passenger_name` varchar(50) DEFAULT NULL,
  `driver_name` varchar(50) DEFAULT NULL,
  `route_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ticket_id`),
  KEY `passenger_name` (`passenger_name`),
  KEY `driver_name` (`driver_name`),
  KEY `route_name` (`route_name`),
  CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`passenger_name`) REFERENCES `passenger` (`passenger_name`),
  CONSTRAINT `ticket_ibfk_2` FOREIGN KEY (`driver_name`) REFERENCES `driver` (`driver_name`),
  CONSTRAINT `ticket_ibfk_3` FOREIGN KEY (`route_name`) REFERENCES `routes` (`route_name`)
) ENGINE=InnoDB AUTO_INCREMENT=910 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table busnavs.ticket: ~4 rows (approximately)
DELETE FROM `ticket`;
INSERT INTO `ticket` (`ticket_id`, `fare`, `passenger_name`, `driver_name`, `route_name`) VALUES
	(124, 100.00, 'lean', 'Toril-Driver', 'SM'),
	(392, 14.00, 'lean', 'Toril-Driver', 'Outs'),
	(549, 22.00, 'lean', 'Toril-Driver', 'SW-Works'),
	(630, 14.00, 'lean', 'Sawmill-Driver', 'Outs');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
