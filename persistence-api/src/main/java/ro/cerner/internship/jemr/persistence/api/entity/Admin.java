package ro.cerner.internship.jemr.persistence.api.entity;

import java.time.LocalDate;

public class Admin extends User {
	

	public Admin(
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
		super(type, firstName, lastName, cnp, gender, dateOfBirth, homeAddress, phoneNumber, emailAddress, userName, password);
	}

	public Admin() {
		super();
	}
	

}
