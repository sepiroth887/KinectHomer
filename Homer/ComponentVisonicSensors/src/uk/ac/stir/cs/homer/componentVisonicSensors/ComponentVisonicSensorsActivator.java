/*
 * Created on Tue Oct 05 16:49:50 BST 2010
 */
package uk.ac.stir.cs.homer.componentVisonicSensors;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.ComponentGateway;

public class ComponentVisonicSensorsActivator implements BundleActivator {
  
  /* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext context) throws Exception 
  {
	  ComponentGateway.Singleton.get().registerComponent(new VisonicSensorComponent());
  }

  /* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception {
  }
}