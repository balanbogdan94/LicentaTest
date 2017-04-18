package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.core.bitdata.BitalinoData;
import ro.cerner.internship.jemr.core.bitdata.BitalinoDataBounds;
import ro.cerner.internship.jemr.core.update.UpdateModel;
import ro.cerner.internship.jemr.core.view.ViewModel;
import ro.cerner.internship.jemr.persistence.api.entity.Analysis;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class ViewOldConsultationController implements Initializable {

	@FXML
	private Label completedDate;
	@FXML
	private Label pacientInfo;
	@FXML
	private TableView<Analysis> examinationTable;
	@FXML
	private TableColumn<Analysis, String> sensorType = new TableColumn<Analysis, String>();
	@FXML
	private TableColumn<Analysis, Integer> frequency = new TableColumn<Analysis, Integer>();
	@FXML
	private TableColumn<Analysis, String> startDateColumn = new TableColumn<Analysis, String>();
	@FXML
	private TableColumn<Analysis, String> stopDateColumn = new TableColumn<Analysis, String>();
	@FXML
	private TextArea diagnostic;
	@FXML
	private TextArea comments;
	@FXML
	private Button viewAnalysis;
	@FXML
	private Button saveChanges;
	@FXML
	private Button saveGraph;
	@FXML
	private Button btnImage;
	@FXML
	private Label beatsPerMinute;
	@FXML
	private LineChart<Number, Number> analysisChart;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	private int currentPositionOnXAxis;
	private int distanceOnXAxis;
	private Series<Number, Number> series;
	private int maxChar = 2000;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	@FXML
	private Slider sliderForChart;
	@FXML
	private Button backButton;
	@FXML
	private Button forwardButton;

	int i = 0;

	Examination currentExamination;
	Doctor doctor;
	Patient patient;
	ViewModel view = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);
	UpdateModel updateModel = SpringApplicationContext.instance().getBean("UpdateModel", UpdateModel.class);
	ObservableList<Analysis> analysisList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isAllDataFromSeriesVisible(false);
		backButton.setVisible(false);
		forwardButton.setVisible(false);
		sliderForChart.setVisible(false);
		saveGraph.setDisable(true);
		diagnostic.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= maxChar ? change : null));
		comments.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= maxChar ? change : null));
		xAxis.setAutoRanging(false);
		sliderForChart.setValue(0);
		DoubleBinding bind = examinationTable.widthProperty().divide(4);

		sensorType.prefWidthProperty().bind(bind);
		frequency.prefWidthProperty().bind(bind);
		startDateColumn.prefWidthProperty().bind(bind);
		stopDateColumn.prefWidthProperty().bind(bind);

		sensorType.setCellValueFactory(new PropertyValueFactory<Analysis, String>("sensorName"));
		frequency.setCellValueFactory(new PropertyValueFactory<Analysis, Integer>("frequency"));
		startDateColumn.setCellValueFactory(new PropertyValueFactory<Analysis, String>("startDateFormated"));
		stopDateColumn.setCellValueFactory(new PropertyValueFactory<Analysis, String>("stopDateFormated"));
		sliderForChart.setVisible(false);
		analysisChart.setCreateSymbols(false);
		beatsPerMinute.setVisible(false);
		examinationTable.setRowFactory(tv -> {
			TableRow<Analysis> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					viewAnalysis(event);
				}
			});
			return row;
		});
	}

	public void setCurrentExamination(Examination selectedExamination, Patient currentPatient, Doctor doctor) {
		currentExamination = selectedExamination;
		if (currentExamination != null) {
			this.patient = currentPatient;
			this.doctor = doctor;
			this.completedDate.setText(selectedExamination.getExaminationDate().format(formatter).toString());
			this.pacientInfo.setText(currentPatient.getFirstName() + " " + currentPatient.getLastName() + " "
					+ currentPatient.getBloodType() + " " + currentPatient.getRH());
			this.diagnostic.setText(selectedExamination.getDiagnostic());
			this.comments.setText(selectedExamination.getComments());
			ObservableList<Analysis> analysisList = FXCollections
					.observableArrayList(view.viewListOfAnalysis(selectedExamination.getObjectID()));
			examinationTable.setItems(analysisList);
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have not selected a Consultation");
			alert.showAndWait();
		}

	}

	@FXML
	public void saveGraphic(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root;
		try {
			root = loader.load(getClass()
					.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/ViewOldConsultation.fxml")
					.openStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//ViewOldConsultationController pageController = (ViewOldConsultationController) loader.getController();
		xAxis.setUpperBound(series.getData().size()/examinationTable.getSelectionModel().getSelectedItem().getFrequency());
		xAxis.setLowerBound(0);
		xAxis.setTickLabelsVisible(false);
		WritableImage image = analysisChart.snapshot(new SnapshotParameters(), null);
		viewAnalysis(event);
		// TODO: probably use a file chooser here
		

		DateTimeFormatter saveFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH~mm");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName(patient.getFirstName()+"_"+patient.getLastName()+"_"+currentExamination.getExaminationDate().format(saveFormatter)+"_"+examinationTable.getSelectionModel().getSelectedItem().getSensorName());
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Open Resource File");
		// File file = new File("chart.png");

		//File file = new File("palette_"+currentExamination.getPacientID()+".png");
		File file=fileChooser.showSaveDialog(primaryStage);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			// TODO: handle exception here
		}
	}

	public void exit(ActionEvent event) {
		try {
			for (Series<Number, Number> series : analysisChart.getData()) {
				series.getData().clear();
			}
			System.gc();
			analysisChart.getData().clear();
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/Consultation.fxml")
							.openStream());
			ConsultationController pageController = (ConsultationController) loader.getController();
			pageController.setCurrentPatient(patient, doctor);
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

	public void viewAnalysisOnButton(ActionEvent event) {
		viewAnalysis(event);
		saveGraph.setDisable(false);
	}

	public <T> void viewAnalysis(T event) {
		
		
		
		analysisChart.getData().clear();
		List<Double> rawData=null ;
		series = new Series<>();
		if (examinationTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have not selected any analysis!");
			alert.showAndWait();
		} else {
			isAllDataFromSeriesVisible(true);
			saveGraph.setDisable(false);
			distanceOnXAxis = getDistance(examinationTable.getSelectionModel().getSelectedItem().getFrequency());
			series.setName(examinationTable.getSelectionModel().getSelectedItem().getSensorName());
			int objectId = examinationTable.getSelectionModel().getSelectedItem().getObjectId();
			String sensorName = examinationTable.getSelectionModel().getSelectedItem().getSensorName();
			rawData= view.viewSensorData(objectId, sensorName,
					examinationTable.getSelectionModel().getSelectedItem().getChannelNumber());
			for (int i = 0; i < rawData.size(); i++) {
				series.getData().add(new Data<Number, Number>((double)i/examinationTable.getSelectionModel().getSelectedItem().getFrequency(), rawData.get(i)));
			}
			analysisChart.getData().add(series);
			currentPositionOnXAxis = 0;
			xAxis.setUpperBound(currentPositionOnXAxis + distanceOnXAxis);
			xAxis.setLowerBound(currentPositionOnXAxis);
			xAxis.setTickUnit(distanceOnXAxis/4);
			backButton.setVisible(true);
			forwardButton.setVisible(true);
			sliderForChart.setVisible(true);
			sliderForChart.setMin(0);
			sliderForChart.setMax(series.getData().size()/examinationTable.getSelectionModel().getSelectedItem().getFrequency() - distanceOnXAxis + 2);
			sliderForChart.setValue(0);
			sliderForChart.setVisible(true);
			sliderForChart.valueProperty().addListener(new InvalidationListener() {

				@Override
				public void invalidated(Observable observable) {

					xAxis.setLowerBound(sliderForChart.getValue());
					xAxis.setUpperBound(sliderForChart.getValue() + distanceOnXAxis);

				}
			});
		}
		yAxis.setAutoRanging(false);

		BitalinoData bitData = new BitalinoData();
		BitalinoDataBounds bounds = bitData
				.calculateBounds(examinationTable.getSelectionModel().getSelectedItem().getSensorName());
		yAxis.setLowerBound(bounds.getyLowerBound());
		yAxis.setUpperBound(bounds.getyUpperBound());
		yAxis.setTickUnit(BitalinoData.calculateTickUnit(yAxis.getUpperBound(), yAxis.getLowerBound()));
		xAxis.setTickUnit(BitalinoData.calculateTickUnit(xAxis.getUpperBound(), xAxis.getLowerBound()));
		
		if(examinationTable.getSelectionModel().getSelectedItem().getSensorName().equalsIgnoreCase("ECG") || examinationTable.getSelectionModel().getSelectedItem().getSensorName().equalsIgnoreCase("PLS"))
		{
			
			beatsPerMinute.setText("BPM: "+Double.toString(BitalinoData.calculateBPM(rawData,((double)rawData.size()/examinationTable.getSelectionModel().getSelectedItem().getFrequency()))));			
			beatsPerMinute.setVisible(true);
		}
	}

	@FXML
	public void saveChanges(ActionEvent event) {
		try {
			currentExamination.setComments(comments.getText());
			currentExamination.setDiagnostic(diagnostic.getText());
			updateModel.updateExamination(currentExamination);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Examination Updated Succesfully!");
			alert.showAndWait();
			// ((Node) (event.getSource())).getScene().getWindow().hide();
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/Consultation.fxml")
							.openStream());
			ConsultationController pageController = (ConsultationController) loader.getController();
			pageController.setCurrentPatient(patient, doctor);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.setMinHeight(root.getPrefHeight());
			primaryStage.setMinWidth(root.getPrefWidth());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void goForward(ActionEvent e) {
		sliderForChart.setValue(sliderForChart.getValue() + 5);
		xAxis.setUpperBound(sliderForChart.getValue() + distanceOnXAxis);
		xAxis.setLowerBound(sliderForChart.getValue());

	}

	@FXML
	private void goBack(ActionEvent e) {
		sliderForChart.setValue(sliderForChart.getValue() - 5);
		xAxis.setUpperBound(sliderForChart.getValue() + distanceOnXAxis);
		xAxis.setLowerBound(sliderForChart.getValue());
	}

	private int getDistance(int freq) {
		if (freq == 1000) {
			return 1;
		}
		return 10;
	}

	public void addNewAnalysisToOldConsultation(ActionEvent event) {

		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			root = loader.load(getClass()
					.getResource(
							"/ro/cerner/internship/jemr/ui/desktop/viewcontroller/NewAnalysisToOldConsultation.fxml")
					.openStream());
			NewAnalysisToOldConsultationController pageController = (NewAnalysisToOldConsultationController) loader
					.getController();
			pageController.setCurrentExamination(currentExamination, patient, doctor);
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
	private void isAllDataFromSeriesVisible(boolean state) {
		xAxis.setTickLabelsVisible(state);
		yAxis.setTickLabelsVisible(state);
		if(state)
		{
			xAxis.setLabel(" Time[ms]");
			yAxis.setLabel(BitalinoData.getUnit(examinationTable.getSelectionModel().getSelectedItem().getSensorName()));
			analysisChart.setLegendVisible(true);
		}
		else
		{
			xAxis.setLabel("");
			yAxis.setLabel("");
			analysisChart.setLegendVisible(false);
		}
	}

}
