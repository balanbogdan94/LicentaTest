USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[DoctorView]    Script Date: 9/14/2016 5:14:30 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[DoctorView]
as 
SET NOCOUNT ON
begin
select 
	D.ObjectID,
	PS.ObjectID as PersonId,
	PS.FirstName,
	PS.LastName,
	PS.DateOfBirth,
	PS.CNP,
	PS.PersonType,
	PS.EmailAddress,
	PS.HomeAddress,
	PS.Gender,
	PS.PhoneNumber,
	U.UserName,
	U.UserPassword,
	D.Specialty
from dbo.Doctor D
join dbo.Person  PS on PS.ObjectID = D.PersonID
join dbo.UserAccount U on D.PersonID=U.PersonID
end