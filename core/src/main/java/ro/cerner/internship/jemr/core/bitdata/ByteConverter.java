package ro.cerner.internship.jemr.core.bitdata;

import java.util.List;

public class ByteConverter {
	
	public static byte[] rawDataToBinary(List<Short> rawData)
	{
		byte[] binaryArray=new byte[2*(rawData.size())];
		int index=0;
		for(int i=0;i<rawData.size();i++)
		{
			byte[] auxByteArray=getBytes(rawData.get(i));
			binaryArray[index]=auxByteArray[0];
			binaryArray[index+1]=auxByteArray[1];
			index=index+2;
		}
		
		return binaryArray;
		
	}
	
	 private static byte[] getBytes(short x)
     {
         return new byte[] { 
        		 (byte)x,
             (byte)(x >>> 8)
             };
     }
	
}
