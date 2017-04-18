package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.core.check.CheckuserName;
import ro.cerner.internship.jemr.core.update.UpdateModel;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.entity.User;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class UpdateUsernameAndPasswordLayoutController implements Initializable {

	@FXML
	private Button saveChanges;
	@FXML
	private TextField userNameInput;
	@FXML
	private PasswordField passwordInput;
	@FXML
	private PasswordField repeatPasswordInput;

	Doctor currentDoctor;
	Patient currentPatient;
	User currentUser;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	public void setCurrentUser(User selectedUser) {
		currentUser = selectedUser;
		if (currentUser.getType() == 2) {
			currentDoctor = (Doctor) selectedUser;
			this.userNameInput.setText(selectedUser.getUserName());
		} else if (currentUser.getType() == 3) {
			currentPatient = (Patient) selectedUser;
			this.userNameInput.setText(selectedUser.getUserName());
		}
	}

	public void saveChangesOnClick(ActionEvent event) {
		saveChanges(event);
	}

	public void saveChangesOnEnter(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.ENTER)) {
			saveChanges(keyEvent);
		}
	}

	public <T> void saveChanges(T event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		try {
			if (!passwordInput.getText().equals(repeatPasswordInput.getText())) {
				alert.setContentText("Passwords do not match!");
				alert.showAndWait();
			} else {
				if (currentUser.getType() == 2) {
					UpdateModel updateModel = SpringApplicationContext.instance().getBean("UpdateModel",
							UpdateModel.class);
					if (currentDoctor.getUserName().equalsIgnoreCase(userNameInput.getText())) {
						currentDoctor.setUserPassword(passwordInput.getText());
						updateModel.updateUser(currentDoctor);
						alert.setContentText("User Updated Succesfully!");
						alert.showAndWait();
						backToDoctorView();
						((Node) (((EventObject) event).getSource())).getScene().getWindow().hide();
					} else {
						CheckuserName checkUsername = SpringApplicationContext.instance().getBean("CheckuserName",
								CheckuserName.class);
						currentDoctor.setUserName(userNameInput.getText());
						currentDoctor.setUserPassword(passwordInput.getText());
						if (checkUsername.checkCurrentUsername(userNameInput.getText()) != 1) {
							updateModel.updateUser(currentDoctor);
							alert.setContentText("User Updated Succesfully!");
							alert.showAndWait();
							backToDoctorView();
							((Node) (((EventObject) event).getSource())).getScene().getWindow().hide();
						} else if (checkUsername.checkCurrentUsername(userNameInput.getText()) == 1) {
							alert.setContentText(
									"This username already exists in our database! Please try another one.");
							alert.showAndWait();
						}
					}
				} else if (currentUser.getType() == 3) {

					UpdateModel updateModel = SpringApplicationContext.instance().getBean("UpdateModel",
							UpdateModel.class);
					if (currentPatient.getUserName().equalsIgnoreCase(userNameInput.getText())) {
						currentPatient.setUserPassword(passwordInput.getText());
						updateModel.updateUser(currentPatient);
						alert.setContentText("User Updated Succesfully!");
						alert.showAndWait();
						backToPacientView();
					} else {
						currentPatient.setUserName(userNameInput.getText());
						currentPatient.setUserPassword(passwordInput.getText());
						CheckuserName checkUsername = SpringApplicationContext.instance().getBean("CheckuserName",
								CheckuserName.class);
						if (checkUsername.checkCurrentUsername(userNameInput.getText()) != 1) {
							updateModel.updateUser(currentPatient);
							alert.setContentText("User Updated Succesfully!");
							alert.showAndWait();
							backToPacientView();
							((Node) (((EventObject) event).getSource())).getScene().getWindow().hide();
						} else if (checkUsername.checkCurrentUsername(userNameInput.getText()) == 1) {
							alert.setContentText(
									"This username already exists in our database! Please try another one.");
							alert.showAndWait();
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void backToPacientView() {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/PacientWindow.fxml")
							.openStream());
			PacientWindowController patientView = (PacientWindowController) loader.getController();
			patientView.setCurrentPatient(currentPatient);
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

	public void backToDoctorView() {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(getClass()
					.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/FirstPage.fxml").openStream());
			FirstPageController doctorView = (FirstPageController) loader.getController();
			doctorView.getCurrentDoctor(currentDoctor);
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

	public void cancel(ActionEvent event) {
		if (currentUser.getType() == 2) {
			backToDoctorView();
		} else if (currentUser.getType() == 3) {
			backToPacientView();
		}
		((Node) (event.getSource())).getScene().getWindow().hide();

	}

}
