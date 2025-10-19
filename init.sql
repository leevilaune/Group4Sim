DROP DATABASE IF EXISTS devsim;
CREATE DATABASE devsim;

DROP USER IF EXISTS 'appuser'@'localhost';
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'StrongPasswordHere';
GRANT ALL ON devsim.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;