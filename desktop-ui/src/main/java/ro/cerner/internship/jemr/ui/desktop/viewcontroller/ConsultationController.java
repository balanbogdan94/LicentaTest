package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;
import java.net.URL;
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
import ro.cerner.internship.jemr.core.create.CreateModel;
import ro.cerner.internship.jemr.core.delete.DeleteModel;
import ro.cerner.internship.jemr.core.view.ViewModel;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class ConsultationController implements Initializable {
	@FXML
	private Label firstName;
	@FXML
	private Label lastName;
	@FXML
	private Label firstName1;
	@FXML
	private Label lastName1;
	@FXML
	private Label cnp;
	@FXML
	private Label gender;
	@FXML
	private Label dateOfBirth;
	@FXML
	private Label phoneNumber;
	@FXML
	private Label bloodType;
	@FXML
	private Button viewConsultation;
	@FXML
	private Button deleteConsultation;

	@FXML
	private TableView<Examination> tableView = new TableView<>();
	@FXML
	private TableColumn<Examination, String> dateColumn = new TableColumn<Examination, String>();
	@FXML
	private TableColumn<Examination, String> diagnosticColumn = new TableColumn<Examination, String>();
	@FXML
	private TableColumn<Examination, String> commentsColumn = new TableColumn<Examination, String>();

	Patient selectedPatient;
	Examination selectedExamination;
	Doctor currentDoctor;
	ViewModel view = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);
	ObservableList<Examination> examinationList;

	public void setCurrentPatient(Patient selectedPatient, Doctor currentDoctor) {
		this.selectedPatient = selectedPatient;
		this.currentDoctor = currentDoctor;
		this.firstName.setText(selectedPatient.getFirstName());
		this.lastName.setText(selectedPatient.getLastName());
		this.cnp.setText(selectedPatient.getCnp());
		this.gender.setText(selectedPatient.getGender());
		this.dateOfBirth.setText(selectedPatient.getDateOfBirth().toString());
		this.phoneNumber.setText(selectedPatient.getPhoneNumber());
		this.bloodType.setText(selectedPatient.getBloodType() + " " + selectedPatient.getRH());
		this.firstName1.setText(currentDoctor.getFirstName());
		this.lastName1.setText(currentDoctor.getLastName());
		ObservableList<Examination> examinationList = FXCollections
				.observableArrayList(view.viewListOfExaminations(selectedPatient.getObjectID()));
		tableView.setItems(examinationList);
	}

	public void backToDoctorView(ActionEvent event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(getClass()
					.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/FirstPage.fxml").openStream());
			FirstPageController pageController = (FirstPageController) loader.getController();
			pageController.getCurrentDoctor(currentDoctor);
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
			((Node) event.getSource()).getScene().getWindow().hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startNewConsultation(ActionEvent event) {
		CreateModel model = SpringApplicationContext.instance().getBean("CreateModel", CreateModel.class);
		int id = model.addExamination(selectedPatient.getObjectID());
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/NewConsultation.fxml")
							.openStream());

			NewConsultationController controler = (NewConsultationController) loader.getController();
			controler.setAnalysis(selectedPatient, currentDoctor, id);
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
						DeleteModel model = SpringApplicationContext.instance().getBean("DeleteModel",
								DeleteModel.class);
						model.deleteExamination(id);
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

	@FXML
	public void viewOldConsultationOnButton(ActionEvent event) {
		viewOldConsultation(event);
	}

	public <T> void viewOldConsultation(T event) {

		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(getClass()
					.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/ViewOldConsultation.fxml")
					.openStream());

			ViewOldConsultationController controler = (ViewOldConsultationController) loader.getController();
			selectedExamination = tableView.getSelectionModel().getSelectedItem();
			if (selectedExamination != null) {
				controler.setCurrentExamination(selectedExamination, selectedPatient, currentDoctor);

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
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have not selected any Examination!");
			alert.showAndWait();
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

	public void refreshTheTable() {
		examinationList = FXCollections.observableArrayList(view.viewListOfExaminations(selectedPatient.getObjectID()));
		tableView.setItems(examinationList);
	}

	public void logOut(ActionEvent event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			((Node) event.getSource()).getScene().getWindow().hide();
			Pane root;
			root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/LogInLayout.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		DoubleBinding bind = tableView.widthProperty().divide(3);

		dateColumn.prefWidthProperty().bind(bind);
		diagnosticColumn.prefWidthProperty().bind(bind);
		commentsColumn.prefWidthProperty().bind(bind);

		dateColumn.setCellValueFactory(new PropertyValueFactory<Examination, String>("examinationDateFormated"));
		diagnosticColumn.setCellValueFactory(new PropertyValueFactory<Examination, String>("Diagnostic"));
		commentsColumn.setCellValueFactory(new PropertyValueFactory<Examination, String>("Comments"));

		viewConsultation.setDisable(true);
		deleteConsultation.setDisable(true);

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
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			((Node) event.getSource()).getScene().getWindow().hide();
			Pane root;
			root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/CompareWindow.fxml")
							.openStream());
			CompareController controller = (CompareController) loader.getController();
			controller.setDoctorAndPatient(currentDoctor, selectedPatient);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
