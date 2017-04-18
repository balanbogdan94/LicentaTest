create procedure SelectCurrentDoctor
(
	@username varchar(50),
	@password varchar(50)
)
as 
SET NOCOUNT ON
begin

SELECT * FROM Doctor doc 
				join Person p on doc.PersonID=p.ObjectID
				join UserAccount u on doc.PersonID=u.PersonID
				WHERE u.UserName=@username AND u.UserPassword=HASHBYTES('SHA1', @password)
end
go

Exec SelectCurrentDoctor 'raluca', 'raluca'