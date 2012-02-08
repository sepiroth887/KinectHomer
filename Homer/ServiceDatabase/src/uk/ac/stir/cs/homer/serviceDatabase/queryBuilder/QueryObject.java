package uk.ac.stir.cs.homer.serviceDatabase.queryBuilder;

public class QueryObject {
	private String userDeviceTypeID;
	private String userDeviceID; 
	private String systemDeviceTypeID;
	private String systemDeviceID; 
	private String locationContextID;
	private String locationID; 
	private String triggerID;
	private String conditionID;
	private String actionID;
	private String currentState;
	private String currentStateParameters;
	
	private String environID;
	
	public QueryObject() {}
	
	public QueryObject(String userDeviceTypeID, String userDeviceID,
			String systemDeviceTypeID, String systemDeviceID,
			String locationContextID, String locationID, String triggerID,
			String conditionID, String actionID, String environID) {
				this.userDeviceTypeID = userDeviceTypeID;
				this.userDeviceID = userDeviceID;
				this.systemDeviceTypeID = systemDeviceTypeID;
				this.systemDeviceID = systemDeviceID;
				this.locationContextID = locationContextID;
				this.locationID = locationID;
				this.triggerID = triggerID;
				this.conditionID = conditionID;
				this.actionID = actionID;
				this.environID = environID;
	}

	public QueryObject environ(String environID) {
		this.environID = environID;
		return this;
	}

	
	public QueryObject userDeviceType(String userDeviceTypeID) {
		this.userDeviceTypeID = userDeviceTypeID;
		return this;
	}
	
	public QueryObject userDevice(String userDeviceID) {
		this.userDeviceID = userDeviceID;
		return this;
	}
	
	public QueryObject systemDeviceType(String systemDeviceTypeID) {
		this.systemDeviceTypeID = systemDeviceTypeID;
		return this;
	}

	public QueryObject systemDevice(String systemDevice) {
		this.systemDeviceID = systemDevice;
		return this;
	}

	public QueryObject locationContext(String locationContextID) {
		this.locationContextID = locationContextID;
		return this;
	}

	public QueryObject location(String locationID) {
		this.locationID = locationID;
		return this;
	}
	
	public QueryObject trigger(String triggerID) {
		this.triggerID = triggerID;
		return this;
	}
	public QueryObject condition(String conditionID) {
		this.conditionID = conditionID;
		return this;
	}
	public QueryObject action(String actionID) {
		this.actionID = actionID;
		return this;
	}
	public QueryObject currentState(String currentState) {
		this.currentState = currentState;
		return this;
	}
	public QueryObject currentStateParameters(String currentStateParameters) {
		this.currentStateParameters = currentStateParameters;
		return this;
	}
	
	public String getEnvironID()
	{
		return environID;
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
	public String getCurrentState() {
		return currentState;
	}
	public String getCurrentStateParameters() {
		return currentStateParameters;
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
