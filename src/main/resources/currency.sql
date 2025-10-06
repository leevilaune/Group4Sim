DROP DATABASE IF EXISTS currencydb;

CREATE DATABASE currencydb;

USE currencydb;

CREATE TABLE currency (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(5) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    rate_to_usd DECIMAL(18,10) NOT NULL
);

INSERT INTO currency (code, name, rate_to_usd) VALUES
('USD', 'United States Dollar', 1.0),
('EUR', 'Euro', 0.8515200899),
('GBP', 'British Pound Sterling', 0.7357800845),
('JPY', 'Japanese Yen', 147.4063972024),
('CNY', 'Chinese Yuan Renminbi', 7.1235407404),
('CHF', 'Swiss Franc', 0.7958601138),
('CAD', 'Canadian Dollar', 1.3830001467),
('AUD', 'Australian Dollar', 1.5010102343),
('NZD', 'New Zealand Dollar', 1.6771501893),
('SGD', 'Singapore Dollar', 1.2815301285),
('HKD', 'Hong Kong Dollar', 7.7783111511),
('INR', 'Indian Rupee', 88.1605639443),
('BRL', 'Brazilian Real', 5.3386706255),
('RUB', 'Russian Ruble', 83.1874093011),
('DKK', 'Danish Krone', 6.3577710368),
('NOK', 'Norwegian Krone', 9.8348217829),
('SEK', 'Swedish Krona', 9.3068211648),
('CZK', 'Czech Koruna', 20.7068638092),
('PLN', 'Polish Zloty', 3.6166705714),
('HUF', 'Hungarian Forint', 332.1580889305),
('RON', 'Romanian Leu', 4.3141107291),
('BGN', 'Bulgarian Lev', 1.661050307),
('HRK', 'Croatian Kuna', 6.228751199),
('RSD', 'Serbian Dinar', 99.2142029888),
('ISK', 'Icelandic Krona', 121.6297663303),
('UAH', 'Ukrainian Hryvnia', 41.3648050354),
('TRY', 'Turkish Lira', 41.3467072603),
('GEL', 'Georgian Lari', 2.6939705335),
('AMD', 'Armenian Dram', 381.702665681),
('AZN', 'Azerbaijani Manat', 1.7);

DROP USER IF EXISTS 'appuser'@'localhost';
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'StrongPasswordHere';
GRANT SELECT, INSERT, UPDATE, DELETE ON currencydb.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;