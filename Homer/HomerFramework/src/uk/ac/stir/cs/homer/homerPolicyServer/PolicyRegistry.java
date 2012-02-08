package uk.ac.stir.cs.homer.homerPolicyServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.stir.cs.homer.homerPolicyServer.conflicts.Conflict;
import uk.ac.stir.cs.homer.homerPolicyServer.conflicts.ConflictChecker;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.HomerOverlapChecker;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.JacopOverlapChecker;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.Overlap;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;

public class PolicyRegistry {
	
	
	private final Map<String, LivePolicy> livePolicies = new HashMap<String, LivePolicy>();
	private final List<Policy> policies = new ArrayList<Policy>();
	
	private final HomerOverlapChecker homerOverlapChecker;
	private final HomerDatabase database;
	
	public PolicyRegistry(HomerDatabase database)
	{
		this.database = database;
		this.homerOverlapChecker = new JacopOverlapChecker(database);
	}
	
	public Overlap isPolicyValid(Policy p)
	{
		LivePolicy livePolicy = new LivePolicyDisabled(p);
		return homerOverlapChecker.getOverlapsWithinOnePolicy(livePolicy);
	}
	
	public Map<Policy, Overlap> getOverlaps(Policy policy)
	{
		LivePolicy livePolicy = new LivePolicyDisabled(policy);

		return homerOverlapChecker.getOverlappingPolicies(livePolicy, livePolicies.values());
	}

	public Map<Policy, Set<Conflict>> getPolicyConflicts(Policy policy, Collection<Policy> possibleOverlappingPolicies)
	{
		LivePolicy livePolicy = new LivePolicyDisabled(policy);

		Map<Policy, Set<Conflict>> conflicts = ConflictChecker.getConflictingPolicies(livePolicy, possibleOverlappingPolicies, livePolicies, database);
		ConflictChecker.describe(livePolicy.getPolicy(), conflicts);
		
		return conflicts;
	}
	
	public void add(Policy p) 
	{	
		LivePolicy livePolicy = new LivePolicy(p);

		livePolicies.put(p.getID(), livePolicy);
		policies.add(p);
	}
	
	public Policy createPolicy(JSONObject policy)
	{
		boolean enabled = true;		
		try
		{
			enabled = Boolean.parseBoolean(policy.getString("enabled"));
		} catch (JSONException e)
		{
			// not a required parameter, so default to true
		}
		
		try
		{
			String name = policy.getString("name");
			String author = policy.getString("author");
			String dateCreated = policy.getString("dateCreated");
			String dateLastEdited = policy.getString("dateLastEdited");
			String whenClause = policy.getString("when");
			String doClause = policy.getString("do");
			
			return new Policy(name, author, dateCreated, dateLastEdited, enabled, whenClause, doClause);
		} catch (JSONException e)
		{
			return null;
		}
	}
	
	public void deletePolicy(Policy policy)
	{
		LivePolicy livePolicy = livePolicies.get(policy.getID());
		livePolicy.deactivate();
		livePolicies.remove(policy.getID());
		policies.remove(policy);
	}
	
	public List<Policy> getPolicies()
	{
		return policies;
	}

	public void togglePolicyEnabled(Policy policy)
	{
		LivePolicy livePolicy = livePolicies.get(policy.getID());
		if (policy.isEnabled())
		{
			livePolicy.activate();
		}
		else
		{
			livePolicy.deactivate();
		}
	}

	
}
