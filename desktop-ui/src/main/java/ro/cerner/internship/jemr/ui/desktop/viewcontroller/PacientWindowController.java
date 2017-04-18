package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.EventObject;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.core.update.UpdateModel;
import ro.cerner.internship.jemr.core.view.ViewModel;
import ro.cerner.internship.jemr.persistence.api.entity.Analysis;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.entity.User;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class PacientWindowController implements Initializable {

	@FXML
	private Label pacientFirstName;
	@FXML
	private Label pacientLastName;
	@FXML
	private Label CNP;
	@FXML
	private Label gender;
	@FXML
	private Label dateOfBirth;
	@FXML
	private Label pacientPhoneNumber;
	@FXML
	private Label bloodType;
	@FXML
	private Label doctorFirstName;
	@FXML
	private Label doctorLastName;
	@FXML
	private Label specialty;
	@FXML
	private Label emailAddress;
	@FXML
	private Label doctorPhoneNumber;
	@FXML
	private Button viewExaminationDetails;
	@FXML
	private Button updateUsernameAndPassword;
	@FXML
	private Button logOut;
	@FXML
	private TableView<Examination> examinationTable;
	@FXML
	private TableColumn<Analysis, String> dateColumn = new TableColumn<Analysis, String>();
	@FXML
	private TableColumn<Analysis, String> diagnosticColumn = new TableColumn<Analysis, String>();
	@FXML
	private TableColumn<Analysis, String> commentsColumn = new TableColumn<Analysis, String>();

	Examination currentExamination;
	Doctor doctor;
	Patient patient;
	ViewModel view = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);
	UpdateModel updateModel = SpringApplicationContext.instance().getBean("UpdateModel", UpdateModel.class);
	ObservableList<Examination> examinationList;
	User currentUser;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		DoubleBinding bind = examinationTable.widthProperty().divide(3);
		
		dateColumn.prefWidthProperty().bind(bind);
		diagnosticColumn.prefWidthProperty().bind(bind);
		commentsColumn.prefWidthProperty().bind(bind);
		
		dateColumn.setCellValueFactory(new PropertyValueFactory<Analysis, String>("examinationDateFormated"));
		diagnosticColumn.setCellValueFactory(new PropertyValueFactory<Analysis, String>("Diagnostic"));
		commentsColumn.setCellValueFactory(new PropertyValueFactory<Analysis, String>("Comments"));

		examinationTable.setRowFactory(tv -> {
			TableRow<Examination> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					viewExaminationDetails(event);
				}
			});
			return row;
		});
	}

	public void setCurrentPatient(Patient selectedPatient) {

		this.patient = selectedPatient;
		Doctor currentDoctor = view.viewDocLabel(selectedPatient.getDoctorID());
		this.doctor = currentDoctor;

		this.doctorFirstName.setText(doctor.getFirstName());
		this.doctorLastName.setText(doctor.getLastName());
		this.specialty.setText(doctor.getSpecialty());
		this.emailAddress.setText(doctor.getEmailAddress());
		this.doctorPhoneNumber.setText(doctor.getPhoneNumber());

		this.pacientFirstName.setText(selectedPatient.getFirstName());
		this.pacientLastName.setText(selectedPatient.getLastName());
		this.CNP.setText(selectedPatient.getCnp());
		this.gender.setText(selectedPatient.getGender());
		this.dateOfBirth.setText(selectedPatient.getDateOfBirth().toString());
		this.pacientPhoneNumber.setText(selectedPatient.getPhoneNumber());
		this.bloodType.setText(selectedPatient.getBloodType() + " " + selectedPatient.getRH());

		ObservableList<Examination> examinationList = FXCollections
				.observableArrayList(view.viewListOfExaminations(selectedPatient.getObjectID()));
		examinationTable.setItems(examinationList);
	}

	@FXML
	public void viewExaminationDetailsOnButton(ActionEvent event) {
		viewExaminationDetails(event);
	}

	public <T> void viewExaminationDetails(T event) {

		try {

			currentExamination = examinationTable.getSelectionModel().getSelectedItem();
			if (currentExamination == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You have not selected any examination!");
				alert.showAndWait();
			} else {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Pane root;
				root = loader.load(getClass()
						.getResource(
								"/ro/cerner/internship/jemr/ui/desktop/viewcontroller/ViewExaminationFromPatient.fxml")
						.openStream());

				ViewExaminationFromPatientController controler = (ViewExaminationFromPatientController) loader
						.getController();
				controler.setCurrentExamination(currentExamination, patient, doctor, currentUser);

				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setMaximized(true);
				primaryStage.setMinHeight(root.getPrefHeight());
				primaryStage.setMinWidth(root.getPrefWidth());
				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				    @Override
				    public void handle(WindowEvent event) {

				        // consume event
				        event.consume();

				        // show close dialog
				        Alert alert = new Alert(AlertType.CONFIRMATION);
				        alert.setTitle("Close Confirmation");
				        alert.setHeaderText("Do you really want to quit THE APPLICATION?");
				        alert.initOwner( primaryStage);

				        Optional<ButtonType> result = alert.showAndWait();
				        if (result.get() == ButtonType.OK){
				            Platform.exit();
				        }
				    }
				});
				primaryStage.show();
				((Node) ((EventObject) event).getSource()).getScene().getWindow().hide();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateUsernameAndPassword(ActionEvent event) {
		try {

			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(getClass()
					.getResource(
							"/ro/cerner/internship/jemr/ui/desktop/viewcontroller/UpdateUsernameAndPasswordLayout.fxml")
					.openStream());
			UpdateUsernameAndPasswordLayoutController updatePatient = (UpdateUsernameAndPasswordLayoutController) loader
					.getController();
			updatePatient.setCurrentUser(patient);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent event) {

			        // consume event
			        event.consume();

			        // show close dialog
			        Alert alert = new Alert(AlertType.CONFIRMATION);
			        alert.setTitle("Close Confirmation");
			        alert.setHeaderText("Do you really want to quit THE APPLICATION?");
			        alert.initOwner( primaryStage);

			        Optional<ButtonType> result = alert.showAndWait();
			        if (result.get() == ButtonType.OK){
			            Platform.exit();
			        }
			    }
			});
			primaryStage.show();
			((Node) event.getSource()).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logOut(ActionEvent event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/LogInLayout.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			((Node) event.getSource()).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
