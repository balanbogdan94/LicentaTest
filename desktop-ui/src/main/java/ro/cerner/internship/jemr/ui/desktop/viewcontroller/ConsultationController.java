package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import ro.cerner.internship.jemr.core.create.CreateModel;
import ro.cerner.internship.jemr.core.delete.DeleteModel;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class ConsultationController extends BaseDoctorController implements Initializable {

	@FXML
	private Button viewConsultation;
	@FXML
	private Button deleteConsultation;
	@FXML
	private TextField diagnosticSearchBox;
	@FXML
	private TableView<Examination> tableView = new TableView<>();
	@FXML
	private TableColumn<Examination, String> dateColumn = new TableColumn<Examination, String>();
	@FXML
	private TableColumn<Examination, String> diagnosticColumn = new TableColumn<Examination, String>();
	@FXML
	private TableColumn<Examination, String> commentsColumn = new TableColumn<Examination, String>();
	Examination selectedExamination;
	ObservableList<Examination> examinationList;

	public void setPatientAndDoctor(Patient selectedPatient, Doctor currentDoctor) 
	{
		setCurrentDoctor(currentDoctor);
		setSelectedPatient(selectedPatient);
		examinationList = FXCollections.observableArrayList(viewModel.viewListOfExaminations(selectedPatient.getObjectID()));
		tableView.setItems(examinationList);
	}



	public void startNewConsultation(ActionEvent event) {
		CreateModel model = SpringApplicationContext.instance().getBean("CreateModel", CreateModel.class);
		int id = model.addExamination(getSelectedPatient().getObjectID());
		try {
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/NewConsultation.fxml")
							.openStream());
			root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			NewConsultationController controler = (NewConsultationController) loader.getController();
			controler.setAnalysis(getSelectedPatient(), getCurrentDoctor(), id);
			diagnosticSearchBox.getScene().setRoot(root);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void viewOldConsultationOnButton(ActionEvent event) {
		viewOldConsultation(event);
	}

	public <T> void viewOldConsultation(T event) {

		try {
			selectedExamination = tableView.getSelectionModel().getSelectedItem();
			if (selectedExamination != null) {
				FXMLLoader loader = new FXMLLoader();
				Pane root = loader.load(getClass()
						.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/ViewOldConsultation.fxml")
						.openStream());
				root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				ViewOldConsultationController controler = (ViewOldConsultationController) loader.getController();
				controler.setCurrentExamination(selectedExamination, getSelectedPatient(), getCurrentDoctor());
				deleteConsultation.getScene().setRoot(root);
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You have not selected a Consultation!");
				alert.showAndWait();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void deleteSelectedExamination(ActionEvent e) {
		DeleteModel deleteModel = SpringApplicationContext.instance().getBean("DeleteModel", DeleteModel.class);
		Examination selectedExamination = tableView.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.WARNING);
		if (selectedExamination == null) {
			patientNotSelectedAlert();
		} else {

			Alert alert1 = new Alert(AlertType.CONFIRMATION);
			alert1.setTitle("Confirmation Dialog");
			alert1.setHeaderText("Confirm action");
			alert1.setContentText("Do you really want to delete this examination?");

			Optional<ButtonType> result = alert1.showAndWait();
			if (result.get() == ButtonType.OK) {
				deleteModel.deleteExamination(selectedExamination.getObjectID());
				refreshTheTable();
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Examination deleted");
				alert.showAndWait();
				viewConsultation.setDisable(true);
				deleteConsultation.setDisable(true);
			}
		}
	}

	public void refreshTheTable() 
	{
		tableView.setItems(examinationList);
	}

	public void searchDiagnostic(ActionEvent e)
	{
		examinationList = FXCollections.observableArrayList(viewModel.searchDiagnostic(diagnosticSearchBox.getText()));
		refreshTheTable();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		dateColumn.setCellValueFactory(new PropertyValueFactory<Examination, String>("examinationDateFormated"));
		diagnosticColumn.setCellValueFactory(new PropertyValueFactory<Examination, String>("Diagnostic"));
		commentsColumn.setCellValueFactory(new PropertyValueFactory<Examination, String>("Comments"));
		tableView.setRowFactory(tv -> {
			TableRow<Examination> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					viewConsultation.setDisable(false);
					deleteConsultation.setDisable(false);
				} else if (event.getClickCount() == 2 && (!row.isEmpty())) {
					viewOldConsultation(event);
				}
			});
			return row;
		});

	}

	public void compareConsultations(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Pane root= loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/CompareWindow.fxml")
							.openStream());
			root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			CompareController controller = (CompareController) loader.getController();
			controller.setDoctorAndPatient(getCurrentDoctor(), getSelectedPatient());
			diagnosticSearchBox.getScene().setRoot(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
