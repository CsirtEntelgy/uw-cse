-- Josh CHO
-- 1773497

-- number of rows : 3

SELECT DISTINCT flight_num as flight_num
FROM FLIGHTS as f, CARRIERS as c, WEEKDAYS as w
WHERE f.carrier_id = c.cid AND
	c.name = 'Alaska Airlines Inc.' AND
	f.day_of_week_id = w.did AND
	w.day_of_week = 'Monday' AND
	f.origin_city = 'Seattle WA' AND
	f.dest_city = 'Boston MA';