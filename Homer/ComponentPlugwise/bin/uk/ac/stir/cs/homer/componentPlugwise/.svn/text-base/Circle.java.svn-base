package uk.ac.stir.cs.homer.componentPlugwise;

import uk.ac.stir.cs.homer.componentPlugwise.driver.Calibration;

public class Circle
{
	private final String systemDeviceID;
	private final boolean isPlus;
	private final String address;
	private Calibration calibration;
	
	private boolean initialised = false;
	private boolean isOn = false;
	
	public Circle(String systemDeviceID, String address, boolean isPlus)
	{
		this.systemDeviceID = systemDeviceID;
		this.address = address;
		this.isPlus = isPlus;
	}
	
	/**
	 * Record any new power reading as and when it is read
	 * @param power
	 * @return true if the state of the device has now changed (ie, from on to off)
	 */
	public boolean recordNewPowerReading(int power)
	{
		boolean newStateIsOn = power > 0;

		if (initialised)
		{
			if (newStateIsOn!=isOn)
			{
				isOn = newStateIsOn;
				return true;
			}
		}
		else
		{
			initialised = true;
			isOn = newStateIsOn;
		}
		return false;
	}
	
	public boolean isOn()
	{
		return isOn;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getSystemDeviceID()
	{
		return systemDeviceID;
	}
	
	public Calibration getCalibration()
	{
		return calibration;
	}
	
	public void setCalibration(Calibration calibration)
	{
		this.calibration = calibration;
	}

	public boolean isPlus()
	{
		return isPlus;
	}
	
	@Override
	public int hashCode()
	{
		return address.hashCode();
	}
	
	@Override
	public boolean equals(Object otherObj)
	{
		if (otherObj != null && otherObj instanceof Circle)
		{
			Circle otherCircle = (Circle)otherObj;
			return otherCircle.getAddress().equals(this.getAddress());
		}
		return false;
	}
}
