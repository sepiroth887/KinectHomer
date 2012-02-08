package uk.ac.stir.cs.homer.homerFrameworkAPI.homerObjects;

public class DeviceTypeResource
{
	private final boolean local;
	private final int amount;
	private final String resourceID;

	public DeviceTypeResource(String resourceID, int amount, boolean local)
	{
		this.resourceID = resourceID;
		this.amount = amount;
		this.local = local;
	}
	
	public int getAmount()
	{
		return amount;
	}
	public String getResourceID()
	{
		return resourceID;
	}
	public boolean isLocal()
	{
		return local;
	}
}
