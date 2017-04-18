package ro.cerner.internship.jemr.persistence.api.doctorRepo;

import java.util.List;

import ro.cerner.internship.jemr.persistence.api.entity.Doctor;

public interface DoctorRepository {
	
	public void AddDoctor(Doctor doctor);
	
	public void deleteDoctorFromDB(int id);
	
	public List<Doctor> displayDoctors(String search);

	public Doctor getInfo(int id);
	
	public void updateDoctorPersonalInfo(Doctor currentDoctor);

	public void updateDoctorSpecificInfo(Doctor currentDoctor);
	
	public int getNumberOfPatients(int doctorId);
	
}
