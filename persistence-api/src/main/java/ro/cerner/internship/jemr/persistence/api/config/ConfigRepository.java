package ro.cerner.internship.jemr.persistence.api.config;

import java.util.List;

import ro.cerner.internship.jemr.persistence.api.entity.Channel;
import ro.cerner.internship.jemr.persistence.api.entity.Frequency;
import ro.cerner.internship.jemr.persistence.api.entity.Sensor;

public interface ConfigRepository {

	List<Frequency> displayFrequency();

	List<Sensor> displaySensor();

	List<Channel> displayChannel();
}
