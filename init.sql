DROP DATABASE IF EXISTS devsim;
CREATE DATABASE devsim;

DROP USER IF EXISTS 'appuser'@'localhost';
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'StrongPasswordHere';
GRANT SELECT, INSERT, UPDATE, DELETE ON currencydb.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;