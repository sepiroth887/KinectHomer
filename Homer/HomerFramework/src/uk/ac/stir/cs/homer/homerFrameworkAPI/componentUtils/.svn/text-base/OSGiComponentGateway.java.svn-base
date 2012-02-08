package uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils;

import java.util.List;

import uk.ac.stir.cs.homer.homerFrameworkAPI.events.ComponentEventBridge;
import uk.ac.stir.cs.homer.homerFrameworkAPI.homerObjects.SystemDeviceType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.SystemGateway;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Action;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Condition;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Trigger;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDevice;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;
import uk.ac.stir.cs.homer.serviceJSON.JSONUtils;

public class OSGiComponentGateway implements ComponentGateway {
	
	private HomerDatabase database;
	private ComponentEventBridge eventBridge;
	private final SystemGateway systemGateway;
	
	public OSGiComponentGateway(HomerDatabase database, ComponentEventBridge eventBridge, SystemGateway sysHandling) {
		this.database = database;
		this.eventBridge = eventBridge;
		this.systemGateway = sysHandling;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.ComponentGateway#registerComponent(uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.Component)
	 */
	public void registerComponent(HomerComponent component) {
		SystemDeviceType[] deviceTypes = component.getSystemDeviceTypeData();
		
		for(SystemDeviceType deviceType: deviceTypes) {
			String deviceTypeId = deviceType.getId();
			
			/*SystemDeviceTypeResource[] systemDeviceTypeResources = new SystemDeviceTypeResource[deviceType.getResources().length];
			int counter = 0;
			for (DeviceTypeResource resource : deviceType.getResources())
			{
				systemDeviceTypeResources[counter] = new SystemDeviceTypeResource(deviceTypeId, resource.getResourceID(), resource.getAmount(), resource.isLocal());
				counter++;
			}
			*/
			database.addSystemDeviceType(deviceTypeId, deviceType.getName(), deviceType.getJsonConfigData());
			
			registerConfiguredDevices(component, deviceTypeId);
			
			for (Class<?> c : component.getClass().getInterfaces())
			{
				if (c == WhichHasTriggers.class) addTriggersToDB(component, deviceTypeId);
				else if (c == WhichHasConditions.class) 
				{
					addConditionsToDB(component, deviceTypeId);
				}
				else if (c == WhichHasActions.class) 
				{
					addActionsToDB(component, deviceTypeId);
					registerActionListener(component, deviceTypeId);
				}
			}
			systemGateway.storeNewComponent(deviceTypeId, component);
		}
	}

	private void registerActionListener(HomerComponent component, String deviceTypeId) {
		List<Action> actions = ((WhichHasActions) component).getActions(deviceTypeId);
		if(actions!=null && actions.size() > 0) {
			eventBridge.handleActionRequestsForSysDeviceType(component, deviceTypeId);
		}
	}

	private void registerConfiguredDevices(HomerComponent component, String systemDeviceTypeId) {
		SystemDevice[] devices = database.getSystemDevices(new QueryObject().systemDeviceType(systemDeviceTypeId));
		
		for(SystemDevice device: devices) {
			component.registerComponentInstance(systemDeviceTypeId, device.getId(), JSONUtils.convertJSONArrayToJavaStringArray(device.getJsonConfigData()));
		}
	}

	private void addTriggersToDB(HomerComponent component, String deviceTypeId) {
		List<Trigger> triggers = ((WhichHasTriggers) component).getTriggers(deviceTypeId);
		if (triggers!=null)
		{
			for(Trigger trigger : triggers) {
				database.addTrigger(deviceTypeId, trigger.getId(), trigger.getDescription(), trigger.convertParameterDataToJSONString(), trigger.getBeforeImage(), trigger.getAfterImage());
			}
		}
	}
	
	private void addConditionsToDB(HomerComponent component, String deviceTypeId) {
		List<Condition> conditions = ((WhichHasConditions) component).getConditions(deviceTypeId);
		if (conditions!=null)
		{
			for(Condition condition : conditions) {
				String idsResultingInThisCondition = JSONUtils.convertJavaArrayToJSONString(condition.getTriggerIDsAndOrActionIDsResultingInThisState());
				database.addCondition(deviceTypeId, condition.getId(), condition.getDescription(), condition.convertParameterDataToJSONString(), condition.getImage(), idsResultingInThisCondition) ;
			}
		}
	}
	
	private void addActionsToDB(HomerComponent component, String deviceTypeId) {
		List<Action> actions = ((WhichHasActions) component).getActions(deviceTypeId);
		if (actions != null)
		{
			for(Action action : actions) {
				database.addAction(deviceTypeId, action.getId(), action.getDescription(), action.convertParameterDataToJSONString(), action.getImage());
			}
		}
	}
	
	public void updateState(String systemDeviceID, String conditionIDofNewState, String[] parameters)
	{
		eventBridge.handleStateEvent(systemDeviceID, conditionIDofNewState, parameters, null);
	}
	
	public void triggerOccured(String sysDeviceID, String triggerID, String[] paramData) {
		eventBridge.triggerOccurred(sysDeviceID, triggerID, paramData);
	}



}
