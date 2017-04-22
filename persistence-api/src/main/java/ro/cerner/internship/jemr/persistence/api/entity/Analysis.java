package ro.cerner.internship.jemr.persistence.api.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Analysis {
	private int objectId;
	private int examinationId;
	private byte[] sensorData;
	private Sensor sensor;
	private LocalDateTime startDate;
	private LocalDateTime stopDate;
	private String startDateFormated;
	private String stopDateFormated;
	private String consultationDiagnostic;
	private int idForTable;
	
	
	public Analysis(int examinationId, byte[]sensorData, Sensor sensor, LocalDateTime startDate,
			LocalDateTime stopDate) {

		this.examinationId = examinationId;
		this.sensorData = sensorData;
		this.sensor = sensor;
		this.startDate = startDate;
		this.stopDate = stopDate;
	}

	public Analysis(Sensor sensor, LocalDateTime startDate,String startDateFormated,int objectId,String diagnostic) {
		this.sensor=sensor;
		this.startDate = startDate;
		this.objectId=objectId;
		this.consultationDiagnostic=diagnostic;
		this.startDateFormated=startDateFormated;
	}

	public Analysis(Sensor sensor, LocalDateTime startDate,String startDateFormated, LocalDateTime stopDate,String stopDateFormated,int objectId) {
		this.sensor=sensor;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.objectId=objectId;
		this.startDateFormated=startDateFormated;
		this.stopDateFormated=stopDateFormated;
	}
	

	public int getObjectId() {
		return objectId;
	}


	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}


	public int getExaminationId() {
		return examinationId;
	}


	public void setExaminationId(int examinationId) {
		this.examinationId = examinationId;
	}


	public byte[] getSensorData() {
		return sensorData;
	}


	public void setSensorData(byte[] sensorData) {
		this.sensorData = sensorData;
	}


	public Sensor getSensor() {
		return sensor;
	}


	public void setSensorId(Sensor sensor) {
		this.sensor = sensor;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}


	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}


	public LocalDateTime getStopDate() {
		return stopDate;
	}


	public void setStopDate(LocalDateTime stopDate) {
		this.stopDate = stopDate;
	}

	public String getConsultationDiagnostic() {
		return consultationDiagnostic;
	}


	public void setConsultationDiagnostic(String consultationDiagnostic) {
		this.consultationDiagnostic = consultationDiagnostic;
	}
	


	@Override
	public String toString() {
		return "Analysis [objectId=" + objectId + ", examinationId=" + examinationId + ", sensorData=" + sensorData
				+ ", sensorId=" + sensor.getObjectId() + ", startDate=" + startDate + ", stopDate="
				+ stopDate + "]";
	}

	public int getIdForTable() {
		return idForTable;
	}

	public void setIdForTable(int idForTable) {
		this.idForTable = idForTable;
	}

	public String getStartDateFormated() {
		return startDateFormated;
	}

	public void setStartDateFormated(String startDateFormated) {
		this.startDateFormated = startDateFormated;
	}

	public String getStopDateFormated() {
		return stopDateFormated;
	}

	public void setStopDateFormated(String stopDateFormated) {
		this.stopDateFormated = stopDateFormated;
	}
	
}
