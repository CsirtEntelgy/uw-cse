CREATE PROCEDURE InitDataModel
AS
BEGIN
Create Table Caregivers(
	CaregiverId int IDENTITY PRIMARY KEY,
	CaregiverName varchar(50)
);

Create Table AppointmentStatusCodes(
	StatusCodeId int PRIMARY KEY,
	StatusCode varchar(30)
);

INSERT INTO AppointmentStatusCodes (statusCodeId, StatusCode)
	VALUES (0, 'Open');
INSERT INTO AppointmentStatusCodes (statusCodeId, StatusCode)
	VALUES (1, 'OnHold');
INSERT INTO AppointmentStatusCodes (statusCodeId, StatusCode)
	VALUES (2, 'Scheduled');
INSERT INTO AppointmentStatusCodes (statusCodeId, StatusCode)
	VALUES (3, 'Completed');
INSERT INTO AppointmentStatusCodes (statusCodeId, StatusCode)
	VALUES (4, 'Missed');
	
Create Table Vaccines(
	VaccineId int IDENTITY PRIMARY KEY,
	VaccineName varchar(50),
	RequiredDose int DEFAULT 0 NOT NULL,
	Stock int DEFAULT 0 NOT NULL
);

Create Table VaccineAppointment(
	VaccineAppointmentId int Identity PRIMARY KEY,
	VaccineId int DEFAULT 0 NOT NULL
		REFERENCES Vaccines(VaccineId)
);

Create Table CareGiverSchedule(
	CaregiverSlotSchedulingId int Identity PRIMARY KEY,
	CaregiverId int DEFAULT 0 NOT NULL
		CONSTRAINT FK_CareGiverScheduleCaregiverId FOREIGN KEY (caregiverId)
			REFERENCES Caregivers(CaregiverId),
	WorkDay date,
	SlotTime time,
	SlotHour int DEFAULT 0 NOT NULL,
	SlotMinute int DEFAULT 0 NOT NULL,
	SlotStatus int  DEFAULT 0 NOT NULL
		CONSTRAINT FK_CaregiverStatusCode FOREIGN KEY (SlotStatus)
		     REFERENCES AppointmentStatusCodes(StatusCodeId),
	VaccineAppointmentId int DEFAULT 0 NOT NULL
		REFERENCES VaccineAppointment(VaccineAppointmentId)
);

Create Table Patients(
	PatientId int IDENTITY PRIMARY KEY,
	PatientName varchar(50),
	ReceivedDose int DEFAULT 0 NOT NULL,
	VaccineAppointmentId int DEFAULT 0 NOT NULL
		REFERENCES VaccineAppointment(VaccineAppointmentId)
);
END