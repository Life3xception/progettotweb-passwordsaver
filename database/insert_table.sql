USE PasswordSaver;

INSERT INTO UserTypes(Name) VALUES ("Admin"), ("User");

INSERT INTO Users(Email, Username, Password, IdUserType) VALUES
("admin@passwordsaver.it", "admin", "$2a$10$q9eGHCoyg/NgnsN8KK6LLeW2C.s2B7Dch4HYI0I1RgStS4CSCwI26", 1);

INSERT INTO ServiceTypes(Name) VALUES ("App"), ("Website"), ("Email");

INSERT INTO Services(Name, IdServiceType, IdUser) VALUES
("Prova App", 1, 1),
("Prova Website", 2, 1),
("Prova Email", 3, 1);

INSERT INTO Passwords(Password, Email, IdUser, IdService, PassPhrase) VALUES
("Pwd App 1", "admin@admin.it", 1, 1, ""),
("Pwd App 2", "admin@admin.it", 1, 1, ""),
("Pwd Website", "admin@admin.it", 1, 2, ""),
("Pwd Email", "admin@admin.it", 1, 3, "");