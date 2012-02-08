package uk.ac.stir.cs.homer.componentPlugwise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import uk.ac.stir.cs.homer.componentPlugwise.driver.PlugwiseTransceiver;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.ComponentGateway;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.HomerComponent;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasActions;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasConditions;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.WhichHasTriggers;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.encoding.IDUtil;
import uk.ac.stir.cs.homer.homerFrameworkAPI.configData.ConfigData;
import uk.ac.stir.cs.homer.homerFrameworkAPI.configData.ConfigPart;
import uk.ac.stir.cs.homer.homerFrameworkAPI.homerObjects.SystemDeviceType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.HomerText;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.HomerToggle;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.Unit;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Action;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Condition;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Trigger;

public class PlugwiseComponent implements HomerComponent, WhichHasTriggers, WhichHasConditions, WhichHasActions
{
	private final static String SYSTEM_DEVICE_TYPE = IDUtil.getUniqueIdentifier(PlugwiseComponent.class, "PLUGWISE_SYSTEM_DEVICE_TYPE");
	
	private final static String TURNS_ON = IDUtil.getUniqueIdentifier(PlugwiseComponent.class, "TURNS_ON");
	private final static String TURNS_OFF = IDUtil.getUniqueIdentifier(PlugwiseComponent.class, "TURNS_OFF");

	private final static String IS_ON = IDUtil.getUniqueIdentifier(PlugwiseComponent.class, "IS_ON");
	private final static String IS_OFF = IDUtil.getUniqueIdentifier(PlugwiseComponent.class, "IS_OFF");

	private final static String TURN_ON = IDUtil.getUniqueIdentifier(PlugwiseComponent.class, "TURN_ON");
	private final static String TURN_OFF = IDUtil.getUniqueIdentifier(PlugwiseComponent.class, "TURN_OFF");

	
	private final Map<String, Circle> circles = new HashMap<String, Circle>();
	private final PlugwiseTransceiver plugwiseTransceiver;
	
	public PlugwiseComponent()
	{
		plugwiseTransceiver = new PlugwiseTransceiver();
		listenForTriggers();
	}
	
	private void listenForTriggers()
	{
		plugwiseTransceiver.addObserver(new Observer(){

			@Override
			public void update(Observable arg0, Object arg1)
			{
				if (!(arg1 instanceof Circle)) return;
				Circle circleUpdated = (Circle) arg1;
				ComponentGateway.Singleton.get().triggerOccured(circleUpdated.getSystemDeviceID(), circleUpdated.isOn()? TURNS_ON : TURNS_OFF, null);
			}
		});
	}

	@Override
	public SystemDeviceType[] getSystemDeviceTypeData()
	{
		ConfigPart addressPart = new ConfigPart("Address:", new HomerText("xxxxxx", 6, 6, Unit.NONE), "");
		ConfigPart isPlusPart = new ConfigPart(new HomerToggle("is plus circle", Unit.NONE, false));
		
		return new SystemDeviceType[] {
				new SystemDeviceType(SYSTEM_DEVICE_TYPE, "Plugwise Plug", new ConfigData(addressPart, isPlusPart))
		};
	}

	@Override
	public void registerComponentInstance(String systemDeviceTypeID,
			String sysDeviceID, String[] parameters)
	{
		newCircle(sysDeviceID, parameters);
	}

	@Override
	public void editComponentInstance(String systemDeviceTypeID,
			String sysDeviceID, String[] newParameters, String[] oldParameters)
	{
		plugwiseTransceiver.removeCircle(circles.get(sysDeviceID));
		newCircle(sysDeviceID, newParameters);
	}

	private void newCircle(String systemDeviceID, String[] properties)
	{
		Circle circle = new Circle(systemDeviceID, properties[0], Boolean.valueOf(properties[1]));
		circles.put(systemDeviceID, circle);
		plugwiseTransceiver.addCircle(circle);
	}
	
	@Override
	public void deleteComponentInstance(String systemDeviceTypeID,
			String sysDeviceID, String[] parameters)
	{
		plugwiseTransceiver.removeCircle(circles.get(sysDeviceID));
		circles.remove(sysDeviceID);
	}

	public void stop()
	{
		plugwiseTransceiver.close();
	}

	
	@Override
	public List<Action> getActions(String systemDeviceTypeID)
	{
		List<Action> actions = new ArrayList<Action>(2);
		actions.add(new Action(TURN_ON, "turn on", Action.DEFAULT_TURN_ON_IMAGE));
		actions.add(new Action(TURN_OFF, "turn off", Action.DEFAULT_TURN_OFF_IMAGE));
		return actions;
	}

	@Override
	public void performAction(String sysDeviceTypeID, String sysDeviceID,
			String actionID, String[] parameters)
	{
		plugwiseTransceiver.changeState(circles.get(sysDeviceID), actionID.equals(TURN_ON));
	}

	@Override
	public List<Trigger> getTriggers(String systemDeviceTypeID)
	{
		List<Trigger> triggers = new ArrayList<Trigger>(2);
		triggers.add(new Trigger(TURNS_ON, "turns on", Trigger.DEFAULT_IS_OFF_IMAGE, Trigger.DEFAULT_IS_ON_IMAGE));
		triggers.add(new Trigger(TURNS_OFF, "turns off", Trigger.DEFAULT_IS_ON_IMAGE, Trigger.DEFAULT_IS_OFF_IMAGE));
		return triggers;
	}

	@Override
	public List<Condition> getConditions(String systemDeviceTypeID)
	{
		List<Condition> conditions = new ArrayList<Condition>(2);
		conditions.add(new Condition(IS_ON, "is on", Condition.DEFAULT_IS_ON_IMAGE, null, TURNS_ON, TURN_ON));
		conditions.add(new Condition(IS_OFF, "is off", Condition.DEFAULT_IS_OFF_IMAGE, null, TURNS_OFF, TURN_OFF));
		return conditions;
	}

	@Override
	public boolean checkCondition(String sysDeviceTypeID, String sysDeviceID, String conditionID, String[] parameters)
	{
		if (conditionID.equals(IS_ON)) 
			return circles.get(sysDeviceID).isOn();
		else 
			return !circles.get(sysDeviceID).isOn();
	}

}
