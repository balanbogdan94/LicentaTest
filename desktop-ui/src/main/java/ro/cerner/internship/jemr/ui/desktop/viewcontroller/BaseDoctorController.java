package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.cerner.internship.jemr.core.view.ViewModel;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public abstract class BaseDoctorController {
	@FXML
	protected Label doctorName;
	@FXML
	private Label patientName;
	@FXML
	private Label patientDOB;
	@FXML
	private Label patientAge;
	@FXML
	private Label patientRH;
	@FXML
	private Label patientBloodType;
	@FXML
	private ImageView femaleSign;
	@FXML
	private ImageView maleSign;
	
	private Patient selectedPatient;
	private Doctor currentDoctor;
	protected ViewModel viewModel = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);

	public Doctor getCurrentDoctor() {
		return currentDoctor;
	}

	public void setCurrentDoctor(Doctor currentDoctor) {
		this.currentDoctor = currentDoctor;
		this.doctorName.setText(currentDoctor.getFirstName() + " " + currentDoctor.getLastName());
	}
	

	public void updateUsernameAndPassword(ActionEvent event)
	{
		try 
		{
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass()
					.getResource(
							"/ro/cerner/internship/jemr/ui/desktop/viewcontroller/UpdateUsernameAndPasswordLayout.fxml")
					.openStream());
			UpdateUsernameAndPasswordLayoutController updateDoctor = (UpdateUsernameAndPasswordLayoutController) loader
					.getController();
			updateDoctor.setCurrentUser(currentDoctor);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.initModality(Modality.WINDOW_MODAL);
			primaryStage.show();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
	
	public void logOut(ActionEvent event) {
		try {

			Pane root;
			root = FXMLLoader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/LogInLayout.fxml"));
			root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			doctorName.getScene().setRoot(root);
			
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
	}
	
	protected void patientNotSelectedAlert()
	{
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("You have not selected a Patient!");
		alert.showAndWait();
	}

	public Patient getSelectedPatient() {
		return selectedPatient;
	}

	public void setSelectedPatient(Patient selectedPatient) {
		this.selectedPatient = selectedPatient;
		initPatientCard(selectedPatient);
	}
	
	public void initPatientCard(Patient selectedPatient)
	{
		this.patientName.setText(selectedPatient.getFirstName() + " " + selectedPatient.getLastName());
		this.patientDOB.setText(selectedPatient.getDateOfBirth().toString());
		this.patientBloodType.setText(selectedPatient.getBloodType());
		this.patientRH.setText(selectedPatient.getRH());
		this.patientAge.setText(String.valueOf(viewModel.patientAge(selectedPatient.getDateOfBirth())) + " years");
		this.femaleSign.setVisible(selectedPatient.getGender().equals("F"));
		this.maleSign.setVisible(selectedPatient.getGender().equals("M"));
	}

	@FXML
	public void backToDoctorView(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass()
					.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/FirstPage.fxml").openStream());
			root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			FirstPageController pageController = (FirstPageController) loader.getController();
			pageController.setStageInfo(currentDoctor);
			doctorName.getScene().setRoot(root);		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
