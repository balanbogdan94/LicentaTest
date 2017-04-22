package ro.cerner.internship.jemr.persistence.mssql.config;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import ro.cerner.internship.jemr.persistence.api.config.ConfigRepository;
import ro.cerner.internship.jemr.persistence.api.entity.Sensor;
import ro.cerner.internship.jemr.persistence.mssql.Database;

@Named("ConfigManage")
public class ConfigManage implements ConfigRepository {
	Connection con = Database.getInstance().getConnect();
	
	@Override
	public List<Sensor> displaySensor() {
		Sensor sensor;
		List<Sensor> sensorList = new ArrayList<>();
		try {
			CallableStatement stmt = con.prepareCall("{ call JSensorView }");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				sensor = new Sensor(rs.getInt("ObjectID"), rs.getString("SensorName"), rs.getInt("IsAnalog"),rs.getInt("Frequency"),rs.getInt("Channel"));
				sensorList.add(sensor);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sensorList;
	}
	
	public Sensor getSensorByID(int Id )
	{
		Sensor sensor=null;
		try
		{
			CallableStatement stmt=con.prepareCall("{call JGetSensorByID (?)}");
			stmt.setInt(1, Id);
			ResultSet rs=stmt.executeQuery();
			while (rs.next())
			{
				sensor = new Sensor(rs.getInt("ObjectID"), rs.getString("SensorName"), rs.getInt("IsAnalog"),rs.getInt("Frequency"),rs.getInt("Channel"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sensor;
	}
}
