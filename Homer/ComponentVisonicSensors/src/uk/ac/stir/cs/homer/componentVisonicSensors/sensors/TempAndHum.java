package uk.ac.stir.cs.homer.componentVisonicSensors.sensors;

import java.util.ArrayList;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.State;

public class TempAndHum extends Sensor {

	public TempAndHum(String systemDeviceID, String sensorID) {
		super(systemDeviceID, sensorID);
	}

	@Override
	public State getStateForSensorID(String sensorID) {
		return null;
	}

	@Override
	public String getTriggerIDForSensorID(String sensorID) {
		return null;
	}

	@Override
	public void updateCodes(String[] newParameters) {
		setSensorID(newParameters[0]);
	}
	@Override
	public ArrayList<String> getCodesThatAreToBeListenedFor() {
		return new ArrayList<String>(0);
	}
}
