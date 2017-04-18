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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.core.update.UpdateModel;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class UpdatePacientLayoutController implements Initializable {
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField cnp;
	@FXML
	private ComboBox gender;
	@FXML
	private DatePicker dateOfBirth;
	@FXML
	private TextField homeAddress;
	@FXML
	private TextField phoneNumber;
	@FXML
	private TextField emailAddress;
	@FXML
	private ComboBox bloodType;
	@FXML
	private ComboBox rh;
	@FXML
	private Button cancel;
	@FXML
	private Label helloMessage;
	@FXML
	private CheckBox checkBox;
	@FXML
	private GridPane infoGrid;

	Patient currentPatient;
	Doctor doctor;
	FirstPageController doctorLayout;

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
		gender.getItems().addAll("M", "F");
		bloodType.getItems().addAll("0 I", "A II", "B III", "AB IV");
		rh.getItems().addAll("Positive", "Negative");
	}

	public void getSelectedPacient(Patient selectedPatient, Doctor currentDoctor,FirstPageController controller) {
		doctor = currentDoctor;
		currentPatient = selectedPatient;
		firstName.setText(currentPatient.getFirstName());
		lastName.setText(currentPatient.getLastName());
		cnp.setText(currentPatient.getCnp());
		gender.setValue(currentPatient.getGender().toString());
		dateOfBirth.setValue(currentPatient.getDateOfBirth());
		homeAddress.setText(currentPatient.getHomeAddress());
		phoneNumber.setText(currentPatient.getPhoneNumber());
		emailAddress.setText(currentPatient.getEmailAddress());
		bloodType.setValue(currentPatient.getBloodType().toString());
		rh.setValue(currentPatient.getRH().toString());
		doctorLayout=controller;
	}

	public void updateSelectedPacient(ActionEvent event) {

		CheckForm check = new CheckForm();
		if (check.checkUpdatePatient(firstName, lastName, cnp, gender, dateOfBirth, emailAddress, phoneNumber, bloodType, rh) == true) {
			currentPatient.setFirstName(firstName.getText());
			currentPatient.setLastName(lastName.getText());
			currentPatient.setCnp(cnp.getText());
			currentPatient.setGender(gender.getValue().toString());
			currentPatient.setDateOfBirth(dateOfBirth.getValue());
			currentPatient.setHomeAddress(homeAddress.getText());
			currentPatient.setPhoneNumber(phoneNumber.getText());
			currentPatient.setEmailAddress(emailAddress.getText());
			currentPatient.setBloodType(bloodType.getValue().toString());
			currentPatient.setRH(rh.getValue().toString());
			UpdateModel updateModel = SpringApplicationContext.instance().getBean("UpdateModel", UpdateModel.class);
			updateModel.updatePatient(currentPatient);
			doctorLayout.refreshTheTable();
			doctorLayout.populatePieChart();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Patient Updated Succesfully!");
			alert.showAndWait();
			((Node) (event.getSource())).getScene().getWindow().hide();
		}

	}
	public void onCheckBoxClicked(ActionEvent e)
	{
		if(checkBox.isSelected())
		{
			infoGrid.setDisable(false);
		}
		else
		{
			infoGrid.setDisable(true);
		}
	}
}
