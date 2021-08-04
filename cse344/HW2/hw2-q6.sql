-- Josh CHO
-- 1773497

-- number of rows : 3

SELECT c.name as carrier,
	MAX(f.price) as max_price
FROM FLIGHTS as f, CARRIERS as c
WHERE f.carrier_id = c.cid and
	((f.origin_city = 'Seattle WA' AND f.dest_city = 'New York NY') OR
	(f.origin_city = 'New York NY' AND f.dest_city = 'Seattle WA'))
GROUP BY c.name;