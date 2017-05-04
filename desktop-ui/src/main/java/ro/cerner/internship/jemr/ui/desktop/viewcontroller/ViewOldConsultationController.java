package ro.cerner.internship.jemr.ui.desktop.viewcontroller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import ro.cerner.internship.jemr.core.bitdata.BitalinoData;
import ro.cerner.internship.jemr.core.bitdata.BitalinoDataBounds;
import ro.cerner.internship.jemr.core.update.UpdateModel;
import ro.cerner.internship.jemr.persistence.api.entity.Analysis;
import ro.cerner.internship.jemr.persistence.api.entity.Doctor;
import ro.cerner.internship.jemr.persistence.api.entity.Examination;
import ro.cerner.internship.jemr.persistence.api.entity.Patient;
import ro.cerner.internship.jemr.persistence.api.entity.Sensor;
import ro.cerner.internship.jemr.ui.desktop.springwiring.SpringApplicationContext;

public class ViewOldConsultationController extends BaseDoctorController implements Initializable {

	@FXML
	private Label completedDate;
	@FXML
	private TableView<Analysis> examinationTable;
	@FXML
	private TableColumn<Analysis, Sensor> sensorType = new TableColumn<Analysis, Sensor>();
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
	private LineChart<Number, Number> analysisChart;
	@FXML
	private ImageView femaleSign;
	@FXML
	private ImageView maleSign;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	@FXML
	private Slider sliderForChart;
	@FXML
	private CheckBox editCheckBox;
	private final int NUMBER_OF_SECOUNDS_TO_DISPLAY=10;
	private int currentPositionOnXAxis;
	private int distanceOnXAxis;
	private int maxChar = 2000;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private Examination currentExamination;
	UpdateModel updateModel = SpringApplicationContext.instance().getBean("UpdateModel", UpdateModel.class);
	ObservableList<Analysis> analysisList;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isAllDataFromSeriesVisible(false);
		sliderForChart.setVisible(false);
		saveGraph.setDisable(true);
		diagnostic.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= maxChar ? change : null));
		comments.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= maxChar ? change : null));
		xAxis.setAutoRanging(false);
		sliderForChart.setValue(0);
		sensorType.setCellValueFactory(new PropertyValueFactory<Analysis, Sensor>("sensor"));
		frequency.setCellValueFactory(new PropertyValueFactory<Analysis, Integer>("frequency"));
		startDateColumn.setCellValueFactory(new PropertyValueFactory<Analysis, String>("startDateFormated"));
		stopDateColumn.setCellValueFactory(new PropertyValueFactory<Analysis, String>("stopDateFormated"));
		analysisChart.setCreateSymbols(false);
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
		setCurrentDoctor(doctor);
		setSelectedPatient(currentPatient);
		if (currentExamination != null) {
			this.completedDate.setText(selectedExamination.getExaminationDate().format(formatter).toString());
			this.diagnostic.setText(selectedExamination.getDiagnostic());
			this.comments.setText(selectedExamination.getComments());
			ObservableList<Analysis> analysisList = FXCollections
					.observableArrayList(viewModel.viewListOfAnalysis(selectedExamination.getObjectID()));
			examinationTable.setItems(analysisList);
		} else {
			patientNotSelectedAlert();
		}

	}

	@FXML
	public void saveGraphic(ActionEvent event) {
//		Stage primaryStage = new Stage();
//		FXMLLoader loader = new FXMLLoader();
//		Pane root;
//		try {
//			root = loader.load(getClass()
//					.getResource("/ro/cerner/internship/jemr/ui/desktop/viewcontroller/ViewOldConsultation.fxml")
//					.openStream());
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		//ViewOldConsultationController pageController = (ViewOldConsultationController) loader.getController();
//		xAxis.setUpperBound(series.getData().size()/examinationTable.getSelectionModel().getSelectedItem().getSensor().getFrequency());
//		xAxis.setLowerBound(0);
//		xAxis.setTickLabelsVisible(false);
//		WritableImage image = analysisChart.snapshot(new SnapshotParameters(), null);
//		viewAnalysis(event);
//		// TODO: probably use a file chooser here
//		
//
//		DateTimeFormatter saveFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH~mm");
//		FileChooser fileChooser = new FileChooser();
//		fileChooser.setInitialFileName(patient.getFirstName()+"_"+patient.getLastName()+"_"+currentExamination.getExaminationDate().format(saveFormatter)+"_"+examinationTable.getSelectionModel().getSelectedItem().getSensor().getSensorName());
//		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
//		fileChooser.getExtensionFilters().add(extFilter);
//		fileChooser.setTitle("Open Resource File");
//		// File file = new File("chart.png");
//
//		//File file = new File("palette_"+currentExamination.getPacientID()+".png");
//		File file=fileChooser.showSaveDialog(primaryStage);
//		try {
//			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//		} catch (IOException e) {
//			// TODO: handle exception here
//		}
	}


	public void viewAnalysisOnButton(ActionEvent event) {
		viewAnalysis(event);
		saveGraph.setDisable(false);
	}

	public <T> void viewAnalysis(T event) 
	{
		analysisChart.getData().clear();
		List<Double> rawData=null ;
		Series<Number, Number> series = new Series<>();
		if (examinationTable.getSelectionModel().getSelectedItem() == null) {
			patientNotSelectedAlert();
		} else {
			isAllDataFromSeriesVisible(true);
			saveGraph.setDisable(false);
			distanceOnXAxis = NUMBER_OF_SECOUNDS_TO_DISPLAY;
			series.setName(examinationTable.getSelectionModel().getSelectedItem().getSensor().getSensorName());
			int objectId = examinationTable.getSelectionModel().getSelectedItem().getObjectId();
			String sensorName = examinationTable.getSelectionModel().getSelectedItem().getSensor().getSensorName();
			rawData= viewModel.viewSensorData(objectId, sensorName,
					examinationTable.getSelectionModel().getSelectedItem().getSensor().getChannel());
			for (int i = 0; i < rawData.size(); i++) {
				series.getData().add(new Data<Number, Number>((double)i/examinationTable.getSelectionModel().getSelectedItem().getSensor().getFrequency(), rawData.get(i)));
			}
			analysisChart.getData().add(series);
			currentPositionOnXAxis = 0;
			xAxis.setUpperBound(currentPositionOnXAxis + distanceOnXAxis);
			xAxis.setLowerBound(currentPositionOnXAxis);
			xAxis.setTickUnit(BitalinoData.calculateTickUnit(xAxis.getUpperBound(), xAxis.getLowerBound()));
			sliderForChart.setVisible(true);
			sliderForChart.setMin(0);
			sliderForChart.setMax(series.getData().size()/examinationTable.getSelectionModel().getSelectedItem().getSensor().getFrequency() - distanceOnXAxis + 2);
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
		
		BitalinoData bitData = new BitalinoData();
		BitalinoDataBounds bounds = bitData
				.calculateBounds(examinationTable.getSelectionModel().getSelectedItem().getSensor().getSensorName());
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(bounds.getyLowerBound());
		yAxis.setUpperBound(bounds.getyUpperBound());
		yAxis.setTickUnit(BitalinoData.calculateTickUnit(yAxis.getUpperBound(), yAxis.getLowerBound()));
		
	}

	@FXML
	public void saveChanges(ActionEvent event) {
			currentExamination.setComments(comments.getText());
			currentExamination.setDiagnostic(diagnostic.getText());
			updateModel.updateExamination(currentExamination);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Examination Updated Succesfully!");
			alert.showAndWait();		
	}

	private void isAllDataFromSeriesVisible(boolean state) {
		xAxis.setTickLabelsVisible(state);
		yAxis.setTickLabelsVisible(state);
		if(state)
		{
			xAxis.setLabel(" Time[ms]");
			yAxis.setLabel(BitalinoData.getUnit(examinationTable.getSelectionModel().getSelectedItem().getSensor().getSensorName()));
			analysisChart.setLegendVisible(true);
		}
		else
		{
			xAxis.setLabel("");
			yAxis.setLabel("");
			analysisChart.setLegendVisible(false);
		}
	}
	
	public void editChecked(ActionEvent e)
	{
		if(editCheckBox.isSelected())
		{
			diagnostic.setDisable(false);
			comments.setDisable(false);
		}
		else
		{
			diagnostic.setDisable(true);
			comments.setDisable(true);
		}
	}
}
