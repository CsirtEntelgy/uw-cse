-- Josh Cho
-- 1773497

-- number of rows : 327
-- query time : 14 seconds

/* [First 20 Rows]
Dothan AL,100
Toledo OH,99.8347107438017
Peoria IL,99.8664886515354
Yuma AZ,100
Bakersfield CA,82.9754601226994
Ontario CA,88.4414771541832
Daytona Beach FL,97.5460122699387
Laramie WY,100
Victoria TX,100
North Bend/Coos Bay OR,100
Erie PA,100
Guam TT,
Columbus GA,100
Wichita Falls TX,100
Juneau AK,99.7237569060773
Hartford CT,87.0527772287013
Hattiesburg/Laurel MS,100
Myrtle Beach SC,99.2542878448919
Arcata/Eureka CA,99.5726495726496
Kotzebue AK,98.7096774193548
*/

SELECT
	f1.origin_city as origin_city,
	(cast((SELECT 
				COUNT(*) as num
			FROM 
				FLIGHTS as f2
			WHERE 
				f2.actual_time < 180 
				and f1.origin_city = f2.origin_city
			GROUP BY 
				f2.origin_city) as float) / COUNT(*) * 100) as percentage
FROM 
	FLIGHTS as f1
GROUP BY 
	f1.origin_city;