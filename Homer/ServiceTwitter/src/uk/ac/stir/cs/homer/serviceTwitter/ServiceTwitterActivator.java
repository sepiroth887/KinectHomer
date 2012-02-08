/*
 * Created on Wed Mar 24 13:23:50 GMT 2010
 */
package uk.ac.stir.cs.homer.serviceTwitter;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerFrameworkAPI.osgi.ServiceUtils;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;


public class ServiceTwitterActivator implements BundleActivator {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void start(BundleContext context) throws Exception {

		String postToTwitter = System.getProperty("uk.ac.stir.cs.homer.user.twitter.postStatusMessages");
		
		if (postToTwitter == null)
		{
			logger.info("Currently not logging homer status' to Twitter as the property: 'uk.ac.stir.cs.homer.user.twitter.postStatusMessages' is not set.");
		}
		else if(postToTwitter.toLowerCase().equals("true") || postToTwitter.toLowerCase().equals("yes"))
		{
			final HomerDatabase database = ServiceUtils.getService(context, HomerDatabase.class);
			new HomerTwitterer(context, database);
		}
		else
		{
			logger.info("Currently not logging homer status' to Twitter as the property: 'uk.ac.stir.cs.homer.user.twitter.postStatusMessage' is set to false.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}
}