alter procedure SearchByFirstName
(
	@FirstName varchar(25)
)
as
begin
	select p.FirstName, p.LastName, p.DateOfBirth, pac.BloodType, pac.RH 
	from dbo.Pacient pac 
	inner join dbo.Person p on pac.PersonID=p.ObjectID
	where p.FirstName=@FirstName
end

exec SearchByFirstName Marius