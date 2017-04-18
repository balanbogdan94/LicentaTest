package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.EventObject;
import java.util.Map;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.core.delete.DeleteModel;
import ro.cerner.internship.jemr.core.update.UpdateModel;
import ro.cerner.internship.jemr.core.view.ViewModel;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class FirstPageController implements Initializable {

	@FXML
	private Label infoLabel;
	
	@FXML
	private PieChart sexChart;

	@FXML
	private Label doctorName;

	Doctor currentDoctor = new Doctor();

	@FXML
	private TableView<Patient> patientTableView;
	@FXML
	private TextField textInput;
	@FXML
	private Button search;
	@FXML
	private Button transferButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button updateButton;
	@FXML
	private Button viewClinicalHistory;

	@FXML
	private TableColumn<Patient, String> tableFirstNameView = new TableColumn<Patient, String>();
	@FXML
	private TableColumn<Patient, String> tableLastNameView = new TableColumn<Patient, String>();
	@FXML
	private TableColumn<Patient, Date> tableAgeView = new TableColumn<Patient, Date>();
	@FXML
	private TableColumn<Patient, String> tableBloodTypeView = new TableColumn<Patient, String>();
	@FXML
	private TableColumn<Patient, String> tableRHView = new TableColumn<Patient, String>();

	ViewModel viewModel = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);
	ObservableList<Patient> patientList;
	

	public void updateUsernameAndPassword(ActionEvent event) {
		try {

			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(getClass()
					.getResource(
							"/ro/cerner/internship/jemr/ui/desktop/viewcontroller/UpdateUsernameAndPasswordLayout.fxml")
					.openStream());
			UpdateUsernameAndPasswordLayoutController updateDoctor = (UpdateUsernameAndPasswordLayoutController) loader
					.getController();
			updateDoctor.setCurrentUser(currentDoctor);
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
					alert.initOwner(primaryStage);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
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

	public void signOut(ActionEvent event) {

		try {
			Stage primaryStage = new Stage();
			((Node) event.getSource()).getScene().getWindow().hide();
			Pane root;

			root = FXMLLoader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/LogInLayout.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setMinHeight(root.getPrefHeight());
			primaryStage.setMinWidth(root.getPrefWidth());
			primaryStage.show();

		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		DoubleBinding bind = patientTableView.widthProperty().divide(5);
		tableBloodTypeView.prefWidthProperty().bind(bind);
		tableRHView.prefWidthProperty().bind(bind);
		tableFirstNameView.prefWidthProperty().bind(bind);
		tableLastNameView.prefWidthProperty().bind(bind);
		tableAgeView.prefWidthProperty().bind(bind);
		tableBloodTypeView.setCellValueFactory(new PropertyValueFactory<Patient, String>("BloodType"));
		tableRHView.setCellValueFactory(new PropertyValueFactory<Patient, String>("RH"));
		tableFirstNameView.setCellValueFactory(new PropertyValueFactory<Patient, String>("FirstName"));
		tableLastNameView.setCellValueFactory(new PropertyValueFactory<Patient, String>("LastName"));
		tableAgeView.setCellValueFactory(new PropertyValueFactory<Patient, Date>("DateOfBirth"));
		transferButton.setDisable(true);
		deleteButton.setDisable(true);
		updateButton.setDisable(true);
		viewClinicalHistory.setDisable(true);
		patientTableView.setRowFactory(tv -> {
			TableRow<Patient> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					transferButton.setDisable(false);
					deleteButton.setDisable(false);
					updateButton.setDisable(false);
					viewClinicalHistory.setDisable(false);
				} else if (event.getClickCount() == 2 && (!row.isEmpty())) {
					viewClinicalHistory(event);
				}
			});
			return row;
		});

	}

	public void createPacient(ActionEvent event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/CreatePacient.fxml")
							.openStream());
			CreatePatientController patient = (CreatePatientController) loader.getController();
			patient.setDoctorID(this);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.initModality(Modality.APPLICATION_MODAL);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void getCurrentDoctor(Doctor doctor) {
		currentDoctor = doctor;
		doctorName.setText(currentDoctor.getFirstName() + " " + currentDoctor.getLastName());
		patientList = FXCollections.observableArrayList(viewModel.viewListOfPacients(currentDoctor.getObjectID()));
		patientTableView.setItems(patientList);
		populatePieChart();
	}

	public void deleteSelectedPatient(ActionEvent e) {
		DeleteModel deleteModel = SpringApplicationContext.instance().getBean("DeleteModel", DeleteModel.class);
		Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.WARNING);
		if (selectedPatient == null) {
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have not selected a Patient!");
			alert.showAndWait();
		} else {
			Alert alert1 = new Alert(AlertType.CONFIRMATION);
			alert1.setTitle("Confirmation Dialog");
			alert1.setHeaderText("Do you really want to delete patient "+selectedPatient.getFirstName()+" "+selectedPatient.getLastName()+"?");

			Optional<ButtonType> result = alert1.showAndWait();
			if (result.get() == ButtonType.OK) {
				deleteModel.deletePacient(selectedPatient.getObjectID());
				refreshTheTable();
				populatePieChart();
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Pacient deleted");
				alert.showAndWait();
			}

		}
	}

	public void updateSelectedPacient(ActionEvent event) {
		try {

			Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
			if (selectedPatient == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You have not selected a Patient!");
				alert.showAndWait();
			} else {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Pane root;
				root = loader.load(getClass()
						.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/UpdatePacientLayout.fxml")
						.openStream());
				UpdatePacientLayoutController updatePatient = (UpdatePacientLayoutController) loader.getController();
				updatePatient.getSelectedPacient(selectedPatient, currentDoctor,this);
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.initModality(Modality.APPLICATION_MODAL);
				primaryStage.setResizable(false);
				primaryStage.show();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void refreshTheTable() {

		patientList = FXCollections.observableArrayList(viewModel.viewListOfPacients(currentDoctor.getObjectID()));
		patientTableView.setItems(patientList);
	}

	public void viewClinicalHistoryOnButton(ActionEvent event) {
		viewClinicalHistory(event);
	}

	public <T> void viewClinicalHistory(T event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;

			root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/Consultation.fxml")
							.openStream());
			ConsultationController patient = (ConsultationController) loader.getController();
			Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
			if (selectedPatient == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You have not selected a Patient!");
				alert.showAndWait();
			} else {
				patient.setCurrentPatient(selectedPatient, currentDoctor);
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
						alert.initOwner(primaryStage);

						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == ButtonType.OK) {
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

	

	public <T> void search(T event) {

		transferButton.setDisable(true);
		deleteButton.setDisable(true);
		updateButton.setDisable(true);
		viewClinicalHistory.setDisable(true);
		patientList = FXCollections.observableArrayList(
				viewModel.viewListOfPacientsByLastName(currentDoctor.getObjectID(), textInput.getText()));
		patientTableView.setItems(patientList);
		if (patientList.isEmpty()) {
			refreshTheTable();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("This patient doesn't exist in our database!");
			alert.showAndWait();
		}

	}

	@FXML
	public void searchOnClick(ActionEvent event) {
		search(event);
	}

	@FXML
	public void searchOnEnter(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.ENTER)) {
			search(keyEvent);
		}
	}

	public void transfer(ActionEvent event) {

		try {

			Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
			UpdateModel transfer = SpringApplicationContext.instance().getBean("UpdateModel", UpdateModel.class);
			if (selectedPatient == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You have not selected a Patient!");
				alert.showAndWait();
			} else {

				int receiverDocID = 0;

				ChoiceDialog<Doctor> dialog = new ChoiceDialog<>();
				dialog.getItems().addAll(viewModel.viewListOfDoctors(""));
				for (int i = 0; i < dialog.getItems().size(); i++) {
					if (dialog.getItems().get(i).getObjectID() == selectedPatient.getDoctorID())
						dialog.getItems().remove(i);
				}

				dialog.setTitle("Transfer");
				dialog.setHeaderText("Do you want to transfer patient: " + selectedPatient.getFirstName() + " "
						+ selectedPatient.getLastName() + " to");
				dialog.setContentText("Choose another doctor:");

				Optional<Doctor> result = dialog.showAndWait();
				if (result.isPresent()) {
					receiverDocID = dialog.getSelectedItem().getObjectID();
					transfer.transferSelectedPatient(receiverDocID, selectedPatient.getObjectID());
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					alert.setContentText("You have transferred a Patient!");
					alert.showAndWait();
					dialog.close();
					refreshTheTable();
					populatePieChart();
					transferButton.setDisable(true);
					deleteButton.setDisable(true);
					updateButton.setDisable(true);
					viewClinicalHistory.setDisable(true);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	void populatePieChart() {
		Map<String,Integer> specialityDocNumber=viewModel.viewSexRatio(patientList);
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
		for(Map.Entry<String, Integer> entry : specialityDocNumber.entrySet())
		{
		pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
		}
		sexChart.setData(pieChartData);
	}

}
