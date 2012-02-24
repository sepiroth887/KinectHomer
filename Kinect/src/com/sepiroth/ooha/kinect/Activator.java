/*
 * Created on Sat Jan 28 12:12:10 GMT 2012
 */
package com.sepiroth.ooha.kinect;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stir.cs.homer.homerFrameworkAPI.osgi.ServiceUtils;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;


public class Activator implements BundleActivator {

	
	private KinectSensorComponent kinect;
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		
		final HomerDatabase database = ServiceUtils.getService(context, HomerDatabase.class);
		//kinect = new KinectSensorComponent(database);
		//kinect.setUI(new KinectUI(kinect));
		//ComponentGateway.Singleton.get().registerComponent(kinect);
		
		UserDevice[] devices = database.getAllUserDevices();
		
		for(UserDevice device : devices){
			System.err.println(device.getName());
		}
		
		
	}

	

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		kinect.storeGestures();
	}
}