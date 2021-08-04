-- Josh CHO
-- 1773497

-- number of rows : 1

SELECT AVG(f.arrival_delay) as delay, w.day_of_week
FROM FLIGHTS as f, WEEKDAYS as w
WHERE f.day_of_week_id = w.did
GROUP BY w.day_of_week
ORDER BY AVG(f.arrival_delay) DESC
LIMIT 1;