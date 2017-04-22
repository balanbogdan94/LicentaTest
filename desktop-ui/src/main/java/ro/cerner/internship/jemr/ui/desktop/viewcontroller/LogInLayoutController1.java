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

	@FXML
	public void logInPerformed(ActionEvent event) {
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
			else if (curentUser.getType() == 1)
			{
				FXMLLoader loader = new FXMLLoader();
				Pane root = loader.load(
						getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/AdminLayout.fxml")
								.openStream());
				root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				AdminLayoutController adminController=(AdminLayoutController)loader.getController();
				adminController.getCurrentAdmin((Admin)curentUser);
				messageLabel.getScene().setRoot(root);

			}
//Doctor
			else if (curentUser.getType() == 2) 
			{
				FXMLLoader loader = new FXMLLoader();
				Pane root= loader.load(
						getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/FirstPage.fxml")
								.openStream());
				root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				FirstPageController pageController = (FirstPageController) loader.getController();
				pageController.getCurrentDoctor(((Doctor)curentUser));
				messageLabel.getScene().setRoot(root);				
			}
		}
		catch (IOException e) 
		{
			messageLabel.setText("Error ocured, please try again");
			e.printStackTrace();
		}
	}
	

}
