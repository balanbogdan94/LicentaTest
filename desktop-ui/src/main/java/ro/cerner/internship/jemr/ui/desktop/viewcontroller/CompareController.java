package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.cerner.internship.jemr.core.bitdata.BitalinoData;
import ro.cerner.internship.jemr.core.bitdata.BitalinoDataBounds;
import ro.cerner.internship.jemr.core.view.ViewModel;
import ro.cerner.internship.jemr.persistence.api.entity.Analysis;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.entity.Sensor;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class CompareController implements Initializable {
	@FXML
	private Label doctorName;
	@FXML
	private Label firstName;
	@FXML
	private Label lastName;
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
	ComboBox<Sensor> analysisType;
	@FXML
	TableView<Analysis> analysisTable;
	@FXML
	TableColumn<Analysis, String> freqencyCol;
	@FXML
	TableColumn<Analysis, String> consultationDateCol;
	@FXML
	TableColumn<Analysis, String> diagnosticCol;
	@FXML
	LineChart<Number, Number> lineChart;
	@FXML
	NumberAxis xAxis;
	@FXML
	NumberAxis yAxis;
	@FXML
	Slider sliderForChart;
	@FXML
	Button compareButton;
	@FXML
	private Button saveGraph;
	@FXML
	TableColumn<Analysis, Integer> idForTable;

	Series<Number, Number> series;
	List<Sensor> sensors;
	private Doctor doctor;
	private Patient patient;
	private ViewModel view = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);
	private int currentPositionOnXAxis;
	private int distanceOnXAxis;
	ObservableList<Analysis> selectedAnalysis;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		DoubleBinding bind = analysisTable.widthProperty().divide(4);
		freqencyCol.prefWidthProperty().bind(bind);
		consultationDateCol.prefWidthProperty().bind(bind);
		diagnosticCol.prefWidthProperty().bind(bind);
		compareButton.setDisable(true);
		isAllDataFromSeriesVisible(false);
		saveGraph.setDisable(true);
		xAxis.setAutoRanging(false);
		lineChart.setCreateSymbols(false);
		sensors = view.viewListOfSensors();
		analysisType.getItems().addAll(sensors);
		freqencyCol.setCellValueFactory(new PropertyValueFactory<Analysis, String>("frequency"));
		consultationDateCol.setCellValueFactory(new PropertyValueFactory<Analysis, String>("startDateFormated"));
		diagnosticCol.setCellValueFactory(new PropertyValueFactory<Analysis, String>("consultationDiagnostic"));
		idForTable.setCellValueFactory(new PropertyValueFactory<Analysis, Integer>("IdForTable"));
		analysisTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		analysisTable.setRowFactory(tv -> {
			TableRow<Analysis> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (analysisTable.getSelectionModel().getSelectedItems().size() >= 2 && (!row.isEmpty())) {
					compareButton.setDisable(false);
				} else {
					compareButton.setDisable(true);
					saveGraph.setDisable(true);
				}

			});

			return row;

		});
	}

	public void setDoctorAndPatient(Doctor doctor, Patient patient) {
		this.doctor = doctor;
		doctorName.setText(this.doctor.getFirstName() + " " + this.doctor.getLastName());
		this.patient = patient;
		this.firstName.setText(patient.getFirstName());
		this.lastName.setText(patient.getLastName());
		this.cnp.setText(patient.getCnp());
		this.gender.setText(patient.getGender());
		this.dateOfBirth.setText(patient.getDateOfBirth().toString());
		this.phoneNumber.setText(patient.getPhoneNumber());
		this.bloodType.setText(patient.getBloodType() + " " + patient.getRH());
	}

	public void searchOfAnalysis(ActionEvent event) {
		if (analysisType.getValue() == null) {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have not selected any type of analysis!");
			alert.showAndWait();

		} else {
			analysisTable.getItems().clear();
			ViewModel view = SpringApplicationContext.instance().getBean("ViewModel", ViewModel.class);
			List<Analysis> analysis = view.viewListOfAllAnalysis(patient.getObjectID(),
					analysisType.getValue().getObjectId());
			analysisTable.getItems().addAll(analysis);
			for (int i = 0; i < analysis.size(); i++) {
				analysisTable.getItems().get(i).setIdForTable(i);
			}

		}
	}

	@FXML
	public void backButton(ActionEvent event) {
		try {
			analysisTable.getItems().clear();
			for (Series<Number, Number> series : lineChart.getData()) {
				series.getData().clear();
			}
			lineChart.getData().clear();
			series = null;
			lineChart = null;
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
			primaryStage.show();
			((Node) event.getSource()).getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void saveGraphic(ActionEvent event) {
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root;
		try {
			root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/CompareWindow.fxml")
							.openStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CompareController pageController = (CompareController) loader.getController();
		xAxis.setUpperBound(series.getData().size()/calculateMaxFreq(selectedAnalysis));
		xAxis.setLowerBound(0);
		xAxis.setTickLabelsVisible(false);
		lineChart.setUserData(lineChart.getData().addAll(series));
		WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);
		compareAnalysis(event);
		// TODO: probably use a file chooser here
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH~mm");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName(patient.getFirstName() + "_" + patient.getLastName() + "_"
				+ analysisTable.getSelectionModel().getSelectedItem().getStartDate().format(formatter) + "_"
				+ analysisTable.getSelectionModel().getSelectedItem().getSensorName());
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Open Resource File");
		// File file = new File("chart.png");

		File file = fileChooser.showSaveDialog(primaryStage);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			// TODO: handle exception here
		}
	}

	public void compareAnalysis(ActionEvent event) {
		int maxSeriesSize = 0;
		lineChart.getData().clear();
		isAllDataFromSeriesVisible(true);
		if (analysisTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("You have not selected any analysis!");
			alert.showAndWait();
		} else {
			saveGraph.setDisable(false);
			selectedAnalysis = analysisTable.getSelectionModel().getSelectedItems();
			int maxFreq = calculateMaxFreq(selectedAnalysis);
			distanceOnXAxis = 10;
			int thicks = 0;
			for (Analysis currentAnalysis : selectedAnalysis) {
				series = new Series<>();
				List<Double> rawData = view.viewSensorData(currentAnalysis.getObjectId(),
						currentAnalysis.getSensorName(), currentAnalysis.getChannelNumber());
				int tickMultiplaier = calculateTheTick(currentAnalysis.getFrequency(), maxFreq);

				for (int i = 0; i < rawData.size(); i++) {
					thicks = i * tickMultiplaier;
					series.getData().add(new Data<Number, Number>((double) thicks / maxFreq, rawData.get(i)));
				}
				series.setName(currentAnalysis.getIdForTable() + ". " + currentAnalysis.getSensorName() + "["
						+ currentAnalysis.getFrequency() + "] ");
				lineChart.getData().add(series);
				if (maxSeriesSize < thicks) {
					maxSeriesSize = thicks;
				}

			}
			xAxis.setUpperBound(currentPositionOnXAxis + distanceOnXAxis);
			xAxis.setLowerBound(currentPositionOnXAxis);
			sliderForChart.setVisible(true);
			sliderForChart.setMin(0);
			sliderForChart.setMax(maxSeriesSize / maxFreq - distanceOnXAxis + 2);
			sliderForChart.setValue(0);
			sliderForChart.setVisible(true);
			sliderForChart.valueProperty().addListener(new InvalidationListener() {

				@Override
				public void invalidated(Observable observable) {

					xAxis.setLowerBound(sliderForChart.getValue());
					xAxis.setUpperBound(sliderForChart.getValue() + distanceOnXAxis);

				}
			});

			yAxis.setAutoRanging(false);
			BitalinoData bitData = new BitalinoData();
			BitalinoDataBounds bounds = bitData
					.calculateBounds(analysisTable.getSelectionModel().getSelectedItem().getSensorName());
			yAxis.setLowerBound(bounds.getyLowerBound());
			yAxis.setUpperBound(bounds.getyUpperBound());
			yAxis.setTickUnit(BitalinoData.calculateTickUnit(yAxis.getUpperBound(), yAxis.getLowerBound()));
			xAxis.setTickUnit(BitalinoData.calculateTickUnit(xAxis.getUpperBound(), xAxis.getLowerBound()));
		}
	}

	private int calculateTheTick(int currentFreq, int maxFreq) {
		int tick = maxFreq / currentFreq;
		return tick;
	}

	private int calculateMaxFreq(ObservableList<Analysis> selectedAnalysis) {
		int maxFreq = 0;
		for (Analysis currentAnalysis : selectedAnalysis) {
			if (currentAnalysis.getFrequency() > maxFreq) {
				maxFreq = currentAnalysis.getFrequency();
			}
		}

		return maxFreq;
	}

	private void isAllDataFromSeriesVisible(boolean state) {
		xAxis.setTickLabelsVisible(state);
		yAxis.setTickLabelsVisible(state);
		if (state) {
			xAxis.setLabel(" Time[ms]");
			yAxis.setLabel(BitalinoData.getUnit(analysisTable.getSelectionModel().getSelectedItem().getSensorName()));
			lineChart.setLegendVisible(true);
		} else {
			xAxis.setLabel("");
			yAxis.setLabel("");
			lineChart.setLegendVisible(false);
		}
	}

}
