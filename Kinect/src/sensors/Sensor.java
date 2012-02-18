package sensors;

import java.util.ArrayList;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.State;

public abstract class Sensor 
{
	private final String systemDeviceID;
	private String sensorID;

	public Sensor (String systemDeviceID, String sensorID)
	{
		this.systemDeviceID = systemDeviceID;
		this.sensorID = sensorID;		
	}
	
	public String getSensorID() {
		return sensorID;
	}
	public String getSystemDeviceID() {
		return systemDeviceID;
	}
	protected void setSensorID(String sensorID) {
		this.sensorID = sensorID;
	}
	public abstract ArrayList<String> getCodesThatAreToBeListenedFor();
	public abstract String getTriggerIDForSensorID(String sensorID);
	public abstract State getStateForSensorID(String sensorID);
	
	@Override
	public boolean equals(Object arg0) {
	
		if (arg0 instanceof Sensor)
		{
			return ((Sensor) arg0).systemDeviceID.equals(systemDeviceID);
		}
		return super.equals(arg0);
	}

	public abstract void updateCodes(String[] newParameters);
	
}
