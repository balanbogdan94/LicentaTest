package ro.cerner.internship.jemr.dataacquisition.bitalino;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.bitalino.comm.BITalinoDevice;
import com.bitalino.comm.BITalinoFrame;
import com.intel.bluetooth.BlueCoveImpl;

import ro.cerner.internship.jemr.dataacquisition.api.BITAlinoInterface;
import ro.cerner.internship.jemr.dataacquisition.api.Configuration;

/**
 * Example on how to work with BITalino from Java.
 */
@Named("BITalinoExample")
public class BITalinoExample implements BITAlinoInterface<Configuration> {

	BITalinoDevice device;
	int[] analogs = { 0, 1, 2, 3, 4, 5 };

	private static final String MAC = "20:16:02:14:75:32";
	final String mac = MAC.replace(":", "");
	StreamConnection conn;

	private static final Logger logger = Logger.getLogger(BITalinoExample.class.getName());

	@Override

	public ArrayList<Short> readData(Configuration configuration) throws Exception {

		String sensorType = configuration.getSensorType();
		Integer CHANNEL = configuration.getChannel();

		int aux1 = 0;

		aux1 = (int) Math.ceil((double) configuration.getFrequency() / 60);

		BITalinoFrame[] frames;
		Integer index1 = 0;
		Integer index2 = 0;

		frames = device.read(aux1);

		ArrayList<Short> auxList1 = new ArrayList<>();

		for (int i = 0; i < aux1; i++) {
			index1 = frames[i].getAnalog(CHANNEL);
			auxList1.add(index1.shortValue());
		}

		return auxList1;

	}

	@Override
	public void startConn(Configuration configuration) throws IOException {
		try {// TODO: safdasfsadfs
				// BITalinoDevice device = new BITalinoDevice(samplerate,
				// analogs);
			device = new BITalinoDevice(configuration.getFrequency(), analogs);
			// connect to BITalino device

			conn = (StreamConnection) Connector.open("btspp://" + mac + ":1", Connector.READ_WRITE);
			device.open(conn.openInputStream(), conn.openOutputStream());

			// get BITalino version
			logger.info("Firmware Version: " + device.version());

			// start acquisition on predefined analog channels

			device.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("BITalino is not connected!");
		}

	}

	@Override
	public void stopConne() throws Exception {
		conn.close();
		device.stop();
		BlueCoveImpl.shutdown();
		System.out.println("I'm here");
	}

}