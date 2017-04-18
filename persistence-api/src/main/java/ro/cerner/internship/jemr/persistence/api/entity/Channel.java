package ro.cerner.internship.jemr.persistence.api.entity;

public class Channel {
	private int idObject;
	private int channelNumber;
	private int resolution;

	public Channel(int idObject, int channelNumber, int resolution) {
		this.idObject = idObject;
		this.channelNumber = channelNumber;
		this.resolution = resolution;
	}

	public int getIdObject() {
		return idObject;
	}

	public void setIdObject(int idObject) {
		this.idObject = idObject;
	}

	public int getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(int channelNumber) {
		this.channelNumber = channelNumber;
	}

	public int getResolution() {
		return resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	@Override
	public String toString() {
		return Integer.toString(channelNumber);
	}
	
	

}
