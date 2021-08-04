-- Josh Cho
-- 1773497

-- number of rows : 4 (origin_city(s) as set of all cities)
-- query time : 44 seconds

/* [First 20 Rows]
Devils Lake ND
Hattiesburg/Laurel MS
St. Augustine FL
Victoria TX
*/

SELECT DISTINCT 
	f1.origin_city as city
FROM 
	FLIGHTS as f1
WHERE
	f1.origin_city NOT IN (SELECT DISTINCT
								f2.dest_city
							FROM 
								FLIGHTS as f2, 
								FLIGHTS as f3
							WHERE
								f2.origin_city = f3.dest_city
								AND f3.origin_city = 'Seattle WA')
	AND f1.origin_city NOT IN (SELECT DISTINCT 
									f4.dest_city
								FROM 
									FLIGHTS as f4
								WHERE
									f4.origin_city = 'Seattle WA')
ORDER BY 
	f1.origin_city ASC;