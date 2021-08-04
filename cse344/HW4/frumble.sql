/*
Step 1 Importing data into table
*/

CREATE TABLE frumble(
	name VARCHAR(100),
	discount VARCHAR(10),
	month VARCHAR(10),
	price INT
);

/*
Step 2 Find all non-trivial functional dependencies

Ignoring trivial FDs (that didn't meet the criteria)
*/

-- name -> price ... is a FD
SELECT 
	*
FROM 
	frumble as f1, frumble as f2
WHERE
	(f1.name = f2.name)
	AND (f1.price != f2.price);
	
-- name -> month, price ... is a FD
SELECT 
	*
FROM 
	frumble as f1, frumble as f2
WHERE
	(f1.name = f2.name)
	AND (f1.month != f2.month)
	AND (f1.price != f2.price);

-- name -> discount, price ... is a FD
SELECT 
	*
FROM 
	frumble as f1, frumble as f2
WHERE
	(f1.name = f2.name)
	AND (f1.discount != f2.discount)
	AND (f1.price != f2.price);
	
-- name -> discount, month, price ... is a FD
SELECT 
	*
FROM 
	frumble as f1, frumble as f2
WHERE
	(f1.name = f2.name)
	AND (f1.discount != f2.discount)
	AND (f1.month != f2.month)
	AND (f1.price != f2.price);
	
-- month -> discount ... is a FD
SELECT 
	*
FROM 
	frumble as f1, frumble as f2
WHERE
	(f1.month = f2.month)
	AND (f1.discount != f2.discount);
	
-- month -> name, discount ... is a FD
SELECT 
	*
FROM 
	frumble as f1, frumble as f2
WHERE
	(f1.month = f2.month)
	AND (f1.name != f2.name)
	AND (f1.discount != f2.discount);
	
-- month -> discount, price ... is a FD
SELECT 
	*
FROM 
	frumble as f1, frumble as f2
WHERE
	(f1.month = f2.month)
	AND (f1.discount != f2.discount)
	AND (f1.price != f2.price);
	

-- month -> name, discount, price ... is a FD
SELECT 
	*
FROM 
	frumble as f1, frumble as f2
WHERE
	(f1.month = f2.month)
	AND (f1.name != f2.name)
	AND (f1.discount != f2.discount)
	AND (f1.price != f2.price);
	
/*
Step 3 Decompose to BCNF & create new tables accordingly

BCNF:
->
1) name+ = {name, price}
2) month+ = {month, discount}
3) !name+ = {name, discount, month}
4) !month+ = {name, month} (since month -> discount)
=>
R1 = {name, price}
R2 = {month, discount}
R3 = {name, month}
(name -> price&month, month -> discount : accounted all 4 fields)

Now creating tables accordingly:
*/

CREATE TABLE frumbleR1(
	name VARCHAR(100) PRIMARY KEY,
	price INT
);

CREATE TABLE frumbleR2(
	month VARCHAR(10) PRIMARY KEY,
	discount VARCHAR(10)
);

CREATE TABLE frumbleR3(
	name VARCHAR(100) REFERENCES frumbleR1,
	month VARCHAR(10) REFERENCES frumbleR2
);

/*
Step 4 Find queries to load data into the new tables

Also include COUNT(*) result as comments
*/

-- COUNT(*) = 36
INSERT INTO
	frumbleR1
SELECT DISTINCT 
	name, price
FROM 
	frumble;

-- COUNT(*) = 12
INSERT INTO
	frumbleR2
SELECT DISTINCT 
	month, discount
FROM 
	frumble;

-- COUNT(*) = 426
INSERT INTO
	frumbleR2
SELECT DISTINCT 
	month, discount
FROM 
	frumble;
	
INSERT INTO
	frumbleR3
SELECT DISTINCT 
	name, month
FROM 
	frumble;