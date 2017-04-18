package ro.cerner.internship.jemr.core.update;

import javax.inject.Inject;
import javax.inject.Named;

import ro.cerner.internship.jemr.persistence.api.doctorRepo.DoctorRepository;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.entity.User;
import ro.cerner.internship.jemr.persistence.api.patientRepo.PatientRepository;
import ro.cerner.internship.jemr.persistence.api.userRepo.UserRepository;

@Named("UpdateModel")
public class UpdateModel {

	@Inject
	@Named("DoctorManage")
	DoctorRepository updateDoc;

	@Inject
	@Named("PatientManage")
	PatientRepository selectedPatient;

	@Inject
	@Named("UserManage")
	UserRepository selectedUser;

	public void updateDoctor(Doctor doc) {
		updateDoc.updateDoctorPersonalInfo(doc);
		updateDoc.updateDoctorSpecificInfo(doc);
	}

	public void updatePatient(Patient currentPatient) {
		selectedPatient.updatePacientPersonalInfo(currentPatient);
		selectedPatient.updatePacientSpecificInfo(currentPatient);
	}

	public void updateExamination(Examination currentExamination) {
		selectedPatient.updateExamination(currentExamination);
	}

	public void updateUser(User currentUser) {
		selectedUser.updateUsername(currentUser);
		selectedUser.updatePassword(currentUser);
	}
	
	public void transferSelectedPatient(int receiverDocID, int patientID) {
		selectedPatient.transferPatient(receiverDocID, patientID);
	}
	
	public void transferAllPatients(int senderId,int reciverId)
	{
		selectedPatient.transferAllPatients(senderId, reciverId);
	}
}
