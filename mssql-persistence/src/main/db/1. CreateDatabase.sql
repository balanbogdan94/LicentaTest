--create database InternshipProjectDB
--go
create table Person
(
	ObjectID int identity primary key,
	FirstName varchar(25) not null,
	LastName varchar(25) not null,
	CNP varchar(13) not null unique,
	Gender varchar(1) not null,
	DateOfBirth date not null,
	PersonType tinyint not null,
	HomeAddress varchar(100),
	PhoneNumber varchar(15),
	EmailAddress varchar(40),
	IsDeleted bit default 0
)
create table UserAccount
(
	ObjectID int identity primary key,
	PersonID int not null unique,
	UserName varchar(15) not null unique,
	UserPassword varchar(50) not null,
	IsDeleted bit default 0,
	FOREIGN KEY (PersonID) REFERENCES Person(ObjectID)
)

create table Doctor
(
	ObjectID int identity primary key,
	PersonID int not null unique,
	Specialty varchar(25),
	IsDeleted bit default 0,
	FOREIGN KEY (PersonID) REFERENCES Person(ObjectID)
)

create table Pacient
(
	ObjectID int identity primary key,
	PersonID int not null unique,
	DoctorID int not null,
	BloodType varchar(5),
	RH varchar(8),
	IsDeleted bit default 0,
	FOREIGN KEY (PersonID) REFERENCES Person(ObjectID),
	FOREIGN KEY (DoctorID) REFERENCES Doctor(ObjectID)
)

create table Examination
(
	ObjectID int identity primary key,
	PacientID int not null,
	ExaminationDate date not null,
	Diagnostic varchar(max),
	IsDeleted bit default 0,
	FOREIGN KEY (PacientID) REFERENCES Pacient(ObjectID)
)

create table SensorType
(
	ObjectID int identity primary key,
	SensorName varchar(3) not null unique,
	IsAnalog bit default 1
)

create table Frequency 
(
	ObjectID int identity primary key,
	Rate int not null unique
)

create table Analysis
(
	ObjectID int identity primary key,
	ExaminationID int not null,
	SensorData varchar(max),
	SensorTypeID int not null,
	FrequencyID int not null,
	StartDateTime datetime not null,
	StopDateTime datetime not null,
	IsDeleted bit default 0,
	FOREIGN KEY (ExaminationID) REFERENCES Examination(ObjectID),
	FOREIGN KEY (SensorTypeID) REFERENCES SensorType(ObjectID),
	FOREIGN KEY (FrequencyID) REFERENCES Frequency(ObjectID),
)


ALTER TABLE Doctor
ALTER COLUMN Specialty varchar(100)
