USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[DeleteFromPerson]    Script Date: 9/14/2016 5:02:22 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure [dbo].[DeleteFromPerson]
(
@ObjectID int
)
as 
begin
SET NOCOUNT ON

UPDATE [dbo].[Pacient]
    SET IsDeleted = 1
    WHERE @ObjectId = [dbo].[Pacient].ObjectID
end
