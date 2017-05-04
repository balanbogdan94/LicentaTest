package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.core.check.CheckuserName;
import ro.cerner.internship.jemr.core.create.CreateModel;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class CreatePatientController implements Initializable {

	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField cnp;
	@FXML
	private ComboBox genderComboBox;
	@FXML
	private DatePicker DateOfBirth;
	@FXML
	private TextField homeAddress;
	@FXML
	private TextField phoneNumber;
	@FXML
	private TextField emailAddress;
	@FXML
	private ComboBox bloodType;
	@FXML
	private ComboBox rhType;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private PasswordField repeatPassword;

	FirstPageController controller;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		firstName.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 25 ? change : null));
		lastName.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 25 ? change : null));
		cnp.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 13 ? change : null));
		homeAddress.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 100 ? change : null));
		phoneNumber.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 15 ? change : null));
		emailAddress.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 40 ? change : null));
		username.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 15 ? change : null));
		password.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 50 ? change : null));
		repeatPassword.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 50 ? change : null));
		
		genderComboBox.getItems().addAll("M", "F");
		bloodType.getItems().addAll("0 I", "A II", "B III", "AB IV");
		rhType.getItems().addAll("Positive", "Negative");

	}

	public void getCreatedPatient(ActionEvent event) {
		CheckForm check = new CheckForm();
		CheckuserName checkUsername = SpringApplicationContext.instance().getBean("CheckuserName", CheckuserName.class);

		if (check.checkPatient(firstName, lastName, cnp, genderComboBox, DateOfBirth, emailAddress, phoneNumber,
				bloodType, rhType, username, password, repeatPassword) == true
				&& checkUsername.checkCurrentUsername(username.getText()) != 1) {
			Patient createdPatient = new Patient(3, firstName.getText(), lastName.getText(), cnp.getText(),
					genderComboBox.getValue().toString(), DateOfBirth.getValue(), homeAddress.getText(),
					phoneNumber.getText(), emailAddress.getText(), bloodType.getValue().toString(),
					rhType.getValue().toString(), this.controller.getCurrentDoctor().getObjectID(), username.getText(), password.getText());

			CreateModel createdModel = SpringApplicationContext.instance().getBean("CreateModel", CreateModel.class);
			createdModel.addPatient(createdPatient);
			this.controller.refreshTheTable();
			this.controller.populatePieChart();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Pacient created");
			alert.showAndWait();
			((Node) (event.getSource())).getScene().getWindow().hide();

		} else if (checkUsername.checkCurrentUsername(username.getText()) == 1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("This username already exists in our database! Please try another one.");
			alert.showAndWait();
		}
	}

	void setDoctorID(FirstPageController controller) {
		this.controller = controller;
	}

	
	

}
