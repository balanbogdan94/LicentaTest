package ro.cerner.internship.jemr.persistence.api.entity;

import java.time.LocalDateTime;


public class Examination {
	
	private int ObjectID;
	private int PacientID;
	private LocalDateTime ExaminationDate;
	private String examinationDateFormated;
	private String Diagnostic;
	private String Comments;

	public Examination(int objectID, int pacientID, LocalDateTime examinationDate,String examinationDateFormated, String diagnostic, String comments) {
		super();
		this.ObjectID = objectID;
		this.PacientID = pacientID;
		this.ExaminationDate = examinationDate;
		this.Diagnostic = diagnostic;
		this.Comments = comments;
		this.examinationDateFormated=examinationDateFormated;
	}
	
	
	public Examination(int idExamination, String text, String text2) {
		this.ObjectID=idExamination;
		this.Diagnostic=text;
		this.Comments=text2;
	}


	public int getObjectID() {
		return ObjectID;
	}


	public void setObjectID(int objectID) {
		ObjectID = objectID;
	}


	public int getPacientID() {
		return PacientID;
	}


	public void setPacientID(int pacientID) {
		PacientID = pacientID;
	}


	public LocalDateTime getExaminationDate() {
		return ExaminationDate;
	}


	public void setExaminationDate(LocalDateTime examinationDate) {
		ExaminationDate = examinationDate;
	}


	public String getDiagnostic() {
		return Diagnostic;
	}


	public void setDiagnostic(String diagnostic) {
		Diagnostic = diagnostic;
	}


	public String getComments() {
		return Comments;
	}


	public void setComments(String comments) {
		Comments = comments;
	}


	public String getExaminationDateFormated() {
		return examinationDateFormated;
	}


	public void setExaminationDateFormated(String examinationDateFormated) {
		this.examinationDateFormated = examinationDateFormated;
	}
	
	


	
	

}

