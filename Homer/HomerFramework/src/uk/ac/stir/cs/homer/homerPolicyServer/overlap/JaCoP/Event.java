package uk.ac.stir.cs.homer.homerPolicyServer.overlap.JaCoP;

import uk.ac.stir.cs.homer.homerFrameworkAPI.events.EventType;

public class Event
{
	private final String id;
	private final String name;
	private final String jsonConfigData;
	private final EventType eventType;

	public Event(String id, String name, String jsonConfigData, EventType eventType)
	{
		this.id = id;
		this.name = name;
		this.jsonConfigData = jsonConfigData;
		this.eventType = eventType;
	}
	
	public String getName()
	{
		return name;
	}
}
