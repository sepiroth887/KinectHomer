package uk.ac.stir.cs.homer.serviceTwitter;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerFrameworkAPI.events.EventUtils;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Log;

public class HomerTwitterer 
{
	private HomerTwitter homerTwitter;
	private final BundleContext context;
	private final HomerDatabase database;
	
	private static Logger logger = LoggerFactory.getLogger(HomerTwitterer.class);

	public HomerTwitterer(BundleContext context, HomerDatabase database) {
		this.context = context;
		this.database = database;
		
		String key = System.getProperty("uk.ac.stir.cs.homer.user.twitter.consumerKey");
		String secret = System.getProperty("uk.ac.stir.cs.homer.user.twitter.consumerSecret");
		
		if (key == null)
		{
			if (secret == null)
			{
				logger.error("Unable to connect to twitter with the default homer account as the properties: 'uk.ac.stir.cs.homer.user.twitter.consumerKey' and 'uk.ac.stir.cs.homer.user.twitter.consumerSecret' are not set.");
			}
			else
			{
				logger.error("Unable to connect to twitter with the default homer account as the property: 'uk.ac.stir.cs.homer.user.twitter.consumerKey' is not set.");				
			}
			return;
		}
		
		if (secret == null)
		{
			logger.error("Unable to connect to twitter with the default homer account as the property: 'uk.ac.stir.cs.homer.user.twitter.consumerSecret' is not set.");
			return;
		}
		
		this.homerTwitter = new HomerTwitter(key, secret);
		registerListeners();
	}
	
	public void registerListeners()
	{
		EventHandler eventHandler = new EventHandler(){
			public void handleEvent(Event event) {
				Log l = EventUtils.getLog(event, database);
				if (l.isDisplay())
				{
					homerTwitter.postTwitterMessage(EventUtils.formatEventForDisplay(event, database));
				}
			}
		};
		
		EventUtils.registerTriggerListener(context, eventHandler, null, null, null, null, null, null, null);
		EventUtils.registerStateChangeListener(context, eventHandler, null, null, null, null, null, null, null);
		EventUtils.registerActionListener(context, eventHandler, null, null, null, null, null, null, null);
	}

}
