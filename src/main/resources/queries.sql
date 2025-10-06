SELECT * FROM Currency;

SELECT *
FROM Currency
WHERE abbreviation = 'EUR';

SELECT COUNT(*) AS num_currencies
FROM Currency;

SELECT *
FROM Currency
ORDER BY rate_to_usd DESC
LIMIT 1;