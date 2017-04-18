package ro.cerner.internship.jemr.persistence.mssql.patient;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.sql.rowset.serial.SerialException;

import javafx.beans.binding.BooleanBinding;
import ro.cerner.internship.jemr.persistence.api.entity.Analysis;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.patientRepo.PatientRepository;
import ro.cerner.internship.jemr.persistence.mssql.Database;
import ro.cerner.internship.jemr.persistence.mssql.config.EncryptPassword;

@Named("PatientManage")
public class PatientManage implements PatientRepository {
	Connection con = Database.getInstance().getConnect();
	Patient currentPatient = null;

	private String getStringFromDate(LocalDateTime date) {
		DateTimeFormatter sdfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String dateFormated = date.format(sdfDate);
		return dateFormated;
	}

	@Override
	public void addAnalysis(Analysis analysis) {
		
		String startDate = getStringFromDate(analysis.getStartDate());
		String stopDate = getStringFromDate(analysis.getStopDate());
		Blob blob = null;
		try {
			blob = new javax.sql.rowset.serial.SerialBlob(analysis.getSensorData());
		} catch (SerialException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			CallableStatement stmt = con.prepareCall("{call JInsertIntoAnalysis(?,?,?,?,?,?,?)}");
			System.out.println("here");
			stmt.setInt(1, analysis.getExaminationId());
	    	stmt.setBlob(2, blob);
			stmt.setInt(3, analysis.getSensorId());
			stmt.setInt(4, analysis.getFrequency());
			stmt.setString(5, startDate);
			stmt.setString(6, stopDate);
			stmt.setInt(7, analysis.getChannelId());
			stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public int addExamination(int pacientId) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String dateAndTimeSql = sdfDate.format(now);
		Integer id = null;
		try {
			CallableStatement stmt = con.prepareCall("{call JInsertNewExamination (?,?)}");
			stmt.setInt(1, pacientId);
			stmt.setString(2, dateAndTimeSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				id = rs.getInt("Id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public void AddPatientWithAccount(Patient patient) {
		EncryptPassword encrypt=new EncryptPassword();
		try {
			System.out.println(patient.getUserName());
			CallableStatement stmt = con.prepareCall("{call JInsertPacientWithAccount (?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			stmt.setInt(1, patient.getType());
			stmt.setString(2, patient.getFirstName());
			stmt.setString(3, patient.getLastName());
			stmt.setString(4, patient.getCnp());
			stmt.setString(5, patient.getGender());
			stmt.setDate(6, java.sql.Date.valueOf(patient.getDateOfBirth()));
			stmt.setString(7, patient.getHomeAddress());
			stmt.setString(8, patient.getPhoneNumber());
			stmt.setString(9, patient.getEmailAddress());
			stmt.setString(10, patient.getBloodType());
			stmt.setString(11, patient.getRH());
			stmt.setInt(12, patient.getDoctorID());
			stmt.setString(13, patient.getUserName());
			stmt.setString(14, encrypt.encryptPassword(patient.getUserPassword()));
			stmt.executeUpdate();
			System.out.println("New pacient added to the database.");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void deleteExaminationFromDB(int objectID) {
		try {
			CallableStatement stmt = con.prepareCall("{call JDeleteExamination (?)}");
			stmt.setInt(1, objectID);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error in deleting Examination");
			e.printStackTrace();
		}

	}

	@Override
	public void deletePatientFromDB(int id) {
		try {
			CallableStatement stmt = con.prepareCall("{call JDeleteFromPatient (?)}");
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error in deleting Pacient");
			e.printStackTrace();
		}

	}

	@Override
	public List<Analysis> displayAnalysis(int examinationId) {
		Analysis currentAnalysis = null;
		List<Analysis> analysisList;
		Connection con = Database.getInstance().getConnect();
		analysisList = new ArrayList<>();
		try {
			CallableStatement stmt = con.prepareCall("{call JViewAnalysis (?)}");
			stmt.setInt(1, examinationId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {

				String dateToBeFormatted = rs.getString("StartDateTime");
				String procesedString = dateToBeFormatted.substring(0, 19);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateTime = LocalDateTime.parse(procesedString, formatter);

				String dateToBeFormatted1 = rs.getString("StopDateTime");
				String procesedString1 = dateToBeFormatted1.substring(0, 19);
				DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateTime1 = LocalDateTime.parse(procesedString1, formatter1);

				currentAnalysis = new Analysis(rs.getString("SensorName"), rs.getInt("Rate"), dateTime,dateToBeFormatted, dateTime1,dateToBeFormatted1,
						rs.getInt("ObjectID"),rs.getInt("Number"));

				analysisList.add(currentAnalysis);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return analysisList;
	}
	
	@Override
	public List<Analysis> displayAllAnalysisForPatient(int patientId,int sensorId){
		Connection con=Database.getInstance().getConnect();
		List<Analysis> allAnalysisList = new ArrayList<>();
		Analysis currentAnalysis = null;
		try {
			CallableStatement stmt = con.prepareCall("{call JviewAllAnalysis (?,?)}");
			stmt.setInt(1, patientId);
			stmt.setInt(2, sensorId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String dateToBeFormatted = rs.getString("ExaminationDate");
				String procesedStringForDate = dateToBeFormatted.substring(0, 19);
				String procesedString=dateToBeFormatted.substring(0, 11)+"| "+dateToBeFormatted.substring(11, 19);
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateTime = LocalDateTime.parse(procesedStringForDate, formatter);
				
				currentAnalysis = new Analysis( rs.getInt("Rate"),rs.getString("SensorName"), dateTime,procesedString,
						rs.getInt("ObjectID"),rs.getString("Diagnostic"),rs.getInt("Number"));

				allAnalysisList.add(currentAnalysis);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allAnalysisList;
	}

	@Override
	public List<Examination> displayExamination(int patientId) {
		Examination currentExamination = null;
		List<Examination> examinationList;
		examinationList = new ArrayList<>();
		try {
			CallableStatement stmt = con.prepareCall("{call JExaminationView (?)}");
			stmt.setInt(1, patientId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {

				String dateToBeFormatted = rs.getString("ExaminationDate");
				String procesedString = dateToBeFormatted.substring(0, 19);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateTime = LocalDateTime.parse(procesedString, formatter);

				currentExamination = new Examination(

						rs.getInt("ObjectID"), rs.getInt("PacientId"), dateTime,procesedString, rs.getString("Diagnostic"),
						rs.getString("Comments"));

				examinationList.add(currentExamination);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return examinationList;
	}

	@Override
	public List<Patient> displayPacients(int doctorID) {
		List<Patient> pacientList = new ArrayList<>();
		try {

			CallableStatement stmt = con.prepareCall("{call JPacientView (?) }");
			stmt.setInt(1, doctorID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				currentPatient = new Patient(rs.getInt("ObjectId"), rs.getInt("PersonID"), rs.getInt("PersonType"),
						rs.getString("FirstName"), rs.getString("LastName"), rs.getString("CNP"),
						rs.getString("Gender"), rs.getDate("DateOfBirth").toLocalDate(), rs.getString("HomeAddress"),
						rs.getString("PhoneNumber"), rs.getString("EmailAddress"), rs.getString("BloodType"),
						rs.getString("RH"), rs.getInt("DoctorID"));
				pacientList.add(currentPatient);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pacientList;
	}

	@Override
	public List<Patient> displayPacientsByLastName(int doctorId, String lastName) {
		List<Patient> pacientList = new ArrayList<>();
		try {

			CallableStatement stmt = con.prepareCall("{call JSearchByLastName (?,?) }");
			stmt.setInt(1, doctorId);
			stmt.setString(2, lastName);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				currentPatient = new Patient(rs.getInt("ObjectId"), rs.getInt("PersonID"), rs.getInt("PersonType"),
						rs.getString("FirstName"), rs.getString("LastName"), rs.getString("CNP"),
						rs.getString("Gender"), rs.getDate("DateOfBirth").toLocalDate(), rs.getString("HomeAddress"),
						rs.getString("PhoneNumber"), rs.getString("EmailAddress"), rs.getString("BloodType"),
						rs.getString("RH"), rs.getInt("DoctorID"));
				pacientList.add(currentPatient);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pacientList;
	}

	@Override
	public List<Short> getSensorData(int objectId) {
		Connection con = Database.getInstance().getConnect();
		List<Short> blobasShort=new ArrayList<>();
		Blob blob=null ;
		try {
			CallableStatement stmt = con.prepareCall("{call JgetSensorData (?)}");
			stmt.setInt(1, objectId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				blob = rs.getBlob("SensorData");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int blobLength;
		try {
			blobLength = (int) blob.length();
			System.out.println(blob.toString());
			byte[] blobAsBytes = blob.getBytes(1, blobLength);
			
			blob.free();
			for(int i=0;i<blobAsBytes.length;i=i+2 )
			{
				try {
					ByteBuffer bb = ByteBuffer.allocate(2);
					bb.order(ByteOrder.LITTLE_ENDIAN);
					bb.put(blobAsBytes[i]);
					bb.put(blobAsBytes[i+1]);
					blobasShort.add(bb.getShort(0));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("Short: "+getShortFromLittleEndianRange(blobAsBytes[i], blobAsBytes[i+1]));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return blobasShort;
	}
	
	
	/*public static short toInt16(byte[] bytes, int index) throws Exception
	{
	if(bytes.length < 8) 
	throw new Exception("The length of the byte array must be at least 8 bytes long.");
	return (short)(
	(0xff & bytes[index]) << 8   |
	(0xff & bytes[index + 1]) << 0
	); 
	}*/

	@Override
	public void transferAllPatients(int senderDoctorId, int reciverDoctorId) {
		try {
			CallableStatement stmt = con.prepareCall("{call JtransferAllPatients (?,?)}");
			stmt.setInt(1, senderDoctorId);
			stmt.setInt(2, reciverDoctorId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error during patient transfer");
			e.printStackTrace();
		}

	}

	@Override
	public void transferPatient(int receiverDocID, int patientID) {
		try {
			CallableStatement stmt = con.prepareCall("{call JTransferOnePatient (?,?)}");
			stmt.setInt(1, receiverDocID);
			stmt.setInt(2, patientID);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error during patient transfer");
			e.printStackTrace();
		}

	}

	@Override
	public void updateExamination(Examination examinationToBeUpdated) {
		try {
			CallableStatement stmt = con.prepareCall("{call JUpdateExamination (?,?,?)}");
			stmt.setInt(1, examinationToBeUpdated.getObjectID());
			stmt.setString(2, examinationToBeUpdated.getComments());
			stmt.setString(3, examinationToBeUpdated.getDiagnostic());

			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error during examination update");
			e.printStackTrace();
		}

	}

	@Override
	public void updatePacientPersonalInfo(Patient pacientToBeUpdated) {
		try {
			CallableStatement stmt = con.prepareCall("{call JupdatePersonalInfo (?,?,?,?,?,?,?,?,?)}");
			stmt.setInt(1, pacientToBeUpdated.getIdObject());
			stmt.setString(2, pacientToBeUpdated.getFirstName());
			stmt.setString(3, pacientToBeUpdated.getLastName());
			stmt.setString(4, pacientToBeUpdated.getCnp());
			stmt.setString(5, pacientToBeUpdated.getGender());
			stmt.setDate(6, java.sql.Date.valueOf(pacientToBeUpdated.getDateOfBirth()));
			stmt.setString(7, pacientToBeUpdated.getHomeAddress());
			stmt.setString(8, pacientToBeUpdated.getPhoneNumber());
			stmt.setString(9, pacientToBeUpdated.getEmailAddress());
			// stmt.executeQuery();
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error during pacient update");
			e.printStackTrace();
		}

	}

	@Override
	public void updatePacientSpecificInfo(Patient pacientToBeUpdated) {
		try {
			CallableStatement stmt = con.prepareCall("{call JupdatePatientSpecificInfo (?,?,?)}");
			stmt.setInt(1, pacientToBeUpdated.getIdObject());
			stmt.setString(2, pacientToBeUpdated.getBloodType());
			stmt.setString(3, pacientToBeUpdated.getRH());
			stmt.executeUpdate();
			// stmt.executeQuery();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

}
