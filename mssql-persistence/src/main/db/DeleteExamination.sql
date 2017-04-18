create procedure DeleteExamination
(
	@ObjectID int
)
as
begin
	Update dbo.Examination set IsDeleted=1 where ObjectID=@ObjectID
end
