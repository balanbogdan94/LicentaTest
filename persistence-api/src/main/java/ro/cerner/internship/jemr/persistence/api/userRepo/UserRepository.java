package ro.cerner.internship.jemr.persistence.api.userRepo;

import ro.cerner.internship.jemr.persistence.api.entity.User;

public interface UserRepository {
	
	int checkForPatient(int doctorId);

	void updatePassword(User userToBeUpdated);
	
	void updateUsername(User userToBeUpdated);
	
	public User VerifyLoginInfo(String username, String password);

	public Integer verifyUsername(String username);
}
