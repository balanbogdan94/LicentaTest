create procedure SelectCurrentAdmin
(
	@username varchar(50),
	@password varchar(50)
)
as 
SET NOCOUNT ON
begin

SELECT * FROM UserAccount ad 
join Person p on ad.PersonID=p.ObjectID 
join UserAccount u on ad.PersonID=u.PersonID
WHERE u.UserName=@username AND u.UserPassword=HASHBYTES('SHA1', @password)

end
go

Exec SelectCurrentAdmin 'admin', 'admin'