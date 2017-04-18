package ro.cerner.internship.jemr.ui.desktop.util;

import java.util.ArrayList;
import java.util.List;

public class StringFormater {

	public List<Double> splitString(String sensorData) {
		List<Double> sensorDataFormated = new ArrayList<>();
		String[] splittedSensorData = sensorData.split(" ");
		for (String individualSensorData : splittedSensorData) {
			sensorDataFormated.add(Double.parseDouble(individualSensorData));
		}
		return sensorDataFormated;
	}

}
