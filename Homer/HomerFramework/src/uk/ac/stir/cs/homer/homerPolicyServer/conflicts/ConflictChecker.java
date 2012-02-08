package uk.ac.stir.cs.homer.homerPolicyServer.conflicts;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.stir.cs.homer.homerPolicyServer.LivePolicy;
import uk.ac.stir.cs.homer.homerPolicyServer.PolicyEvent;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Action;
import uk.ac.stir.cs.homer.serviceDatabase.objects.ActionEnvironEffect;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Environ;
import uk.ac.stir.cs.homer.serviceDatabase.objects.EnvironEffect;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;

public class ConflictChecker
{

	private static HomerDatabase database;

	public static Map<Policy, Set<Conflict>> getConflictingPolicies(LivePolicy newPolicy, Collection<Policy> possibleOverlappingPolicies, Map<String, LivePolicy> livePolicies, HomerDatabase database)
	{
		ConflictChecker.database = database;
		Map<Policy, Set<Conflict>> conflictingPolicies = new HashMap<Policy, Set<Conflict>>();
		
		if (possibleOverlappingPolicies != null)
		{
			for (Policy p : possibleOverlappingPolicies)
			{
				recordConflictsWithLivePolicy(newPolicy, conflictingPolicies, livePolicies.get(p.getID()));
			}
		}
		return conflictingPolicies;
	}

	private static void recordConflictsWithLivePolicy(LivePolicy newPolicy, Map<Policy, Set<Conflict>> conflictingPolicies, LivePolicy otherPolicy)
	{
		Set<Conflict> conflicts = isPolicyConflicting(newPolicy, otherPolicy);
		if (conflicts.size()>0)
		{
			conflictingPolicies.put(otherPolicy.getPolicy(), conflicts);
		}
	}

	private static Set<Conflict> isPolicyConflicting(LivePolicy newPolicy, LivePolicy p)
	{
		Set<Conflict> conflicts = new HashSet<Conflict>();

		for (PolicyEvent policyEventA : newPolicy.getDoEventTerms())
		{
			for (PolicyEvent policyEventB : p.getDoEventTerms())
			{
				// if same device
				if (policyEventA.getUserDeviceID().equals(policyEventB.getUserDeviceID()))
				{
					conflicts.addAll(getConflictsForSameDevice(policyEventA, policyEventB));
				}
				// if different device
				else
				{
					conflicts.addAll(getConflictsForDifferentActions(policyEventA, policyEventB));	
				}
			}
		}
		return conflicts;
	}

	private static Set<Conflict> getConflictsForSameDevice(PolicyEvent policyEventA, PolicyEvent policyEventB)
	{	
		Set<Conflict> conflicts = new HashSet<Conflict>();
		
		//Same Action?
		if (policyEventA.getID().equals(policyEventB.getID()))
		{
			// has parameters
			if (policyEventA.getParameters().length > 0)
			{
				for (int paramIndex = 0; paramIndex < policyEventA.getParameters().length; paramIndex++)
				{
					// if params aren't the same and the action effects the environment
					if (!policyEventA.getParameters()[paramIndex].equals(policyEventB.getParameters()[paramIndex])) 
					{
						ActionEnvironEffect[] environsEffects = database.getActionEnvironEffects(new QueryObject().action(policyEventA.getID()).userDevice(policyEventA.getUserDeviceID()));
						if (environsEffects.length > 0)
						{
							conflicts.add(makeConflict(policyEventA, policyEventB, null, null, null, ConflictType.POTENTIAL));
						}
						return conflicts;
					}
				}
			}
			
			conflicts.add(makeConflict(policyEventA, policyEventB, null, null, null, ConflictType.SAME));
			return conflicts;
		}
		
		// different actions
		conflicts.addAll(getConflictsForDifferentActions(policyEventA, policyEventB));	
		
		return conflicts;
	}

	private static Conflict makeConflict(PolicyEvent policyEventA, PolicyEvent policyEventB, ActionEnvironEffect actionEnvironEffectA, 
			ActionEnvironEffect actionEnvironEffectB, Environ environ, ConflictType conflictType)
	{
		Action aA = database.getActions(new QueryObject().action(policyEventA.getID()))[0];
		Action aB = database.getActions(new QueryObject().action(policyEventB.getID()))[0];
		
		UserDevice udA = database.getUserDevices(new QueryObject().userDevice(policyEventA.getUserDeviceID()))[0];
		UserDevice udB = database.getUserDevices(new QueryObject().userDevice(policyEventB.getUserDeviceID()))[0];
		
		return new Conflict(udA, udB, aA, aB, actionEnvironEffectA, actionEnvironEffectB, environ, conflictType);
	}

	private static Set<Conflict> getConflictsForDifferentActions(PolicyEvent policyEventA, PolicyEvent policyEventB)
	{
		Set<Conflict> conflicts = new HashSet<Conflict>();
		
		ActionEnvironEffect[] environsEffectsA = database.getActionEnvironEffects(new QueryObject().action(policyEventA.getID()).userDevice(policyEventA.getUserDeviceID()));
		ActionEnvironEffect[] environsEffectsB = database.getActionEnvironEffects(new QueryObject().action(policyEventB.getID()).userDevice(policyEventB.getUserDeviceID()));

		for (ActionEnvironEffect actionEnvironEffectA : environsEffectsA)
		{
			for (ActionEnvironEffect actionEnvironEffectB : environsEffectsB)
			{
				Conflict potentialConflict = getConflictForActionEnvironEffects(policyEventA, policyEventB, actionEnvironEffectA, actionEnvironEffectB);
				if (potentialConflict!=null)
				{
					conflicts.add(potentialConflict);
				}
			}
		}
		return conflicts;
	}

	private static Conflict getConflictForActionEnvironEffects(PolicyEvent policyEventA, PolicyEvent policyEventB, ActionEnvironEffect actionEnvironEffectA, ActionEnvironEffect actionEnvironEffectB)
	{
		if (!actionEnvironEffectA.getEnvironID().equals(actionEnvironEffectB.getEnvironID()))
		{
			return null;
		}
		
		Environ environ = database.getEnvironWithID(actionEnvironEffectA.getEnvironID());
		
		switch (environ.getType())
		{
			case MAXIMISE: 	
				if (actionEnvironEffectA.getEnvironEffect() == EnvironEffect.DECREASE && actionEnvironEffectB.getEnvironEffect() == EnvironEffect.DECREASE)
				{
					return makeConflict(policyEventA, policyEventB, actionEnvironEffectA, actionEnvironEffectB, environ, ConflictType.DEFINITE);
				} 
				break;
			case MINIMISE:
				if (actionEnvironEffectA.getEnvironEffect() == EnvironEffect.INCREASE && actionEnvironEffectB.getEnvironEffect() == EnvironEffect.INCREASE)
				{
					return makeConflict(policyEventA, policyEventB, actionEnvironEffectA, actionEnvironEffectB, environ, ConflictType.DEFINITE);
				} 
				break;
			case NEUTRAL:
				if ((actionEnvironEffectA.getEnvironEffect() == EnvironEffect.INCREASE && actionEnvironEffectB.getEnvironEffect() == EnvironEffect.DECREASE) ||
						(actionEnvironEffectA.getEnvironEffect() == EnvironEffect.DECREASE && actionEnvironEffectB.getEnvironEffect() == EnvironEffect.INCREASE))
				{
					if (policyEventA.getUserDeviceID().equals(policyEventB.getUserDeviceID()))
					{
						return makeConflict(policyEventA, policyEventB, actionEnvironEffectA, actionEnvironEffectB, environ, ConflictType.OPPOSING);
					}
					else
					{
						return makeConflict(policyEventA, policyEventB, actionEnvironEffectA, actionEnvironEffectB, environ, ConflictType.POTENTIAL);
					}
				} 
				break;
			default: break;
		}
		return null;
	}

	public static String describe(Policy policy, Map<Policy, Set<Conflict>> conflicts)
	{
		StringBuilder message = new StringBuilder();
		
		if (conflicts.size() > 0)
		{
			for (Policy p : conflicts.keySet()) 
			{
				message.append("New policy \"" + (policy==null? "unknown" : policy.getName()) + "\" conflicts with \"" + p.getName() + "\" as ");
				message.append(describeConflict(p, conflicts.get(p)));
			}
		}
		return message.toString();
	}

	public static String describeConflict(Policy policy, Set<Conflict> conflicts)
	{
		StringBuilder message = new StringBuilder();
		int c = 0;
		for (Conflict conflict : conflicts)
		{
			if (c > 0) 
			{
				message.append(", and ");
			}
			
			message.append(conflict.toString(database));
			
			if (c+1 == conflicts.size())
			{
				message.append(".");
			}
			c++;
		}
		
		return message.toString();
	}

}
