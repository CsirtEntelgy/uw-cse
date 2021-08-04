-- Josh CHO
-- 1773497

-- number of rows : 22

SELECT c.name as name,
	SUM(f.departure_delay) as delay
FROM FLIGHTS as f, CARRIERS as c
WHERE f.carrier_id = c.cid
GROUP BY c.name;