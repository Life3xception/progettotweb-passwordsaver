USE PasswordSaver;

INSERT INTO UserTypes(Name) VALUES ("Admin"), ("User");

INSERT INTO Users(Email, Username, Password, IdUserType) VALUES
("admin@passwordsaver.it", "admin", "$2a$10$q9eGHCoyg/NgnsN8KK6LLeW2C.s2B7Dch4HYI0I1RgStS4CSCwI26", 1), --pwd: nidma
("stefano@passwordsaver.it", "stefano", "$2a$10$b4.Y1bdBCoSCtPCwJNwGXe7xbVImAgCgZZA0QoBFpQaqaLExDMudy", 2); -- pwd:test

INSERT INTO ServiceTypes(Name) VALUES ("App"), ("Website"), ("Email");

INSERT INTO Services(Name, IdServiceType, IdUser) VALUES
("Prova App", 1, 1),
("Prova Website", 2, 1),
("Prova Email", 3, 1),
("Prova User", 2, 2);

INSERT INTO Passwords(Password, Email, Username, IdUser, IdService, PassPhrase, IsStarred) VALUES
("password 1", "Pwd App 1", "admin@admin.it", "admin", 1, 1, "", 0),
("password 2", "Pwd App 2", "admin@admin.it", "admin", 1, 1, "", 1),
("password 3", "Pwd Website", "admin@admin.it", "admin", 1, 2, "", 1),
("password 4", "Pwd Email", "admin@admin.it", "admin", 1, 3, "", 0);