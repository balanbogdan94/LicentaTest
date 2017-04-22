package ro.cerner.internship.jemr.persistence.api.config;

import java.util.List;

import ro.cerner.internship.jemr.persistence.api.entity.Sensor;

public interface ConfigRepository {

	List<Sensor> displaySensor();
}
