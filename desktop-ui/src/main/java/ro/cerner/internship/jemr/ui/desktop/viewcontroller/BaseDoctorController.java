package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.cerner.internship.jemr.core.view.ViewModel;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public abstract class BaseDoctorController {
	@FXML
	protected Label doctorName;
	private Doctor currentDoctor;
	protected ViewModel viewModel = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);

	public Doctor getCurrentDoctor() {
		return currentDoctor;
	}

	public void setCurrentDoctor(Doctor currentDoctor) {
		this.currentDoctor = currentDoctor;
	}
	
	
	
	
	public void updateUsernameAndPassword(ActionEvent event)
	{
		try 
		{
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass()
					.getResource(
							"/ro/cerner/internship/jemr/ui/desktop/viewcontroller/UpdateUsernameAndPasswordLayout.fxml")
					.openStream());
			UpdateUsernameAndPasswordLayoutController updateDoctor = (UpdateUsernameAndPasswordLayoutController) loader
					.getController();
			updateDoctor.setCurrentUser(currentDoctor);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.initModality(Modality.WINDOW_MODAL);
			primaryStage.show();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
	
	public void logOut(ActionEvent event) {
		try {

			Pane root;
			root = FXMLLoader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/LogInLayout.fxml"));
			root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			doctorName.getScene().setRoot(root);
			
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}
	}
	
}
