-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.6.5-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- movie 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `movie` /*!40100 DEFAULT CHARACTER SET utf8mb3 */;
USE `movie`;

-- 테이블 movie.customerdb 구조 내보내기
CREATE TABLE IF NOT EXISTS `customerdb` (
  `ID` varchar(20) NOT NULL,
  `PW` varchar(20) NOT NULL,
  `NAME` varchar(10) NOT NULL,
  `AGE` int(11) NOT NULL,
  `PHONE` int(11) NOT NULL,
  `EMAIL` varchar(30) NOT NULL,
  `GRADE` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 movie.customerdb:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `customerdb` DISABLE KEYS */;
/*!40000 ALTER TABLE `customerdb` ENABLE KEYS */;

-- 테이블 movie.moviedb 구조 내보내기
-- CREATE TABLE IF NOT EXISTS `moviedb` (
--   `movieName` varchar(30) NOT NULL,
--   `movieDate` date NOT NULL,
--   `movieAge` int(11) NOT NULL,
--   `movieCost` int(11) DEFAULT NULL,
--   PRIMARY KEY (`movieName`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 movie.moviedb:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `moviedb` DISABLE KEYS */;
/*!40000 ALTER TABLE `moviedb` ENABLE KEYS */;

-- 테이블 movie.reservationdb 구조 내보내기
CREATE TABLE IF NOT EXISTS `reservationdb` (
  `AREA` varchar(20) NOT NULL,
  `ZONE` varchar(20) NOT NULL,
  `BUY` int(11) NOT NULL,
  `LOCATION` varchar(5) NOT NULL,
  `VIEWDAY` date NOT NULL,
  `PAYDAY` date NOT NULL,
  `COST` int(11) NOT NULL,
  `movieName` varchar(30) NOT NULL,
  `customerID` varchar(20) NOT NULL,
  KEY `movieName` (`movieName`),
  KEY `customerID` (`customerID`),
  CONSTRAINT `customerID` FOREIGN KEY (`customerID`) REFERENCES `customerdb` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `movieName` FOREIGN KEY (`movieName`) REFERENCES `moviedb` (`movieName`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 movie.reservationdb:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `reservationdb` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservationdb` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
