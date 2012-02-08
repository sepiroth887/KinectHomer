/*
 * Created on Sat May 29 10:49:15 BST 2010
 */
package uk.ac.stir.cs.homer.homerFrameworkAPI;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.ComponentGateway;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.OSGiComponentGateway;
import uk.ac.stir.cs.homer.homerFrameworkAPI.events.ComponentEventBridge;
import uk.ac.stir.cs.homer.homerFrameworkAPI.events.OSGiComponentEventBridge;
import uk.ac.stir.cs.homer.homerFrameworkAPI.osgi.ServiceUtils;
import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.OSGiSystemGateway;
import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.SystemGateway;
import uk.ac.stir.cs.homer.homerFrameworkAPI.tcas.Condition;
import uk.ac.stir.cs.homer.homerFrameworkAPI.time.TimeAndDateComponent;
import uk.ac.stir.cs.homer.homerPolicyServer.PolicyRegistry;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;

public class HomerFrameworkAPIActivator implements BundleActivator {
  
  /* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext context) throws Exception {
	 HomerDatabase database = ServiceUtils.getService(context, HomerDatabase.class);
		 
	 ComponentEventBridge eventBridge = new OSGiComponentEventBridge(context, database);
		
	 PolicyRegistry policyFactory = new PolicyRegistry(database);
	 
	 SystemGateway sysHandling = new OSGiSystemGateway(database, context, policyFactory);
	 SystemGateway.Singleton.set(sysHandling);
	 
	 ComponentGateway handling = new OSGiComponentGateway(database, eventBridge, sysHandling);
	 ComponentGateway.Singleton.set(handling);
	
	 // set up time stuff
	 TimeAndDateComponent timeComponent = new TimeAndDateComponent();
	 ComponentGateway.Singleton.get().registerComponent(timeComponent);
	 
	 String systemDeviceDateID = TimeAndDateComponent.SYSTEM_DEVICE_DATE;
	 if (database.getUserDevices(new QueryObject().systemDeviceType(systemDeviceDateID)).length==0)
	 {
		 SystemDeviceType[] systemDeviceTypes = database.getSystemDeviceTypes(new QueryObject().systemDeviceType(systemDeviceDateID));
		 Map<String, String[]> parameters = new HashMap<String, String[]>();
		 parameters.put(systemDeviceDateID, new String[]{});
		 
		 String userDeviceTypeID = database.addUserDeviceType("Date", Condition.DEFAULT_DATE_IMAGE, null);

		 SystemGateway.Singleton.get().registerNewDevice(systemDeviceTypes, parameters, userDeviceTypeID, "Day", "1", Condition.DEFAULT_DATE_IMAGE, null);
		 
	 }
	 
	 String systemDeviceTimeID = TimeAndDateComponent.SYSTEM_DEVICE_TIME;
	 if (database.getUserDevices(new QueryObject().systemDeviceType(systemDeviceTimeID)).length==0)
	 {
		 SystemDeviceType[] systemDeviceTypes = database.getSystemDeviceTypes(new QueryObject().systemDeviceType(systemDeviceTimeID));
		 Map<String, String[]> parameters = new HashMap<String, String[]>();
		 parameters.put(systemDeviceTimeID, new String[]{});
		 
		 String userDeviceTypeID = database.addUserDeviceType("Time", Condition.DEFAULT_DATE_IMAGE, null);

		 SystemGateway.Singleton.get().registerNewDevice(systemDeviceTypes, parameters, userDeviceTypeID, "Time", "1", Condition.DEFAULT_TIME_IMAGE, null);
		 
	 }
	 
	 // load policies
	 for (Policy p: database.getPolicies())
	 {
		policyFactory.add(p);
	 }

  }

  /* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception {
  }
}