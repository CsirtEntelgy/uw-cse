-- Josh CHO
-- 1773497

-- number of rows : 6

SELECT c.name as name,
	(AVG(f.canceled) * 100) as percentage
FROM FLIGHTS as f, CARRIERS as c
WHERE f.origin_city = 'Seattle WA' AND
	f.carrier_id = c.cid
GROUP BY c.name
HAVING AVG(f.canceled) > 0.005;