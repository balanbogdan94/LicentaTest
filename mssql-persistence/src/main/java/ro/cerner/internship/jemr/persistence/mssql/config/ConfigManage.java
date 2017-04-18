package ro.cerner.internship.jemr.persistence.mssql.config;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import ro.cerner.internship.jemr.persistence.api.config.ConfigRepository;
import ro.cerner.internship.jemr.persistence.api.entity.Channel;
import ro.cerner.internship.jemr.persistence.api.entity.Frequency;
import ro.cerner.internship.jemr.persistence.api.entity.Sensor;
import ro.cerner.internship.jemr.persistence.mssql.Database;

@Named("ConfigManage")
public class ConfigManage implements ConfigRepository {
	Connection con = Database.getInstance().getConnect();
	@Override
	public List<Frequency> displayFrequency() {
		Frequency frequency;
		
		List<Frequency> frequencyList = new ArrayList<>();
		try {
			CallableStatement stmt = con.prepareCall("{call JselectAllFrequency }");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				frequency = new Frequency(rs.getInt("ObjectId"), rs.getString("Rate"));
				frequencyList.add(frequency);

			}
		} catch (Exception e) {

		}

		return frequencyList;
	}

	@Override
	public List<Sensor> displaySensor() {
		Sensor sensor;
		List<Sensor> sensorList = new ArrayList<>();
		try {
			CallableStatement stmt = con.prepareCall("{ call JSensorView }");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				sensor = new Sensor(rs.getInt("ObjectID"), rs.getString("SensorName"), rs.getInt("IsAnalog"));
				sensorList.add(sensor);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sensorList;
	}
	
	@Override
	public List<Channel> displayChannel(){
		Channel channel;
		List<Channel> channelList=new ArrayList<>();
		try{
			CallableStatement stmt=con.prepareCall("{call JChannelView}");
			ResultSet rs=stmt.executeQuery();
			while(rs.next())
			{
				channel=new Channel(rs.getInt("ObjectID"), rs.getInt("Number"), rs.getInt("Resolution"));
				channelList.add(channel);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return channelList;
	}

}
