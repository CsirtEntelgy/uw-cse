-- Josh CHO
-- 1773497

-- number of rows : 1

SELECT SUM(f.capacity) as capacity
FROM FLIGHTS as f, MONTHS as m
WHERE f.month_id = m.mid AND
	m.month = 'July' AND
	f.day_of_month = 10 AND
	((f.origin_city = 'Seattle WA' AND f.dest_city = 'San Francisco CA') OR
	(f.origin_city = 'San Francisco CA' AND f.dest_city = 'Seattle WA'));