-- Josh CHO
-- 1773497

-- number of rows : 12

SELECT DISTINCT c.name as name
FROM FLIGHTS as f, CARRIERS as c, MONTHS as m
WHERE f.carrier_id = c.cid AND
	f.month_id = m.mid
GROUP BY m.month, f.day_of_month, c.name
HAVING COUNT(*) > 1000;