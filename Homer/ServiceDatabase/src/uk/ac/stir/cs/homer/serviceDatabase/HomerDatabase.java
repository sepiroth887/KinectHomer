package uk.ac.stir.cs.homer.serviceDatabase;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import uk.ac.stir.cs.homer.serviceDatabase.objects.Action;
import uk.ac.stir.cs.homer.serviceDatabase.objects.ActionEnvironEffect;
import uk.ac.stir.cs.homer.serviceDatabase.objects.ActionEnvironEffects;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Condition;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Environ;
import uk.ac.stir.cs.homer.serviceDatabase.objects.IDsForSystemDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.IDsForUserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Location;
import uk.ac.stir.cs.homer.serviceDatabase.objects.LocationContext;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Log;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Trigger;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.LoggerQueryObject;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;

public interface HomerDatabase {

	//******** ADD *************************
	public void addSystemDeviceType(String sysDeviceTypeId, String displayName, String jsonConfigData);	
	public String addSystemDevice(String jsonConfig, String systemDeviceTypeID, String userDeviceID);

	public String addUserDevice(String userDeviceName, String locationID, String userDeviceTypeID, String image, String defaultState);
	public String addUserDeviceType(String userDeviceTypeName, String image, String parent);
	
	public void addUserTypeToSystemType(String userDeviceTypeID, String systemDeviceTypeID);

	public String addLocationContext(String name, String image);
	public String addLocation(String name, String locationContextID, String image);
	
	public void addTrigger(String sysDeviceTypeId, String triggerId, String description, String jsonParameterData, String beforeImage, String afterImage);
	public void addCondition(String sysDeviceTypeId, String conditionId, String description, String jsonParameterData, String image, String actionIDresultingInThisState);
	public void addAction(String sysDeviceTypeId, String actionId, String description, String jsonParameterData, String image);
	
	public String addEnviron(String name, String type, boolean report);

	public void addActionEnvironEffect(String userDeviceTypeID, String systemDeviceTypeID, String actionID, String environID, String effect);
	public void addActionEnvironEffects(ActionEnvironEffects actionEnvironEffect);
	
	public String addImage(File imageFile);
	
	public void addUser(String name);
	//***************************************
	
	//********* UPDATE **********************
	public void updateUserDeviceDetails(UserDevice userDevice);
	public void updateUserDeviceTypeDetails(UserDeviceType userDeviceType);
	public void updateSystemDeviceDetails(SystemDevice systemDevice);
	
	public void updateCondition(Condition condition);
	public void updateAction(Action action);
	public void updateTrigger(Trigger trigger);
	
	public void updateSysDeviceTypeDisplayName(String sysDeviceTypeId, String displayName);

	public void updateLocationContextDetails(LocationContext locationContext);
	public void updateLocationDetails(Location location);
	
	public void updateDeviceState(String userDeviceID, String stateID, String parameters);
	public void updateUser(int id, String name);
	
	//***************************************
	
	
	//******** GET **************************
	public ActionEnvironEffect[] getActionEnvironEffects(QueryObject qryObj);

	public Environ[] getAllEnirons();
	public Environ getEnvironWithName(String name);
	public Environ getEnvironWithID(String id);
	
	public Trigger[] getAllTriggers();
	public Trigger[] getTriggers(QueryObject qryObj);
	
	public Condition[] getAllConditions();
	public Condition[] getConditions(QueryObject qryObj);
	
	public Action[] getAllActions();
	public Action[] getActions(QueryObject qryObj);
	
	public Location[] getAllLocations();
	public Location[] getLocations(QueryObject qryObj);
	
	public LocationContext[] getAllLocationContexts();
	public LocationContext[] getLocationContexts(QueryObject qryObj);
	
	public UserDevice[] getAllUserDevices();
	public UserDevice[] getUserDevices(QueryObject qryObj);
	
	public UserDeviceType[] getAllUserDeviceTypes();
	public UserDeviceType[] getUserDeviceTypes(QueryObject qryObj);
	
	public SystemDevice[] getAllSystemDevices();
	public SystemDevice[] getSystemDevices(QueryObject qryObj);
	
	public SystemDeviceType[] getAllSystemDeviceTypes();
	public SystemDeviceType[] getSystemDeviceTypes(QueryObject qryObj);
	
	public IDsForSystemDevice getDetailsAboutSysDeviceID(String sysDeviceID);
	public IDsForUserDevice getDetailsAboutUserDeviceID(String userDeviceID);

	public InputStream getImage(String imageName);
	public String getUserName(int id);
	public Map<Integer,String> getAllUsers();
	//********************************************
	
	
	//****** DELETE *******************************
	public SystemDevice[] HOMER_USE_ONLY_deleteLocationContext(String locationContextID);
	public SystemDevice[] HOMER_USE_ONLY_deleteLocation(String locationID);
	public SystemDevice[] HOMER_USE_ONLY_deleteUserDevice(String userDeviceID);
	public SystemDevice[] HOMER_USE_ONLY_deleteUserDeviceType(String userDeviceTypeID);
	public SystemDevice[] HOMER_USE_ONLY_deleteSystemDeviceType(String systemDeviceTypeID);
	public void HOMER_USE_ONLY_deleteSystemDevice(String systemDeviceID);
	public void HOMER_USE_ONLY_deleteActionEnvironEffects(String userDeviceID, String systemDeviceID);
	public void deleteUser(int id);
	//****************************************
	
	
	
	//******POLICIES*******************************
	public Policy[] getPolicies();
	public void savePolicy(Policy p);
	public void updatePolicy(Policy policy);
	public void deletePolicy(Policy policy);
	//****************************************	
	
	
	//********LOGS********************************
	public String saveLog(Log log);
	public Log[] getLogs(LoggerQueryObject queryObj);
	//*********************************************
	
}
