-- Josh Cho
-- 1773497

-- number of rows : 4
-- query time : 4 seconds

/* [First 20 Rows]
Alaska Airlines Inc.
SkyWest Airlines Inc.
United Air Lines Inc.
Virgin America
*/

WITH
	nest as (SELECT DISTINCT 
				f.carrier_id
			FROM 
				FLIGHTS as f
			WHERE
				f.origin_city = 'Seattle WA'
				AND f.dest_city = 'San Francisco CA')
SELECT DISTINCT 
	c.name as carrier
FROM 
	CARRIERS as c, nest as n
WHERE
	c.cid = n.carrier_id
ORDER BY 
	c.name ASC;