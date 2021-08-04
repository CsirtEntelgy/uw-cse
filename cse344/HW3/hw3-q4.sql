-- Josh Cho
-- 1773497

-- number of rows : 256
-- query time : 21 seconds

/* [First 20 Rows]
Aberdeen SD
Abilene TX
Adak Island AK
Aguadilla PR
Akron OH
Albany GA
Albany NY
Alexandria LA
Allentown/Bethlehem/Easton PA
Alpena MI
Amarillo TX
Appleton WI
Arcata/Eureka CA
Asheville NC
Ashland WV
Aspen CO
Atlantic City NJ
Augusta GA
Bakersfield CA
Bangor ME
*/

SELECT DISTINCT 
	f1.dest_city as city
FROM 
	FLIGHTS as f1, 
	FLIGHTS as f2
WHERE
	f2.dest_city = f1.origin_city
	AND f1.dest_city != 'Seattle WA'
	AND f2.origin_city = 'Seattle WA'
	AND f1.dest_city NOT IN (SELECT DISTINCT 
								f3.dest_city
							FROM 
								FLIGHTS as f3
							WHERE 
								f3.origin_city = 'Seattle WA')
ORDER BY 
	f1.dest_city ASC;