package ro.cerner.internship.jemr.persistence.api.patientRepo;

import java.util.List;

import ro.cerner.internship.jemr.persistence.api.entity.Analysis;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;

public interface PatientRepository {
	
	void addAnalysis(Analysis analysis);
	
	int addExamination(int pacientId);
	
	public void AddPatientWithAccount(Patient patient);
	
	void deleteExaminationFromDB(int objectID);
	
	void deletePatientFromDB(int id);
	
	public List<Analysis> displayAnalysis(int examinationId);
	
	public List<Analysis> displayAllAnalysisForPatient(int patientId,int sensorId);
	
	public List<Examination> displayExamination(int patientId);
	
	public List<Patient> displayPacients(int doctorID);
	
	public List<Patient> displayPacientsByLastName(int doctorId, String lastName);

	public List<Short> getSensorData(int objectId);
	
	public void transferAllPatients(int senderDoctorId,int reciverDoctorId);

	public void transferPatient(int receiverDocID, int patientID);

	void updateExamination(Examination examinationToBeUpdated);

	void updatePacientPersonalInfo(Patient pacientToBeUpdated);
	
	void updatePacientSpecificInfo(Patient pacientToBeUpdated);
}
