package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.cerner.internship.jemr.core.bitdata.BitalinoData;
import ro.cerner.internship.jemr.core.bitdata.BitalinoDataBounds;
import ro.cerner.internship.jemr.core.bitdata.ByteConverter;
import ro.cerner.internship.jemr.core.bitdata.DataReader;
import ro.cerner.internship.jemr.core.create.CreateModel;
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
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class NewAnalysisToOldConsultationController implements Initializable {

	private static int MAX_DATA_POINTS;
	@FXML
	Button backButton;
	@FXML
	private ComboBox<Channel> chanelComboBox;
	@FXML
	private ComboBox<Frequency> frequncyComboBox;
	@FXML
	private ComboBox<Sensor> typeOfAnalissis;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	@FXML
	private Button saveButton;
	@FXML
	private Button stopButton;
	@FXML
	private Button startButton;
	@FXML
	private Button dropButton;
	@FXML
	private ListView<String> doneAnalysis;
	@FXML
	private LineChart<Number, Number> lineChart;

	private int xSeriesData = 0;
	private ViewModel view = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);
	private List<Frequency> freq = new ArrayList<>();
	private List<Sensor> sensor = new ArrayList<>();
	private Series<Number, Number> series1;
	private LocalDateTime startDate;
	private LocalDateTime stopDate;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private ConcurrentLinkedQueue<Number> valueStore = new ConcurrentLinkedQueue<>();
	private Configuration configuration = SpringApplicationContext.instance().getBean("ArduinoConfiguration",
			Configuration.class);
	private DataReader reader;
	private Sensor sensorType;
	private Frequency frequencyAux;
	private ObservableList<String> analysisMade = FXCollections.observableArrayList();
	private ArrayList<Short> rawData1 = new ArrayList<>();

	Patient patient;
	Doctor doctor;
	Examination examination;

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
				// JOptionPane.showMessageDialog(null,
				// "BITalino is not Connected",
				// "BITalino warning",
				// JOptionPane.WARNING_MESSAGE);
			}
		}
	};

	public void addDataToSeries() {

		BitalinoData bitData = new BitalinoData();
		BitalinoDataBounds bounds = bitData.calculateBounds(sensorType.getSensorName());
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
									sensorType.getSensorName(), chanelComboBox.getValue().getChannelNumber())));

			xSeriesData++;
		}
		/*
		 * series1.getData().addAll(auxSeries1.getData()); if
		 * (chanelComboBox1.isVisible() == true) {
		 * series2.getData().addAll(auxSeries2.getData()); }
		 */

		if (series1.getData().size() > MAX_DATA_POINTS * Integer.parseInt(frequncyComboBox.getValue().getFrequency())) {
			series1.getData().remove(0, series1.getData().size()
					- MAX_DATA_POINTS * Integer.parseInt(frequncyComboBox.getValue().getFrequency()));
		}

		xAxis.setLowerBound(
				(double) xSeriesData / Integer.parseInt(frequncyComboBox.getValue().getFrequency()) - MAX_DATA_POINTS);
		xAxis.setUpperBound((double) xSeriesData / Integer.parseInt(frequncyComboBox.getValue().getFrequency()) - 0.1);

	}

	public void backToOldConsultaions(ActionEvent event) {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Close Confirmation");
			alert.setHeaderText("Are you sure you want to go back?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				if (reader != null && reader.isAlive()) {
					stopButton(null);
				}
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Pane root;
				root = loader.load(getClass()
						.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/ViewOldConsultation.fxml")
						.openStream());

				ViewOldConsultationController controler = (ViewOldConsultationController) loader.getController();
				controler.setCurrentExamination(examination, patient, doctor);
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
			}
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void dropDataFromChart(ActionEvent event) {
		dropDataFromSeries();
		reader = null;
		startButton.setDisable(false);
		saveButton.setDisable(true);
		dropButton.setDisable(true);
	}

	private void dropDataFromSeries() {
		series1.getData().clear();
		isAllDataFromChartVisible(false);
		rawData1.clear();
		xSeriesData = 0;
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(MAX_DATA_POINTS);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chanelComboBox.getItems().addAll(view.viewListOfChannels());
		freq = view.viewListOfFrequency();
		sensor = view.viewListOfSensors();
		frequncyComboBox.getItems().addAll(freq);
		typeOfAnalissis.getItems().addAll(sensor);
		xAxis.setAutoRanging(false);
		yAxis.setAutoRanging(false);
		lineChart.setAnimated(false);
		lineChart.setCreateSymbols(false);
		series1 = new XYChart.Series<Number, Number>();
		isAllDataFromChartVisible(false);
		lineChart.getData().add(series1);
		saveButton.setDisable(true);
		dropButton.setDisable(true);
		stopButton.setDisable(true);
	}

	private void isAllDataFromChartVisible(boolean state) {
		xAxis.setTickLabelsVisible(state);
		yAxis.setTickLabelsVisible(state);
		chanelComboBox.setDisable(state);
		frequncyComboBox.setDisable(state);
		typeOfAnalissis.setDisable(state);
		if (state) {
			xAxis.setLabel(" Time[s]");
			yAxis.setLabel(BitalinoData.getUnit(typeOfAnalissis.getValue().getSensorName()));
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
		UpdateModel updateModel = SpringApplicationContext.instance().getBean("UpdateModel", UpdateModel.class);
		Analysis analysis1 = new Analysis(examination.getObjectID(), ByteConverter.rawDataToBinary(rawData1),
				sensorType.getObjectId(), frequencyAux.getIdObject(), startDate, stopDate,
				chanelComboBox.getValue().getIdObject());
		model.addAnalysis(analysis1);
		updateModel.updateExamination(examination);
		analysisMade.add(typeOfAnalissis.getValue().getSensorName() + " " + frequncyComboBox.getValue().getFrequency());
		doneAnalysis.setItems(analysisMade);
		saveButton.setDisable(true);
		rawData1.clear();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("You have added new analysis");
		alert.showAndWait();
		dropDataFromChart(event);
	}

	public void setCurrentExamination(Examination examination, Patient patient, Doctor doctor) {
		this.patient = patient;
		this.doctor = doctor;
		this.examination = examination;
	}

	public void startButton(ActionEvent e) {
	
		if (frequncyComboBox.getValue() != null && typeOfAnalissis.getValue() != null
				&& chanelComboBox.getValue() != null) {
				isAllDataFromChartVisible(true);
				startDate = LocalDateTime.now();
				startDate.format(formatter);
				frequencyAux = frequncyComboBox.getValue();
				sensorType = typeOfAnalissis.getValue();
				series1.setName(sensorType.getSensorName());
				reader = SpringApplicationContext.instance().getBean("DataReader", DataReader.class);
				int chanel = chanelComboBox.getValue().getChannelNumber();
				int frequency = Integer.parseInt(frequencyAux.getFrequency());
				configuration.setSensorType(sensorType.getSensorName());
				configuration.setFrequency(frequency);
				configuration.setChannel(chanel);
				reader.setValueStore(valueStore);
				reader.setConfiguration(configuration);
				reader.start();
				prepareTimeline();
				stopButton.setDisable(false);
				startButton.setDisable(true);
			
		} else {
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
		try {
			stopDate = LocalDateTime.now();
			stopDate.format(formatter);
			reader.setRunning(false);
			saveButton.setDisable(false);
			dropButton.setDisable(false);
			stopButton.setDisable(true);
			timer.stop();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		yAxis.setTickUnit(BitalinoData.calculateTickUnit(yAxis.getUpperBound(), yAxis.getLowerBound()));
		xAxis.setTickUnit(BitalinoData.calculateTickUnit(xAxis.getUpperBound(), xAxis.getLowerBound()));
	}
}
