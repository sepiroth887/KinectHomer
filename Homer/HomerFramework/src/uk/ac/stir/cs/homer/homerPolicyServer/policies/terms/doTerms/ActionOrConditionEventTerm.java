package uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.doTerms;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerFrameworkAPI.events.EventType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.SystemGateway;
import uk.ac.stir.cs.homer.homerPolicyServer.PolicyEvent;
import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;

public class ActionOrConditionEventTerm extends DoTerm
{
	private final static Logger logger = LoggerFactory.getLogger(ActionOrConditionEventTerm.class);
	
	private PolicyEvent policyEvent;
	
	public ActionOrConditionEventTerm(JSONObject json) throws JSONException 
	{
		policyEvent = new PolicyEvent(json);
	}

	@Override
	public TermType getTermType()
	{
		return policyEvent.getType().equals(EventType.CONDITION)? TermType.CONDITION : TermType.ACTION;
	}

	@Override
	public void reset()
	{
	}

	@Override
	public void executeActions()
	{
		if (getTermType() == TermType.ACTION)
		{
			SystemGateway.Singleton.get().doActionForPolicy(policy.getName(), policyEvent.getUserDeviceID(), policyEvent.getID(), policyEvent.getParameters());
		}
	}

	@Override
	public boolean evaluate()
	{
		if (getTermType() == TermType.CONDITION)
		{
			return SystemGateway.Singleton.get().checkCondition(policyEvent.getUserDeviceID(), policyEvent.getID(), policyEvent.getParameters());
		}
		return false;
	}

	public PolicyEvent getPolicyEvent()
	{
		return policyEvent;
	}

}
