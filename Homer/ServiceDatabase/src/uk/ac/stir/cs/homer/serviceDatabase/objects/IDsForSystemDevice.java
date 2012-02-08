package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class IDsForSystemDevice {

	private String sysDeviceID;
	private String sysDeviceTypeID;
	
	private String userDeviceID;
	private String userDeviceTypeID;
	
	private String locationID;
	private String locationContextID;
	
	public void setSysDeviceID(String sysDeviceID) {
		this.sysDeviceID = sysDeviceID;
	}
	
	public void setSysDeviceTypeID(String sysDeviceTypeID) {
		this.sysDeviceTypeID = sysDeviceTypeID;
	}
	
	public String getSysDeviceID() {
		return sysDeviceID;
	}
	
	public String getSysDeviceTypeID() {
		return sysDeviceTypeID;
	}
	
	public void setUserDeviceTypeID(String userDeviceTypeID) {
		this.userDeviceTypeID = userDeviceTypeID;
	}
	public void setUserDeviceID(String userDeviceID) {
		this.userDeviceID = userDeviceID;
	}

	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}
	public void setLocationContextID(String locationContextID) {
		this.locationContextID = locationContextID;
	}
	public String getUserDeviceTypeID() {
		return userDeviceTypeID;
	}
	public String getUserDeviceID() {
		return userDeviceID;
	}
	public String getLocationID() {
		return locationID;
	}
	public String getLocationContextID() {
		return locationContextID;
	}
	
}
