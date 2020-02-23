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
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumpar data för tabell holiday_maker.bookings: ~12 rows (ungefär)
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` (`id`, `customer_id`, `start_date`, `end_date`, `number_of_customers`, `room_id`) VALUES
	(16, 1, '2020-06-01', '2020-06-07', 2, 20),
	(18, 3, '2020-06-06', '2020-06-10', 5, 18),
	(19, 4, '2020-07-05', '2020-07-12', 4, 6),
	(20, 2, '2020-07-01', '2020-07-07', 3, 4),
	(21, 5, '2020-06-20', '2020-06-27', 7, 9),
	(22, 3, '2020-06-24', '2020-06-30', 5, 2),
	(26, 7, '2020-06-06', '2020-06-12', 5, 2),
	(28, 16, '2020-07-20', '2020-07-27', 2, 1),
	(29, 5, '2020-06-04', '2020-06-27', 2, 7),
	(30, 1, '2020-06-29', '2020-07-06', 2, 12),
	(31, 6, '2020-06-01', '2020-06-08', 2, 12),
	(32, 17, '2020-06-08', '2020-06-17', 4, 20),
	(33, 11, '2020-06-06', '2020-06-13', 2, 14),
	(34, 13, '2020-06-01', '2020-06-07', 2, 6),
	(35, 12, '2020-06-06', '2020-06-13', 2, 8),
	(36, 7, '2020-06-17', '2020-06-24', 2, 1),
	(37, 16, '2020-07-09', '2020-07-16', 2, 19),
	(38, 15, '2020-07-01', '2020-07-07', 2, 16),
	(39, 14, '2020-06-25', '2020-07-01', 2, 3),
	(40, 1, '2020-07-06', '2020-07-13', 2, 8),
	(41, 10, '2020-06-15', '2020-06-22', 2, 8),
	(42, 3, '2020-06-07', '2020-06-15', 2, 15),
	(43, 9, '2020-06-30', '2020-07-06', 2, 7),
	(44, 5, '2020-06-15', '2020-06-22', 2, 14),
	(45, 4, '2020-06-01', '2020-06-07', 2, 1),
	(48, 6, '2020-06-12', '2020-06-19', 2, 12);
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;

-- Dumpar struktur för tabell holiday_maker.customers
CREATE TABLE IF NOT EXISTS `customers` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumpar data för tabell holiday_maker.customers: ~16 rows (ungefär)
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` (`id`, `name`) VALUES
	(1, 'Marie Severfelt'),
	(2, 'Tobbe Severfelt'),
	(3, 'Adam Adon'),
	(4, 'Hassan Abed'),
	(5, 'William Pepe'),
	(6, 'Marcus Aubameyang'),
	(7, 'Johan Karlsson'),
	(8, 'Anna Nanna'),
	(9, 'Benjamin De Bruyne'),
	(10, 'Helena Sterling'),
	(11, 'Sebbe Ag'),
	(12, 'Mantas William'),
	(13, 'Sandra Tost'),
	(14, 'Thomas Lönkvist'),
	(15, 'Sebastian Öbö'),
	(16, 'Zlatan Elvinsson'),
	(17, 'Kevin Messi');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;

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

-- Dumpar data för tabell holiday_maker.rooms: ~20 rows (ungefär)
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` (`id_room`, `room_size_id`, `location`, `pool`, `live_music`, `childrens_club`, `restaurant`) VALUES
	(1, 3, 'Bahamas', b'1', b'1', b'0', b'0'),
	(2, 3, 'Bahamas', b'0', b'1', b'1', b'0'),
	(3, 3, 'Crete', b'1', b'0', b'0', b'1'),
	(4, 2, 'Mallorca', b'0', b'0', b'0', b'1'),
	(5, 2, 'Dubai', b'1', b'1', b'1', b'1'),
	(6, 2, 'Barcelona', b'0', b'1', b'1', b'0'),
	(7, 1, 'Crete', b'1', b'0', b'0', b'0'),
	(8, 1, 'Mallorca', b'1', b'1', b'0', b'1'),
	(9, 3, 'Dubai', b'1', b'1', b'1', b'0'),
	(10, 1, 'Barcelona', b'1', b'0', b'0', b'1'),
	(11, 2, 'Bahamas', b'0', b'0', b'0', b'1'),
	(12, 2, 'Crete', b'1', b'1', b'0', b'1'),
	(13, 1, 'Bahamas', b'1', b'1', b'1', b'1'),
	(14, 3, 'Crete', b'1', b'1', b'0', b'0'),
	(15, 3, 'Mallorca', b'1', b'0', b'1', b'1'),
	(16, 1, 'Mallorca', b'0', b'1', b'1', b'1'),
	(17, 1, 'Dubai', b'1', b'0', b'1', b'1'),
	(18, 3, 'Dubai', b'1', b'0', b'0', b'1'),
	(19, 3, 'Barcelona', b'1', b'1', b'1', b'0'),
	(20, 2, 'Barcelona', b'1', b'0', b'1', b'0');
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;

-- Dumpar struktur för tabell holiday_maker.room_size
CREATE TABLE IF NOT EXISTS `room_size` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `number_of_beds` tinyint unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumpar data för tabell holiday_maker.room_size: ~3 rows (ungefär)
/*!40000 ALTER TABLE `room_size` DISABLE KEYS */;
INSERT INTO `room_size` (`id`, `name`, `number_of_beds`) VALUES
	(1, 'Small', 2),
	(2, 'Medium', 4),
	(3, 'Large', 8);
/*!40000 ALTER TABLE `room_size` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
