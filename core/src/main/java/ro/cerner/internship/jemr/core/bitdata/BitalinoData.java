package ro.cerner.internship.jemr.core.bitdata;

import java.util.ArrayList;
import java.util.List;



public class BitalinoData {
	
	private double minToInterval;
	private double maxToInterval;
	
	public static double transformToRelevantData(short value, int minFromInterval,int maxFromInterval,double minToInterval,double maxToInterval){
		double newValue=minToInterval + (value - minFromInterval) * (maxToInterval - minToInterval) / (maxFromInterval - minFromInterval)	;	 
		return newValue;
	}
	
	
	
	public static List<Double> relevantDataList(List<Short> valueList,String sensorName,int channelNumber){
		List<Double> relevantData=new ArrayList<>();
		int minFromInterval=0;
		int maxFromInterval;
		double minToInterval;
		double maxToInterval;
		if(channelNumber<4)
		{
			maxFromInterval=1023;	
		}
		else
		{
			maxFromInterval=63;
		}
		if(sensorName.equalsIgnoreCase("ACC"))
		{
			minToInterval=-3;
			maxToInterval=3;
		}
		else if(sensorName.equalsIgnoreCase("LUX"))
		{
			minToInterval=0;
			maxToInterval=100;
		}
		else if(sensorName.equalsIgnoreCase("ECG"))
		{
			minToInterval=-1.5;
			maxToInterval=1.5;
		}
		else if(sensorName.equalsIgnoreCase("EDA"))
		{
			minToInterval=-1.2;
			maxToInterval=13.7;
		}
		else if(sensorName.equalsIgnoreCase("EEG"))
		{
			minToInterval=-41.25;
			maxToInterval=41.25;
		}
		else if(sensorName.equalsIgnoreCase("EMG"))
		{
			minToInterval=-1.63;
			maxToInterval=1.63;
		}
		else
		{
			minToInterval=0;
			maxToInterval=1023;
		}
		
		
		
		for(Short value:valueList)
		{
			relevantData.add(transformToRelevantData(value, minFromInterval, maxFromInterval, minToInterval, maxToInterval));
		}
		return relevantData;
		
	}
	
	
	
	public static double relevantDataValue(short value,String sensorName,int channel){
		double minToInterval;
		double maxToInterval;
		int minFromInterval=0;
		int maxFromInterval=1023;
		
		if(channel<4)
		{
			minFromInterval=0;
			maxFromInterval=1023;
		}
		else
		{
			minFromInterval=0;
			maxFromInterval=63;
		}
		if(sensorName.equalsIgnoreCase("ACC"))
		{
			minToInterval=-3;
			maxToInterval=3;
		}
		else if(sensorName.equalsIgnoreCase("LUX"))
		{
			minToInterval=0;
			maxToInterval=100;
		}
		else if(sensorName.equalsIgnoreCase("ECG"))
		{
			minToInterval=-1.5;
			maxToInterval=1.5;
		}
		else if(sensorName.equalsIgnoreCase("EDA"))
		{
			minToInterval=-1.2;
			maxToInterval=13.7;
		}
		else if(sensorName.equalsIgnoreCase("EEG"))
		{
			minToInterval=-41.25;
			maxToInterval=41.25;
		}
		else if(sensorName.equalsIgnoreCase("EMG"))
		{
			minToInterval=-1.63;
			maxToInterval=1.63;
		}
		else
		{
			minToInterval=0;
			maxToInterval=1023;
		}
		return transformToRelevantData(value, minFromInterval, maxFromInterval, minToInterval, maxToInterval);
	}


	public BitalinoDataBounds calculateBounds(String sensorName){
		BitalinoDataBounds b;
		if(sensorName.equalsIgnoreCase("ACC"))
		{
			 b=new BitalinoDataBounds(-3, 3);
		}
		else if(sensorName.equalsIgnoreCase("LUX"))
		{
			 b=new BitalinoDataBounds(0,100);
		}
		else if(sensorName.equalsIgnoreCase("ECG"))
		{
			 b=new BitalinoDataBounds(-1.5,1.5);
		}
		else if(sensorName.equalsIgnoreCase("EDA"))
		{
			 b=new BitalinoDataBounds(-1.2,13.7);
		}
		else if(sensorName.equalsIgnoreCase("EEG"))
		{
			 b=new BitalinoDataBounds(-41.25,41.25);
		}
		else if(sensorName.equalsIgnoreCase("EMG"))
		{
			 b=new BitalinoDataBounds(-1.63,1.63);
		}
		else
		{
			 b=new BitalinoDataBounds(200,800);
		}
		
		return b;
	}
	
	public BitalinoDataBounds calculateBounds(double minInterval1,double maxInterval1, double minInterval2,double maxInterval2){
		double min;
		double max;
		if(minInterval1<maxInterval2)
		{
			min=minInterval1;
		}else
		{
			min=minInterval2;
		}
		
		if(maxInterval1<maxInterval2)
		{
			max=maxInterval2;
		}
		else
		{
			max=maxInterval1;
		}
		return new BitalinoDataBounds(min, max);
	}
	
	public static double calculateTickUnit(double upperBounds,double lowerBound)
	{
		return (upperBounds-lowerBound)/4;
	}
	
	//Final
	public static int calculateBPM(List<Double> myValueList, double time){
		boolean beat=false;
		int beats=0;
		int beatsPerMinute=0;
		double nominalValue=calculateTheNominalValue(myValueList);
		
		for(int i=0;i<myValueList.size()-1;i++)
		{
			if(beat && myValueList.get(i)>nominalValue)
			{
				beat=!beat;
				beats++;
			}
			else if(!beat && myValueList.get(i)<nominalValue)
			{
				beat=!beat;
			}
		}
		beatsPerMinute=(int)((beats*60)/time);
		
		return beatsPerMinute;
	}
	
	
	private static double calculateTheNominalValue(List<Double> myValueList){
		double nominalValue=0;
		double average=0;
		double max=0;
		for(Double currentValue:myValueList)
		{
			if(currentValue>max)
			{
				max=currentValue;
			}
			average=average+currentValue;
		}
		average=average/(myValueList.size());
		
		nominalValue=(max-average)/2;
		
		
		
		return (max-nominalValue);
	}

	public static String getUnit(String sensorName){
		BitalinoDataBounds bitDataBounds=new BitalinoDataBounds(sensorName);
		return bitDataBounds.getUnitForChart();
	}
	
	
		
}
