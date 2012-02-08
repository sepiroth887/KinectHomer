package uk.ac.stir.cs.homer.homerPolicyServer.overlap;

import uk.ac.stir.cs.homer.homerFrameworkAPI.time.TimeAndDateComponent;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP.Event;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;

public class OverlappingVariable
{
	private final UserDevice userDevice;
	private int time;
	private int value;

	private boolean valueSet = false;
	private boolean timeSet = false;
	private Event event;
	private boolean isDate = false;
	private boolean isTime = false;
	
	public OverlappingVariable(UserDevice userDevice)
	{
		this.userDevice = userDevice;
	}

	public void setTime(int time)
	{
		this.time = time;
		timeSet=true;
	}

	public void setValue(int value)
	{
		this.value = value;
		valueSet = true;
	}

	public int getTime()
	{
		return time;
	}
	public int getValue()
	{
		return value;
	}
	
	public void setIsDate(boolean isDate)
	{
		this.isDate = isDate;
	}
	
	public void setIsTime(boolean isTime)
	{
		this.isTime = isTime;
	}
	
	public UserDevice getUserDevice()
	{
		return userDevice;
	}
	
	public boolean isTimeSet()
	{
		return timeSet;
	}
	public boolean isValueSet()
	{
		return valueSet;
	}

	public void setEvent(Event e)
	{
		this.event = e;
	}
	
	public Event getEvent()
	{
		return event;
	}

	public String describe()
	{
		if (getEvent()!=null)
		{
			return (" " + getEvent().getName());
		}
		else if (isDate)
		{
			return " is " + TimeAndDateComponent.describeDay(value-1);
		}
		else if (isTime)
		{
			return " is " + TimeAndDateComponent.describeTime(value-1);
		}
		else
		{
			return (" = " + getValue());
		}
	}

	
}
