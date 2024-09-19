-- Adminer 4.8.1 MySQL 8.0.33 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `PasswordSaver`;
CREATE DATABASE `PasswordSaver` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `PasswordSaver`;

DROP TABLE IF EXISTS `LogTable`;
CREATE TABLE `LogTable` (
  `IdLog` int NOT NULL AUTO_INCREMENT,
  `IdUser` int NOT NULL,
  `Timestamp` timestamp NOT NULL,
  `Message` varchar(500) NOT NULL,
  PRIMARY KEY (`IdLog`),
  KEY `FK_LogTable_Users` (`IdUser`),
  CONSTRAINT `FK_LogTable_Users` FOREIGN KEY (`IdUser`) REFERENCES `Users` (`IdUser`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `LogTable` (`IdLog`, `IdUser`, `Timestamp`, `Message`) VALUES
(3,	0,	'2024-09-19 16:58:23',	'POST request to /login'),
(4,	8,	'2024-09-19 16:58:24',	'Successful login'),
(5,	8,	'2024-09-19 16:58:24',	'UserManagerDB - getUserTypeOfUser: User Type loaded'),
(6,	8,	'2024-09-19 16:58:28',	'GET request to /services/getmostusedservicesbyuser'),
(7,	8,	'2024-09-19 16:58:28',	'GET request to /passwords/getdetailedstarredpasswords'),
(8,	8,	'2024-09-19 16:58:28',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(9,	8,	'2024-09-19 16:58:28',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(10,	8,	'2024-09-19 16:58:28',	'PasswordManagerDB - getAllDetailedStarredPasswords: Starred password not loaded'),
(11,	8,	'2024-09-19 16:58:28',	'ServiceManagerDB - getMostUsedServicesByUser: Services not loaded'),
(12,	8,	'2024-09-19 17:15:16',	'GET request to /passwords/getdetailedpasswords'),
(13,	8,	'2024-09-19 17:15:17',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(14,	8,	'2024-09-19 17:15:16',	'GET request to /services'),
(15,	8,	'2024-09-19 17:15:17',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(16,	8,	'2024-09-19 17:16:48',	'GET request to /services/getdetailedservices'),
(17,	8,	'2024-09-19 17:16:48',	'GET request to /servicetypes'),
(18,	8,	'2024-09-19 17:16:48',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(19,	8,	'2024-09-19 17:16:48',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(20,	8,	'2024-09-19 17:16:48',	'ServiceManagerDB - getAllDetailedServices: Services not loaded'),
(21,	8,	'2024-09-19 17:16:49',	'ServiceManagerDB - getAllServiceTypes: Service Types loaded'),
(22,	8,	'2024-09-19 17:17:14',	'GET request to /servicetypes'),
(23,	8,	'2024-09-19 17:17:14',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(24,	8,	'2024-09-19 17:17:14',	'ServiceManagerDB - getAllServiceTypes: Service Types loaded'),
(25,	8,	'2024-09-19 17:17:28',	'POST request to /services/addservice'),
(26,	8,	'2024-09-19 17:17:28',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(27,	8,	'2024-09-19 17:17:28',	'ServiceManagerDB - checkIfServiceNameExists: Service name doesn\'t exist'),
(28,	8,	'2024-09-19 17:17:28',	'ServiceManagerDB - getServiceType: Service Type loaded'),
(29,	8,	'2024-09-19 17:17:28',	'ServiceManagerDB - addNewService: Service added'),
(30,	8,	'2024-09-19 17:17:28',	'ServiceManagerDB - getService: Service loaded'),
(31,	8,	'2024-09-19 17:17:30',	'GET request to /servicetypes'),
(32,	8,	'2024-09-19 17:17:30',	'GET request to /services/getdetailedservice'),
(33,	8,	'2024-09-19 17:17:30',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(34,	8,	'2024-09-19 17:17:30',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(35,	8,	'2024-09-19 17:17:30',	'ServiceManagerDB - getAllServiceTypes: Service Types loaded'),
(36,	8,	'2024-09-19 17:17:30',	'ServiceManagerDB - serviceExists: Service exists'),
(37,	8,	'2024-09-19 17:17:30',	'ServiceManagerDB - getDetailedService: Service loaded'),
(38,	8,	'2024-09-19 17:17:32',	'GET request to /services/getdetailedservices'),
(39,	8,	'2024-09-19 17:17:32',	'GET request to /servicetypes'),
(40,	8,	'2024-09-19 17:17:32',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(41,	8,	'2024-09-19 17:17:32',	'UserManagerDB - checkIfUserIsAdmin: User is not admin'),
(42,	8,	'2024-09-19 17:17:32',	'ServiceManagerDB - getAllDetailedServices: Services loaded'),
(43,	8,	'2024-09-19 17:17:32',	'ServiceManagerDB - getAllServiceTypes: Service Types loaded'),
(44,	0,	'2024-09-19 17:18:45',	'POST request to /login'),
(45,	8,	'2024-09-19 17:18:45',	'Invalid login'),
(46,	0,	'2024-09-19 17:19:07',	'POST request to /login');

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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `Passwords` (`IdPassword`, `Name`, `Password`, `Email`, `Username`, `IdUser`, `IdService`, `PassPhrase`, `IsStarred`, `Validity`) VALUES
(1,	'password modificata',	'UTakvLgvT6AMX7N78vKv3EK5NuPIxrgi33qGziOAeAo=',	'',	'admin',	1,	2,	'prova',	0,	1),
(2,	'password 2',	'M6zyUHZkrFOAIlpDc+RGzQ==',	'admin@admin.it',	'admin',	1,	1,	'',	1,	1),
(3,	'password 3',	'd/PkB1YwsFXzJ3H3KZRIaA==',	'admin@admin.it',	'admin',	1,	2,	'',	1,	1),
(4,	'password 4',	'HYhP7ZJ9KHRuxgzkQ9AoAQ==',	'admin@admin.it',	'admin',	1,	3,	'',	1,	0),
(5,	'prova',	'wsI7NpZLra2GWAlk+13Ilw==',	'',	'stefano',	2,	4,	'Sinonimo di prova, inglese',	0,	1),
(8,	'Password di Giovanni',	'HdoACLrXiIrApanPeue33w==',	'',	'',	1,	2,	'',	1,	1),
(9,	'Prova',	'sfsD+x3eqvcJS8bLrEsR+w==',	'',	'',	1,	1,	'',	0,	1),
(10,	'dsad',	'L4+3DMvumHLDOVIsOLRIkQ==',	'sad@d.asd',	'asd',	1,	1,	'asdas',	1,	0),
(11,	'password 2',	'smtAc4Y7oVm3Gf4HPRntuw==',	'',	'',	1,	1,	'',	0,	1),
(12,	'gfdfg',	'SDG+GnmU9vs4oHvF45JlMg==',	'',	'',	1,	2,	'',	1,	0),
(13,	'test',	'qnxHsFlZPtTLvylDZuWpPw==',	'stefano@prova.it',	'stefano',	2,	5,	'Sinonimo di prova, inglese',	1,	1),
(14,	'account unito',	'JOmSbtm6KRoSFmJ/vnAc7Q==',	'stefano.fontana266@edu.unito.it',	'stefano.fontana266',	2,	5,	'',	1,	1),
(15,	'asdasd',	'ras9QlaFtVTKQCV1HvezGg==',	'dsad@dad.asd',	'sadad',	1,	6,	'dsa',	0,	0),
(16,	'Account Spotify',	'9nsRLIekzR/C59pyT0cdXg==',	'giovanni@passwordsaver.it',	'',	5,	17,	'',	1,	1);

DROP TABLE IF EXISTS `ServiceTypes`;
CREATE TABLE `ServiceTypes` (
  `IdServiceType` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Validity` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdServiceType`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `ServiceTypes` (`IdServiceType`, `Name`, `Validity`) VALUES
(1,	'App',	1),
(2,	'Website',	1),
(3,	'Email',	1),
(4,	'Online Banking',	0),
(5,	'Prova',	0);

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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `Services` (`IdService`, `Name`, `IdServiceType`, `IdUser`, `Validity`) VALUES
(1,	'Prova App',	1,	1,	1),
(2,	'Prova Website',	2,	1,	1),
(3,	'Prova Email',	3,	1,	1),
(4,	'Prova User',	2,	2,	1),
(5,	'Prova App Stefano',	1,	2,	1),
(6,	'Online Banking',	2,	1,	1),
(7,	'Prova App 2',	1,	1,	0),
(8,	'asdas',	1,	1,	1),
(9,	'sadsad',	4,	1,	0),
(10,	'Prova da app',	4,	1,	0),
(11,	'fdg',	4,	1,	0),
(12,	'fdgf',	3,	1,	0),
(13,	'fdsf',	4,	1,	0),
(14,	'dfghjkl',	1,	1,	1),
(15,	'fdgfsdfsfsfsdf',	1,	1,	0),
(16,	'fsdfsdf',	1,	2,	0),
(17,	'Spotify',	1,	5,	1),
(18,	'Gmail',	3,	5,	1),
(19,	'Outlook',	3,	8,	1);

DROP TABLE IF EXISTS `UserTypes`;
CREATE TABLE `UserTypes` (
  `IdUserType` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Validity` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdUserType`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `UserTypes` (`IdUserType`, `Name`, `Validity`) VALUES
(1,	'ADMIN',	1),
(2,	'USER',	1),
(3,	'MANAGER',	0),
(4,	'SYSTEM',	0);

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `Users` (`IdUser`, `Email`, `Username`, `Password`, `IdUserType`, `EncodedSecretKey`, `InitializationVector`, `Validity`) VALUES
(0,	'',	'not-logged-user',	'',	2,	'',	'',	1),
(1,	'admin@passwordsaver.it',	'admin',	'$2a$10$q9eGHCoyg/NgnsN8KK6LLeW2C.s2B7Dch4HYI0I1RgStS4CSCwI26',	1,	'eUSVO1ChNus/ua5mgYkMJVVlWDd9AwWwIl1Nq+xOFXk=',	'nDuBQ5/98j40QdO5U+cdSg==',	1),
(2,	'stefano@passwordsaver.it',	'stefano',	'$2a$10$b4.Y1bdBCoSCtPCwJNwGXe7xbVImAgCgZZA0QoBFpQaqaLExDMudy',	2,	'MbKm3YGa4uWqbNF6ixNKMyzObeICuFLeeh7My0HlR7g=',	'JmEQnRmESsdvKmUl+bw44g==',	1),
(3,	'marco@passwordsaver.it',	'marco',	'$2a$10$CoAMFzyESyCADBr7gFae4uL5O04VwBVFjkk1F0fGylL0VW8EcCMIm',	2,	'XRsZwZH0VXrW15ENVHKxnl5NkAITXKIm9gByDs/L1as=',	'Ay1s/8cg4NGUrU24DWqRpg==',	0),
(4,	'sada@dasd.asd',	'sada',	'$2a$10$GbDpuOTjmD7pYCfb7J16/Obm63YpgYcU2zPwm52S3otPiKBNoDXLm',	1,	'gvwLV3iL+lGn05va2n41aXMrNl8mJtYXPqdrzHGX2ac=',	'RE9LnPzXQySfhqknwBD6cg==',	0),
(5,	'giovanni@passwordsaver.it',	'giovanni',	'$2a$10$UVFag1mXZe4OtDDi8aUzKOVBDlTtRyDlvHBpE8bTGkOPgJvqoOa6G',	2,	'fNqdEvkEUvBZN/Kz4uSmVRLbRs6tocAaQLnssblZoLs=',	'ELMsr26Uo7oOC0Ezd69/ag==',	1),
(6,	'franco@passwordsaver.it',	'franco',	'$2a$10$gbwzR8fJgAT/dr5eg0liV.5phvDufyUP9xWwDTWbQIbwhOP8/DsN6',	4,	'MsGk9xGLNXl2g3h0PwiVlhLJzg+Aewkl7wZPuVLloWI=',	'ikBvDZLCE1XXlBVYk6CE+Q==',	1),
(8,	'luigi@passwordsaver.it',	'luigi',	'$2a$10$efPKZ9NdlXQlUiagsAX7MexmuBiq97/xocM.tnjieMd5Lv06/DHlG',	2,	'/9yK5w2Tq195uT/BtU5FYrM5RkQP25Pg+/Z8XMu5qL8=',	'fK01hUWn6tjB5l9CXDi8FQ==',	1);

-- 2024-09-19 15:22:00
