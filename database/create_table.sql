--DROP DATABASE IF EXISTS PasswordSaver;

CREATE DATABASE PasswordSaver;

USE PasswordSaver;

/*
DROP TABLE IF EXISTS Translations;
DROP TABLE IF EXISTS Languages;
DROP TABLE IF EXISTS Passwords;
DROP TABLE IF EXISTS Services;
DROP TABLE IF EXISTS ServiceTypes;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS UserTypes;
*/

CREATE TABLE UserTypes (
    IdUserType INT NOT NULL AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Validity BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (IdUserType)
);

CREATE TABLE Users (
    IdUser INT NOT NULL AUTO_INCREMENT,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Username VARCHAR(100) NOT NULL UNIQUE, -- non so se ha senso
    Password VARCHAR(55) NOT NULL, -- CONTROLLARE Lunghezza Password criptate con Bcrypt
    IdUserType INT NOT NULL,
    Validity BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (IdUser),
    CONSTRAINT FK_Users_UserTypes FOREIGN KEY (IdUserType) REFERENCES `UserTypes`(IdUserType)
);

CREATE TABLE ServiceTypes (
    IdServiceType INT NOT NULL AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Validity BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (IdServiceType)
);

CREATE TABLE Services (
    IdService INT NOT NULL AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    IdServiceType INT NOT NULL,
    Validity BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (IdService),
    CONSTRAINT FK_Services_ServiceTypes FOREIGN KEY IdServiceType REFERENCES `ServiceTypes`(IdServiceType)
);

CREATE TABLE Passwords (
    IdPassword INT NOT NULL AUTO_INCREMENT,
    Password VARCHAR(55) NOT NULL, -- CONTROLLARE Lunghezza Password criptate con Bcrypt 
    IdUser INT NOT NULL,
    IdService INT NOT NULL,
    PassPhrase VARCHAR(200) NOT NULL,
    Validity BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (IdPassword),
    CONSTRAINT FK_Passwords_Users FOREIGN KEY (IdUser) REFERENCES `Users`(IdUser),
    CONSTRAINT FK_Passwords_Services FOREIGN KEY (IdService) REFERENCES `Services`(IdService)
);

CREATE TABLE Languages (
    IdLanguage VARCHAR(2) NOT NULL, -- esempio: it, en
    Name VARCHAR(100) NOT NULL,
    -- Flag VARCHAR(200) NOT NULL, -- image path
    Validity BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (IdLanguage)
);

CREATE TABLE Translations (
    IdTranslation VARCHAR(100) NOT NULL, -- sarà qualcosa del tipo HEADER_PAGE
    IdLanguage VARCHAR(2) NOT NULL,
    Value VARCHAR(200) NOT NULL,
    Validity BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (IdTranslation)
);

