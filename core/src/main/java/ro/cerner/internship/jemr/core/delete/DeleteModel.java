package ro.cerner.internship.jemr.core.delete;

import javax.inject.Inject;
import javax.inject.Named;

import ro.cerner.internship.jemr.persistence.api.doctorRepo.DoctorRepository;
import ro.cerner.internship.jemr.persistence.api.patientRepo.PatientRepository;

@Named("DeleteModel")
public class DeleteModel {

	@Inject
	@Named("PatientManage")
	PatientRepository patientModel;

	@Inject
	@Named("DoctorManage")
	DoctorRepository doctorModel;

	public void deletePacient(int id) {
		patientModel.deletePatientFromDB(id);
	}
	
	public void deleteDoctor(int id) {
		doctorModel.deleteDoctorFromDB(id);
	}

	public void deleteExamination(int objectID) {
		patientModel.deleteExaminationFromDB(objectID);
	}
}
