package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;
import java.util.EventObject;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.core.check.CheckUser;
import ro.cerner.internship.jemr.persistence.api.entity.Admin;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.entity.User;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class LogInLayoutController1 {
	@FXML
	private TextField usernameInput;
	@FXML
	private PasswordField passwordInput;
	@FXML
	private Label messageLabel;

	User curentUser = new User();

	public void logInOnClick(ActionEvent event) {
		logInPerformed(event);
	}

	public <T> void logInPerformed(T event) {
		messageLabel.setText("aaaaaaaaaaaaaaaaa");
		try {
			CheckUser checkUser = SpringApplicationContext.instance().getBean("CheckUser", CheckUser.class);

			curentUser = checkUser.checkCurentUser(usernameInput.getText(), passwordInput.getText());

			if (curentUser == null) {
				messageLabel.setText("Database is not connected.");
			}

			else if ((usernameInput.getText().trim().isEmpty() && passwordInput.getText().trim().isEmpty())
					|| curentUser.getUserName() == "wrong" && curentUser.getUserPassword() == "wrong") {
				messageLabel.setText("User/Password incorect");
			}
//Admin
			else if (curentUser.getType() == 1) {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Pane root = loader.load(
						getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/AdminLayout.fxml")
								.openStream());
				AdminLayoutController adminController=(AdminLayoutController)loader.getController();
				adminController.getCurrentAdmin((Admin)curentUser);
				Scene scene = new Scene(root);
				root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setMaximized(true);
				primaryStage.setScene(scene);
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

			// Doctor
			else if (curentUser.getType() == 2) {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Pane root;
				root = loader.load(
						getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/FirstPage.fxml")
								.openStream());
				FirstPageController pageController = (FirstPageController) loader.getController();
				Doctor currentDoctor = (Doctor) curentUser;
				pageController.getCurrentDoctor(currentDoctor);
				Scene scene = new Scene(root);
				root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
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
			messageLabel.setText("Error ocured, please try again");
			e.printStackTrace();
		}

	}

}
