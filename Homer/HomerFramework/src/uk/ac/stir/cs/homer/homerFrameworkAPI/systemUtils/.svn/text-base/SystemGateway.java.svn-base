package uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.osgi.framework.ServiceRegistration;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.HomerComponent;
import uk.ac.stir.cs.homer.homerFrameworkAPI.events.EventType;
import uk.ac.stir.cs.homer.homerPolicyServer.PolicyRegistry;
import uk.ac.stir.cs.homer.homerPolicyServer.PolicyState;
import uk.ac.stir.cs.homer.serviceDatabase.objects.ActionEnvironEffects;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;

public interface SystemGateway {
	
	public static class Singleton {
		static SystemGateway singleton;
		
		public static SystemGateway get() {
			if(singleton == null) {
				throw new IllegalStateException("not initialized yet!");
			}
			return singleton;
		}
		
		public static void set(SystemGateway s) {
			singleton = s;
		}
	}
	void storeNewComponent(String systemDeviceTypeID, HomerComponent c);
	
	//a new device may have multiple parent system device types, 
	//parameters is a map from system device type id to the string[] parameters
	UserDevice registerNewDevice(SystemDeviceType[] systemDeviceTypes, Map<String, String[]> parameters, String userDeviceTypeID, String userDeviceName, String locationID, String imagePath, String defaultState);
	void updateDevice(UserDevice userDevice, SystemDeviceType[] systemDeviceTypes, Map<String, String[]> parameters);

	void doAction(String userDeviceID, String actionID, String[] parameters);
	void doActionForPolicy(String policyName, String userDeviceID, String actionID, String[] parameters);
	
	boolean checkCondition(String userDeviceID, String conditionID, String[] parameters);
	ServiceRegistration registerTriggerListener(EventListener listener, String sysDeviceId, String userDeviceID, String triggerID, String[] parameters);
	ServiceRegistration registerStateChangesListener(EventListener listener, String sysDeviceId, String userDeviceID, String triggerID, String[] parameters);
	
	void deleteUserDevice(String userDeviceID);
	void deleteUserDeviceType(String userDeviceTypeID);
	void deleteLocation(String locationID);
	void deleteLocationContext(String locationContextID);

	boolean didEventOccurWithinTime(EventType type, String userDeviceID, String id, String[] parameters, long earliestTime, long latestTime);
	boolean isDeviceInState(String userDeviceID, String id, String[] parameters);
	
	List<Policy> getPolicies();
	PolicyState validateNewPolicy(Policy p);
	void addNewPolicy(Policy p);
	boolean addNewPolicy(JSONObject p);
	void enablePolicy(Policy policy, boolean selected);
	void updatePolicy(Policy policy);
	void deletePolicy(Policy policy);

	SystemDeviceType[] sysDevTypesRequiringActEnvEffInfo(String userDeviceTypeID, SystemDeviceType[] array);

	void addActionEnvironEffects(ActionEnvironEffects[] newEnvirons);

	PolicyRegistry getPolicyRegistry();



	
}
