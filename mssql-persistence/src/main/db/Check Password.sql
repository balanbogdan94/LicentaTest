USE [InternshipProjectDB]
GO
/****** Object:  StoredProcedure [dbo].[CheckPassword]    Script Date: 9/14/2016 4:59:59 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER procedure  [dbo].[CheckPassword]
--alter procedure  CheckPassword
    @Username VARCHAR(50),
    @UserPassword varchar(50)
as
begin

SET NOCOUNT ON

if exists(select * 	from [dbo].[UserAccount] U where U.Username = @username and U.UserPassword = HASHBYTES('SHA1', @UserPassword))
    select P.PersonType as UserExists 
	from [dbo].[Person] P
		join [dbo].[UserAccount] U on P.ObjectID = U.PersonID
			where U.Username = @username 
else
    select '-1' as UserExists
end