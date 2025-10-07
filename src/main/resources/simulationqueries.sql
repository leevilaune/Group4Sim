DROP DATABASE IF EXISTS simulations;
CREATE DATABASE simulations;
USE simulations;

CREATE TABLE SIMULATION (
    id INT NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    parameter_1 INT(10) NOT NULL,
    parameter_2 INT(10) NOT NULL,
    parameter_3 INT(10) NOT NULL,
    parameter_4 INT(10) NOT NULL,
    parameter_5 INT(10) NOT NULL,
    parameter_6 BIGINT NOT NULL,
    distribution VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE, CREATE, DELETE ON simulations.* TO 'appuser'@'localhost';