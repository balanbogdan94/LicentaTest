alter procedure InsertDoctor
(
	@FirstName varchar(25), 
    @LastName varchar(25),
	@CNP varchar(13),
	@Gender varchar(1),
	@DateOfBirth date,
	@HomeAddress varchar(100),
	@PhoneNumber varchar(15),
	@EmailAddress varchar(40),
	@UserName varchar(15),
	@UserPassword varchar(50),
	@PersonType tinyint,
	@Specialty varchar(25)
)
AS 

BEGIN 
DECLARE 
	@PersonID int 

	Exec InsertUserWithAccount 
			@FirstName, 
			@LastName, 
			@CNP, 
			@Gender, 
			@DateOfBirth,
			@HomeAddress,
			@PhoneNumber,
			@EmailAddress,
			@UserName,
			@UserPassword,
			@PersonType;
	set @personID = (SELECT p.ObjectID
			FROM dbo.Person p
			WHERE @CNP=p.CNP);
	INSERT INTO dbo.Doctor
	(	
		PersonID,
		Specialty
	)   
	VALUES
	(
		@PersonID,
		@Specialty
	)
end