USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[PacientView]    Script Date: 9/14/2016 5:16:40 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[PacientView]
@DoctorID int
as 
SET NOCOUNT ON
begin
select 
	PC.ObjectID,
	PS.ObjectID as PersonId,
	PS.FirstName,
	PS.PersonType,
	PS.LastName,
	PS.CNP,
	PS.Gender,
	PS.EmailAddress,
	PS.HomeAddress,
	PS.PhoneNumber,
	PC.DoctorID,
	PC.BloodType,
	PC.RH,
	PS.DateOfBirth 
from dbo.Pacient PC
join dbo.Person  PS on PS.ObjectID = PC.PersonID
where PC.DoctorID = @DoctorID and PC.IsDeleted = 0
end