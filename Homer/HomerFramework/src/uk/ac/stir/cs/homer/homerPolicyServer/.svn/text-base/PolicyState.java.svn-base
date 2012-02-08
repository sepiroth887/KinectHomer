package uk.ac.stir.cs.homer.homerPolicyServer;

import java.util.Map;
import java.util.Set;

import uk.ac.stir.cs.homer.homerPolicyServer.conflicts.Conflict;
import uk.ac.stir.cs.homer.homerPolicyServer.conflicts.ConflictChecker;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.HomerOverlapChecker;
import uk.ac.stir.cs.homer.homerPolicyServer.overlap.Overlap;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;

public class PolicyState
{
	private Overlap validity;
	private Map<Policy, Overlap> overlaps;
	private Map<Policy, Set<Conflict>> conflicts;
	private final Policy policy;
	
	public PolicyState(Policy p)
	{
		this.policy = p;
	}
	
	public Map<Policy, Set<Conflict>> getConflicts()
	{
		return conflicts;
	}
	public Map<Policy, Overlap> getOverlaps()
	{
		return overlaps;
	}
	public Overlap getValidity()
	{
		return validity;
	}
	public Policy getPolicy()
	{
		return policy;
	}
	
	public void setConflicts(Map<Policy, Set<Conflict>> conflicts)
	{
		this.conflicts = conflicts;
	}
	public void setValidity(Overlap validity)
	{
		this.validity = validity;
	}

	public void setOverlaps(Map<Policy, Overlap> overlaps)
	{
		this.overlaps = overlaps;
	}

	public boolean isValid()
	{
		return isPolicyValid() && !hasConflicts();
	}

	private boolean hasConflicts()
	{
		return !conflicts.isEmpty();
	}

	private boolean hasOverlaps()
	{
		return !overlaps.isEmpty();
	}

	private boolean isPolicyValid()
	{
		return !validity.getVariables().isEmpty();
	}
	

	public String describe(HomerDatabase database)
	{
		StringBuilder message = new StringBuilder();
		if (!isPolicyValid())
		{
			message.append("\nThe Policy itself isn't valid! The first part of the policy could not ever happen!");
		}
		if (hasOverlaps())
		{
			message.append("\n");
			for (Policy policy : overlaps.keySet())
			{
				if (conflicts.containsKey(policy))
				{
					message.append(HomerOverlapChecker.describeOverlap(database, policy, overlaps.get(policy)));
					message.append(ConflictChecker.describeConflict(policy, conflicts.get(policy)));
					
				}
			}
		}
		return message.toString();
	}
}
