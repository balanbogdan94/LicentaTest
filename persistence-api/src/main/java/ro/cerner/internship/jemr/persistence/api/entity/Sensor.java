package ro.cerner.internship.jemr.persistence.api.entity;

public class Sensor {
	private int objectId;
	private String sensorName;
	private boolean isAnalog;
	private int frequency;
	private int channel;
	
	
	
	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public Sensor(int objectId,String sensorName, int isAnalog,int frequency,int channel){
		this.objectId=objectId;
		this.sensorName=sensorName;
		this.channel=channel;
		this.frequency=frequency;
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
