package ro.cerner.internship.jemr.dataacquisition.bitalino;

public class Formulas {

	public double LUX(int value, int channel) {
		if (channel < 4)
			return value / Math.pow(2, 10) * 100;
		return value / Math.pow(2, 6) * 100;
	}

	public double ECG(int value, int channel) {
		double ECG;
		if (channel < 4)
			ECG = (value / Math.pow(2, 10) - 1 / 2) * 3.3 / 1100;
		else
			ECG = (value / Math.pow(2, 6) - 1 / 2) * 3.3 / 1100;
		return ECG * 1000;
	}

	public double EDA(int value, int channel) {
		double EDA;
		if (channel < 4)
			EDA = (value / Math.pow(2, 10)) * 3.3 - 0.259388;
		else
			EDA = (value / Math.pow(2, 6)) * 3.3 - 0.259388;
		return EDA * Math.pow(10, -6);
	}

	public double TMP(int value, int channel) {
		double TMP;
		if (channel < 4)
			TMP = (value / Math.pow(2, 10) * 3.3 - 0.5) * 100;
		else
			TMP = (value / Math.pow(2, 6) * 3.3 - 0.5) * 100;
		return TMP * (9 / 5) + 32;
	}

	public double EEG(int value, int channel) {
		double EEG;
		if (channel < 4)
			EEG = (value / Math.pow(2, 10) - 1 / 2) * 3.3 / 40000;
		else
			EEG = (value / Math.pow(2, 6) - 1 / 2) * 3.3 / 40000;
		return EEG * Math.pow(10, 6);
	}

	public void ACC(int value, int channel) {

	}

}
