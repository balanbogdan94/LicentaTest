package ro.cerner.internship.jemr.dataacquisition.api;

import java.util.ArrayList;

public interface Configuration {

	void setFrequency(int frequency);

	int getFrequency();

	void setSensorType(String sensorType);

	String getSensorType();

	void setChannel(int channel);

	int getChannel();

}
