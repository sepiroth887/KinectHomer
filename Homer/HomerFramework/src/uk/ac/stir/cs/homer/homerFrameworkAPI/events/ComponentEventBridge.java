package uk.ac.stir.cs.homer.homerFrameworkAPI.events;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.HomerComponent;
import uk.ac.stir.cs.homer.serviceDatabase.objects.IDsForSystemDevice;

public interface ComponentEventBridge {

	public void handleActionRequestsForSysDeviceType(HomerComponent component, String sysDeviceTypeId);
	public void triggerOccurred(String sysDeviceID, String triggerID, String[] parameters);
	boolean isStateChanged(String systemDeviceID, String conditionIDofNewState, String[] parameters, IDsForSystemDevice idsIfAlreadyKnown);
	
	public void handleStateEvent(String sysDeviceID, String triggerID,
			String[] parameters, IDsForSystemDevice idsIfAlreadyKnown);
}
