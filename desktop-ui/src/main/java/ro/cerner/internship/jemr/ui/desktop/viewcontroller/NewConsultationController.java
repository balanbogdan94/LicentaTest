package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.core.bitdata.BitalinoData;
import ro.cerner.internship.jemr.core.bitdata.BitalinoDataBounds;
import ro.cerner.internship.jemr.core.bitdata.ByteConverter;
import ro.cerner.internship.jemr.core.bitdata.DataReader;
import ro.cerner.internship.jemr.core.create.CreateModel;
import ro.cerner.internship.jemr.core.delete.DeleteModel;
import ro.cerner.internship.jemr.core.update.UpdateModel;
import ro.cerner.internship.jemr.core.view.ViewModel;
import ro.cerner.internship.jemr.dataacquisition.api.Configuration;
import ro.cerner.internship.jemr.persistence.api.entity.Analysis;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.entity.Sensor;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class NewConsultationController extends BaseDoctorController implements Initializable {
	private int MAX_DATA_POINTS;
	int idExamination;
	@FXML
	private TextArea newDoctorComments;
	@FXML
	private TextArea newDiagnostic;
	@FXML
	private Button saveButton;
	@FXML
	private Button stopButton;
	@FXML
	private Button startButton;
	@FXML
	private Button dropButton;
	@FXML
	private Label dateAndTime;
	@FXML
	private ComboBox<Sensor> typeOfAnalissis;
	public ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
	@FXML
	private NumberAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	@FXML
	private LineChart<Number, Number> lineChart;
	@FXML
	private ListView<String> doneAnalysis;
	@FXML
	private Button addExaminationButton;
	private Series<Number,Number> series;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private LocalDateTime startDate;
	private LocalDateTime stopDate;
	private List<Short> rawData=new ArrayList<>();
	private ConcurrentLinkedQueue<Number> valueStore = new ConcurrentLinkedQueue<>();
	private Configuration configuration = SpringApplicationContext.instance().getBean("ArduinoConfiguration",
			Configuration.class);
	private DataReader reader;
	private ObservableList<String> analysisMade = FXCollections.observableArrayList();
	private int maxChar = 2000;
	private int xSeriesData=0;

	AnimationTimer timer = new AnimationTimer() {
		@Override
		public void handle(long now) {
			addDataToSeries();
			if (reader.isAlive() == false && stopButton.isPressed() == false) {
				timer.stop();
				reader.setRunning(false);
				dropDataFromSeries();
				stopButton.setDisable(true);
				startButton.setDisable(false);
				saveButton.setDisable(true);
				dropButton.setDisable(true);
				System.out.println("Problema in timmer");
			}
		}
	};

	@FXML
	private void addCommentsAndDiagnostic(ActionEvent ev) {
		if (newDiagnostic.getText().trim().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Please add diagnostic");
			alert.showAndWait();
		} else {

			Examination examination = new Examination(idExamination, newDiagnostic.getText(),
					newDoctorComments.getText());
			UpdateModel model = SpringApplicationContext.instance().getBean("UpdateModel", UpdateModel.class);
			model.updateExamination(examination);
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have added a consultation");
			alert.showAndWait();
			try {
				
				FXMLLoader loader = new FXMLLoader();
				Pane root=loader.load(
						getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/Consultation.fxml")
								.openStream());
				ConsultationController pageController = (ConsultationController) loader.getController();
				pageController.setPatientAndDoctor(getSelectedPatient(), getCurrentDoctor());
				dropButton.getScene().setRoot(root);
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	private void addDataToSeries() {
		BitalinoData bitData = new BitalinoData();
		BitalinoDataBounds bounds = bitData.calculateBounds(typeOfAnalissis.getValue().toString());
		yAxis.setLowerBound(bounds.getyLowerBound());
		yAxis.setUpperBound(bounds.getyUpperBound());
		yAxis.setTickUnit((yAxis.getUpperBound() - yAxis.getLowerBound()) / 4);

		MAX_DATA_POINTS = 5;

		xAxis.setTickUnit(MAX_DATA_POINTS / 4);

		int counter = valueStore.size();

		for (int i = 0; i < counter; i++) {
			rawData.add((Short) valueStore.remove());
			series.getData()
					.add(new Data<Number, Number>(
							(double) xSeriesData / typeOfAnalissis.getValue().getFrequency(),
							BitalinoData.relevantDataValue(rawData.get(rawData.size() - 1),
									typeOfAnalissis.getValue().toString(), typeOfAnalissis.getValue().getChannel())));
			xSeriesData++;
		}

		if (series.getData().size() > MAX_DATA_POINTS * typeOfAnalissis.getValue().getFrequency()) {
			series.getData().remove(0, series.getData().size()
					- MAX_DATA_POINTS * typeOfAnalissis.getValue().getFrequency());

		}
		xAxis.setLowerBound(
				(double) xSeriesData / typeOfAnalissis.getValue().getFrequency() - MAX_DATA_POINTS);
		xAxis.setUpperBound((double) xSeriesData / typeOfAnalissis.getValue().getFrequency() - 0.1);
	}

	public void cancelTheConsultation(MouseEvent event) {
		DeleteModel model = SpringApplicationContext.instance().getBean("DeleteModel", DeleteModel.class);
		Alert alert1 = new Alert(AlertType.CONFIRMATION);
		alert1.setTitle("Confirmation Dialog");
		alert1.setHeaderText("Confirm action");
		alert1.setContentText("Do you really want to delete this consultation?");

		Optional<ButtonType> result = alert1.showAndWait();
		if (result.get() == ButtonType.OK) {
			timer.stop();
			model.deleteExamination(idExamination);
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have deleted a consultation");
			alert.showAndWait();
			if (reader != null && reader.isAlive()) {
				stopButton(null);
			}
			try {
				
				FXMLLoader loader = new FXMLLoader();
				Pane root = loader.load(
						getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/Consultation.fxml")
								.openStream());
				root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				ConsultationController pageController = (ConsultationController) loader.getController();
				dateAndTime.getScene().setRoot(root);
				pageController.setPatientAndDoctor(getSelectedPatient(), getCurrentDoctor());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void dropDataFromChart(ActionEvent event) {
		dropDataFromSeries();
		isAllDataFromChartVisible(false);
		reader = null;
		startButton.setDisable(false);
		saveButton.setDisable(true);
		dropButton.setDisable(true);
	}

	private void dropDataFromSeries() {
		series.getData().clear();
		rawData.clear();
		xSeriesData = 0;
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(MAX_DATA_POINTS);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		isAllDataFromChartVisible(false);
		newDoctorComments.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= maxChar ? change : null));
		newDiagnostic.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= maxChar ? change : null));
		dateAndTime.setText(LocalDateTime.now().format(formatter).toString());
		typeOfAnalissis.getItems().addAll(viewModel.viewListOfSensors());
		xAxis.setAutoRanging(false);
		yAxis.setAutoRanging(false);
		lineChart.setAnimated(false);
		lineChart.setCreateSymbols(false);
		series = new XYChart.Series<Number, Number>();
		lineChart.getData().add(series);
		saveButton.setDisable(true);
		dropButton.setDisable(true);
		stopButton.setDisable(true);
		addExaminationButton.setDisable(true);
	}

	private void isAllDataFromChartVisible(boolean state) {
		xAxis.setTickLabelsVisible(state);
		yAxis.setTickLabelsVisible(state);
		typeOfAnalissis.setDisable(state);

		if (state) {
			xAxis.setLabel(" Time[ms]");
			yAxis.setLabel(BitalinoData.getUnit(typeOfAnalissis.getValue().toString()));
			lineChart.setLegendVisible(true);
		} else {
			xAxis.setLabel("");
			yAxis.setLabel("");
			lineChart.setLegendVisible(false);
		}

	}

	private void prepareTimeline() {
		timer.start();
	}

	@FXML
	public void saveDataFromChart(ActionEvent event) {
		CreateModel model = SpringApplicationContext.instance().getBean("CreateModel", CreateModel.class);
		Analysis analysis1 = new Analysis(idExamination, ByteConverter.rawDataToBinary(rawData),
				typeOfAnalissis.getValue(), startDate, stopDate);
		model.addAnalysis(analysis1);
		analysisMade.add(typeOfAnalissis.getValue().toString());

		doneAnalysis.setItems(analysisMade);
		saveButton.setDisable(true);
		addExaminationButton.setDisable(false);
		rawData.clear();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("You have added new analysis");
		alert.showAndWait();
		dropDataFromChart(event);
	}

	public void startButton(ActionEvent e)
	{
		if (typeOfAnalissis.getValue() != null)
		{
			isAllDataFromChartVisible(true);
			startDate = LocalDateTime.now();
			startDate.format(formatter);
			series.setName(typeOfAnalissis.getValue().toString());
			reader = SpringApplicationContext.instance().getBean("DataReader", DataReader.class);
			configuration.setSensorType(typeOfAnalissis.getValue().toString());
			configuration.setFrequency(typeOfAnalissis.getValue().getFrequency());
			reader.setValueStore(valueStore);
			reader.setConfiguration(configuration);
			reader.start();
			prepareTimeline();
			stopButton.setDisable(false);
			startButton.setDisable(true);
		} 
		else 
		{
			patientNotSelectedAlert();
		}
	}

	public void stopButton(ActionEvent e) {
		try 
		{
			System.out.println("Seria: "+series.getData().size());
			stopDate = LocalDateTime.now();
			stopDate.format(formatter);
			reader.setRunning(false);
			saveButton.setDisable(false);
			dropButton.setDisable(false);
			stopButton.setDisable(true);
			timer.stop();
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
	}

	public void setAnalysis(Patient selectedPatient, Doctor currentDoctor, int id) {
		setCurrentDoctor(currentDoctor);
		setSelectedPatient(selectedPatient);
		idExamination=id;
		
	}
	
	

}
