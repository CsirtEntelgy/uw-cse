-- Josh Cho
-- 1773497

-- number of rows : 4
-- query time : 3 seconds

/* [First 20 Rows]
Alaska Airlines Inc.
SkyWest Airlines Inc.
United Air Lines Inc.
Virgin America
*/

SELECT DISTINCT 
	c.name as carrier
FROM 
	CARRIERS as c, FLIGHTS as f
WHERE
	c.cid = f.carrier_id
	AND f.dest_city = 'San Francisco CA'
	AND f.origin_city = 'Seattle WA'
ORDER BY 
	c.name ASC;