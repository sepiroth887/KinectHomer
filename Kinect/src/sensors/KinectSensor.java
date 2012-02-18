package sensors;

import java.util.ArrayList;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.State;

public class KinectSensor extends Sensor{
	

	public KinectSensor(String systemDeviceID, String sensorID) {
		super(systemDeviceID, sensorID);
	}

	@Override
	public ArrayList<String> getCodesThatAreToBeListenedFor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTriggerIDForSensorID(String sensorID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public State getStateForSensorID(String sensorID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCodes(String[] newParameters) {
		// TODO Auto-generated method stub
		
	}

}
