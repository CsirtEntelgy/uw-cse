/*
Part A
*/

CREATE TABLE InsuranceCo(
	name VARCHAR(100) PRIMARY KEY,
	phone INT,
	maxLiability REAL
);

CREATE TABLE Person(
	ssn VARCHAR(100) PRIMARY KEY CHECK (ssn NOT LIKE'%[^0-9]%'),
	name VARCHAR(100)
);

CREATE TABLE Driver(
	FOREIGN KEY(ssn) REFERENCES Person,
	name VARCHAR(100),
	driverID VARCHAR(100) CHECK (ssn NOT LIKE'%[^0-9]%')
);

CREATE TABLE NonProfessionalDriver(
	FOREIGN KEY(ssn) REFERENCES Person,
	name VARCHAR(100),
	driverID VARCHAR(100) CHECK (ssn NOT LIKE'%[^0-9]%')
);

CREATE TABLE ProfessionalDriver(
	FOREIGN KEY(ssn) REFERENCES Person,
	name VARCHAR(100),
	driverID VARCHAR(100) CHECK (ssn NOT LIKE'%[^0-9]%'),
	medicalHistory VARCHAR(300)
);

CREATE TABLE Vehicle(
	licensePlate VARCHAR(100) PRIMARY KEY CHECK (licensePlate NOT LIKE'%[^a-zA-Z0-9]%'),
	insurance VARCHAR(100) REFERENCES InsuranceCo(name),
	owner VARCHAR(100) REFERENCES Person(ssn),
	year INT
);

CREATE TABLE Truck(
	FOREIGN KEY(licensePlate) REFERENCES Vehicle,
	insurance VARCHAR(100) REFERENCES InsuranceCo(name),
	owner VARCHAR(100) REFERENCES Person(ssn),
	operator VARCHAR(100) REFERENCES ProfessionalDriver(ssn),
	year INT,
	capacity INT
);

CREATE TABLE Car(
	FOREIGN KEY(licensePlate) REFERENCES Vehicle,
	insurance VARCHAR(100) REFERENCES InsuranceCo(name),
	owner VARCHAR(100) REFERENCES Person(ssn),
	year INT,
	make VARCHAR(100)
);

CREATE TABLE Drives(
	driverInfo VARCHAR(100) REFERENCES NonProfessionalDriver(ssn),
	carInfo VARCHAR(100) REFERENCES Car(licensePlate),
	PRIMARY KEY (driverInfo, carInfo)
);

/*
Part B

"Insures" is represented with a static field inside the "InsuranceCo".
Since the relation is many-to-one, it is reasonable to represent the relation
within the target. An insurance's maximum liability depends on the insurance,
so logically it should be a field within the "InsuranceCo" object.
Following logic goes same for the"Owns" and "Operates" relations.
*/

/*
Part C

The "Operates" relation is explained and logically implemented like stated
in the above problem (part b). The "Drives" relation is a many-to-many relation,
thus needs a seperate table to represent it. The "Drives" table contains
references for the NonProfessionalDriver's ssn and Car's licensePlate and
the combination of those two is the primary key for the relation.
*/