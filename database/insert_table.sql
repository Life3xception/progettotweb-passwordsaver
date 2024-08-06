USE PasswordSaver;

INSERT INTO UserTypes(Name) VALUES ("Admin"), ("User");

INSERT INTO Users(Email, Username, Password, IdUserType) VALUES
("admin@passwordsaver.it", "admin", "$2a$12$yE7L/PSAKZhbPwL14lD1bu0ZQeqCjjs9rKPOkEAv/rzw3tUn8okQC", 1);