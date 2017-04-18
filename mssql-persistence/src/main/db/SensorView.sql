USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[SensorView]    Script Date: 9/14/2016 5:17:20 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[SensorView]
as 
begin
select 
	s.ObjectID,
	s.SensorName,
	s.IsAnalog
from dbo.SensorType as s
end