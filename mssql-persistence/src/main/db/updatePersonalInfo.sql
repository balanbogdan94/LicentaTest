USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[updatePersonalInfo]    Script Date: 9/14/2016 5:10:13 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[updatePersonalInfo]
(
	@ObjectId int,
	@FirstName varchar(25),
	@LastName varchar(25),
	@CNP varchar(13),
	@Gender varchar(1),
	@DateOfBirth date,
	@HomeAddress varchar(100),
	@PhoneNumber varchar(15),
	@EmailAddress varchar(40)
)
AS 
SET NOCOUNT ON
BEGIN 

UPDATE [dbo].[Person]
    SET 
		FirstName = @FirstName,
		LastName = @LastName,
		CNP = @CNP,
		Gender=@Gender,
		DateOfBirth=@DateOfBirth,
		HomeAddress=@HomeAddress,
		PhoneNumber=@PhoneNumber,
		EmailAddress=@EmailAddress
		
    WHERE @ObjectId = [dbo].[Person].ObjectID
end

