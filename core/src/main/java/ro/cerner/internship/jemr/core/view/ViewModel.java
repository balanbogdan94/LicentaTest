package ro.cerner.internship.jemr.core.view;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import javafx.collections.ObservableList;
import ro.cerner.internship.jemr.core.bitdata.BitalinoData;
import ro.cerner.internship.jemr.persistence.api.config.ConfigRepository;
import ro.cerner.internship.jemr.persistence.api.doctorRepo.DoctorRepository;
import ro.cerner.internship.jemr.persistence.api.entity.Analysis;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.entity.Sensor;
import ro.cerner.internship.jemr.persistence.api.patientRepo.PatientRepository;

@Named("ViewModel")
public class ViewModel {

	@Inject
	@Named("PatientManage")
	PatientRepository viewPatient;

	@Inject
	@Named("DoctorManage")
	DoctorRepository viewDoctor;

	@Inject
	@Named("ConfigManage")
	ConfigRepository view;
	
	private List<Examination> examinationList;

	public List<Patient> viewListOfPacients(int doctorID) {
		List<Patient> patientList = viewPatient.displayPacients(doctorID);
		return patientList;
	}

	public List<Patient> viewListOfPacientsByLastName(int doctorID, String lastName) {
		List<Patient> patientList = viewPatient.displayPacientsByLastName(doctorID, lastName);
		return patientList;
	}

	public List<Doctor> viewListOfDoctors(String search) {
		List<Doctor> doctorList = viewDoctor.displayDoctors(search);
		return doctorList;
	}

	
	public List<Examination> viewListOfExaminations(int patientId) {
		 examinationList= viewPatient.displayExamination(patientId);
		return examinationList;
	}
	
	public List<Analysis> viewListOfAllAnalysis(int patientId,int sensorId) {
		List<Analysis> analysisList = viewPatient.displayAllAnalysisForPatient(patientId, sensorId);
		return analysisList;
	}

	public List<Analysis> viewListOfAnalysis(int examinationId) {
		List<Analysis> analysisList = viewPatient.displayAnalysis(examinationId);
		return analysisList;
	}
	


	public List<Double> viewSensorData(int objectId, String sensorName, int channelNumber) {
		List<Short> rawData= viewPatient.getSensorData(objectId);
		
		return BitalinoData.relevantDataList(rawData, sensorName,channelNumber);
	}

	public Doctor viewDocLabel(int id) {
		Doctor doctor = viewDoctor.getInfo(id);
		return doctor;
	}
	
	public int viewPatientOfDoctor(int doctorId)
	{
		return viewDoctor.getNumberOfPatients(doctorId);
	}
	
	public Map<String ,Integer> viewAllDocNumberBySpecility(List<Doctor> docList)
	{
		Map<String,Integer> numberOfDocsBySpeciality=new HashMap<>();
		for(Doctor curentDoc:docList)
		{
			if(numberOfDocsBySpeciality.containsKey(curentDoc.getSpecialty()))
				numberOfDocsBySpeciality.put(curentDoc.getSpecialty(), 
					numberOfDocsBySpeciality.get(curentDoc.getSpecialty())+1);
			else
				numberOfDocsBySpeciality.put(curentDoc.getSpecialty(),1);
		}

		return numberOfDocsBySpeciality;
	}

	public Map<String, Integer> viewSexRatio(ObservableList<Patient> patientList) {
		Map<String,Integer> sexRatio=new HashMap<>();
		for(Patient currentPatient:patientList)
		{
			if(sexRatio.containsKey(currentPatient.getGender()))
				sexRatio.put(currentPatient.getGender(), 
						sexRatio.get(currentPatient.getGender())+1);
			else
				sexRatio.put(currentPatient.getGender(),1);
		
		}
		return sexRatio;
	}

	public int patientAge(LocalDate patientDOB) {

		return Period.between(patientDOB, LocalDate.now()).getYears();
	}

	public ArrayList<Examination> searchDiagnostic(String searchText) {
		if(searchText.replaceAll("\\s+","").equalsIgnoreCase(""))
			return new ArrayList<>(examinationList);
		ArrayList<Examination> examinationListAux=new ArrayList<>();
		for(Examination examination:examinationList)
		{
			if(examination != null && examination.getDiagnostic()!=null)
			{
				if(examination.getDiagnostic().contains(searchText))
					examinationListAux.add(examination);
			}
		}
		return examinationListAux;
	}
	
	public List<Sensor> viewListOfSensors()
	{
		return view.displaySensor();
	}

}
