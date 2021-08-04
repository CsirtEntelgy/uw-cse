-- Josh Cho
-- 1773497

-- number of rows : 109
-- query time : 16 seconds

/* [First 20 Rows]
Aberdeen SD
Abilene TX
Alpena MI
Ashland WV
Augusta GA
Barrow AK
Beaumont/Port Arthur TX
Bemidji MN
Bethel AK
Binghamton NY
Brainerd MN
Bristol/Johnson City/Kingsport TN
Butte MT
Carlsbad CA
Casper WY
Cedar City UT
Chico CA
College Station/Bryan TX
Columbia MO
Columbus GA
*/

SELECT DISTINCT 
	f1.origin_city as city
FROM 
	FLIGHTS as f1
WHERE 
	f1.origin_city NOT IN (SELECT DISTINCT 
								f.origin_city
							FROM 
								FLIGHTS as f 
							WHERE 
								f.actual_time >= 180)
ORDER BY 
	f1.origin_city ASC;