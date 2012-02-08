package uk.ac.stir.cs.homer.homerPolicyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

public class LivePolicyDisabled extends LivePolicy implements TermListener 
{
	public LivePolicyDisabled(Policy policy) {
		super(policy);
	}
	
	@Override
	protected boolean getEnabled()
	{
		return false;
	}

}