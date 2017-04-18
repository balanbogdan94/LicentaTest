package ro.cerner.internship.jemr.core.check;

import javax.inject.Inject;
import javax.inject.Named;

import ro.cerner.internship.jemr.persistence.api.entity.User;
import ro.cerner.internship.jemr.persistence.api.userRepo.UserRepository;

@Named("CheckUser")
public class CheckUser {

	@Inject
	@Named("UserManage")
	UserRepository check;

	public User checkCurentUser(String username, String password) {
		User user = check.VerifyLoginInfo(username, password);
		return user;
	}
	
	public int checkIfPatientExist(int doctorId)
	{
		int patientExists=check.checkForPatient(doctorId);
		return patientExists;
	}

}
