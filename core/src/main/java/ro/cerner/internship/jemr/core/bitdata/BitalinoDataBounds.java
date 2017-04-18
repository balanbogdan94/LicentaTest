package ro.cerner.internship.jemr.core.bitdata;

public class BitalinoDataBounds {
	private double yUpperBound;
	private double yLowerBound;
	private String sensorName;
	
	public BitalinoDataBounds(String sensorName)
	{
		this.sensorName=sensorName;
	}
	public BitalinoDataBounds(double yLowerBound,double yUpperBound) {
		this.yUpperBound = yUpperBound;
		this.yLowerBound = yLowerBound;
	}

	public double getyUpperBound() {
		return yUpperBound;
	}

	public double getyLowerBound() {
		return yLowerBound;
	}
	
	public String getUnitForChart(){
		String unit=null;
		if(sensorName.equalsIgnoreCase("ACC"))
		{
			unit="Accelaration [G]";
		}else if(sensorName.equalsIgnoreCase("ECG"))
		{
			unit="Electrocardiography [mV]";
		}else if(sensorName.equalsIgnoreCase("EDA"))
		{
			unit="Electrodermal Activity [\u00b5S]";
		}else if(sensorName.equalsIgnoreCase("EEG"))
		{
			unit="Electroencephalography [\u00b5V]";
		}
		else if(sensorName.equalsIgnoreCase("EMG"))
		{
			unit="Electromyography [mV]";
		}else if(sensorName.equalsIgnoreCase("LUX"))
		{
			unit="Light [%]";
		}else if(sensorName.equalsIgnoreCase("PLS"))
		{
			unit="Pulse [mV]";
		}
		
		
		return unit;
	}
	
	
	
	
}
