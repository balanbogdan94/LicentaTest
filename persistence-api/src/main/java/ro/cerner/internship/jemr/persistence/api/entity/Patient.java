package ro.cerner.internship.jemr.persistence.api.entity;

import java.time.LocalDate;

public class Patient extends User {

	
	private int doctorID;
	private String BloodType;
	private String RH;
	private int objectIDpa;
	
	
//constructor pacient fara cont fara ID
	public Patient(
				int type,
				String firstName,
				String lastName,
				String cnp,
				String gender,
				LocalDate dateOfBirth, 
				String homeAddress,
				String phoneNumber,
				String emailAddress,
				String bloodType,
				String rH,
				int doctorID
				) 
	{
		super(type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress);
		this.doctorID=doctorID;
		this.BloodType = bloodType;
		this.RH = rH;
		this.setType(3);
	}
	
//constructor pacient cu cont fara ID
	public Patient(
				int type,
				String firstName,
				String lastName,
				String cnp,
				String gender,
				LocalDate dateOfBirth, 
				String homeAddress,
				String phoneNumber,
				String emailAddress,
				String bloodType,
				String rH,
				int doctorID,
				String userName,
				String userPassword
				)
	{
		super(type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress, userName,  userPassword);
		this.doctorID=doctorID;
		this.BloodType = bloodType;
		this.RH = rH;
		this.setType(3);
	}

//constructor pacient fara cont cu ID	
	public Patient(
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
				String bloodType,
				String rH,
				int doctorID					
				) 
	{
		super(type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress);
		this.doctorID=doctorID;
		this.BloodType = bloodType;
		this.RH = rH;
		this.objectIDpa=objectID;
		this.setType(3);
	}
	
//constructor pacient cu cont cu ID	
	public Patient(
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
				String bloodType,
				String rH,
				int doctorID,
				String userName,
				String userPassword
				)
	{
		super(type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress, userName,  userPassword);
		this.doctorID=doctorID;
		this.BloodType = bloodType;
		this.RH = rH;
		objectIDpa=objectID;
		this.setType(3);
	}
	
	public Patient(
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
			String bloodType,
			String rH,
			int doctorID
			)
{
	super(personId,type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress);
	this.doctorID=doctorID;
	this.BloodType = bloodType;
	this.RH = rH;
	objectIDpa=objectID;
	this.setType(3);
}
	public  Patient( 
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
			String bloodType,
			String rH,
			int doctorID,
            String userName, 
            String userPassword
        )
	{
		super(personId,type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress, userName, userPassword);
		this.doctorID=doctorID;
		this.BloodType = bloodType;
		this.RH = rH;
		objectIDpa=objectID;
		this.setType(3);
	}
	


	public Patient(String firstName,
			String lastName, LocalDate dateOfBirth, String bloodType,
			String rH) {
		
		super(firstName, lastName, dateOfBirth);
		this.BloodType = bloodType;
		this.RH = rH;
		this.setType(3);
	}

	public int getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}

	public String getBloodType() {
		return BloodType;
	}
	
	public String getRH() {
		return RH;
	}

	@Override
	public String toString() {
		return "Pacient [DoctorID=" + doctorID + ", type=" + getType() + ", BloodType=" + BloodType + ", RH=" + RH + "]";
	}

	public int getObjectID() {
		return objectIDpa;
	}

	public void setObjectID(int objectID) {
		this.objectIDpa = objectID;
	}

	public void setBloodType(String bloodType) {
		BloodType = bloodType;
	}

	public void setRH(String rH) {
		RH = rH;
	}
	
	
}
