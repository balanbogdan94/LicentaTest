package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CheckForm {

	Alert alert = new Alert(AlertType.WARNING);

	private boolean checkName(TextField firstName, TextField lastName, TextField cnp) {

		ArrayList<TextField> textFields = new ArrayList<>();

		textFields.add(firstName);
		textFields.add(lastName);
		textFields.add(cnp);

		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Fields with * must be completed !");

		for (TextField textbox : textFields) {
			if (textbox.getText().trim().isEmpty()) {

				alert.showAndWait();
				// JOptionPane.showMessageDialog(null, "Fields with * must be
				// completed !");
				return false;
			}
			textbox.setText(textbox.getText(0, 1).toUpperCase() + textbox.getText(1, textbox.getText().length()));
		}
		if (firstName.getText().matches("^[a-zA-Z]*$") == false || lastName.getText().matches("^[a-zA-Z]*$") == false) {
			alert.setContentText("Name must contain only letters");
			alert.showAndWait();
			return false;
		}

		return true;
	}

	private boolean checkBloodTypeAndRH(ComboBox<?> bloodType, ComboBox<?> rhType) {

		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Blood & RH type fields must be completed !");

		if (bloodType.getValue() != null && rhType.getValue() != null) {
			return true;
		} else {

			alert.showAndWait();
			// JOptionPane.showMessageDialog(null, "Blood & RH type fields must
			// be completed !");
			return false;
		}

	}

	private boolean checkSpecialty(TextField specialty) {

		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Specialty must be completed !");
		if (specialty.getText().trim().isEmpty()) {
			alert.showAndWait();
			// JOptionPane.showMessageDialog(null, "Gender must be completed
			// !");
			return false;
		} else if (specialty.getText().matches("^[a-zA-Z]+$") == false) {
			alert.setContentText("Specialty must contain only letters !");
			alert.showAndWait();
			return false;
		}
		specialty.setText(specialty.getText(0, 1).toUpperCase() + specialty.getText(1, specialty.getText().length()));
		return true;
	}

	private boolean checkGender(ComboBox<?> gender) {

		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Gender must be completed !");

		if (gender.getValue() != null) {
			return true;
		} else {
			alert.showAndWait();
			// JOptionPane.showMessageDialog(null, "Gender must be completed
			// !");
			return false;
		}

	}

	private boolean checkDateOfBirth(DatePicker dateOfBirth) {

		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);

		if (dateOfBirth.getValue().compareTo(LocalDate.now()) > 0) {
			alert.setContentText("Invalid Date of Birth");
			alert.showAndWait();
			return false;
		} else {
			if (dateOfBirth.getValue() != null) {
				return true;
			} else {
				alert.setContentText("Select Date of Birth!");
				alert.showAndWait();
				return false;
			}
		}
	}

	private boolean checkCNP(TextField cnp, DatePicker dateOfBirth, ComboBox<?> gender) {

		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);

		String cnpAux, month1, month2, day1, day2, year1, year2, sex1, sex2, genderAux;
		int year3;
		cnpAux = cnp.getText();

		String dateAux = dateOfBirth.getValue().toString();

		if (cnp.getText().matches("^[0-9]{13}$")) { // sau
													// doar
													// cnp.getText().matches("^[0-9]{13}$")
			month1 = dateAux.substring(5, 7);
			month2 = cnpAux.substring(3, 5);

			day1 = dateAux.substring(8, 10);
			day2 = cnpAux.substring(5, 7);

			year1 = dateAux.substring(2, 4);
			year2 = cnpAux.substring(1, 3);

			year3 = Integer.parseInt(dateAux.substring(0, 4));

			if (month1.equals(month2) && day1.equals(day2) && year1.equals(year2)) {
				genderAux = gender.getValue().toString();

				sex1 = genderAux.substring(0, 1);
				sex2 = cnpAux.substring(0, 1);

				if ((sex1.equals("M")
						&& (sex2.equals("1") && year3 < 2000 || sex2.equals("3") || sex2.equals("5") && year3 >= 2000))
						|| (sex1.equals("F") && (sex2.equals("2") && year3 < 2000 || sex2.equals("4")
								|| sex2.equals("6") && year3 >= 2000))) {

					return true;
				} else {
					alert.setContentText("CNP and Gender do not match!");
					alert.showAndWait();
					// JOptionPane.showMessageDialog(null, "CNP and Gender do
					// not match!");
					return false;
				}
			} else {
				alert.setContentText("CNP and Date of Birth do not match!");
				alert.showAndWait();
				// JOptionPane.showMessageDialog(null, "CNP and Date of Birth do
				// not match!");
				return false;
			}
		} else {
			alert.setContentText("CNP must contain only 13 NUMBERS !");
			alert.showAndWait();
			// JOptionPane.showMessageDialog(null, "CNP must contain only 13
			// NUMBERS !");
			return false;
		}

	}
	
	public boolean checkPhone(TextField phone) {
		
		if (phone.getText().trim().isEmpty() == true) {
			return true;
		}
		else if (phone.getText().matches("^[0-9+\\s]+$") == false) {
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Invalid phone number!");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	public boolean checkEmail(TextField email) {
		if (email.getText().isEmpty()) {
			return true;
		} else if (email.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$") == false) {
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Invalid e-mail!");
			alert.showAndWait();
			return false;
		} else
			return true;
	}

	public boolean checkUsernameAndPassword(TextField userName, PasswordField passWord, PasswordField repeatPassWord) {
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);

		if (!passWord.getText().equals(repeatPassWord.getText())) {
			alert.setContentText("Passwords do not match!");
			alert.showAndWait();
			return false;
		} else {
			if (userName.getText().trim().isEmpty() && passWord.getText().trim().isEmpty()) {
				alert.setContentText("Username or Password are not completed!");
				alert.showAndWait();
				return false;
			} else if (userName.getText().length() < 4) {
				alert.setContentText("Username must contain at least 4 characters!");
				alert.showAndWait();
				return false;
			} else if (passWord.getText().length() < 4) {
				alert.setContentText("Password must contain at least 4 characters!");
				alert.showAndWait();
				return false;
			} else if (userName.getText().matches("^[a-zA-Z0-9_]+$") == false) {
				alert.setContentText("Username must contain only \"a-z, A-Z, 0-9 and Underscore\"!");
				alert.showAndWait();
				return false;
			} else if (passWord.getText().matches("^[a-zA-Z0-9_]+$") == false) {
				alert.setContentText("Password must contain only \"a-z, A-Z, 0-9 and Underscore\"!");
				alert.showAndWait();
				return false;
			}
		}
		return true;
	}

	public boolean checkDoctor(TextField firstName, TextField lastName, TextField cnp, ComboBox<?> gender,
			DatePicker dateOfBirth, TextField email, TextField phone, TextField speciality, TextField userName, PasswordField passWord,
			PasswordField repeatPassword) {

		if (checkName(firstName, lastName, cnp) == true && checkGender(gender) == true
				&& checkDateOfBirth(dateOfBirth) == true && checkEmail(email) == true
				&& checkPhone(phone) == true
				&& checkSpecialty(speciality) == true && checkCNP(cnp, dateOfBirth, gender) == true
				&& checkUsernameAndPassword(userName, passWord, repeatPassword) == true)
			return true;
		else {
			return false;
		}
	}

	public boolean checkUpdateDoctor(TextField firstName, TextField lastName, TextField cnp, ComboBox<?> gender,
			DatePicker dateOfBirth, TextField email, TextField phone, TextField speciality) {

		if (checkName(firstName, lastName, cnp) == true && checkGender(gender) == true
				&& checkDateOfBirth(dateOfBirth) == true && checkEmail(email) == true
				&& checkPhone(phone) == true
				&& checkSpecialty(speciality) == true && checkCNP(cnp, dateOfBirth, gender) == true)
			return true;
		else {
			return false;
		}
	}

	public boolean checkPatient(TextField firstName, TextField lastName, TextField cnp, ComboBox<?> gender,
			DatePicker dateOfBirth, TextField email, TextField phone, ComboBox<?> bloodType, ComboBox<?> rhType, TextField username,
			PasswordField password, PasswordField repeatPassword) {

		if (checkName(firstName, lastName, cnp) == true && checkGender(gender) == true
				&& checkDateOfBirth(dateOfBirth) == true && checkEmail(email) == true
				&& checkPhone(phone) == true
				&& checkBloodTypeAndRH(bloodType, rhType) == true && checkCNP(cnp, dateOfBirth, gender) == true
				&& checkUsernameAndPassword(username, password, repeatPassword) == true)
			return true;
		else {
			return false;
		}
	}

	public boolean checkUpdatePatient(TextField firstName, TextField lastName, TextField cnp, ComboBox<?> gender,
			DatePicker dateOfBirth, TextField email, TextField phone, ComboBox<?> bloodType, ComboBox<?> rhType) {

		if (checkName(firstName, lastName, cnp) == true && checkGender(gender) == true
				&& checkDateOfBirth(dateOfBirth) == true && checkEmail(email) == true
				&& checkPhone(phone) == true
				&& checkBloodTypeAndRH(bloodType, rhType) == true && checkCNP(cnp, dateOfBirth, gender) == true

		)
			return true;
		else {
			return false;
		}
	}
}
