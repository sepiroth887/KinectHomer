package uk.ac.stir.cs.homer.serviceDatabase.objects;

import java.util.Calendar;

public class Log extends DBObject
{
	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS Logs ( " +
						"id INT PRIMARY KEY AUTO_INCREMENT, " +
						"timestamp LONG, " +
						"userDeviceID INT, " +
						"userDeviceTypeID INT, " +
						"systemDeviceID VARCHAR, " +
						"systemDeviceTypeID VARCHAR, " +
						"locationID INT, " +
						"locationContextID INT, " +
						"triggerID VARCHAR, " +
						"conditionID VARCHAR, " +
						"actionID VARCHAR, " +
						"parameters VARCHAR, " +
						"userFriendly VARCHAR, " +
						"display BOOLEAN " +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO Logs (timestamp, userDeviceID, userDeviceTypeID, systemDeviceID, " +
						"systemDeviceTypeId, locationID, locationContextID, triggerID, conditionID, actionID, parameters, userFriendly, display) " +
						"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
	
	//public static final String SQL_UPDATE = "ERROR: a log should never be updated!";
	//public static final String SQL_DELETE = "ERROR: a log should never be deleted!";
	
	
	private String timestamp;
	private String userDeviceID;
	private String userDeviceTypeID;
	private String systemDeviceId;
	private String systemDeviceTypeID;
	private String locationID;
	private String locationContextId;
	private String actionID;
	private String conditionID;
	private String triggerID;
	private String paramters;
	private String userFriendly;
	private boolean display;
	private String policyNameForcingAction;
	

	public Log() {}
	
	public Log(String timestamp, String userDeviceID, String userDeviceTypeID, String systemDeviceId, String systemDeviceTypeID, 
			String locationID, String locationContextId, String actionID, String conditionID, String triggerID, String paramters, String userFriendly, boolean display) 
	{
		this.timestamp = timestamp;
		this.userDeviceID = userDeviceID;
		this.userDeviceTypeID = userDeviceTypeID;
		this.systemDeviceId = systemDeviceId;
		this.systemDeviceTypeID = systemDeviceTypeID;
		this.locationID = locationID;
		this.locationContextId = locationContextId;
		this.actionID = actionID;
		this.conditionID = conditionID;
		this.triggerID = triggerID;
		this.paramters = paramters;
		this.userFriendly = userFriendly;
		this.display = display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public void setUserDeviceTypeID(String userDeviceTypeID) {
		this.userDeviceTypeID = userDeviceTypeID;
	}
	public void setUserDeviceID(String userDeviceID) {
		this.userDeviceID = userDeviceID;
	}
	public void setTriggerID(String triggerID) {
		this.triggerID = triggerID;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public void setSystemDeviceTypeID(String systemDeviceTypeID) {
		this.systemDeviceTypeID = systemDeviceTypeID;
	}
	public void setSystemDeviceId(String systemDeviceId) {
		this.systemDeviceId = systemDeviceId;
	}
	public void setParamters(String paramters) {
		this.paramters = paramters;
	}
	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}
	public void setLocationContextId(String locationContextId) {
		this.locationContextId = locationContextId;
	}
	public void setConditionID(String conditionID) {
		this.conditionID = conditionID;
	}
	public void setActionID(String actionID) {
		this.actionID = actionID;
	}
	public boolean isPolicyDrivenAction() {
		return policyNameForcingAction!=null;
	}
	public String getPolicyNameForcingAction() {
		return policyNameForcingAction;
	}
	public void setPolicyNameForcingAction(String policyNameForcingAction) {
		this.policyNameForcingAction = policyNameForcingAction;
	}
	public String getUserFriendly() {
		return userFriendly;
	}
	
	public String getUserDeviceTypeID() {
		return userDeviceTypeID;
	}
	public String getUserDeviceID() {
		return userDeviceID;
	}
	public String getTriggerID() {
		return triggerID;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public String getSystemDeviceTypeID() {
		return systemDeviceTypeID;
	}
	public String getSystemDeviceId() {
		return systemDeviceId;
	}
	public String getParameters() {
		return paramters;
	}
	public String getLocationID() {
		return locationID;
	}
	public String getLocationContextId() {
		return locationContextId;
	}
	public String getConditionID() {
		return conditionID;
	}
	public String getActionID() {
		return actionID;
	}
	public String getDisplay() {
		return Boolean.toString(display);
	}
	public boolean isDisplay() {
		return display;
	}
	@Override
	public int getConstructorSize() {
		return 14;
	}

	@Override
	public void init(String[] params) {
		this.timestamp = params[1];
		this.userDeviceID = params[2];
		this.userDeviceTypeID = params[3];
		this.systemDeviceId = params[4];
		this.systemDeviceTypeID = params[5];
		this.locationID = params[6];
		this.locationContextId = params[7];
		this.actionID = params[8];
		this.conditionID = params[9];
		this.triggerID = params[10];
		this.paramters = params[11];
		this.userFriendly = params[12];
		this.display = Boolean.parseBoolean(params[13]);
	}

	public void setTimeStamp(Calendar instance) {
		this.timestamp = Long.toString(instance.getTimeInMillis());
	}

	public void setUserFriendly(String userFriendly) {
		this.userFriendly = userFriendly;
	}
	
}
