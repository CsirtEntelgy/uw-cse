-- Josh CHO
-- 1773497

-- number of rows : 1472
-- COUNT(*)

SELECT c.name as name,
	f1.flight_num as f1_flight_num,
	f1.origin_city as f1_origin_city,
	f1.dest_city as f1_dest_city,
	f1.actual_time as f1_actual_time,
	f2.flight_num as f2_flight_num,
	f2.origin_city as f2_origin_city,
	f2.dest_city as f2_dest_city,
	f2.actual_time as f2_actual_time,
	(f1.actual_time + f2.actual_time) as actual_time
FROM FLIGHTS as f1, FLIGHTS as f2, CARRIERS as c, MONTHS as m
WHERE f1.dest_city = f2.origin_city AND
	f1.origin_city = 'Seattle WA' AND
	f2.dest_city = 'Boston MA' AND
	f1.month_id = m.mid AND
	f2.month_id = m.mid AND
	m.month = 'July' AND
	f1.day_of_month = 15 AND
	f2.day_of_month = 15 AND
	f1.carrier_id = c.cid AND
	f2.carrier_id = c.cid AND
	(f1.actual_time + f2.actual_time) < 420;