alter procedure ExaminationView
(
	@PatientID int
)
as
begin
	select e.ObjectID, e.PacientID, e.ExaminationDate, e.Diagnostic, e.Comments from dbo.Examination e where e.IsDeleted=0 and e.PacientID=@PatientID
end

exec ExaminationView