USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[InsertIntoAnalysis]    Script Date: 9/14/2016 5:07:56 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[InsertIntoAnalysis]
(
@ExaminationID int,
@SensorData varchar(max),
@SensorTypeID int,
@FrequencyID int,
@StartDateTime datetime,
@StopDateTime datetime
)
as
begin
	insert into dbo.Analysis( ExaminationId,SensorData,SensorTypeID,FrequencyID,StartDateTime,StopDateTime)
	 values 
	 (
	 @ExaminationID,
	 @SensorData,
	 @SensorTypeID,
	 @FrequencyID,
	 @StartDateTime,
	 @StopDateTime
	 )


end