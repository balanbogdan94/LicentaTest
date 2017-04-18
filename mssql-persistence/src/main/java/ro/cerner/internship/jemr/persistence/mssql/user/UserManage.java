package ro.cerner.internship.jemr.persistence.mssql.user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import ro.cerner.internship.jemr.persistence.api.entity.Admin;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.entity.User;
import ro.cerner.internship.jemr.persistence.api.userRepo.UserRepository;
import ro.cerner.internship.jemr.persistence.mssql.Database;
import ro.cerner.internship.jemr.persistence.mssql.config.EncryptPassword;

@Named("UserManage")
public class UserManage implements UserRepository {

	Connection con = Database.getInstance().getConnect();

	@Override
	public int checkForPatient(int doctorId) {
		int docExists = 0;
		try {
			Connection con = Database.getInstance().getConnect();

			CallableStatement stmt;

			stmt = con.prepareCall("{call JverifyIfADoctorHasPatients (?)}");

			stmt.setInt(1, doctorId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				docExists = rs.getInt("PatientsExist");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docExists;
	}

	@Override
	public void updatePassword(User userToBeUpdated) {
		EncryptPassword encrypt=new EncryptPassword();
		try {
			CallableStatement stmt = con.prepareCall("{call JUpdatePersonPassWordInDatabase (?,?)}");
			stmt.setInt(1, userToBeUpdated.getIdObject());
			stmt.setString(2, encrypt.encryptPassword(userToBeUpdated.getUserPassword()));
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error during user update");
			e.printStackTrace();
		}

	}

	@Override
	public void updateUsername(User userToBeUpdated) {
		try {
			CallableStatement stmt = con.prepareCall("{call JUpdatePersonUserNameInDatabase (?,?)}");
			stmt.setInt(1, userToBeUpdated.getIdObject());
			stmt.setString(2, userToBeUpdated.getUserName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error during user update");
			e.printStackTrace();
		}

	}

	@Inject
	@Named("jdbcTemplate")
	JdbcTemplate jdbcTemplate;

	@Override
	public User VerifyLoginInfo(String username, String password) {
		User user = null;

		try {
		EncryptPassword encrypt=new EncryptPassword();
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

		simpleJdbcCall.withProcedureName("JCheckPassword").returningResultSet("PASSWORD_CHECK_RS",
				new SingleColumnRowMapper<Integer>() {
					@Override
					public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getInt("UserExists");
					}

				});

		// Map<String, Object> inputParameters = new HashMap<String, Object>(){{
		// put("Username", username);
		// put("UserPassword", password);
		// }};

		Map<String, Object> results = simpleJdbcCall.execute(username, encrypt.encryptPassword(password));
		List<Integer> resultSet = (List<Integer>) results.get("PASSWORD_CHECK_RS");

		int type = resultSet.get(0);

		
			switch (type) {
			case 1:
				user = getCurrentAdmin(username, password);
				break;
			case 2:
				user = getCurrentDoctor(username, password);
				break;
			case 3:
				user = getCurrentPatient(username, password);
				break;
			default:
				user = getDefaultUser(username, password);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Database connect error...");
		}

		return user;
	}
	
	public User getDefaultUser(String username, String password) {
		User user = new User();
		
		user.setUserName("wrong");
		user.setUserPassword("wrong");
		
		return user;
	}

	public Admin getCurrentAdmin(String username, String password) throws SQLException {
		Admin admin = null;
		try {
			EncryptPassword encrypt=new EncryptPassword();
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

			simpleJdbcCall.withProcedureName("JSelectCurrentAdmin").returningResultSet("CURRENT_ADMIN_RS",
					new RowMapper<Admin>() {
						@Override
						public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
							int personType = rs.getInt("PersonType");
							String firstName = rs.getString("FirstName");
							String lastName = rs.getString("LastName");
							String cnp = rs.getString("CNP");
							String gender = rs.getString("Gender");
							LocalDate dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
							String address = rs.getString("HomeAddress");
							String phoneNumber = rs.getString("PhoneNumber");
							String email = rs.getString("EmailAddress");
							String userName = rs.getString("UserName");
							String password = encrypt.encryptPassword(rs.getString("UserPassword"));

							Admin admin = new Admin(personType, firstName, lastName, cnp, gender, dateOfBirth, address,
									phoneNumber, email, userName, password);

							System.out.println(personType);

							return admin;
						}
					});

			// Map<String, Object> inputParameters = new HashMap<String, Object>()
			// {{
			// put("@Username", username);
			// put("@UserPassword", password);
			// }};

			Map<String, Object> results = simpleJdbcCall.execute(username, encrypt.encryptPassword(password));
			List<Admin> resultSet = (List<Admin>) results.get("CURRENT_ADMIN_RS");

			return resultSet.get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return admin;
	}

	public Doctor getCurrentDoctor(String username, String password) throws SQLException {
		Doctor doctor = null;
		try {
			EncryptPassword encrypt=new EncryptPassword();
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

			simpleJdbcCall.withProcedureName("JSelectCurrentDoctor").returningResultSet("CURRENT_DOCTOR_RS",
					new RowMapper<Doctor>() {
						@Override
						public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
							int doctorId = rs.getInt("ObjectID");
							int personID = rs.getInt("PersonID");
							int personType = rs.getInt("PersonType");
							String firstName = rs.getString("FirstName");
							String lastName = rs.getString("LastName");
							String cnp = rs.getString("CNP");
							String gender = rs.getString("Gender");
							LocalDate dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
							String address = rs.getString("HomeAddress");
							String phoneNumber = rs.getString("PhoneNumber");
							String email = rs.getString("EmailAddress");
							String userName = rs.getString("UserName");
							String password = encrypt.encryptPassword(rs.getString("UserPassword"));
							String speciality = rs.getString("Specialty");

							Doctor doctor = new Doctor(doctorId, personID, personType, firstName, lastName, cnp, gender,
									dateOfBirth, address, phoneNumber, email, userName, password, speciality);

							return doctor;
						}

					});

			// Map<String, Object> inputParameters = new HashMap<String, Object>()
			// {{
			// put("Username", username);
			// put("UserPassword", password);
			// }};

			Map<String, Object> results = simpleJdbcCall.execute(username, encrypt.encryptPassword(password));
			List<Doctor> resultSet = (List<Doctor>) results.get("CURRENT_DOCTOR_RS");

			return resultSet.get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doctor;
	}

	public Patient getCurrentPatient(String username, String password) throws SQLException {
		Patient patient = null;
		try {
			EncryptPassword encrypt=new EncryptPassword();
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

			simpleJdbcCall.withProcedureName("JSelectCurrentPacient").returningResultSet("CURRENT_PATIENT_RS",
					new RowMapper<Patient>() {
						@Override
						public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
							int patientId = rs.getInt("ObjectID");
							int personIDbla = rs.getInt("PersonID");
							int personType = rs.getInt("PersonType");
							String firstName = rs.getString("FirstName");
							String lastName = rs.getString("LastName");
							String cnp = rs.getString("CNP");
							String gender = rs.getString("Gender");
							LocalDate dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
							String address = rs.getString("HomeAddress");
							String phoneNumber = rs.getString("PhoneNumber");
							String email = rs.getString("EmailAddress");

							String bloodType = rs.getString("BloodType");
							String rh = rs.getString("RH");
							int doctorID = rs.getInt("DoctorID");

							String userName = rs.getString("UserName");
							String password = encrypt.encryptPassword(rs.getString("UserPassword"));

							Patient patient = new Patient(patientId, personIDbla, personType, firstName, lastName, cnp,
									gender, dateOfBirth, address, phoneNumber, email, bloodType, rh, doctorID, userName,
									password);

							return patient;
						}

					});

			// Map<String, Object> inputParameters = new HashMap<String, Object>()
			// {{
			// put("@Username", username);
			// put("@UserPassword", password);
			// }};

			Map<String, Object> results = simpleJdbcCall.execute(username, encrypt.encryptPassword(password));
			List<Patient> resultSet = (List<Patient>) results.get("CURRENT_PATIENT_RS");

			return resultSet.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Database connect error...");
		}
		return patient;
	}

	@Override
	public Integer verifyUsername(String username) {
		int type = 0;
		try {
			Connection con = Database.getInstance().getConnect();
			CallableStatement stmt = con.prepareCall("{call JCheckUsernameExists (?)}");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				type = rs.getInt("UserExists");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}

}
