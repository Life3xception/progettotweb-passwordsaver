-- Adminer 4.8.1 MySQL 8.0.33 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `PasswordSaver`;
CREATE DATABASE `PasswordSaver` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `PasswordSaver`;

DROP TABLE IF EXISTS `Languages`;
CREATE TABLE `Languages` (
  `IdLanguage` varchar(2) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Validity` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdLanguage`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `Passwords`;
CREATE TABLE `Passwords` (
  `IdPassword` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Password` varchar(75) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IdUser` int NOT NULL,
  `IdService` int NOT NULL,
  `PassPhrase` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IsStarred` tinyint(1) DEFAULT '0',
  `Validity` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdPassword`),
  KEY `FK_Passwords_Users` (`IdUser`),
  KEY `FK_Passwords_Services` (`IdService`),
  CONSTRAINT `FK_Passwords_Services` FOREIGN KEY (`IdService`) REFERENCES `Services` (`IdService`),
  CONSTRAINT `FK_Passwords_Users` FOREIGN KEY (`IdUser`) REFERENCES `Users` (`IdUser`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `Passwords` (`IdPassword`, `Name`, `Password`, `Email`, `Username`, `IdUser`, `IdService`, `PassPhrase`, `IsStarred`, `Validity`) VALUES
(1,	'password modificata',	'UTakvLgvT6AMX7N78vKv3EK5NuPIxrgi33qGziOAeAo=',	'',	'admin',	1,	2,	'prova',	0,	1),
(2,	'password 2',	'M6zyUHZkrFOAIlpDc+RGzQ==',	'admin@admin.it',	'admin',	1,	1,	'',	1,	1),
(3,	'password 3',	'd/PkB1YwsFXzJ3H3KZRIaA==',	'admin@admin.it',	'admin',	1,	2,	'',	1,	1),
(4,	'password 4',	'HYhP7ZJ9KHRuxgzkQ9AoAQ==',	'admin@admin.it',	'admin',	1,	3,	'',	1,	0),
(5,	'prova',	'wsI7NpZLra2GWAlk+13Ilw==',	'',	'stefano',	2,	4,	'Sinonimo di prova, inglese',	0,	1),
(6,	'test',	'nW/3gUNzYXBD/NvTvxCUVA==',	'',	'stefano',	2,	3,	'Sinonimo di prova, inglese',	0,	1),
(8,	'Password di Giovanni',	'HdoACLrXiIrApanPeue33w==',	'',	'',	1,	2,	'',	1,	1),
(9,	'Prova',	'sfsD+x3eqvcJS8bLrEsR+w==',	'',	'',	1,	1,	'',	0,	1),
(10,	'asdsa',	'V0yz27kIfH6SWLeEfgkiFg==',	'',	'',	1,	3,	'',	0,	1);

DROP TABLE IF EXISTS `ServiceTypes`;
CREATE TABLE `ServiceTypes` (
  `IdServiceType` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Validity` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdServiceType`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `ServiceTypes` (`IdServiceType`, `Name`, `Validity`) VALUES
(1,	'App',	1),
(2,	'Website',	1),
(3,	'Email',	1),
(4,	'Online Banking',	0);

DROP TABLE IF EXISTS `Services`;
CREATE TABLE `Services` (
  `IdService` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `IdServiceType` int NOT NULL,
  `IdUser` int NOT NULL,
  `Validity` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdService`),
  KEY `FK_Services_ServiceTypes` (`IdServiceType`),
  KEY `FK_Services_Users` (`IdUser`),
  CONSTRAINT `FK_Services_ServiceTypes` FOREIGN KEY (`IdServiceType`) REFERENCES `ServiceTypes` (`IdServiceType`),
  CONSTRAINT `FK_Services_Users` FOREIGN KEY (`IdUser`) REFERENCES `Users` (`IdUser`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `Services` (`IdService`, `Name`, `IdServiceType`, `IdUser`, `Validity`) VALUES
(1,	'Prova App',	1,	1,	1),
(2,	'Prova Website',	2,	1,	1),
(3,	'Prova Email',	3,	1,	1),
(4,	'Prova User',	2,	2,	1),
(5,	'Prova App',	1,	2,	1),
(6,	'Online Banking',	2,	1,	1);

DROP TABLE IF EXISTS `Translations`;
CREATE TABLE `Translations` (
  `IdTranslation` varchar(100) NOT NULL,
  `IdLanguage` varchar(2) NOT NULL,
  `Value` varchar(200) NOT NULL,
  `Validity` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdTranslation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `UserTypes`;
CREATE TABLE `UserTypes` (
  `IdUserType` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Validity` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdUserType`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `UserTypes` (`IdUserType`, `Name`, `Validity`) VALUES
(1,	'Admin',	1),
(2,	'User',	1),
(3,	'Manager',	0);

DROP TABLE IF EXISTS `Users`;
CREATE TABLE `Users` (
  `IdUser` int NOT NULL AUTO_INCREMENT,
  `Email` varchar(100) NOT NULL,
  `Username` varchar(100) NOT NULL,
  `Password` varchar(75) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IdUserType` int NOT NULL,
  `EncodedSecretKey` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `InitializationVector` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Validity` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdUser`),
  UNIQUE KEY `Email` (`Email`),
  UNIQUE KEY `Username` (`Username`),
  KEY `FK_Users_UserTypes` (`IdUserType`),
  CONSTRAINT `FK_Users_UserTypes` FOREIGN KEY (`IdUserType`) REFERENCES `UserTypes` (`IdUserType`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `Users` (`IdUser`, `Email`, `Username`, `Password`, `IdUserType`, `EncodedSecretKey`, `InitializationVector`, `Validity`) VALUES
(1,	'admin@passwordsaver.it',	'admin',	'$2a$10$q9eGHCoyg/NgnsN8KK6LLeW2C.s2B7Dch4HYI0I1RgStS4CSCwI26',	1,	'eUSVO1ChNus/ua5mgYkMJVVlWDd9AwWwIl1Nq+xOFXk=',	'nDuBQ5/98j40QdO5U+cdSg==',	1),
(2,	'stefano@passwordsaver.it',	'stefano',	'$2a$10$b4.Y1bdBCoSCtPCwJNwGXe7xbVImAgCgZZA0QoBFpQaqaLExDMudy',	2,	'MbKm3YGa4uWqbNF6ixNKMyzObeICuFLeeh7My0HlR7g=',	'JmEQnRmESsdvKmUl+bw44g==',	1),
(3,	'marco@passwordsaver.it',	'marco',	'$2a$10$CoAMFzyESyCADBr7gFae4uL5O04VwBVFjkk1F0fGylL0VW8EcCMIm',	2,	'XRsZwZH0VXrW15ENVHKxnl5NkAITXKIm9gByDs/L1as=',	'Ay1s/8cg4NGUrU24DWqRpg==',	1);

-- 2024-09-15 14:14:52
