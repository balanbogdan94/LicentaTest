package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.cerner.internship.jemr.core.delete.DeleteModel;
import ro.cerner.internship.jemr.core.update.UpdateModel;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class FirstPageController extends BaseDoctorController implements Initializable {

	@FXML
	private Label infoLabel;
	@FXML
	private PieChart sexChart;
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
	private ObservableList<Patient> patientList;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableBloodTypeView.setCellValueFactory(new PropertyValueFactory<Patient, String>("BloodType"));
		tableRHView.setCellValueFactory(new PropertyValueFactory<Patient, String>("RH"));
		tableFirstNameView.setCellValueFactory(new PropertyValueFactory<Patient, String>("FirstName"));
		tableLastNameView.setCellValueFactory(new PropertyValueFactory<Patient, String>("LastName"));
		tableAgeView.setCellValueFactory(new PropertyValueFactory<Patient, Date>("DateOfBirth"));
		viewClinicalHistory.setDisable(true);
		patientTableView.setRowFactory(tv -> {
			TableRow<Patient> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					setStateOfButtons(false);
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
			Pane root = loader.load(
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

	public void setStageInfo(Doctor doctor) {
		setCurrentDoctor(doctor);
		doctorName.setText(doctor.getFirstName() + " " + doctor.getLastName());
		patientList=FXCollections.observableArrayList(viewModel.viewListOfPacients(doctor.getObjectID()));
		patientTableView.setItems(patientList);
		populatePieChart();
	}

	public void deleteSelectedPatient(ActionEvent e) {
		DeleteModel deleteModel = SpringApplicationContext.instance().getBean("DeleteModel", DeleteModel.class);
		Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.WARNING);
		if (selectedPatient == null) {
			patientNotSelectedAlert();
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
				patientNotSelectedAlert();
			} else {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Pane root;
				root = loader.load(getClass()
						.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/UpdatePacientLayout.fxml")
						.openStream());
				UpdatePacientLayoutController updatePatient = (UpdatePacientLayoutController) loader.getController();
				updatePatient.getSelectedPacient(selectedPatient, getCurrentDoctor(),this);
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

	public void refreshTheTable() 
	{
		patientTableView.setItems(FXCollections.observableArrayList(viewModel.viewListOfPacients(getCurrentDoctor().getObjectID())));
	}

	public void viewClinicalHistoryOnButton(ActionEvent event) {
		viewClinicalHistory(event);
	}

	public <T> void viewClinicalHistory(T event) {
		try {
			Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
			if (selectedPatient == null) {
				patientNotSelectedAlert();
			} else {
				FXMLLoader loader = new FXMLLoader();
				Pane root = loader.load(
						getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/Consultation.fxml")
								.openStream());
				root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				ConsultationController patient = (ConsultationController) loader.getController();
				patient.setPatientAndDoctor(selectedPatient, getCurrentDoctor());
				deleteButton.getScene().setRoot(root);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	public void search(ActionEvent event) 
	{
		setStateOfButtons(true);
		patientTableView.setItems(FXCollections.observableArrayList(
				viewModel.viewListOfPacientsByLastName(getCurrentDoctor().getObjectID(), textInput.getText())));
	}


	public void transfer(ActionEvent event) 
	{
		try 
		{
			Patient selectedPatient = patientTableView.getSelectionModel().getSelectedItem();
			UpdateModel transfer = SpringApplicationContext.instance().getBean("UpdateModel", UpdateModel.class);
			if (selectedPatient == null) 
			{
				patientNotSelectedAlert();
			} 
			else 
			{
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
	
	private void setStateOfButtons(boolean state)
	{
		transferButton.setDisable(state);
		deleteButton.setDisable(state);
		updateButton.setDisable(state);
		viewClinicalHistory.setDisable(state);
	}
}
