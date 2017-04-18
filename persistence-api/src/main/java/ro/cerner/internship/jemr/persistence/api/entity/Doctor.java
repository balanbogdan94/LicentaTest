package ro.cerner.internship.jemr.persistence.api.entity;

import java.time.LocalDate;

public class Doctor extends User {
	
	
	private String specialty;
	private int ObjectID;
	
	public String getSpecialty() {
		return specialty;
	}


	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	
	public Doctor(
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
			String password,
			String specialty
			)
	{
		
		super(type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress, userName, password);
		this.specialty=specialty;
		this.setType(2);
	}
	
	

	public Doctor(
			int objectID,
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
			String password,
			String specialty
			) 
	{
		super(type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress, userName, password);
		this.specialty = specialty;
		ObjectID = objectID;
		this.setType(2);
	}
	
	
	public Doctor(
			int objectID,
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
			String password,
			String specialty
			) 
	{
		super(personId,type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress, userName, password);
		this.specialty = specialty;
		ObjectID = objectID;
		this.setType(2);
	}
	
	public Doctor(
			String firstName,
			String lastName,
			String specialty,
			String emailAddress,
			String phoneNumber
			) 
	{
		super(firstName, lastName, phoneNumber, emailAddress);
		this.specialty = specialty;
		this.setType(2);
	}
	


	public Doctor() {
		super();
	}


	@Override
	public String toString() {
		return getFirstName()+" "+getLastName();
	}


	public int getObjectID() {
		return ObjectID;
	}


	public void setObjectID(int objectID) {
		ObjectID = objectID;
	}
	
	
}
