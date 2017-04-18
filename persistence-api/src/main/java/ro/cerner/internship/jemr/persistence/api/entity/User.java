package ro.cerner.internship.jemr.persistence.api.entity;

import java.time.LocalDate;

public class User extends Person {
	
	private String userName;
	private String userPassword;
	
	public User(
				int type,
				String firstName,
				String lastName,
				String cnp,
				String gender,
				LocalDate dateOfBirth,
				String homeAddress,
				String phoneNumber,
				String emailAddress
				) 
	{
		super(type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress);
	}
	
	public User(
			String firstName,
			String lastName,
			String emailAddress,
			String phoneNumber
			) 
{
	super(firstName, lastName, phoneNumber, emailAddress);
}
	
	public User(
			String userName,
			String password
			)
	{
		this.userName=userName;
		this.userPassword=password;
	}
	
	
	public User(
				int type,
				String firstName,
				String lastName,
				String cnp,
				String gender,
				LocalDate dateOfBirth,
				String homeAddress,
				String phoneNumber,
				String emailAddress,
				String userName,
				String password
				)
	{
		super(type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress);
		this.userName=userName;
		this.userPassword=password;
	}
	
	
	public User(
			int personId,
			int type,
			String firstName,
			String lastName,
			String cnp,
			String gender,
			LocalDate dateOfBirth,
			String homeAddress,
			String phoneNumber,
			String emailAddress,
			String userName,
			String password
			)
{
	super(personId,type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress);
	this.userName=userName;
	this.userPassword=password;
}
	
	
	public User(
			int personId,
			int type,
			String firstName,
			String lastName,
			String cnp,
			String gender,
			LocalDate dateOfBirth,
			String homeAddress,
			String phoneNumber,
			String emailAddress
		
			)
{
	super(personId,type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress);

}


	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String firstName,
			String lastName,
			LocalDate dateOfBirth) {
		
		super(firstName, lastName,dateOfBirth);
		
	}

	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserPassword() {
		return userPassword;
	}


	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}



	
	
	
	
}
