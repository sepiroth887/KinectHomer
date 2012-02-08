package uk.ac.stir.cs.homer.homerPolicyServer;

import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.stir.cs.homer.homerFrameworkAPI.events.EventType;
import uk.ac.stir.cs.homer.serviceJSON.JSONUtils;

public class PolicyEvent 
{
	private final String id;
	private final String userDeviceID;
	private final String[] parameters;
	private EventType type;

	public PolicyEvent(String id, String userDeviceID, String[] parameters, EventType type)
	{
		this.id = id;
		this.userDeviceID = userDeviceID;
		this.parameters = parameters;
		this.type = type;
	}
	
	public PolicyEvent(JSONObject child) throws JSONException
	{
		this.id = child.getString("eventid");
		this.userDeviceID = child.getString("userdeviceid");
		if (child.has("parameters"))
		{
			this.parameters = JSONUtils.convertJSONArrayToJavaStringArray(child.getString("parameters"));
		}
		else
		{
			this.parameters = new String[0];
		}
		setType(child.getString("type"));
		
	}
	
	private void setType(String type)
	{
		if (type.equals(EventType.TRIGGER.name()))
		{
			this.type = EventType.TRIGGER;
		}
		else if (type.equals(EventType.CONDITION.name()))
		{
			this.type = EventType.CONDITION;
		}
		else if (type.equals(EventType.ACTION.name()))
		{
			this.type = EventType.ACTION;
		}
		else
		{
			this.type = EventType.UNKNOWN;
		}
	}
	
	public String getID() {
		return id;
	}
	
	public String[] getParameters() {
		return parameters;
	}
	
	public String getUserDeviceID() {
		return userDeviceID;
	}
	public EventType getType() {
		return type;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0!=null && arg0 instanceof PolicyEvent)
		{
			PolicyEvent otherPE = (PolicyEvent) arg0;
			return otherPE.getID().equals(id) && otherPE.getUserDeviceID().equals(userDeviceID) && otherPE.getParameters().equals(parameters);
		}
		return super.equals(arg0);
	}
}
