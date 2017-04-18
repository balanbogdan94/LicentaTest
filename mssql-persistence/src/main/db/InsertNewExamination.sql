USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[InsertNewExamination]    Script Date: 9/14/2016 5:08:53 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[InsertNewExamination]
(
	@PatientID int,
	@ExaminationDate datetime
)
as
begin
	insert into dbo.Examination (PacientID  , ExaminationDate)

	OUTPUT INSERTED.ObjectID as Id

	 values (@PatientID, @ExaminationDate)
end