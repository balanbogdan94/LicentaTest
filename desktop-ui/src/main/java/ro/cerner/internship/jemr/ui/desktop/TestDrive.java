package ro.cerner.internship.jemr.ui.desktop;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.ui.desktop.viewcontroller.AdminLayoutController;
import ro.cerner.internship.jemr.ui.desktop.viewcontroller.FirstPageController;
import ro.cerner.internship.jemr.ui.desktop.viewcontroller.LogInLayoutController1;

public class TestDrive extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/LogInLayout.fxml").openStream());
			root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.show();
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}
