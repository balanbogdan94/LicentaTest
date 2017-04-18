package ro.cerner.internship.jemr.dataacquisition.arduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import ro.cerner.internship.jemr.dataacquisition.api.BITAlinoInterface;
import ro.cerner.internship.jemr.dataacquisition.api.Configuration;

@Named("ArduinoAcquisition")
public class ArduinoAcquisition implements BITAlinoInterface<Configuration>
{
	private StreamConnection streamConnection;
	private InputStream is;
	private OutputStream os;

	@Override
	public ArrayList<Short> readData(Configuration configuration) throws Exception {
		System.out.println("Sunt aici");
		ArrayList<Short> rawData=new ArrayList<>();
		rawData.add(getNumberFromArduino());
		System.out.println("SUNT AICI");
		return rawData;
	}

	@Override
	public void startConn(Configuration configuration) throws IOException {
		streamConnection=(StreamConnection)Connector.open("btspp://98D331FC1375:1;authenticate=false;encrypt=false;master=false");
		is = streamConnection.openInputStream();
	    os=streamConnection.openOutputStream();
		os.write(2);
	}

	@Override
	public void stopConne() throws Exception {
		streamConnection.close();
		is.close();
		os.close();
		
	}
	
	private short getNumberFromArduino(){
		short number;
		String res=new String();
		try {
			while((number=(short) is.read())!=10)
			{
				res=res+(char)number;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Short.parseShort(res.trim());
	}
}
