package uk.ac.stir.cs.homer.componentVisonicSensors.sensors;

import java.util.ArrayList;

public abstract class OpenCloseSensor extends Sensor {

	protected String openSensorID;
	protected String closedSensorID;
	private ArrayList<String> codes;
	
	public OpenCloseSensor(String systemDeviceID, String sensorID, String openSensorID, String closedSensorID) {
		super(systemDeviceID, sensorID);
		this.openSensorID = openSensorID;
		this.closedSensorID = closedSensorID;
	}

	public String getClosedSensorID() {
		return closedSensorID;
	}
	public String getOpenSensorID() {
		return openSensorID;
	}
	
	@Override
	public ArrayList<String> getCodesThatAreToBeListenedFor() {
		if (codes == null)
		{
			codes = new ArrayList<String>(2);
			codes.add(openSensorID);
			codes.add(closedSensorID);
		}
		return codes;
	}
	protected boolean isOpenSensorID(String sensorID)
	{
		return (sensorID.equals(openSensorID));
	}
	protected void setClosedSensorID(String closedSensorID) {
		this.closedSensorID = closedSensorID;
	}
	protected void setOpenSensorID(String openSensorID) {
		this.openSensorID = openSensorID;
	}
}
