package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
	private Label patientName;
	@FXML
	private Label patientDOB;
	@FXML
	private Label patientAge;
	@FXML
	private Label patientRH;
	@FXML
	private Label patientBloodType;
	@FXML
	private ImageView femaleSign;
	@FXML
	private ImageView maleSign;
	@FXML
	ComboBox<Sensor> analysisType;
	@FXML
	TableView<Analysis> analysisTable;
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
		compareButton.setDisable(true);
		isAllDataFromSeriesVisible(false);
		saveGraph.setDisable(true);
		xAxis.setAutoRanging(false);
		lineChart.setCreateSymbols(false);
		sensors = view.viewListOfSensors();
		analysisType.getItems().addAll(sensors);
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
		this.patientName.setText(patient.getFirstName() + " " + patient.getLastName());
		this.patientDOB.setText(patient.getDateOfBirth().toString());
		this.patientBloodType.setText(patient.getBloodType());
		this.patientRH.setText(patient.getRH());
		this.patientAge.setText(String.valueOf(view.patientAge(patient.getDateOfBirth())) + " years");
		this.femaleSign.setVisible(patient.getGender().equals("F"));
		this.maleSign.setVisible(patient.getGender().equals("M"));

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
	public void backButton(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/Consultation.fxml")
							.openStream());
			root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			ConsultationController pageController = (ConsultationController) loader.getController();
			pageController.setCurrentPatient(patient, doctor);
			analysisTable.getScene().setRoot(root);
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
		xAxis.setUpperBound(series.getData().size()/analysisTable.getSelectionModel().getSelectedItem().getSensor().getFrequency());
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
				+ analysisTable.getSelectionModel().getSelectedItem().getSensor().getSensorName());
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
			distanceOnXAxis = 10;
			for (Analysis currentAnalysis : selectedAnalysis) {
				series = new Series<>();
				List<Double> rawData = view.viewSensorData(currentAnalysis.getObjectId(),
						currentAnalysis.getSensor().getSensorName(), currentAnalysis.getSensor().getChannel());
				for (int i = 0; i < rawData.size(); i++) {
					series.getData().add(new Data<Number, Number>
					((double)i/analysisTable.getSelectionModel().getSelectedItem().getSensor().getFrequency(), rawData.get(i)));
				}
				series.setName(currentAnalysis.getIdForTable() + ". " + currentAnalysis.getSensor().getSensorName() + "["
						+ currentAnalysis.getSensor().getFrequency() + "] ");
				lineChart.getData().add(series);
				if (maxSeriesSize < series.getData().size()) {
					maxSeriesSize = series.getData().size();
				}

			}
			xAxis.setUpperBound(currentPositionOnXAxis + distanceOnXAxis);
			xAxis.setLowerBound(currentPositionOnXAxis);
			sliderForChart.setVisible(true);
			sliderForChart.setMin(0);
			sliderForChart.setMax(maxSeriesSize / analysisTable.getSelectionModel().getSelectedItem().getSensor().getFrequency() - distanceOnXAxis + 2);
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
					.calculateBounds(analysisTable.getSelectionModel().getSelectedItem().getSensor().getSensorName());
			yAxis.setLowerBound(bounds.getyLowerBound());
			yAxis.setUpperBound(bounds.getyUpperBound());
			yAxis.setTickUnit(BitalinoData.calculateTickUnit(yAxis.getUpperBound(), yAxis.getLowerBound()));
			xAxis.setTickUnit(BitalinoData.calculateTickUnit(xAxis.getUpperBound(), xAxis.getLowerBound()));
		}
	}



	private void isAllDataFromSeriesVisible(boolean state) {
		xAxis.setTickLabelsVisible(state);
		yAxis.setTickLabelsVisible(state);
		if (state) {
			xAxis.setLabel(" Time[ms]");
			yAxis.setLabel(BitalinoData.getUnit(analysisTable.getSelectionModel().getSelectedItem().getSensor().getSensorName()));
			lineChart.setLegendVisible(true);
		} else {
			xAxis.setLabel("");
			yAxis.setLabel("");
			lineChart.setLegendVisible(false);
		}
	}

	public void logOut(ActionEvent event) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(
					getClass().getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/LogInLayout.fxml")
					.openStream());
			root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			analysisType.getScene().setRoot(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
