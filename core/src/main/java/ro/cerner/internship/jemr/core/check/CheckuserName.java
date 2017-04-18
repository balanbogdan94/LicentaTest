package ro.cerner.internship.jemr.core.check;

import javax.inject.Inject;
import javax.inject.Named;

import ro.cerner.internship.jemr.persistence.api.userRepo.UserRepository;

@Named("CheckuserName")
public class CheckuserName {
	
	@Inject
	@Named("UserManage")
	UserRepository check;
	
	public int checkCurrentUsername(String username)
	{
		int contor=check.verifyUsername(username);
		return contor;
	}

}
