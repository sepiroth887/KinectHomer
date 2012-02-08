package uk.ac.stir.cs.homer.homerFrameworkAPI.events;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.HomerComponent;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasActions;
import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.SystemGateway;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Condition;
import uk.ac.stir.cs.homer.serviceDatabase.objects.IDsForSystemDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;
import uk.ac.stir.cs.homer.serviceJSON.JSONUtils;

public class OSGiComponentEventBridge implements ComponentEventBridge {

	private BundleContext context;
	private HomerDatabase database;
	
	public OSGiComponentEventBridge(BundleContext context, HomerDatabase database) {
		this.context = context;
		this.database = database;
	}

	@Override
	public void handleActionRequestsForSysDeviceType(final HomerComponent component, String sysDeviceTypeId) {
		EventHandler e = new EventHandler() {
			public void handleEvent(Event e) {
				Logger.getLogger(this.getClass().toString()).info("Action requested to happen!! " + e.getTopic());
				((WhichHasActions) component).performAction(EventUtils.getSysDeviceTypeID(e), EventUtils.getSysDeviceID(e), EventUtils.getActionID(e), EventUtils.getParameters(e));
				//handleStateEvent(EventUtils.getSysDeviceID(e), EventUtils.getActionID(e), EventUtils.getParameters(e), null);
			}
		};
		EventUtils.registerActionListener(context, e, sysDeviceTypeId, null, null, null, null, null, null);
	}

	@Override
	public void triggerOccurred(String sysDeviceID, String triggerID, String[] parameters) {
		
		IDsForSystemDevice ids = database.getDetailsAboutSysDeviceID(sysDeviceID);
		
		String conditionIDofNewState = getConditionIDofNewState(sysDeviceID, triggerID, parameters, ids);
		
		if(conditionIDofNewState == null)
		{
			long currentTime = Calendar.getInstance().getTimeInMillis();
			long timeBeforeRepost = Long.parseLong(System.getProperty("uk.ac.stir.cs.homer.triggers.timeBeforeRepost")) * 1000;
			
			// Only post trigger if it hasn't already happened within the past x minutes, where x is defined in seconds 
			// in system property: "uk.ac.stir.cs.homer.triggers.timeBeforeRepost"
			if (!SystemGateway.Singleton.get().didEventOccurWithinTime(EventType.TRIGGER, ids.getUserDeviceID(), triggerID, parameters, currentTime-timeBeforeRepost, currentTime))
				EventUtils.postTriggerOccured(context, database, ids.getSysDeviceTypeID(), sysDeviceID, triggerID, parameters, 
									ids.getUserDeviceID(), ids.getUserDeviceTypeID(), ids.getLocationID(),
									ids.getLocationContextID(), true);
		}
		else if (isStateChanged(sysDeviceID, conditionIDofNewState, parameters, ids))
		{
			EventUtils.postTriggerOccured(context,database, ids.getSysDeviceTypeID(), sysDeviceID, triggerID, parameters, 
					ids.getUserDeviceID(), ids.getUserDeviceTypeID(), ids.getLocationID(),
					ids.getLocationContextID(), true);
			
			EventUtils.postStateChanged(context, database,ids.getSysDeviceTypeID(), sysDeviceID, conditionIDofNewState, parameters, 
					ids.getUserDeviceID(), ids.getUserDeviceTypeID(), ids.getLocationID(),
					ids.getLocationContextID(), false);

			database.updateDeviceState(database.getUserDevices(new QueryObject().systemDevice(sysDeviceID))[0].getId(), conditionIDofNewState, JSONUtils.convertJavaArrayToJSONString(parameters));
		}
	}

	private String getConditionIDofNewState(String sysDeviceID, String triggerID, String[] parameters, IDsForSystemDevice idsIfAlreadyKnown) {
		IDsForSystemDevice ids = idsIfAlreadyKnown==null? database.getDetailsAboutSysDeviceID(sysDeviceID) : idsIfAlreadyKnown;
		for (Condition c : database.getConditions(new QueryObject().systemDeviceType(ids.getSysDeviceTypeID())))
		{
			List<String> possibleIDsResultingInThisCondition = JSONUtils.convertJSONArrayToJavaStringList(c.getTriggerIDsAndOrActionIDsResultingInThisState());
			if (possibleIDsResultingInThisCondition.size()>0 && possibleIDsResultingInThisCondition.contains(triggerID))
			{
				return c.getId();
			}
		}
		return null;
	}

	@Override
	public boolean isStateChanged(String systemDeviceID, String conditionIDofNewState, String[] parameters, IDsForSystemDevice idsIfAlreadyKnown)
	{
		IDsForSystemDevice ids = idsIfAlreadyKnown==null? database.getDetailsAboutSysDeviceID(systemDeviceID) : idsIfAlreadyKnown;
		
		UserDevice u = database.getUserDevices(new QueryObject().userDevice(ids.getUserDeviceID()))[0];
		return (u!=null && (u.getCurrentState()==null || !u.getCurrentState().equals(conditionIDofNewState)));
	}
		
	@Override
	public void handleStateEvent(String sysDeviceID, String triggerID, String[] parameters, IDsForSystemDevice idsIfAlreadyKnown)
	{
		IDsForSystemDevice ids = database.getDetailsAboutSysDeviceID(sysDeviceID);
		
		String conditionIDofNewState = getConditionIDofNewState(sysDeviceID, triggerID, parameters, ids);
		
		if (conditionIDofNewState!=null && isStateChanged(sysDeviceID, conditionIDofNewState, parameters, ids))
		{
			EventUtils.postStateChanged(context, database,ids.getSysDeviceTypeID(), sysDeviceID, conditionIDofNewState, parameters, 
					ids.getUserDeviceID(), ids.getUserDeviceTypeID(), ids.getLocationID(),
					ids.getLocationContextID(), true);

			database.updateDeviceState(database.getUserDevices(new QueryObject().systemDevice(sysDeviceID))[0].getId(), conditionIDofNewState, JSONUtils.convertJavaArrayToJSONString(parameters));
		}
	}
}
