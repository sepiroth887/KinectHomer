package uk.ac.stir.cs.homer.componentX10;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.HomerComponent;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasActions;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasConditions;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasTriggers;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.encoding.IDUtil;
import uk.ac.stir.cs.homer.homerFrameworkAPI.configData.ConfigData;
import uk.ac.stir.cs.homer.homerFrameworkAPI.configData.ConfigPart;
import uk.ac.stir.cs.homer.homerFrameworkAPI.homerObjects.SystemDeviceType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.Comparitor;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.HomerNumber;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.HomerText;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.Parameter;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.ParameterData;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.Unit;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Action;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Condition;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Trigger;


public class X10Component implements HomerComponent, WhichHasTriggers, WhichHasConditions, WhichHasActions
{
	static final String X10_APPLIANCE_MODULE_SYSTEM_TYPE  = IDUtil.getUniqueIdentifier(X10Component.class, "X10 Appliance Module");
	static final String X10_LAMP_MODULE_SYSTEM_TYPE  = IDUtil.getUniqueIdentifier(X10Component.class, "X10 Lamp Module");
	
	static final String AM_TURN_ON_ID = IDUtil.getUniqueIdentifier(X10Component.class, "am turn on");
	static final String AM_TURN_OFF_ID = IDUtil.getUniqueIdentifier(X10Component.class, "am turn off");
	
	static final String AM_IS_ON_ID = IDUtil.getUniqueIdentifier(X10Component.class, "am is on");
	static final String AM_IS_OFF_ID = IDUtil.getUniqueIdentifier(X10Component.class, "am is off");
	
	static final String AM_TURNS_ON_ID = IDUtil.getUniqueIdentifier(X10Component.class, "am turned on");
	static final String AM_TURNS_OFF_ID = IDUtil.getUniqueIdentifier(X10Component.class, "am turned off");
	
	static final String LM_TURN_ON_ID = IDUtil.getUniqueIdentifier(X10Component.class, "lm turn on");
	static final String LM_TURN_OFF_ID = IDUtil.getUniqueIdentifier(X10Component.class, "lm turn off");
	static final String LM_DIM_ID = IDUtil.getUniqueIdentifier(X10Component.class, "lm dim");
	
	static final String LM_IS_ON_ID = IDUtil.getUniqueIdentifier(X10Component.class, "lm is on");
	static final String LM_IS_OFF_ID = IDUtil.getUniqueIdentifier(X10Component.class, "lm is off");
	
	static final String LM_TURNS_ON_ID = IDUtil.getUniqueIdentifier(X10Component.class, "lm turned on");
	static final String LM_TURNS_OFF_ID = IDUtil.getUniqueIdentifier(X10Component.class, "lm turned off");

	public static final String COMMPORT = System.getProperty("uk.ac.stir.cs.homer.componentX10.port");
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private X10Commander x10commander;
	
	public X10Component() {
		x10commander = new X10Commander();
	}
	
	@Override
	public SystemDeviceType[] getSystemDeviceTypeData() {
		
		ConfigPart configPart = new ConfigPart("X10 Code:", new HomerText("A1", 2, 3, Unit.NONE) ,null);
		ConfigData configData = new ConfigData(configPart);
		return new SystemDeviceType[] { 
				new SystemDeviceType(X10_LAMP_MODULE_SYSTEM_TYPE, "X10 Lamp Module", configData),
				new SystemDeviceType(X10_APPLIANCE_MODULE_SYSTEM_TYPE, "X10 Appliance Module", configData)};
	}
	
	@Override
	public List<Action> getActions(String deviceTypeID) {
		List<Action> actions = new ArrayList<Action>();
		
		if (X10_LAMP_MODULE_SYSTEM_TYPE.equals(deviceTypeID))
		{
			actions.add(new Action(LM_TURN_ON_ID, "turn on", Action.DEFAULT_LIGHT_ON_IMAGE));
			actions.add(new Action(LM_TURN_OFF_ID,"turn off", Action.DEFAULT_LIGHT_OFF_IMAGE));
			ParameterData paramData = new ParameterData(new Parameter(new HomerNumber(0, 100, 10, 0, 50, Unit.PERCENT), Comparitor.EQUAL));
			actions.add(new Action(LM_DIM_ID, "set light level", Action.DEFAULT_LIGHT_ON_IMAGE, paramData));
		}
		else if (X10_APPLIANCE_MODULE_SYSTEM_TYPE.equals(deviceTypeID))
		{
			actions.add(new Action(AM_TURN_ON_ID, "turn on", Action.DEFAULT_TURN_ON_IMAGE));
			actions.add(new Action(AM_TURN_OFF_ID,"turn off", Action.DEFAULT_TURN_OFF_IMAGE));
		}
		return actions;
	}

	@Override
	public List<Condition> getConditions(String deviceTypeID) {
		List<Condition> conditions = new ArrayList<Condition>();
		
		if (X10_LAMP_MODULE_SYSTEM_TYPE.equals(deviceTypeID))
		{
			conditions.add(new Condition(LM_IS_ON_ID, "is on", Condition.DEFAULT_LIGHT_ON_IMAGE, null, LM_TURNS_ON_ID, LM_TURN_ON_ID, LM_DIM_ID));
			conditions.add(new Condition(LM_IS_OFF_ID, "is off", Condition.DEFAULT_LIGHT_OFF_IMAGE, null, LM_TURNS_OFF_ID, LM_TURN_OFF_ID));
		}
		else if (X10_APPLIANCE_MODULE_SYSTEM_TYPE.equals(deviceTypeID))
		{
			conditions.add(new Condition(AM_IS_ON_ID, "is on", Condition.DEFAULT_IS_ON_IMAGE, null, AM_TURNS_ON_ID, AM_TURN_ON_ID ));
			conditions.add(new Condition(AM_IS_OFF_ID,"is off", Condition.DEFAULT_IS_OFF_IMAGE, null, AM_TURNS_OFF_ID, AM_TURN_OFF_ID));
		}
		return conditions;
	}

	@Override
	public List<Trigger> getTriggers(String deviceTypeID) {
		List<Trigger> triggers = new ArrayList<Trigger>();
		if (X10_LAMP_MODULE_SYSTEM_TYPE.equals(deviceTypeID))
		{
			triggers.add(new Trigger(LM_TURNS_ON_ID, "turns on", Trigger.DEFAULT_LIGHT_OFF_IMAGE, Trigger.DEFAULT_LIGHT_ON_IMAGE));
			triggers.add(new Trigger(LM_TURNS_OFF_ID, "turns off", Trigger.DEFAULT_LIGHT_ON_IMAGE, Trigger.DEFAULT_LIGHT_OFF_IMAGE));
		}
		else if (X10_APPLIANCE_MODULE_SYSTEM_TYPE.equals(deviceTypeID))
		{
			triggers.add(new Trigger(AM_TURNS_ON_ID, "turns on", Trigger.DEFAULT_IS_OFF_IMAGE, Trigger.DEFAULT_IS_ON_IMAGE));
			triggers.add(new Trigger(AM_TURNS_OFF_ID, "turns off", Trigger.DEFAULT_IS_ON_IMAGE, Trigger.DEFAULT_IS_OFF_IMAGE));
		}
		
		return triggers;
	}
	
	@Override
	public boolean checkCondition(String deviceTypeID, String deviceID, String conditionID, String[] parameters) 
	{
		return false;
	}
	
	@Override
	public void performAction(String deviceTypeID, String deviceID, String actionID, String[] parameters) {
	
		if (LM_TURN_OFF_ID.equals(actionID) )
		{
			x10commander.turnOff(deviceID);
			System.err.println("X10 LM OFF Event received");
			//ComponentGateway.Singleton.get().triggerOccured(deviceID, LM_TURNS_OFF_ID, null);
		}
		else if (AM_TURN_OFF_ID.equals(actionID))
		{
			x10commander.turnOff(deviceID);
			System.err.println("X10 AM OFF Event received");
			//ComponentGateway.Singleton.get().triggerOccured(deviceID, AM_TURNS_OFF_ID, null);
		}
		else if (LM_TURN_ON_ID.equals(actionID))
		{
			x10commander.turnOn(deviceID);
			//ComponentGateway.Singleton.get().triggerOccured(deviceID, LM_TURNS_ON_ID, null);
		}
		else if (AM_TURN_ON_ID.equals(actionID))
		{
			x10commander.turnOn(deviceID);
			//ComponentGateway.Singleton.get().triggerOccured(deviceID, AM_TURNS_ON_ID, null);
		}
		else if (LM_DIM_ID.equals(actionID))
		{
			x10commander.dim(deviceID, parameters[0]);
			//ComponentGateway.Singleton.get().triggerOccured(deviceID, LM_TURNS_ON_ID, null);
		}
	}
	
	@Override
	public void registerComponentInstance(String systemDeviceTypeID, String deviceID, String[] parameters) {
		logger.info("New X10 device registered: " + deviceID + " with X10 code: " + parameters[0]);
		x10commander.addNewX10Device(deviceID, parameters[0]);
	}

	@Override
	public void editComponentInstance(String systemDeviceTypeID, String deviceID, String[] newParameters,  String[] oldParameters) {
		logger.info("X10 device ammended: " + deviceID + " to X10 code: " + newParameters[0]);
		x10commander.updateX10Device(deviceID, newParameters[0]);
	}

	@Override
	public void deleteComponentInstance(String systemDeviceTypeID, String deviceID, String[] parameters) {
		logger.info("X10 device deleted: " + deviceID + " with X10 code: " + parameters[0]);
		x10commander.removeX10Device(deviceID);
	}
	

}
