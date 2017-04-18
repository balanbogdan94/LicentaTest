alter procedure ViewAnalysis
(
	@ExaminationID int
)
as
begin
	select f.Rate, s.SensorName, a.StartDateTime, a.StopDateTime
	from dbo.Analysis a 
	inner join dbo.Frequency f on a.FrequencyID=f.ObjectID 
	inner join dbo.SensorType s on a.SensorTypeID=s.ObjectID  
	where a.ExaminationID=@ExaminationID 
end

exec ViewAnalysis 38