package ro.cerner.internship.jemr.core.bitdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import ro.cerner.internship.jemr.dataacquisition.api.BITAlinoInterface;
import ro.cerner.internship.jemr.dataacquisition.api.Configuration;

@Named("DataReader")
@Scope("prototype")
public class DataReader extends Thread {
	public volatile boolean startPresed = false;
	public volatile boolean stopPresed = false;
	private Configuration configuration;
	@Inject
	@Named("ArduinoAcquisition")
	private BITAlinoInterface<Configuration> bit;

	boolean running = true;
	private ConcurrentLinkedQueue<Number> valueStore;

	public DataReader() {
	}

	public DataReader(Configuration configuration, ConcurrentLinkedQueue<Number> valueStore)
	{
		this.configuration = configuration;
		this.valueStore = valueStore;
	}
	
	@Override
	public void run() {
		try {
			
			bit.startConn(configuration);
			while (running) {
				ArrayList<Short> auxValue = new ArrayList<>();
				auxValue = bit.readData(configuration);
				System.out.println("aici");
				valueStore.addAll(auxValue);
				System.out.println("AICI");
				auxValue.clear();
			}
			bit.stopConne();
			
		} catch (Exception e) {
 			e.printStackTrace();
			System.out.println("BITalino is not connected!");
		} finally {
			this.interrupt();
		}

	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setValueStore(ConcurrentLinkedQueue<Number> valueList) {
		this.valueStore = valueList;
	}

}
