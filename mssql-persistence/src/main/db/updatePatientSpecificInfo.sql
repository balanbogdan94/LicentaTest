USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[updatePatientSpecificInfo]    Script Date: 9/14/2016 5:12:51 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[updatePatientSpecificInfo]
(
	@PersonId int,
	@BloodType varchar(5),
	@Rh varchar(8)
	
)
AS 
SET NOCOUNT ON
BEGIN 

UPDATE [dbo].[Pacient]
    SET 
		BloodType=@BloodType,
		RH=@Rh
	
	From dbo.Pacient as pa inner join dbo.Person as pe
	 on pa.PersonID=pe.ObjectID
    WHERE @PersonId = pa.PersonID
end