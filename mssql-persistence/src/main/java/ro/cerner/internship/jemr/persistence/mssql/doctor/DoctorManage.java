package ro.cerner.internship.jemr.persistence.mssql.doctor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import ro.cerner.internship.jemr.persistence.api.doctorRepo.DoctorRepository;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.mssql.Database;
import ro.cerner.internship.jemr.persistence.mssql.config.EncryptPassword;

@Named("DoctorManage")
public class DoctorManage implements DoctorRepository {

	Connection con = Database.getInstance().getConnect();

	@Override
	public void AddDoctor(Doctor doctor) {

		EncryptPassword encrypt=new EncryptPassword();
		try {
			CallableStatement stmt = con.prepareCall("{call JInsertDoctor (?,?,?,?,?,?,?,?,?,?,?,?) }");
			stmt.setInt(1, doctor.getType());
			stmt.setString(2, doctor.getFirstName());
			stmt.setString(3, doctor.getLastName());
			stmt.setString(4, doctor.getCnp());
			stmt.setString(5, doctor.getGender());
			stmt.setDate(6, java.sql.Date.valueOf(doctor.getDateOfBirth()));
			stmt.setString(7, doctor.getHomeAddress());
			stmt.setString(8, doctor.getPhoneNumber());
			stmt.setString(9, doctor.getEmailAddress());
			stmt.setString(10, doctor.getUserName());
			stmt.setString(11, encrypt.encryptPassword(doctor.getUserPassword()));
			stmt.setString(12, doctor.getSpecialty());
			stmt.executeUpdate();
			System.out.println("New doctor added to the database.");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void deleteDoctorFromDB(int id) {

		try {
			CallableStatement stmt = con.prepareCall("{call JDeleteFromDoctor (?)}");
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error in deleting Doctor");
			e.printStackTrace();
		}

	}

	@Override
	public List<Doctor> displayDoctors(String search) {

		Doctor currentDoctor = null;
		List<Doctor> doctorList;
		doctorList = new ArrayList<>();
		try {
			CallableStatement stmt = con.prepareCall("{call JDoctorView (?)}");
			stmt.setString(1, search);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				currentDoctor = new Doctor(rs.getInt("ObjectID"), rs.getInt("PersonId"), rs.getInt("PersonType"),
						rs.getString("FirstName"), rs.getString("LastName"), rs.getString("CNP"),
						rs.getString("Gender"), rs.getDate("DateOfBirth").toLocalDate(), rs.getString("HomeAddress"),
						rs.getString("PhoneNumber"), rs.getString("EmailAddress"), rs.getString("UserName"),
						rs.getString("UserPassword"), rs.getString("Specialty"));

				doctorList.add(currentDoctor);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doctorList;
	}

	@Override
	public Doctor getInfo(int id) {

		Doctor doc = null;
		try {
			CallableStatement stmt = con.prepareCall("{call JViewDoctorDetails (?) }");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				doc = new Doctor(rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Specialty"),
						rs.getString("EmailAddress"), rs.getString("PhoneNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return doc;
	}

	@Override
	public void updateDoctorPersonalInfo(Doctor currentDoctor) {
		try {
			CallableStatement stmt = con.prepareCall("{call JupdatePersonalInfo(?,?,?,?,?,?,?,?,?)}");
			stmt.setInt(1, currentDoctor.getIdObject());
			stmt.setString(2, currentDoctor.getFirstName());
			stmt.setString(3, currentDoctor.getLastName());
			stmt.setString(4, currentDoctor.getCnp());
			stmt.setString(5, currentDoctor.getGender());
			stmt.setDate(6, Date.valueOf(currentDoctor.getDateOfBirth()));
			stmt.setString(7, currentDoctor.getHomeAddress());
			stmt.setString(8, currentDoctor.getPhoneNumber());
			stmt.setString(9, currentDoctor.getEmailAddress());
			// stmt.executeQuery();
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQl error during update");
			e.printStackTrace();
		}
	}

	@Override
	public void updateDoctorSpecificInfo(Doctor currentDoctor) {
		try {
			CallableStatement stmt = con.prepareCall("{call JupdateDoctorSpeciality(?,?)}");
			stmt.setInt(1, currentDoctor.getIdObject());
			stmt.setString(2, currentDoctor.getSpecialty());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public int getNumberOfPatients(int doctorId)
	{
		int numberOfPatients=0;
		try {
			CallableStatement stmt = con.prepareCall("{call JCountNumberOfPatients(?)}");
			stmt.setInt(1, doctorId);
			ResultSet rs=stmt.executeQuery();
			while(rs.next())
			{
				numberOfPatients=rs.getInt("NumberOfPatients");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numberOfPatients;
	}

}
