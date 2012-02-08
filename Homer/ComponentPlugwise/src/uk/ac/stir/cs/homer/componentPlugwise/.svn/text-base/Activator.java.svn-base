package uk.ac.stir.cs.homer.componentPlugwise;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.ComponentGateway;

/**
 * Activator for PlugwiseDriver.
 * 
 * @author Kenneth J. Turner
 * @version 1.0: 17/05/11 (KJT)
 * @version 2.0: ammended by Claire Maternaghan for Homer
 */
public class Activator implements BundleActivator
{	
	public static final String PORT_NAME_PROPERTY_NAME = "uk.ac.stir.cs.homer.componentPlugwise.port";
	public static final String RETRY_LIMIT_PROPRETY_NAME = "uk.ac.stir.cs.homer.componentPlugwise.retryLimit";
	public static final String POWER_INTERVAL_PROPERTY_NAME = "uk.ac.stir.cs.homer.componentPlugwise.powerIntervalPollSecs";
	public static final String ENERGY_INTERVAL_PROPERTY_NAME = "uk.ac.stir.cs.homer.componentPlugwise.energyIntervalPollMins";
	
	private PlugwiseComponent component;
	
	/**
	 * Start PlugwiseDriver bundle.
	 * 
	 * @param context
	 *            bundle context
	 * @exception any
	 *                exception
	 */
	public void start(BundleContext context) throws Exception
	{
		component = new PlugwiseComponent();
		ComponentGateway.Singleton.get().registerComponent(component);
	}

	/**
	 * Stop PlugwiseDriver bundle.
	 * 
	 * @param context
	 *            bundle context
	 * @exception any
	 *                exception
	 */
	public void stop(BundleContext context) throws Exception
	{
		component.stop();
	}

}
