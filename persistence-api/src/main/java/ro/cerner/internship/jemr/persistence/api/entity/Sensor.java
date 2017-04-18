package ro.cerner.internship.jemr.persistence.api.entity;

public class Sensor {
	private int objectId;
	private String sensorName;
	private boolean isAnalog;
	
	
	
	public Sensor(int objectId,String sensorName, int isAnalog){
		this.objectId=objectId;
		this.sensorName=sensorName;
		if(isAnalog==1)
		{
			this.isAnalog=true;
		}
		else{
			this.isAnalog=false;
		}
		
	}

	public int getObjectId() {
		return objectId;
	}



	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}



	public String getSensorName() {
		return sensorName;
	}



	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}



	public boolean isAnalog() {
		return isAnalog;
	}



	public void setAnalog(boolean isAnalog) {
		this.isAnalog = isAnalog;
	}



	@Override
	public String toString() {
		return sensorName;
	}
	
	
	
	
	
}
