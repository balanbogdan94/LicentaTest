package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.awt.List;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.core.check.CheckUser;
import ro.cerner.internship.jemr.core.delete.DeleteModel;
import ro.cerner.internship.jemr.core.update.UpdateModel;
import ro.cerner.internship.jemr.core.view.ViewModel;
import ro.cerner.internship.jemr.persistence.api.entity.Admin;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class AdminLayoutController implements Initializable {
	@FXML
	 TableView<Doctor> doctorTable = new TableView<>();
	@FXML
	private TableColumn<Doctor, String> firstName = new TableColumn<Doctor, String>();
	@FXML
	private TableColumn<Doctor, String> lastName = new TableColumn<Doctor, String>();
	@FXML
	private TableColumn<Doctor, String> phoneNumber = new TableColumn<Doctor, String>();
	@FXML
	private TableColumn<Doctor, String> emailAddress = new TableColumn<Doctor, String>();
	@FXML
	private TableColumn<Doctor, String> speciality = new TableColumn<Doctor, String>();
	@FXML
	private AnchorPane ap;
	@FXML
	Button updateButton;
	@FXML
	TextField searchTextBox;
	@FXML
	Button deleteButton;
	@FXML
	private Label specialty;
	@FXML
	private Label adminName;
	@FXML
	 PieChart specialtyPieChart;
	@FXML 
	private BarChart<String, Integer> numberOfPatientsChart;
	
	Admin currentAdmin = new Admin();
	ObservableList<Doctor> doctorList;
	ViewModel view = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);
	private ArrayList<Doctor> allDoctorsList;
	
	public void getCurrentAdmin(Admin admin) {
		currentAdmin = admin;
		adminName.setText(admin.getFirstName()+" "+admin.getLastName());
	}

	public void logOut(ActionEvent event) {
		try {

			Stage primaryStage = new Stage();
			Pane root;
			root = FXMLLoader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/LogInLayout.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

			((Node) event.getSource()).getScene().getWindow().hide();

		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}

	}

	public void createDoctor(ActionEvent event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader=new FXMLLoader();
			Pane root;
			root =loader.load(getClass()
					.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/CreateDoctorLayout.fxml")
					.openStream());
			CreateDoctorLayoutController createDocController=(CreateDoctorLayoutController)loader.getController();
			createDocController.setAdminController(this);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.initModality(Modality.APPLICATION_MODAL);
			primaryStage.setResizable(false);
			primaryStage.show();
//			populateTable();
		}

		catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
	}

	public <T> void editDoctor(T e) {
		try {

			Doctor doc = doctorTable.getSelectionModel().getSelectedItem();
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Pane root;
				root = loader.load(getClass()
						.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/UpdateDoctorLayout.fxml")
						.openStream());
				UpdateDoctorLayoutController updateDoctor = (UpdateDoctorLayoutController) loader.getController();
				updateDoctor.getDoctor(doc);
				updateDoctor.setController(this);
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.initModality(Modality.APPLICATION_MODAL);
				primaryStage.setResizable(false);
				primaryStage.show();			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void editOnButton(ActionEvent event) {
		editDoctor(event);
	}

	public void populateTable() {

		firstName.setCellValueFactory(new PropertyValueFactory<Doctor, String>("firstName"));
		lastName.setCellValueFactory(new PropertyValueFactory<Doctor, String>("lastName"));
		phoneNumber.setCellValueFactory(new PropertyValueFactory<Doctor, String>("phoneNumber"));
		emailAddress.setCellValueFactory(new PropertyValueFactory<Doctor, String>("emailAddress"));
		speciality.setCellValueFactory(new PropertyValueFactory<Doctor, String>("specialty"));
		doctorList=FXCollections.observableArrayList(view.viewListOfDoctors(searchTextBox.getText()));
		doctorTable.setItems(doctorList);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		DoubleBinding bind = doctorTable.widthProperty().divide(5);
		firstName.prefWidthProperty().bind(bind);
		lastName.prefWidthProperty().bind(bind);
		phoneNumber.prefWidthProperty().bind(bind);
		emailAddress.prefWidthProperty().bind(bind);
		speciality.prefWidthProperty().bind(bind);
		updateButton.setDisable(true);
		deleteButton.setDisable(true);
		populateTable();
		populatePieChart();
		populateBarChart();
		doctorTable.setRowFactory(tv -> {
			TableRow<Doctor> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					updateButton.setDisable(false);
					deleteButton.setDisable(false);
				} else if (event.getClickCount() == 2 && (!row.isEmpty())) {
					editDoctor(event);
				}
			});
			return row;
		});

	}

	 private void populateBarChart() {
		 XYChart.Series<String, Integer> series = new Series<String, Integer>();
			series.setName("Patients");
		for(Doctor curentDoctor:doctorList)
		{
			series.getData().add(new XYChart.Data<String, Integer>(curentDoctor.getLastName(),view.viewPatientOfDoctor
					(curentDoctor.getObjectID())));
		}
		numberOfPatientsChart.getData().addAll(series);
	}

	void populatePieChart() {
		Map<String,Integer> specialityDocNumber=view.viewAllDocNumberBySpecility(doctorList);
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
		for(Map.Entry<String, Integer> entry : specialityDocNumber.entrySet())
		{
		pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
		}

		specialtyPieChart.setData(pieChartData);
		
	}

	public void deleteDoctor(ActionEvent e) {
		CheckUser check = SpringApplicationContext.instance().getBean("CheckUser", CheckUser.class);
		int doctorId = doctorTable.getSelectionModel().getSelectedItem().getObjectID();
		Doctor selectedDoctor = doctorTable.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Close Confirmation");
		alert.setHeaderText("Are you sure you want to delete Dr. "+selectedDoctor.getFirstName()+" "+selectedDoctor.getLastName()+"?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			if (check.checkIfPatientExist(doctorId) == 1) {
				popUpForTransfer();
			} else {
				DeleteModel delete = SpringApplicationContext.instance().getBean("DeleteModel", DeleteModel.class);
				delete.deleteDoctor(doctorId);
				populateTable();
			}
		}
	}

	public void popUpForTransfer() {
		UpdateModel transfer = SpringApplicationContext.instance().getBean("UpdateModel", UpdateModel.class);
		int id = doctorTable.getSelectionModel().getSelectedItem().getObjectID();
		ChoiceDialog<Doctor> dialog = new ChoiceDialog<>();
		dialog.getItems().addAll(doctorList);
		for (Doctor currentDoc : doctorList) {
			if (id == currentDoc.getObjectID()) {
				dialog.getItems().remove(currentDoc);
			}
		}
		dialog.setTitle("Choice Dialog");
		dialog.setHeaderText(
				"This doctor has " + view.viewPatientOfDoctor(id) + " patients! They need to be transfered");
		dialog.setContentText("Transfer patients to Dr.");
		Optional<Doctor> result = dialog.showAndWait();
		if (result.isPresent()) {
			transfer.transferAllPatients(id, result.get().getObjectID());
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText(
					"All doctors transfered to " + result.get().getFirstName() + " " + result.get().getLastName());
			alert.showAndWait();
			deleteDoctor(null);
		}
	}
	
	public void doSearch(ActionEvent e)
	{
		populateTable();
	}
}
