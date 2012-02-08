package uk.ac.stir.cs.homer.componentVisonicSensors.sensors;

import uk.ac.stir.cs.homer.componentVisonicSensors.VisonicSensorComponent;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.State;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.encoding.IDUtil;

public class Window extends OpenCloseSensor
{
	public static final String SENSOR_WINDOW_TYPE_OPENING = IDUtil.getUniqueIdentifier(VisonicSensorComponent.class, "SENSOR_WINDOW_TYPE_OPENING");
	public static final String SENSOR_WINDOW_TYPE_CLOSING = IDUtil.getUniqueIdentifier(VisonicSensorComponent.class, "SENSOR_WINDOW_TYPE_CLOSING");
	
	public static final String SENSOR_WINDOW_TYPE_ISOPEN = IDUtil.getUniqueIdentifier(VisonicSensorComponent.class, "SENSOR_WINDOW_TYPE_ISOPEN");
	public static final String SENSOR_WINDOW_TYPE_ISCLOSED = IDUtil.getUniqueIdentifier(VisonicSensorComponent.class, "SENSOR_WINDOW_TYPE_ISCLOSED");
	public static final String SENSOR_WINDOW_TYPE_CLOSE = IDUtil.getUniqueIdentifier(VisonicSensorComponent.class, "SENSOR_WINDOW_TYPE_CLOSE");
	public static final String SENSOR_WINDOW_TYPE_OPEN = IDUtil.getUniqueIdentifier(VisonicSensorComponent.class, "SENSOR_WINDOW_TYPE_OPEN");
	
	public Window(String systemDeviceID, String sensorID, String openSensorID, String closedSensorID) {
		super(systemDeviceID, sensorID, openSensorID, closedSensorID);
	}
	
	@Override
	public State getStateForSensorID(String sensorID) {
		if (isOpenSensorID(sensorID))
		{
			return new State(SENSOR_WINDOW_TYPE_ISOPEN);
		}
		else
		{
			return new State(SENSOR_WINDOW_TYPE_ISCLOSED);
		}
	}

	@Override
	public String getTriggerIDForSensorID(String sensorID) {
		if (isOpenSensorID(sensorID))
		{
			return SENSOR_WINDOW_TYPE_OPENING;
		}
		else
		{
			return SENSOR_WINDOW_TYPE_CLOSING;
		}
	}

	@Override
	public void updateCodes(String[] newParameters) {
		setSensorID(newParameters[0]);
		setOpenSensorID(newParameters[1]);
		setClosedSensorID(newParameters[2]);
	}

}
