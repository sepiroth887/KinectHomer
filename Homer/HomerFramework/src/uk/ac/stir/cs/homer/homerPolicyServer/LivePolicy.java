package uk.ac.stir.cs.homer.homerPolicyServer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;
import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.doTerms.ActionOrConditionEventTerm;
import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.doTerms.DoTerm;
import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.whenTerms.TermListener;
import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.whenTerms.WhenEventTerm;
import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.whenTerms.WhenTerm;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;

public class LivePolicy implements TermListener
{
	private final Policy policy;
	private final static Logger logger = LoggerFactory.getLogger(LivePolicy.class);
	public static final int DEFAULT_WHEN_DURATION = Integer.parseInt(System.getProperty("uk.ac.stir.cs.homer.policies.eventTimeFrame"));
		
	private WhenTerm rootWhenTerm;
	private DoTerm rootDoTerm;

	public LivePolicy(Policy policy) {
		this.policy = policy;
		initialise();
	}
	
	protected void initialise()
	{
		logger.trace("Starting new live policy: " + policy.getName());
		
		try {
			rootWhenTerm = WhenTerm.create(this, new JSONObject(policy.getWhenClauseAsJSON()), this, getEnabled());
		} catch (JSONException e) {
			// TODO change back to error
			logger.trace("Unable to start policy as couldn't parse the JSON when terms. " + e.getMessage());
		}
		
		try {
			rootDoTerm = DoTerm.create(policy, new JSONObject(policy.getDoClauseAsJSON()));
		} catch (JSONException e) {
			// TODO change back to error
			logger.trace("Unable to start policy as couldn't parse the JSON do terms. " + e.getMessage());
		}
	}

	protected boolean getEnabled()
	{
		return policy.isEnabled();
	}
	
	private void doActions() {
		logger.debug("Policy: " + policy.getName() + ": Carrying out the actions.");
		
		rootDoTerm.executeActions();
	}

	@Override
	public void termNowTrue(WhenTerm term) {
		logger.debug("Policy: " + policy.getName() + " Fired!");
		rootWhenTerm.reset();
		rootWhenTerm.startListening();
		doActions();
	}

	public void deactivate()
	{
		rootWhenTerm.reset();
		rootWhenTerm.stopListening();
	}

	public void activate()
	{
		rootWhenTerm.reset();
		rootWhenTerm.startListening();
	}
	
	public Policy getPolicy()
	{
		return policy;
	}

	public List<PolicyEvent> getWhenEventTerms()
	{
		return getChildrenForWhenTerms(rootWhenTerm);
	}
	public List<PolicyEvent> getDoEventTerms()
	{
		return getChildrenForDoTerms(rootDoTerm);
	}
	
	private List<PolicyEvent> getChildrenForWhenTerms(WhenTerm parentWhenTerm)
	{
		List<PolicyEvent> terms = new ArrayList<PolicyEvent>();
		if (parentWhenTerm.getChildren()== null)
		{
			terms.add(((WhenEventTerm) parentWhenTerm).getPolicyEvent());
		}
		else
		{
			for (WhenTerm whenTerm : parentWhenTerm.getChildren())
			{
				if (whenTerm instanceof WhenEventTerm)
				{
					terms.add(((WhenEventTerm) whenTerm).getPolicyEvent());
				}
				terms.addAll(getChildrenForWhenTerms(whenTerm));
			}
		}
		return terms;
	}

	private List<PolicyEvent> getChildrenForDoTerms(DoTerm parentDoTerm)
	{
		List<PolicyEvent> terms = new ArrayList<PolicyEvent>();
		if (parentDoTerm.hasChildren())
		{
			for (DoTerm doTerm : parentDoTerm.getChildren())
			{
				if (doTerm instanceof ActionOrConditionEventTerm)
				{
					ActionOrConditionEventTerm doTermChild = (ActionOrConditionEventTerm) doTerm;
					if (doTermChild.getTermType() == TermType.ACTION)
					{
						terms.add(doTermChild.getPolicyEvent());
					}
				}
				terms.addAll(getChildrenForDoTerms(doTerm));
			}
		}
		return terms;
	}

	public WhenTerm getRootWhenTerm()
	{
		return rootWhenTerm;
	}
	
}
