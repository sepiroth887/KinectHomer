/*
 * Created on Sat Jan 28 12:12:10 GMT 2012
 */
package Kinect;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.ComponentGateway;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Activator implements BundleActivator {

	
	private KinectSensorComponent kinect;
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		 
		kinect = new KinectSensorComponent();
		
		new KinectUI(kinect);
		ComponentGateway.Singleton.get().registerComponent(kinect);
		
		
	}

	

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
	}
}