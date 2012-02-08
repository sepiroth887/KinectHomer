package uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.HomerComponent;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasConditions;
import uk.ac.stir.cs.homer.homerFrameworkAPI.events.EventType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.events.EventUtils;
import uk.ac.stir.cs.homer.homerPolicyServer.PolicyRegistry;
import uk.ac.stir.cs.homer.homerPolicyServer.PolicyState;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.Overlap;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.ActionEnvironEffect;
import uk.ac.stir.cs.homer.serviceDatabase.objects.ActionEnvironEffects;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Condition;
import uk.ac.stir.cs.homer.serviceDatabase.objects.IDsForUserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.LoggerQueryObject;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;
import uk.ac.stir.cs.homer.serviceJSON.JSONUtils;

public class OSGiSystemGateway implements SystemGateway{

	private Logger logger = LoggerFactory.getLogger(OSGiSystemGateway.class);
	private HomerDatabase database;
	private BundleContext context;
	private Map<String, HomerComponent> components = new HashMap<String, HomerComponent>();
	private final PolicyRegistry policyFactory;
	
	public OSGiSystemGateway(HomerDatabase database, BundleContext context, PolicyRegistry policyFactory) {
		this.database = database;
		this.context = context;
		this.policyFactory = policyFactory;
	} 
	
	public void storeNewComponent(String systemDeviceTypeID, HomerComponent c)
	{
		components.put(systemDeviceTypeID, c);
	}

	public UserDevice registerNewDevice(SystemDeviceType[] systemDeviceTypes, Map<String, String[]> parameters, String userDeviceTypeID, String userDeviceName, String locationID, String imagePath, String defaultState)
	{
		String userDeviceID = database.addUserDevice(userDeviceName, locationID, userDeviceTypeID, imagePath, defaultState);
		for (SystemDeviceType systemDeviceType : systemDeviceTypes)
		{
			String systemDeviceTypeID = systemDeviceType.getId();
			
			String systemDeviceID = database.addSystemDevice(JSONUtils.convertJavaStringArrayToJSONArray(parameters.get(systemDeviceTypeID)), systemDeviceTypeID, userDeviceID);
			database.addUserTypeToSystemType(userDeviceTypeID, systemDeviceTypeID);
		
			components.get(systemDeviceTypeID).registerComponentInstance(systemDeviceTypeID, systemDeviceID, parameters.get(systemDeviceTypeID));
		}
		return new UserDevice(userDeviceID, userDeviceName, locationID, userDeviceTypeID, imagePath, defaultState, null, null);
	}
	public void updateDevice(UserDevice userDevice, SystemDeviceType[] systemDeviceTypes, Map<String, String[]> parameters)
	{
		IDsForUserDevice originalIDs = database.getDetailsAboutUserDeviceID(userDevice.getId());
		
		Set<String> newSystemDeviceTypeIDs = new HashSet<String>();
		for (SystemDeviceType systemDeviceType : systemDeviceTypes)
		{
			newSystemDeviceTypeIDs.add(systemDeviceType.getId());
		}
		
		database.updateUserDeviceDetails(userDevice);
		
		//for each of the original system device types (before any possible changes were made)
		for (String originalSysDeviceTypeID : originalIDs.getSysDeviceTypeIDs())
		{
			SystemDevice originalSystemDevice = database.getSystemDevices(new QueryObject().systemDevice(originalIDs.getSysDeviceIDForSysDeviceTypeID(originalSysDeviceTypeID)))[0];
			SystemDevice updatedSystemDevice = new SystemDevice(originalSystemDevice.getId(), JSONUtils.convertJavaStringArrayToJSONArray(parameters.get(originalSysDeviceTypeID)), originalSysDeviceTypeID, userDevice.getId());
			
			
			String[] originalParameters = JSONUtils.convertJSONArrayToJavaStringArray(originalSystemDevice.getJsonConfigData());
			String[] newParameters = JSONUtils.convertJSONArrayToJavaStringArray(updatedSystemDevice.getJsonConfigData());
			
			
			//if the original device type is still a type for the new device
			if (newSystemDeviceTypeIDs.contains(originalSysDeviceTypeID))
			{
				//check to see if parameter values have changed
				if (newParameters.length > 0 && !originalSystemDevice.getJsonConfigData().equals(updatedSystemDevice.getJsonConfigData()))
				{
					//tell original component that the parameters for one of it's children have changed
					components.get(updatedSystemDevice.getSystemDeviceTypeID()).editComponentInstance(updatedSystemDevice.getSystemDeviceTypeID(), updatedSystemDevice.getId(), newParameters, originalParameters);
					database.updateSystemDeviceDetails(updatedSystemDevice);
					//System.out.println("editing system device: " + originalSystemDevice.getId() + " from value: " + originalSystemDevice.getJsonConfigData() + " to value: " + updatedSystemDevice.getJsonConfigData());
				}
			}
			
			//if the original device type is no longer a type for the new device
			else
			{
				//tell original component about the loss of it's device
				components.get(originalSystemDevice.getSystemDeviceTypeID()).deleteComponentInstance(originalSystemDevice.getSystemDeviceTypeID(), originalSystemDevice.getId(), originalParameters);
				database.HOMER_USE_ONLY_deleteSystemDevice(originalSystemDevice.getId());
				//System.out.println("deleting system device: " + originalSystemDevice.getId() + " with value: " + originalSystemDevice.getJsonConfigData());
			}
			
			//because this systemdevicetype is being handled here, then remove it from the list of possible new device types
			newSystemDeviceTypeIDs.remove(originalSysDeviceTypeID);
		}
	
		//any device types which werent being edited or deleted must be new, so handle it
		for (String newSystemDeviceType: newSystemDeviceTypeIDs)
		{
			String systemDeviceID = database.addSystemDevice(JSONUtils.convertJavaStringArrayToJSONArray(parameters.get(newSystemDeviceType)), newSystemDeviceType, userDevice.getId());
			database.addUserTypeToSystemType(userDevice.getTypeID(), newSystemDeviceType);
				
			//tell new component about it's new device
			components.get(newSystemDeviceType).registerComponentInstance(newSystemDeviceType,systemDeviceID, parameters.get(newSystemDeviceType));
			
			//System.out.println("adding new system device: " + systemDeviceID + " with params: " + parameters.get(newSystemDeviceType));
		}
		
	}
	@Override
	public boolean checkCondition(String userDeviceID, String conditionID, String[] parameters) {

		SystemDevice[] systemDevices = database.getSystemDevices(new QueryObject().userDevice(userDeviceID).condition(conditionID));
		if (systemDevices.length > 0)
		{
			return ((WhichHasConditions) components.get(systemDevices[0].getSystemDeviceTypeID())).checkCondition(systemDevices[0].getSystemDeviceTypeID(), systemDevices[0].getId(), conditionID, parameters);
		}
		return false;
    }

	
	@Override
	public void doActionForPolicy(String policyName, String userDeviceID, String actionID, String[] parameters) {
		
		IDsForUserDevice ids = database.getDetailsAboutUserDeviceID(userDeviceID);
		
		Condition newStateCondition = getConditionIDofNewState(userDeviceID, actionID, parameters, ids);
		
		if(newStateCondition == null)
		{
			EventUtils.postDoAction(context,database, userDeviceID, actionID, parameters, true, policyName);
		}
		else if (isStateChanged(userDeviceID, newStateCondition.getId(), parameters))
		{
			EventUtils.postDoAction(context,database, userDeviceID, actionID, parameters, true, policyName);
			
			String[] otherIDsResultingInNewState = JSONUtils.convertJSONArrayToJavaStringArray(newStateCondition.getTriggerIDsAndOrActionIDsResultingInThisState());
			
			SystemDevice systemDevice = database.getSystemDevices(new QueryObject().action(actionID).userDevice(userDeviceID))[0];

			if (otherIDsResultingInNewState.length>1)
			{
				for (String id : otherIDsResultingInNewState)
				{
					if (!id.equals(actionID))
					{
						if (database.getTriggers(new QueryObject().trigger(id)).length==1)
						{
							EventUtils.postTriggerOccured(context, database, systemDevice.getSystemDeviceTypeID(), systemDevice.getId(), id, parameters, 
									ids.getUserDeviceID(), ids.getUserDeviceTypeID(), ids.getLocationID(),
									ids.getLocationContextID(), false);
						}
						break;
					}
				}
			}
			
			EventUtils.postStateChanged(context, database, systemDevice.getSystemDeviceTypeID(), systemDevice.getId(), newStateCondition.getId(), parameters, 
					ids.getUserDeviceID(), ids.getUserDeviceTypeID(), ids.getLocationID(),
					ids.getLocationContextID(), false);

			database.updateDeviceState(database.getUserDevices(new QueryObject().systemDevice(systemDevice.getId()))[0].getId(), newStateCondition.getId(), JSONUtils.convertJavaArrayToJSONString(parameters));
		}
	}
	
	@Override
	public void doAction(String userDeviceID, String actionID, String[] parameters) {
		
		IDsForUserDevice ids = database.getDetailsAboutUserDeviceID(userDeviceID);
		
		Condition newStateCondition = getConditionIDofNewState(userDeviceID, actionID, parameters, ids);
		
		if(newStateCondition == null)
		{
			EventUtils.postDoAction(context,database, userDeviceID, actionID, parameters, true, null);
		}
		else if (isStateChanged(userDeviceID, newStateCondition.getId(), parameters))
		{
			EventUtils.postDoAction(context,database, userDeviceID, actionID, parameters, true, null);
			
			String[] otherIDsResultingInNewState = JSONUtils.convertJSONArrayToJavaStringArray(newStateCondition.getTriggerIDsAndOrActionIDsResultingInThisState());
			
			SystemDevice systemDevice = database.getSystemDevices(new QueryObject().action(actionID).userDevice(userDeviceID))[0];

			if (otherIDsResultingInNewState.length>1)
			{
				for (String id : otherIDsResultingInNewState)
				{
					if (!id.equals(actionID))
					{
						if (database.getTriggers(new QueryObject().trigger(id)).length==1)
						{
							EventUtils.postTriggerOccured(context, database, systemDevice.getSystemDeviceTypeID(), systemDevice.getId(), id, parameters, 
									ids.getUserDeviceID(), ids.getUserDeviceTypeID(), ids.getLocationID(),
									ids.getLocationContextID(), false);
						}
						break;
					}
				}
			}
			
			EventUtils.postStateChanged(context, database, systemDevice.getSystemDeviceTypeID(), systemDevice.getId(), newStateCondition.getId(), parameters, 
					ids.getUserDeviceID(), ids.getUserDeviceTypeID(), ids.getLocationID(),
					ids.getLocationContextID(), false);

			database.updateDeviceState(database.getUserDevices(new QueryObject().systemDevice(systemDevice.getId()))[0].getId(), newStateCondition.getId(), JSONUtils.convertJavaArrayToJSONString(parameters));
		}
	}
	
	private Condition getConditionIDofNewState(String userDeviceId, String actionID, String[] parameters, IDsForUserDevice idsIfAlreadyKnown) {
		IDsForUserDevice ids = idsIfAlreadyKnown==null? database.getDetailsAboutUserDeviceID(userDeviceId) : idsIfAlreadyKnown;
	
		for (String sysDeviceTypeId : ids.getSysDeviceTypeIDs())
		{
			for (Condition c : database.getConditions(new QueryObject().systemDeviceType(sysDeviceTypeId)))
			{
				List<String> possibleIDsResultingInThisCondition = JSONUtils.convertJSONArrayToJavaStringList(c.getTriggerIDsAndOrActionIDsResultingInThisState());
				if (possibleIDsResultingInThisCondition.size()>0 && possibleIDsResultingInThisCondition.contains(actionID))
				{
					return c;
				}
			}
		}
		return null;
	}
	
	public boolean isStateChanged(String userDeviceID, String conditionIDofNewState, String[] parameters)
	{
		UserDevice u = database.getUserDevices(new QueryObject().userDevice(userDeviceID))[0];
		return (u!=null && (u.getCurrentState()==null || !u.getCurrentState().equals(conditionIDofNewState)));
	}
	
	@Override
	public ServiceRegistration  registerTriggerListener(final EventListener listener,
			String sysDeviceId, String userDeviceID, String triggerID, String[] parameters) {
		EventHandler e = new EventHandler()
		{
			public void handleEvent(Event e) {
				listener.eventOccured(EventUtils.getEventType(e), EventUtils.getSysDeviceID(e), EventUtils.getUserDeviceID(e),
												EventUtils.getTriggerID(e), EventUtils.getParameters(e));
			}
		};
		return EventUtils.registerTriggerListener(context, e, null, sysDeviceId, null, userDeviceID, null, null, triggerID);
	}
	
	@Override
	public ServiceRegistration registerStateChangesListener(final EventListener listener,
			String sysDeviceId, String userDeviceID, String triggerID, String[] parameters) {
		EventHandler e = new EventHandler()
		{
			public void handleEvent(Event e) {
				listener.eventOccured(EventUtils.getEventType(e), EventUtils.getSysDeviceID(e), EventUtils.getUserDeviceID(e),
												EventUtils.getConditionID(e), EventUtils.getParameters(e));
			}
		};
		return EventUtils.registerStateChangeListener(context, e, null, sysDeviceId, null, userDeviceID, null, null, triggerID);
		
	}
	
	@Override
	public void deleteUserDevice(String userDeviceID) {
		handleSystemDevicesDeleted(database.HOMER_USE_ONLY_deleteUserDevice(userDeviceID));
	}
	@Override
	public void deleteUserDeviceType(String userDeviceTypeID) {
		handleSystemDevicesDeleted(database.HOMER_USE_ONLY_deleteUserDeviceType(userDeviceTypeID));	
	}
	@Override
	public void deleteLocation(String locationID) {
		handleSystemDevicesDeleted(database.HOMER_USE_ONLY_deleteLocation(locationID));
	}
	@Override
	public void deleteLocationContext(String locationContextID) {
		handleSystemDevicesDeleted(database.HOMER_USE_ONLY_deleteLocationContext(locationContextID));
	}

	private void handleSystemDevicesDeleted(SystemDevice[] deletedSystemDevices)
	{
		for (SystemDevice s : deletedSystemDevices)
		{
			components.get(s.getSystemDeviceTypeID()).deleteComponentInstance(s.getSystemDeviceTypeID(), s.getId(), JSONUtils.convertJSONArrayToJavaStringArray(s.getJsonConfigData()));
		}
	}
	
	@Override
	public PolicyState validateNewPolicy(Policy p)
	{
		PolicyState policyState = new PolicyState(p);
		policyState.setValidity(policyFactory.isPolicyValid(p));
		policyState.setOverlaps(policyFactory.getOverlaps(p));
		policyState.setConflicts(policyFactory.getPolicyConflicts(p, policyState.getOverlaps().keySet()));
		return policyState;
	}

	@Override
	public void addNewPolicy(Policy p)
	{
		database.savePolicy(p);
		policyFactory.add(p);	
	}
	
	@Override
	public boolean addNewPolicy(JSONObject p)
	{
		Policy policy = policyFactory.createPolicy(p);
		if (policy==null) 
			return false;
		addNewPolicy(policy);
		return true;
	}
	
	@Override
	public void deletePolicy(Policy policy)
	{
		policyFactory.deletePolicy(policy);
		database.deletePolicy(policy);
	}

	@Override
	public void enablePolicy(Policy policy, boolean selected)
	{
		if (policy.isEnabled() != selected)
		{
			policy.setEnabled(selected);
			database.updatePolicy(policy);
			policyFactory.togglePolicyEnabled(policy);
		}
	}

	@Override
	public List<Policy> getPolicies()
	{
		return policyFactory.getPolicies();
	}

	@Override
	public void updatePolicy(Policy policy)
	{
		database.updatePolicy(policy);
	}
	
	@Override
	public boolean didEventOccurWithinTime(EventType type, String userDeviceID, String eventID, String[] parameters, long earliestTime, long latestTime) 
	{
		LoggerQueryObject loggerQueryObject = new LoggerQueryObject().userDevice(userDeviceID).parameters(JSONUtils.convertJavaArrayToJSONString(parameters));
		loggerQueryObject.earliestDate(Long.toString(earliestTime));
		loggerQueryObject.latestDate(Long.toString(latestTime));
		
		switch(type)
		{
			case TRIGGER: loggerQueryObject.trigger(eventID); break;
			case CONDITION: loggerQueryObject.condition(eventID); break;
			case ACTION: loggerQueryObject.action(eventID); break;
		}
		return database.getLogs(loggerQueryObject).length>0;
	}
	
	@Override
	public boolean isDeviceInState(String userDeviceID, String id, String[] parameters) 
	{
		return database.getUserDevices(new QueryObject().userDevice(userDeviceID).currentState(id).currentStateParameters(JSONUtils.convertJavaArrayToJSONString(parameters))).length > 0;
	}

	@Override
	public SystemDeviceType[] sysDevTypesRequiringActEnvEffInfo(String userDeviceTypeID, SystemDeviceType[] systemDeviceTypes)
	{
		List<SystemDeviceType> sysDevTypesRequiring = new ArrayList<SystemDeviceType>();
		for (SystemDeviceType sysDevType : systemDeviceTypes)
		{
			//only want to get environ effects for sys dev types which has actions.
			if (database.getActions(new QueryObject().systemDeviceType(sysDevType.getId())).length == 0) continue;
			
			ActionEnvironEffect[] effects = database.getActionEnvironEffects(new QueryObject().systemDeviceType(sysDevType.getId()).userDeviceType(userDeviceTypeID));
			if (effects.length == 0)
			{
				sysDevTypesRequiring.add(sysDevType);
			}
		}
		return sysDevTypesRequiring.toArray(new SystemDeviceType[0]);
	}
	
	@Override
	public void addActionEnvironEffects(ActionEnvironEffects[] newEnvirons)
	{
		for (ActionEnvironEffects actionEnvironEffect: newEnvirons)
		{
			database.addActionEnvironEffects(actionEnvironEffect); 
		}
	}

	public PolicyRegistry getPolicyRegistry()
	{
		return policyFactory;
	}
}
