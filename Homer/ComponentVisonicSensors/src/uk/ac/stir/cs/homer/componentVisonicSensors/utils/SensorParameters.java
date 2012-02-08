package uk.ac.stir.cs.homer.componentVisonicSensors.utils;

import uk.ac.stir.cs.homer.componentVisonicSensors.VisonicSensorComponent;

/**
 * Sensor id and code handling for RFXCOMDriver.
 * 
 * @author Feng Wang, Kenneth J. Turner
 * @version 1.0: 25/02/09 (FW) <br/>
 *          1.1: 13/05/09 (KJT)
 */

public final class SensorParameters {

	private String sensorID;
	private String sensorCode;

	/**
	 * Constructor for the SensorParameters object
	 * 
	 * @param id
	 *            Parameter
	 * @param code
	 *            Parameter
	 */
	public SensorParameters(String id, String code) {
		sensorID = id;
		sensorCode = code;
	}

	/**
	 * Sets the sensorID attribute of the SensorParameters object
	 * 
	 * @param id
	 *            The new sensorID value
	 */
	public void setSensorID(String id) {
		sensorID = id;
	}

	/**
	 * Sets the sensorCode attribute of the SensorParameters object
	 * 
	 * @param code
	 *            The new sensorCode value
	 */
	public void setSensorCode(String code) {
		sensorCode = code;
	}

	/**
	 * Gets the sensorID attribute of the SensorParameters object
	 * 
	 * @return The sensorID value
	 */
	public String getSensorID() {
		return sensorID;
	}

	/**
	 * Gets the sensorCode attribute of the SensorParameters object
	 * 
	 * @return The sensorCode value
	 */
	public String getSensorCode() {
		return sensorCode;
	}

	/**
	 * Compares this to the parameter.
	 * 
	 * @param o
	 *            the reference object with which to compare.
	 * @return <tt>true</tt> if this object is the same as the obj argument;
	 *         <tt>false</tt> otherwise.
	 */
	public boolean equals(Object o) {
		if ((o != null) && (o.getClass().equals(this.getClass()))) {
			SensorParameters sic = (SensorParameters) o;
			if ((sic.getSensorID().equals(sensorID))
					&& (sic.getSensorCode().equals(sensorCode)))
				return true;
		}
		return false;
	}

	/**
	 * Get hash code for entry.
	 * 
	 * @return hash code
	 */

	public int hashCode() {
		return 17 * sensorID.hashCode() + sensorCode.hashCode();
	}

	// compares if two instances of sensorParameters have the same attributes
	public boolean equalsSameAttributes(SensorParameters sensorParameters) {
		return (sensorParameters != null
				&& this.sensorCode.equals(sensorParameters.getSensorCode()) && this.sensorID
				.equals(sensorParameters.getSensorID()));
	}

	public boolean isIgnored() {
		return !sensorParameterKnown();
	}

	public boolean sensorParameterKnown() {
		if (VisonicSensorComponent.sensors.containsKey(sensorID))
		{
			return VisonicSensorComponent.sensors.get(sensorID).getCodesThatAreToBeListenedFor().contains(sensorCode);
		}
		return false;
	}
}
