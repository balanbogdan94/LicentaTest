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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
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
import ro.cerner.internship.jemr.persistence.api.entity.Channel;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Frequency;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.entity.Sensor;
import ro.cerner.internship.jemr.persistence.api.entity.SensorType;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class NewConsultationController implements Initializable {
	private static int MAX_DATA_POINTS;
	Doctor doctor;
	Patient patient;
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
	private ComboBox<Frequency> frequncyComboBox;
	@FXML
	private ComboBox<SensorType> typeOfAnalissis;
	@FXML
	private ComboBox<Channel> chanelComboBox;

	private Series<Number, Number> series1;

	private int xSeriesData = 0;
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
	@FXML
	private Label patientName;
	@FXML
	private Button print;

	private Frequency frequencyAux;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private LocalDateTime startDate;
	private LocalDateTime stopDate;

	private ConcurrentLinkedQueue<Number> valueStore = new ConcurrentLinkedQueue<>();
	private Configuration configuration = SpringApplicationContext.instance().getBean("ArduinoConfiguration",
			Configuration.class);
	private DataReader reader;
	private ViewModel view = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);
	private List<Frequency> freq = new ArrayList<>();
	private List<Sensor> sensor = new ArrayList<>();
	private ObservableList<String> analysisMade = FXCollections.observableArrayList();
	private ArrayList<Short> rawData1 = new ArrayList<>();

	private int maxChar = 2000;

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
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning Dialog");
				alert.setHeaderText(null);
				alert.setContentText("BITAlino is not connected!");
				alert.show();
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
				((Node) ev.getSource()).getScene().getWindow().hide();
			} catch (IOException e) {
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
			rawData1.add((Short) valueStore.remove());
			series1.getData()
					.add(new Data<Number, Number>(
							(double) xSeriesData / Integer.parseInt(frequncyComboBox.getValue().getFrequency()),
							BitalinoData.relevantDataValue(rawData1.get(rawData1.size() - 1),
									typeOfAnalissis.getValue().toString(), chanelComboBox.getValue().getChannelNumber())));

			xSeriesData++;
		}

		if (series1.getData().size() > MAX_DATA_POINTS * Integer.parseInt(frequncyComboBox.getValue().getFrequency())) {
			series1.getData().remove(0, series1.getData().size()
					- MAX_DATA_POINTS * Integer.parseInt(frequncyComboBox.getValue().getFrequency()));

		}
		xAxis.setLowerBound(
				(double) xSeriesData / Integer.parseInt(frequncyComboBox.getValue().getFrequency()) - MAX_DATA_POINTS);
		xAxis.setUpperBound((double) xSeriesData / Integer.parseInt(frequncyComboBox.getValue().getFrequency()) - 0.1);
	}

	private <T> void cancelTheConsultation(T event) {
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
				((Node) ((EventObject) event).getSource()).getScene().getWindow().hide();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void cancelTheConsultationOnButton(ActionEvent event) {
		cancelTheConsultation(event);
	}

	public <T> void cancelTheConsultationOnX(T event) {
		cancelTheConsultation(event);
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
		series1.getData().clear();
		rawData1.clear();
		xSeriesData = 0;
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(MAX_DATA_POINTS);
	}

	public void getCurrentPatient(Patient patient) {
		this.patient = patient;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		isAllDataFromChartVisible(false);
		newDoctorComments.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= maxChar ? change : null));
		newDiagnostic.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= maxChar ? change : null));
		chanelComboBox.getItems().addAll(view.viewListOfChannels());
		dateAndTime.setText(LocalDateTime.now().format(formatter).toString());
		freq = view.viewListOfFrequency();
		frequncyComboBox.getItems().addAll(freq);
		typeOfAnalissis.getItems().addAll(SensorType.values());
		xAxis.setAutoRanging(false);
		yAxis.setAutoRanging(false);
		lineChart.setAnimated(false);
		lineChart.setCreateSymbols(false);
		series1 = new XYChart.Series<Number, Number>();
		lineChart.getData().add(series1);
		saveButton.setDisable(true);
		dropButton.setDisable(true);
		stopButton.setDisable(true);
		addExaminationButton.setDisable(true);
	}

	private void isAllDataFromChartVisible(boolean state) {
		xAxis.setTickLabelsVisible(state);
		yAxis.setTickLabelsVisible(state);
		chanelComboBox.setDisable(state);
		frequncyComboBox.setDisable(state);
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
		Analysis analysis1 = new Analysis(idExamination, ByteConverter.rawDataToBinary(rawData1),
				1, frequencyAux.getIdObject(), startDate, stopDate,
				chanelComboBox.getValue().getIdObject());
		model.addAnalysis(analysis1);
		analysisMade.add(typeOfAnalissis.getValue().toString() + " " + frequncyComboBox.getValue().getFrequency());

		doneAnalysis.setItems(analysisMade);
		saveButton.setDisable(true);
		addExaminationButton.setDisable(false);
		rawData1.clear();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("You have added new analysis");
		alert.showAndWait();
		dropDataFromChart(event);
	}

	public void setAnalysis(Patient selectedPatient, Doctor currentDoctor, int id) {
		patient = selectedPatient;
		doctor = currentDoctor;
		idExamination = id;
		patientName.setText(patient.getFirstName() + " " + patient.getLastName());
	}

	public void startButton(ActionEvent e)
	{
		if (frequncyComboBox.getValue() != null && typeOfAnalissis.getValue() != null
				&& chanelComboBox.getValue() != null) {
			isAllDataFromChartVisible(true);
			startDate = LocalDateTime.now();
			startDate.format(formatter);
			frequencyAux = frequncyComboBox.getValue();
			series1.setName(typeOfAnalissis.getValue().toString());
			reader = SpringApplicationContext.instance().getBean("DataReader", DataReader.class);
			int chanel = chanelComboBox.getValue().getChannelNumber();
			int frequency = Integer.parseInt(frequencyAux.getFrequency());
			String listNumeSenzori = typeOfAnalissis.getValue().toString();
			int listaCanale = chanel;
			configuration.setSensorType(listNumeSenzori);
			configuration.setFrequency(frequency);
			configuration.setChannel(listaCanale);
			reader.setValueStore(valueStore);
			reader.setConfiguration(configuration);
			reader.start();
			prepareTimeline();
			stopButton.setDisable(false);
			startButton.setDisable(true);
		} 
		else 
		{
			stopButton.setDisable(true);
			startButton.setDisable(false);
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Select Sensor / Channel / Frequency!");
			alert.showAndWait();
		}
	}

	public void stopButton(ActionEvent e) {
		try 
		{
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

}
