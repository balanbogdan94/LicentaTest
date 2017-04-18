package ro.cerner.internship.jemr.core.create;

import javax.inject.Inject;
import javax.inject.Named;

import ro.cerner.internship.jemr.persistence.api.doctorRepo.DoctorRepository;
import ro.cerner.internship.jemr.persistence.api.entity.Analysis;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.patientRepo.PatientRepository;

@Named("CreateModel")
public class CreateModel {

	@Inject
	@Named("DoctorManage")
	DoctorRepository newDoctor;

	@Inject
	@Named("PatientManage")
	PatientRepository newPatientStuff;

	public void addDoctor(Doctor currentDoctor) {
		newDoctor.AddDoctor(currentDoctor);
	}

	public void addPatient(Patient currentPatient) {
		newPatientStuff.AddPatientWithAccount(currentPatient);
	}

	public int addExamination(int pacientId) {
		int id = newPatientStuff.addExamination(pacientId);
		return id;
	}

	public void addAnalysis(Analysis analysis) {
		newPatientStuff.addAnalysis(analysis);
	}
}
