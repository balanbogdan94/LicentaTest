USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[updateDoctorSpeciality]    Script Date: 9/14/2016 5:10:50 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[updateDoctorSpeciality]
(
	@PersonId int,
	@Speciality varchar(25)
	
)
AS 
SET NOCOUNT ON
BEGIN 

UPDATE [dbo].[Doctor]
    SET 
		Specialty=@Speciality
	
	From dbo.Doctor as d inner join dbo.Person as p
	 on d.PersonID=p.ObjectID	
    WHERE @PersonId = d.PersonID
end