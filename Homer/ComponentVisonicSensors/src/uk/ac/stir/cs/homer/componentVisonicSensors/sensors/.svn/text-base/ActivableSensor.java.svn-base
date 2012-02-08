package uk.ac.stir.cs.homer.componentVisonicSensors.sensors;

import java.util.ArrayList;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.State;

public class ActivableSensor extends Sensor {

	private String activatedSensorID;
	private final String triggerID;
	private ArrayList<String> codes;
	
	public ActivableSensor(String systemDeviceID, String sensorID, String activatedSensorID, String triggerID) {
		super(systemDeviceID, sensorID);
		this.activatedSensorID = activatedSensorID;
		this.triggerID = triggerID;
	}

	public String getActivatedSensorID() {
		return activatedSensorID;
	}

	@Override
	public State getStateForSensorID(String sensorID) {
		return null;
	}
	@Override
	public ArrayList<String> getCodesThatAreToBeListenedFor() {
		if (codes == null)
		{
			codes = new ArrayList<String>(2);
			codes.add(activatedSensorID);
		}
		return codes;
	}
	@Override
	public String getTriggerIDForSensorID(String sensorID) {
		return triggerID;
	}

	@Override
	public void updateCodes(String[] newParameters) {
		setSensorID(newParameters[0]);
		activatedSensorID = newParameters[1];
	}

	

	
}
