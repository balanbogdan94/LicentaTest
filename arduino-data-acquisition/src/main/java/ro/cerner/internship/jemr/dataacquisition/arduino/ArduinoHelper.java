package ro.cerner.internship.jemr.dataacquisition.arduino;

public class ArduinoHelper {
	public static String freqToCharachter(int freq)
	{
		switch (freq) 
		{
		case 1:
			return "1";
		case 10: 
			return "2";
		case 100:
			return "3";

		default:
			return "0";
		}
	}
}
