package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.awt.Checkbox;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import ro.cerner.internship.jemr.core.update.UpdateModel;
import ro.cerner.internship.jemr.persistence.api.entity.Admin;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class UpdateDoctorLayoutController implements Initializable {
	@FXML
	private Label helloMessage;
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField cnp;
	@FXML
	private DatePicker dateOfBirth;
	@FXML
	private TextField homeAdress;
	@FXML
	private TextField emailAdress;
	@FXML
	private TextField phoneNumber;
	@FXML
	private TextField speciality;
	@FXML
	private ComboBox Gender;
	@FXML
	private Label doctorName;
	@FXML
	private CheckBox editChekBox;
	@FXML
	private GridPane infoGrid;

	private Doctor curentDoctor;
	private AdminLayoutController controller;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		firstName.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 25 ? change : null));
		lastName.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 25 ? change : null));
		cnp.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 13 ? change : null));
		homeAdress.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 100 ? change : null));
		phoneNumber.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 15 ? change : null));
		emailAdress.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 40 ? change : null));
		speciality.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 100 ? change : null));
		Gender.getItems().addAll("M", "F");
	}

	public AdminLayoutController getController() {
		return controller;
	}



	public void setController(AdminLayoutController controller) {
		this.controller = controller;
	}

	
	public void getDoctor(Doctor doc) {
		this.curentDoctor = doc;
		firstName.setText(curentDoctor.getFirstName());
		lastName.setText(curentDoctor.getLastName());
		cnp.setText(curentDoctor.getCnp());
		dateOfBirth.setValue(curentDoctor.getDateOfBirth());
		homeAdress.setText(curentDoctor.getHomeAddress());
		phoneNumber.setText(curentDoctor.getPhoneNumber());
		emailAdress.setText(curentDoctor.getEmailAddress());
		speciality.setText(curentDoctor.getSpecialty());
		Gender.setValue(curentDoctor.getGender());
	}

	public void updateDoctor(ActionEvent event) {
		CheckForm check = new CheckForm();
		if (check.checkUpdateDoctor(firstName, lastName, cnp, Gender, dateOfBirth, emailAdress, phoneNumber, speciality) == true)
		{
			curentDoctor.setFirstName(firstName.getText());
			curentDoctor.setLastName(lastName.getText());
			curentDoctor.setCnp(cnp.getText());
			curentDoctor.setDateOfBirth(dateOfBirth.getValue());
			curentDoctor.setHomeAddress(homeAdress.getText());
			curentDoctor.setEmailAddress(emailAdress.getText());
			curentDoctor.setPhoneNumber(phoneNumber.getText());
			curentDoctor.setSpecialty(speciality.getText());
			curentDoctor.setGender(Gender.getValue().toString());

			UpdateModel updateModel = SpringApplicationContext.instance().getBean("UpdateModel", UpdateModel.class);
			updateModel.updateDoctor(curentDoctor);

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Doctor Updated Succesfully!");
			alert.showAndWait();
			controller.populateTable();
			controller.deleteButton.setDisable(true);
			controller.updateButton.setDisable(true);
			((Node) (event.getSource())).getScene().getWindow().hide();
		}
	}

	public void onCheckBoxClicked(ActionEvent e)
	{
		if(editChekBox.isSelected())
		{
			infoGrid.setDisable(false);
		}
		else
		{
			infoGrid.setDisable(true);
		}
			
	}
	
}
