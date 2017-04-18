create procedure SelectCurrentPacient
(
	@username varchar(50),
	@password varchar(50)
)
as 
SET NOCOUNT ON
begin

SELECT * FROM Pacient pac 
join Person p on pac.PersonID=p.ObjectID 
join UserAccount u on pac.PersonID=u.PersonID 
WHERE u.UserName=@username AND u.UserPassword=HASHBYTES('SHA1', @password)

end
go

Exec SelectCurrentPacient'raluca', 'raluca'