-- --------------------------------------------------------
-- Värd:                         127.0.0.1
-- Serverversion:                8.0.19 - MySQL Community Server - GPL
-- Server-OS:                    Win64
-- HeidiSQL Version:             10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumpar databasstruktur för holiday_maker
CREATE DATABASE IF NOT EXISTS `holiday_maker` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_swedish_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `holiday_maker`;

-- Dumpar struktur för tabell holiday_maker.bookings
CREATE TABLE IF NOT EXISTS `bookings` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` int unsigned NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `number_of_customers` int NOT NULL,
  `room_id` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_bookings_customers` (`customer_id`),
  KEY `FK_booking_room` (`room_id`),
  CONSTRAINT `FK_booking_room` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id_room`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_bookings_customers` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dataexport var bortvalt.

-- Dumpar struktur för tabell holiday_maker.customers
CREATE TABLE IF NOT EXISTS `customers` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dataexport var bortvalt.

-- Dumpar struktur för tabell holiday_maker.rooms
CREATE TABLE IF NOT EXISTS `rooms` (
  `id_room` int unsigned NOT NULL AUTO_INCREMENT,
  `room_size_id` int unsigned NOT NULL DEFAULT '1',
  `location` varchar(50) NOT NULL,
  `pool` bit(1) NOT NULL,
  `live_music` bit(1) NOT NULL,
  `childrens_club` bit(1) NOT NULL,
  `restaurant` bit(1) NOT NULL,
  PRIMARY KEY (`id_room`),
  KEY `FK_rooms_room_size` (`room_size_id`),
  CONSTRAINT `FK_rooms_room_size` FOREIGN KEY (`room_size_id`) REFERENCES `room_size` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dataexport var bortvalt.

-- Dumpar struktur för tabell holiday_maker.room_size
CREATE TABLE IF NOT EXISTS `room_size` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `number_of_beds` tinyint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dataexport var bortvalt.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
