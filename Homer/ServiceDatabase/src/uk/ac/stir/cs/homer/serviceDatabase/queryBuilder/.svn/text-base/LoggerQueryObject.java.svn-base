package uk.ac.stir.cs.homer.serviceDatabase.queryBuilder;

public class LoggerQueryObject {
	private String id;
	private String earliestDate;
	private String latestDate;
	private String maxNumEntries;
	private String userDeviceTypeID;
	private String userDeviceID; 
	private String systemDeviceTypeID;
	private String systemDeviceID; 
	private String locationContextID;
	private String locationID; 
	private String triggerID;
	private String conditionID;
	private String actionID;
	private String parameters;
	private String display;
	
	public LoggerQueryObject() {}
	public LoggerQueryObject(String earliestDate, String latestDate, String maxNumEntries, String userDeviceTypeID, 
			String userDeviceId, String systemDeviceID, String systemDeviceTypeID, String locationID, String locationContextID, 
			String actionID, String triggerID, String conditionID, String parameters, boolean display) 
	{
		this.earliestDate = earliestDate;
		this.latestDate = latestDate;
		this.maxNumEntries = maxNumEntries;
		this.userDeviceTypeID = userDeviceTypeID;
		this.userDeviceID = userDeviceId;
		this.systemDeviceID = systemDeviceID;
		this.systemDeviceTypeID = systemDeviceTypeID;
		this.locationID = locationID;
		this.locationContextID = locationContextID;
		this.actionID = actionID;
		this.triggerID = triggerID;
		this.conditionID = conditionID;
		this.parameters = parameters;
		display(display);
	}
	
	public LoggerQueryObject id(String id) {
		this.id = id;
		return this;
	}
	
	public LoggerQueryObject userDeviceType(String userDeviceTypeID) {
		this.userDeviceTypeID = userDeviceTypeID;
		return this;
	}
	
	public LoggerQueryObject userDevice(String userDeviceID) {
		this.userDeviceID = userDeviceID;
		return this;
	}
	
	public LoggerQueryObject systemDeviceType(String systemDeviceTypeID) {
		this.systemDeviceTypeID = systemDeviceTypeID;
		return this;
	}

	public LoggerQueryObject systemDevice(String systemDevice) {
		this.systemDeviceID = systemDevice;
		return this;
	}

	public LoggerQueryObject locationContext(String locationContextID) {
		this.locationContextID = locationContextID;
		return this;
	}

	public LoggerQueryObject location(String locationID) {
		this.locationID = locationID;
		return this;
	}
	
	public LoggerQueryObject trigger(String triggerID) {
		this.triggerID = triggerID;
		return this;
	}
	public LoggerQueryObject condition(String conditionID) {
		this.conditionID = conditionID;
		return this;
	}
	public LoggerQueryObject action(String actionID) {
		this.actionID = actionID;
		return this;
	}
	
	public LoggerQueryObject earliestDate(String earliestDate) {
		this.earliestDate = earliestDate;
		return this;
	}
	public LoggerQueryObject latestDate(String latestDate) {
		this.latestDate = latestDate;
		return this;
	}
	public LoggerQueryObject maxNumEntries(String maxNumEntries) {
		this.maxNumEntries = maxNumEntries;
		return this;
	}
	public LoggerQueryObject parameters(String parameters) {
		this.parameters = parameters;
		return this;
	}
	public LoggerQueryObject display(boolean display) {
		this.display = Boolean.toString(display);
		return this;
	}
	
	public String getEarliestDate() {
		return earliestDate;
	}
	public String getId() {
		return id;
	}
	public String getLatestDate() {
		return latestDate;
	}
	public String getMaxNumEntries() {
		return maxNumEntries;
	}
	public String getParameters() {
		return parameters;
	}
	public String getActionID() {
		return actionID;
	}
	public String getConditionID() {
		return conditionID;
	}
	public String getLocationContextID() {
		return locationContextID;
	}
	public String getLocationID() {
		return locationID;
	}
	public String getSystemDeviceID() {
		return systemDeviceID;
	}
	public String getSystemDeviceTypeID() {
		return systemDeviceTypeID;
	}
	public String getTriggerID() {
		return triggerID;
	}
	public String getUserDeviceID() {
		return userDeviceID;
	}
	public String getUserDeviceTypeID() {
		return userDeviceTypeID;
	}
	public String getDisplay() {
		return display;
	}
	public boolean mustHaveTriggers()
	{
		return isAStar(triggerID);
	}
	public boolean mustHaveConditions()
	{
		return isAStar(conditionID);
	}
	public boolean mustHaveActions()
	{
		return isAStar(actionID);
	}
	public boolean canBeAnyLocationContext()
	{
		return isAStar(locationContextID);
	}
	public boolean canBeAnyLocation()
	{
		return isAStar(locationID);
	}
	public boolean canBeAnyUserDeviceType()
	{
		return isAStar(userDeviceTypeID);
	}
	public boolean canBeAnyUserDevice()
	{
		return isAStar(userDeviceID);
	}
	public boolean canBeAnySystemDeviceType()
	{
		return isAStar(systemDeviceTypeID);
	}
	public boolean canBeAnySystemDevice()
	{
		return isAStar(systemDeviceID);
	}
	private boolean isAStar(String property)
	{
		return property!=null && property.equals("*");
	}
}
