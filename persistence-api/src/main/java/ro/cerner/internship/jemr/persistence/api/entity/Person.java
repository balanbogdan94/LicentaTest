package ro.cerner.internship.jemr.persistence.api.entity;

import java.time.LocalDate;

public class Person {
	private int idObject;


	private int type;
	private String FirstName;
	private String LastName;
	private String cnp;
	private String gender;
	private LocalDate dateOfBirth;
	private String homeAddress;
	private String phoneNumber;
	private String emailAddress;
	
	public Person(
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
		
		this.type = type;
		FirstName = firstName;
		LastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.cnp = cnp;
		this.emailAddress = emailAddress;
		this.homeAddress = homeAddress;
		this.gender = gender;
		this.phoneNumber = phoneNumber;
	}
	
	
	public Person(
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
this.idObject=personId;
this.type = type;
FirstName = firstName;
LastName = lastName;
this.dateOfBirth = dateOfBirth;
this.cnp = cnp;
this.emailAddress = emailAddress;
this.homeAddress = homeAddress;
this.gender = gender;
this.phoneNumber = phoneNumber;
}
	
	public Person(
			String firstName,
			String lastName,
			String emailAddress,
			String phoneNumber
			) 
{
FirstName = firstName;
LastName = lastName;
this.emailAddress = emailAddress;
this.phoneNumber = phoneNumber;
}

	
	public Person(){}

	public Person(String firstName,
			String lastName,LocalDate dateOfBirth) {
		
		FirstName = firstName;
		LastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}


	public int getType() {
		return type;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCnp() {
		return cnp;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getIdObject() {
		return idObject;
	}

	public void setIdObject(int idObject) {
		this.idObject = idObject;
	}
	
}
