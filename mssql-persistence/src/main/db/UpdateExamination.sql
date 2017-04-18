USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[UpdateExamination]    Script Date: 9/14/2016 5:11:42 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[UpdateExamination]
(
	@ObjectId int,
	@DoctorComments varchar(max),
	@DoctorDiagnostic varchar(max)
)
AS 
SET NOCOUNT ON
BEGIN 

UPDATE [dbo].[Examination]
    SET 
		Comments = @DoctorComments,
		Diagnostic = @DoctorDiagnostic
		
    WHERE @ObjectId = [dbo].[Examination].ObjectID
end