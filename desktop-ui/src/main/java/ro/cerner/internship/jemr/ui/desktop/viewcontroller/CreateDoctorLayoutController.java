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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import ro.cerner.internship.jemr.core.check.CheckuserName;
import ro.cerner.internship.jemr.core.create.CreateModel;
import ro.cerner.internship.jemr.persistence.api.entity.Admin;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class CreateDoctorLayoutController implements Initializable {
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField cnp;
	@FXML
	private TextField homeAddress;
	@FXML
	private TextField phoneNumber;
	@FXML
	private ComboBox gender;
	@FXML
	private DatePicker dateOfBirth;
	@FXML
	private TextField emailAddress;
	@FXML
	private TextField speciality;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private PasswordField repeatPassword;
	
	private AdminLayoutController adminController;

	public AdminLayoutController getAdminController() {
		return adminController;
	}

	public void setAdminController(AdminLayoutController adminController) {
		this.adminController = adminController;
	}

	Admin admin;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
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
		speciality.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 100 ? change : null));
		username.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 15 ? change : null));
		password.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 50 ? change : null));
		repeatPassword.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 50 ? change : null));
		
		gender.getItems().addAll("M", "F");
	}

	public void createDoctor(ActionEvent event) throws IOException {
		CheckForm check = new CheckForm();
		CheckuserName checkUsername = SpringApplicationContext.instance().getBean("CheckuserName", CheckuserName.class);

		if (check.checkDoctor(firstName, lastName, cnp, gender, dateOfBirth, phoneNumber, emailAddress, speciality, username,
				password, repeatPassword) == true && checkUsername.checkCurrentUsername(username.getText()) != 1) {

			Doctor doc = new Doctor(2, firstName.getText(), lastName.getText(), cnp.getText(),
					gender.getValue().toString(), dateOfBirth.getValue(), homeAddress.getText(), phoneNumber.getText(),
					emailAddress.getText(), username.getText(), password.getText(), speciality.getText());

			CreateModel createModel = SpringApplicationContext.instance().getBean("CreateModel", CreateModel.class);
			createModel.addDoctor(doc);

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Doctor created");
			alert.showAndWait();
			((Node) (event.getSource())).getScene().getWindow().hide();
			adminController.populateTable();
			adminController.populatePieChart();

		} else if (checkUsername.checkCurrentUsername(username.getText()) == 1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("This username already exists in our database! Please try another one.");
			alert.showAndWait();
		}
	}

}
