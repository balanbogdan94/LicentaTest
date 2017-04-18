package ro.cerner.internship.jemr.dataacquisition.bitalino;

import java.util.ArrayList;

import javax.inject.Named;

import ro.cerner.internship.jemr.dataacquisition.api.Configuration;

@Named("BitalinoConfiguration")
public class BitalinoConfiguration implements Configuration {

	private int frequency;
	private String sensorType;
	private int channel;

	@Override
	public int getChannel() {
		return channel;
	}

	@Override
	public int getFrequency() {
		return frequency;
	}

	@Override
	public String getSensorType() {
		return sensorType;
	}

	@Override
	public void setChannel(int channel) {
		this.channel = channel;
	}

	@Override
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

}
