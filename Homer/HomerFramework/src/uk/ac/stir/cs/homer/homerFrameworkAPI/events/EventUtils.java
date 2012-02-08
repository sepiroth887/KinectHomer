package uk.ac.stir.cs.homer.homerFrameworkAPI.events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import uk.ac.stir.cs.homer.homerFrameworkAPI.osgi.ServiceUtils;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.IDsForUserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Log;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDevice;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.LoggerQueryObject;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;
import uk.ac.stir.cs.homer.serviceJSON.JSONUtils;

public class EventUtils {

	private static final String EVENT_PREFIX = "uk/ac/stir/cs/homer";
	
	//*****************POSTS*****************************************************
	private static String formatEventTopic(String sysDeviceTypeID)
	{
		String topic = EVENT_PREFIX + "/" + (sysDeviceTypeID==null? "*" : sysDeviceTypeID);		
		return topic.replaceAll(" ", "_");
	}
	public static void postDoAction(BundleContext context, HomerDatabase database, String userDeviceID, String actionID, String[] parameters, boolean display, String policyNameForcingAction) {
		
		IDsForUserDevice ids = database.getDetailsAboutUserDeviceID(userDeviceID);
		SystemDevice systemDevice = database.getSystemDevices(new QueryObject().action(actionID).userDevice(userDeviceID))[0];
		
		Log logForDatabase = new Log();
		logForDatabase.setUserDeviceID(userDeviceID);
		logForDatabase.setUserDeviceTypeID(ids.getUserDeviceTypeID());
		logForDatabase.setSystemDeviceId(systemDevice.getId());
		logForDatabase.setSystemDeviceTypeID(systemDevice.getSystemDeviceTypeID());
		logForDatabase.setLocationID(ids.getLocationID());
		logForDatabase.setLocationContextId(ids.getLocationContextID());
		logForDatabase.setTimeStamp(Calendar.getInstance());
		logForDatabase.setActionID(actionID);
		logForDatabase.setParamters(JSONUtils.convertJavaStringArrayToJSONArray(parameters));
		logForDatabase.setDisplay(display);
		logForDatabase.setPolicyNameForcingAction(policyNameForcingAction);
		
		postEvent(context, createActionRequestEvent(logForDatabase, database));
	}
	public static void postTriggerOccured(BundleContext context, HomerDatabase database, String sysDeviceTypeID, String sysDeviceID, String triggerID,
			String[] parameters, String userDeviceID, String userDeviceTypeID, String locationID, String locationContextID, boolean display) {
		
		Log logForDatabase = new Log();
		logForDatabase.setUserDeviceID(userDeviceID);
		logForDatabase.setUserDeviceTypeID(userDeviceTypeID);
		logForDatabase.setSystemDeviceId(sysDeviceID);
		logForDatabase.setSystemDeviceTypeID(sysDeviceTypeID);
		logForDatabase.setLocationID(locationID);
		logForDatabase.setLocationContextId(locationContextID);
		logForDatabase.setTimeStamp(Calendar.getInstance());
		logForDatabase.setTriggerID(triggerID);
		logForDatabase.setParamters(JSONUtils.convertJavaStringArrayToJSONArray(parameters));
		logForDatabase.setDisplay(display);
		
		postEvent(context, createTriggerOccurredEvent(logForDatabase, database));
	}
	public static void postStateChanged(BundleContext context,HomerDatabase database,
			String sysDeviceTypeID, String systemDeviceID,
			String conditionIDofNewState, String[] parameters,
			String userDeviceID, String userDeviceTypeID, String locationID,
			String locationContextID, boolean display) {
		
		Log logForDatabase = new Log();
		logForDatabase.setUserDeviceID(userDeviceID);
		logForDatabase.setUserDeviceTypeID(userDeviceTypeID);
		logForDatabase.setSystemDeviceId(systemDeviceID);
		logForDatabase.setSystemDeviceTypeID(sysDeviceTypeID);
		logForDatabase.setLocationID(locationID);
		logForDatabase.setLocationContextId(locationContextID);
		logForDatabase.setTimeStamp(Calendar.getInstance());
		logForDatabase.setConditionID(conditionIDofNewState);
		logForDatabase.setParamters(JSONUtils.convertJavaStringArrayToJSONArray(parameters));
		logForDatabase.setDisplay(display);
		
		postEvent(context, createStateChangedEvent(logForDatabase, database));
	}
	private static void postEvent(BundleContext context, Event e)
	{
		Logger.getLogger(EventUtils.class.getName()).log(Level.INFO, "Posting Event: " + EventUtils.formatEventForDisplay(e));
		EventUtils.getEventAdmin(context).postEvent(e);
	}
	//****************************************************************************
	private static void setUserFriendlyName(Log log, HomerDatabase database)
	{
		StringBuffer sb = new StringBuffer();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Long.parseLong(log.getTimestamp()));
		sb.append(dateFormat.format(c.getTime()));
		
		String formattedParameters = "";
		String[] parameters = JSONUtils.convertJSONArrayToJavaStringArray(log.getParameters());
		if (parameters.length>0)
		{
			formattedParameters = (" (with information: ");
			for (int i = 0; i<parameters.length; i++)
			{
				if (i>0) sb.append(", ");
				formattedParameters+=(parameters[i]);
			}
			formattedParameters+=(")");
		};
		
		if (log.getTriggerID()!=null)
		{
			sb.append(database.getUserDevices(new QueryObject().userDevice(log.getUserDeviceID()))[0].getName());				
			sb.append(" (in " +database.getLocations(new QueryObject().location(log.getLocationID()))[0].getName());				
			sb.append(" at " +database.getLocationContexts(new QueryObject().locationContext(log.getLocationContextId()))[0].getName());				
			sb.append(") " + database.getTriggers(new QueryObject().trigger(log.getTriggerID()))[0].getDescription());
			sb.append(formattedParameters);
		}
		else if (log.getConditionID()!=null)
		{
			sb.append(database.getUserDevices(new QueryObject().userDevice(log.getUserDeviceID()))[0].getName());				
			sb.append(" (in " +database.getLocations(new QueryObject().location(log.getLocationID()))[0].getName());				
			sb.append(" at " +database.getLocationContexts(new QueryObject().locationContext(log.getLocationContextId()))[0].getName());				
			sb.append(") " + database.getConditions(new QueryObject().condition(log.getConditionID()))[0].getDescription());
			sb.append(formattedParameters);
		}
		else
		{
			sb.append(log.isPolicyDrivenAction()? "The \"" + log.getPolicyNameForcingAction() + "\" policy asked the " : "Homer asked the ");
			sb.append(database.getUserDevices(new QueryObject().userDevice(log.getUserDeviceID()))[0].getName());				
			sb.append(" (in " +database.getLocations(new QueryObject().location(log.getLocationID()))[0].getName());				
			sb.append(" at " +database.getLocationContexts(new QueryObject().locationContext(log.getLocationContextId()))[0].getName());				
			sb.append(") to ");
			sb.append(database.getActions(new QueryObject().action(log.getActionID()))[0].getDescription());
			sb.append(formattedParameters);
		}

		log.setUserFriendly(sb.toString());

	}
	
	public static String formatEventForDisplay(Event e) {
		StringBuffer sb = new StringBuffer();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append(dateFormat.format(getDate(e).getTime()));
		
		switch(EventType.valueOf(e.getProperty("EventType").toString()))
		{
			case TRIGGER: 				{sb.append(" TRIGGER "); break; }
			case ACTION: 				{sb.append(" ACTION "); break; }
			case CONDITION: 				{sb.append(" STATE CHANGED "); break; }
			default: break;
		}
		sb.append("[SysDeviceID: "+ getSysDeviceID(e) + "] ");
		switch(EventType.valueOf(e.getProperty("EventType").toString()))
		{
			case TRIGGER: 				{sb.append("Trigger: "); break; }
			case ACTION: 				{sb.append("Action: "); break; }
			case CONDITION: 				{sb.append("Condition: "); break; }
			default: break;
		}
		sb.append(getEventID(e));
		
		String[] parameters = (String[]) e.getProperty("Parameters");
		if (parameters == null || parameters.length==0)
		{
			sb.append(" and no parameters");
		}
		else
		{
			sb.append(" and with parameters: ");
			for (int i = 0; i<parameters.length; i++)
			{
				if (i>0) sb.append(", ");
				sb.append(parameters[i]);
			}
		}
		sb.append(". SystemDeviceType: " + EventUtils.getSysDeviceTypeID(e));
		sb.append(", UserDeviceType: " + EventUtils.getUserDeviceTypeID(e));
		sb.append(", UserDevice: " + EventUtils.getUserDeviceID(e));
		sb.append(", Location: " + EventUtils.getLocationID(e));
		sb.append(", LocationContext: " + EventUtils.getLocationContextID(e));
		sb.append(". ");
		return sb.toString();
	}
	
	public static String formatEventForDisplay(Event e, HomerDatabase database) {
		Log l = getLog(e, database);
		return l.getUserFriendly();
	}
	
	private static Event createTriggerOccurredEvent(Log log, HomerDatabase database)
	{
		Map<String, Object> h = createEventMap(log, database);
		h.put("EventID", log.getTriggerID());
		h.put("EventType", EventType.TRIGGER.toString());
		return new Event(EventUtils.formatEventTopic(log.getSystemDeviceTypeID()), h);
	}
	
	private static Event createStateChangedEvent(Log log, HomerDatabase database)
	{
		Map<String, Object> h = createEventMap(log, database);
		h.put("EventID", log.getConditionID());
		h.put("EventType", EventType.CONDITION.toString());
		return new Event(EventUtils.formatEventTopic(log.getSystemDeviceTypeID()), h);
	}
	
	private static Event createActionRequestEvent(Log log, HomerDatabase database)
	{
		Map<String, Object> h = createEventMap(log, database);
		h.put("EventID", log.getActionID());
		h.put("EventType", EventType.ACTION.toString());
		return new Event(EventUtils.formatEventTopic(log.getSystemDeviceTypeID()), h);
	}

	private static Map<String, Object> createEventMap(Log log, HomerDatabase database) {
		Map<String, Object> h = new HashMap<String, Object>();
		h.put("date", log.getTimestamp());
		h.put("SysDeviceID", log.getSystemDeviceId());
		h.put("UserDeviceTypeID", log.getUserDeviceTypeID());
		h.put("UserDeviceID", log.getUserDeviceID());
		h.put("LocationID", log.getLocationID());
		h.put("LocationContextID", log.getLocationContextId());
		
		if (log.getParameters() !=null)
		{
			h.put("Parameters", JSONUtils.convertJSONArrayToJavaStringArray(log.getParameters()));
		}
		
		setUserFriendlyName(log, database);
		h.put("logId", database.saveLog(log));
		
		return h;
	}
	//********************LISTENERS**************************************
	private static ServiceRegistration registerListener(BundleContext context,
									EventHandler eventHandler, EventType eventType, 
									String sysDeviceTypeID, String sysDeviceID, 
									String userDeviceTypeID, String userDeviceID, String locationID, 
									String locationContextID, String eventID) {
		
		sysDeviceTypeID = sysDeviceTypeID==null? "*" : sysDeviceTypeID;

		String[] topics = new String[] {EventConstants.EVENT_TOPIC, formatEventTopic(sysDeviceTypeID)};
		
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.put(EventConstants.EVENT_TOPIC, topics);
		
		ArrayList<String> eventFilters = new ArrayList<String>();
		
		eventFilters.add("(EventType="+			eventType.toString()	+")");
		
		if (sysDeviceID!=null && !sysDeviceID.equals("*"))
			eventFilters.add("(SysDeviceID="+		sysDeviceID			+")");
		
		if (userDeviceTypeID!=null && !userDeviceTypeID.equals("*"))
			eventFilters.add("(UserDeviceTypeID="+	userDeviceTypeID	+")");
		
		if (userDeviceID!=null && !userDeviceID.equals("*"))
			eventFilters.add("(UserDeviceID="+		userDeviceID		+")");
		
		if (locationID!=null && !locationID.equals("*"))
			eventFilters.add("(LocationID="+			locationID			+")");
		
		if (locationContextID!=null && !locationContextID.equals("*"))
			eventFilters.add("(LocationContextID="+	locationContextID	+")");
		
		if (eventID!=null && !eventID.equals("*"))
			eventFilters.add("(EventID="+	eventID	+")");
		
		
		StringBuilder filter = new StringBuilder();
		for (String eventFilter:eventFilters)
		{
			filter.append(eventFilter);
		}
		if (eventFilters.size()>1)
		{
			filter.insert(0, "(&");
			filter.append(")");
		}
		ht.put(EventConstants.EVENT_FILTER, filter.toString());
				
		StringBuffer sb = new StringBuffer(0);
		switch (eventType)
		{
			case TRIGGER: 				{sb.append("TRIGGER"); break; }
			case ACTION: 				{sb.append("ACTION"); break; }
			case CONDITION: 				{sb.append("STATE"); break; }
			default: break;
		}
		sb.append(" listener registered for SysDeviceID:"+sysDeviceID +", SysDeviceTypeID:" + sysDeviceTypeID +
					", UserDeviceTypeID:" + userDeviceTypeID +", UserDeviceID:" + userDeviceID +
					", LocationID:" + locationID +", LocationContextID:" + locationContextID +", EventID:" + eventID);
		Logger.getLogger(EventUtils.class.getName()).info(sb.toString());
		
		return context.registerService(EventHandler.class.getName(), eventHandler, ht);
	}
	
	public static ServiceRegistration registerActionListener(BundleContext context, 
								EventHandler eventHandler, String sysDeviceTypeId, String sysDeviceID, 
										String userDeviceTypeID, String userDeviceID, String locationID, 
														String locationContextID, String actionID) {
		
		return registerListener(context, eventHandler, EventType.ACTION, sysDeviceTypeId, sysDeviceID, userDeviceTypeID, 
									userDeviceID, locationID, locationContextID, actionID);
	}
	
	public static ServiceRegistration registerTriggerListener(BundleContext context,
								EventHandler eventHandler, String sysDeviceTypeId, String sysDeviceID, 
								String userDeviceTypeID, String userDeviceID, String locationID, 
								String locationContextID, String triggerID) {
		return registerListener(context, eventHandler, EventType.TRIGGER, sysDeviceTypeId, sysDeviceID, userDeviceTypeID, 
									userDeviceID, locationID, locationContextID, triggerID);
	}
	public static ServiceRegistration registerStateChangeListener(BundleContext context,
			EventHandler eventHandler, String sysDeviceTypeId, String sysDeviceID, 
			String userDeviceTypeID, String userDeviceID, String locationID, 
			String locationContextID, String stateID) {
		return registerListener(context, eventHandler, EventType.CONDITION, sysDeviceTypeId, sysDeviceID, userDeviceTypeID, 
				userDeviceID, locationID, locationContextID, stateID);
	}
	//********************************************************************

	
	
	
	//********************BASIC GETS**************************************
	public static String getSysDeviceID(Event e)
	{
		return (String) e.getProperty("SysDeviceID");
	}
	public static String getSysDeviceTypeID(Event e) {
		return e.getTopic().substring(e.getTopic().lastIndexOf("/")+1);
	}
	private static String getLocationContextID(Event e) {
		return (String) e.getProperty("LocationContextID");
	}
	private static String getLocationID(Event e) {
		return (String) e.getProperty("LocationID");
	}
	public static String getUserDeviceID(Event e) {
		return (String) e.getProperty("UserDeviceID");
	}
	private static String getUserDeviceTypeID(Event e) {
		return (String) e.getProperty("UserDeviceTypeID");
	}
	public static String getActionID(Event e) {
		return (String) e.getProperty("EventID");
	}
	public static String getConditionID(Event e) {
		return (String) e.getProperty("EventID");
	}
	public static String getTriggerID(Event e) {
		return (String) e.getProperty("EventID");
	}
	private static String getEventID(Event e) {
		return (String) e.getProperty("EventID");
	}
	private static Calendar getDate(Event e) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Long.parseLong((String)e.getProperty("date")));
		return c;
	}
	public static boolean getConditionResult(Event e) {
		return (Boolean) e.getProperty("Result");
	}
	public static String[] getParameters(Event e) {
		Object property = e.getProperty("Parameters");
		if (property!=null)
		{
			return (String[]) property;
		}
		return new String[0];
	}
	public static Log getLog(Event e, HomerDatabase database) {
		 return database.getLogs(new LoggerQueryObject().id((String)e.getProperty("logId")))[0];
	}
	public static EventType getEventType(Event e) {
		return EventType.valueOf((String) e.getProperty("EventType"));
	}
	private static EventAdmin getEventAdmin(BundleContext context)
	{
		return (EventAdmin) ServiceUtils.getService(context, EventAdmin.class);
	}
	//********************************************************************

}
