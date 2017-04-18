package ro.cerner.internship.jemr.persistence.api.entity;

public class Frequency {
	private int idObject;
	private String frequency;
	
	
	public Frequency(int idObject,String frequency){
		this.idObject=idObject;
		this.frequency=frequency;
	}


	public int getIdObject() {
		return idObject;
	}


	public void setIdObject(int idObject) {
		this.idObject = idObject;
	}


	public String getFrequency() {
		return frequency;
	}


	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}


	@Override
	public String toString() {
		return frequency;
	}
	
	
	
	
}