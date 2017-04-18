package ro.cerner.internship.jemr.dataacquisition.arduino;

import java.util.ArrayList;

import javax.inject.Named;

import ro.cerner.internship.jemr.dataacquisition.api.Configuration;

@Named("ArduinoConfiguration")
public class ArduinoConfiguration implements Configuration{
	private int frequency;
	private String sensorType;
	private Integer channel;
	
	@Override
	public void setFrequency(int frequency) {
		this.frequency=frequency;
		
	}

	@Override
	public int getFrequency() {
		// TODO Auto-generated method stub
		return frequency;
	}

	@Override
	public void setSensorType(String sensorType) 
	{
		this.sensorType=sensorType;
		
	}

	@Override
	public String getSensorType() {
		return sensorType;
	}

	@Override
	public void setChannel(int channel) {
		this.channel=channel;
		
	}

	@Override
	public int getChannel() {
		// TODO Auto-generated method stub
		return channel;
	}

}
