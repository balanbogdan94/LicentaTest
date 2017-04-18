package ro.cerner.internship.jemr.dataacquisition.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public interface BITAlinoInterface<T> {

	ArrayList<Short> readData(T configuration) throws Exception;

	void startConn(T configuration) throws IOException;

	void stopConne() throws Exception;

}