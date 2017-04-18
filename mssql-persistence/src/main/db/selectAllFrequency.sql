USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[selectAllFrequency]    Script Date: 9/14/2016 5:15:50 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[selectAllFrequency]

as
begin
Select * From dbo.Frequency
end