package uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.whenTerms;

import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerFrameworkAPI.events.EventType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.EventListener;
import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.SystemGateway;
import uk.ac.stir.cs.homer.homerPolicyServer.PolicyEvent;
import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;

public class WhenEventTerm extends WhenTerm implements EventListener
{
	private final static Logger logger = LoggerFactory.getLogger(WhenEventTerm.class);
	
	private PolicyEvent policyEvent;
	private ServiceRegistration eventListener;
	
	private boolean termOccured = false;
	
	public WhenEventTerm(JSONObject eventAsJSON) throws JSONException 
	{
		policyEvent = new PolicyEvent(eventAsJSON);
	}

	@Override
	public boolean evaluate() {
		return termOccured;
	}

	@Override
	public void reset() 
	{
		logger.trace("Reseting event term.");
		termOccured = false;
	}

	@Override
	public TermType getTermType() {
		return TermType.EVENT;
	}

	public void startListening() 
	{
		if (eventListener==null)
		{
			logger.debug("Started listening for event term.");
			switch (policyEvent.getType())
			{
				case TRIGGER:			
					eventListener = SystemGateway.Singleton.get().registerTriggerListener(this, null, policyEvent.getUserDeviceID(), policyEvent.getID(), policyEvent.getParameters());
					break;
				case CONDITION:		
					eventListener = SystemGateway.Singleton.get().registerStateChangesListener(this, null, policyEvent.getUserDeviceID(), policyEvent.getID(), policyEvent.getParameters());
					break;
			}
		}
	}
	
	public void stopListening() 
	{
		logger.debug("Stopping listening for event term.");
		if (eventListener!=null) eventListener.unregister();
		eventListener = null;
	}

	@Override
	public void eventOccured(EventType type, String sysDeviceID, String userDeviceID, String eventID, String[] parameters) 
	{		
		termOccured = true;
		if (parentListener!=null) parentListener.termNowTrue(this);	
	}

	public boolean evaluateCondition() {
		if (policyEvent.getType() == EventType.CONDITION && !termOccured)
		{
			termOccured = SystemGateway.Singleton.get().isDeviceInState(policyEvent.getUserDeviceID(), policyEvent.getID(), policyEvent.getParameters());
			if (parentListener!=null && termOccured) 
			{
				parentListener.termNowTrue(this);
				return true;
			}
		}
		return false;
	}
	
	public PolicyEvent getPolicyEvent()
	{
		return policyEvent;
	}

	@Override
	public void termNowTrue(WhenTerm term) {}

}
