/*
 * Created on Sat May 29 10:48:17 BST 2010
 */
package uk.ac.stir.cs.homer.serviceDatabase;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ServiceDatabaseActivator implements BundleActivator {
  
  private H2HomerDatabase database;

/* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext context) throws Exception {
	  database = new H2HomerDatabase();
	  context.registerService(HomerDatabase.class.getName(), database, null);
  }

  /* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception {
	  database.shutdown();
  }
}