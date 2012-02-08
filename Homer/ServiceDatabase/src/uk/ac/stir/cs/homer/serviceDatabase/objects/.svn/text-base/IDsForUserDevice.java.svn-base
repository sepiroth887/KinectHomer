package uk.ac.stir.cs.homer.serviceDatabase.objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IDsForUserDevice {

	private Set<String> sysDeviceIDs;
	private Map<String, String> sysDeviceTypeIDs;
	private String userDeviceID;
	private String userDeviceTypeID;
	private String locationID;
	private String locationContextID;
	
	public IDsForUserDevice() {
		sysDeviceIDs = new HashSet<String>();
		sysDeviceTypeIDs = new HashMap<String, String>();
	}
	
	public void addSysDeviceID(String sysDeviceID) {
		this.sysDeviceIDs.add(sysDeviceID);
	}
	
	public Set<String> getSysDeviceIDs() {
		return sysDeviceIDs;
	}
	
	public void addSysDeviceTypeID(String sysDeviceID, String sysDeviceTypeID) {
		this.sysDeviceTypeIDs.put(sysDeviceID, sysDeviceTypeID);
	}
	public String getSysDeviceTypeIDForSysDevice(String sysDevice) {
		return sysDeviceTypeIDs.get(sysDevice);
	}
	
	public void setUserDeviceID(String userDeviceID) {
		this.userDeviceID = userDeviceID;
	}
	public String getUserDeviceID() {
		return userDeviceID;
	}
	
	public void setUserDeviceTypeID(String userDeviceTypeID) {
		this.userDeviceTypeID = userDeviceTypeID;
	}
	public String getUserDeviceTypeID() {
		return userDeviceTypeID;
	}
	
	public String getLocationID() {
		return locationID;
	}
	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}
	
	public void setLocationContextID(String locationContextID) {
		this.locationContextID = locationContextID;
	}
	public String getLocationContextID() {
		return locationContextID;
	}

	public Collection<String> getSysDeviceTypeIDs() {
		return sysDeviceTypeIDs.values();
	}

	public String getSysDeviceIDForSysDeviceTypeID(String sysDeviceTypeID) {
		for (String sd : sysDeviceTypeIDs.keySet())
		{
			if (sysDeviceTypeIDs.get(sd).equals(sysDeviceTypeID))
			{
				return sd;
			}
		}
		return null; 
	}
	
}
