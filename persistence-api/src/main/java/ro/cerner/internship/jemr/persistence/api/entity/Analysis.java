package ro.cerner.internship.jemr.persistence.api.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Analysis {
	private int objectId;
	private int examinationId;
	private byte[] sensorData;
	private int sensorId;
	private String sensorName;
	private int frequency;
	private LocalDateTime startDate;
	private LocalDateTime stopDate;
	private String startDateFormated;
	private String stopDateFormated;
	private String consultationDiagnostic;
	private int channelId;
	private int channelNumber;
	private int idForTable;
	
	
	public Analysis(int examinationId, byte[]sensorData, int sensorId, int frequency, LocalDateTime startDate,
			LocalDateTime stopDate,int channelId) {

		this.examinationId = examinationId;
		this.sensorData = sensorData;
		this.sensorId = sensorId;
		this.frequency = frequency;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.channelId= channelId;
	}

	public Analysis(int frequency,String sensorName, LocalDateTime startDate,String startDateFormated,int objectId,String diagnostic,int channelNumber) {
		this.setSensorName(sensorName);
		this.frequency = frequency;
		this.startDate = startDate;
		this.objectId=objectId;
		this.consultationDiagnostic=diagnostic;
		this.channelNumber=channelNumber;
		this.startDateFormated=startDateFormated;
	}

	public Analysis(String sensorName, int frequency, LocalDateTime startDate,String startDateFormated, LocalDateTime stopDate,String stopDateFormated,int objectId,int channelNumber) {
		this.setSensorName(sensorName);
		this.frequency = frequency;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.objectId=objectId;
		this.channelNumber=channelNumber;
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


	public int getSensorId() {
		return sensorId;
	}


	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}


	public int getFrequency() {
		return frequency;
	}


	public void setFrequency(int frequency) {
		this.frequency = frequency;
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
	
	

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(int channelNumber) {
		this.channelNumber = channelNumber;
	}

	@Override
	public String toString() {
		return "Analysis [objectId=" + objectId + ", examinationId=" + examinationId + ", sensorData=" + sensorData
				+ ", sensorId=" + sensorId + ", frequency=" + frequency + ", startDate=" + startDate + ", stopDate="
				+ stopDate + "]";
	}


	public String getSensorName() {
		return sensorName;
	}


	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
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
