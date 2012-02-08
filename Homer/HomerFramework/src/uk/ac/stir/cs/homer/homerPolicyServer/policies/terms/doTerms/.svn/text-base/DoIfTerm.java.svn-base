package uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.doTerms;

import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;

public class DoIfTerm extends DoTerm
{
	private DoTerm conditions;
	private DoTerm actions;
	
	private DoTerm elseConditions;
	private DoTerm elseActions;

	public DoIfTerm(Policy policy, JSONObject jsonObject) throws JSONException
	{
		conditions = DoTerm.create(policy, jsonObject.getJSONObject(TermType.CONDITION.name()));
		actions = DoTerm.create(policy, jsonObject.getJSONObject(TermType.ACTION.name()));
		
		if (jsonObject.has(TermType.ELSE_CONDITION.name()))
		{
			elseConditions = DoTerm.create(policy, jsonObject.getJSONObject(TermType.ELSE_CONDITION.name()));
			elseActions = DoTerm.create(policy, jsonObject.getJSONObject(TermType.ELSE_ACTION.name()));
		}
	}

	@Override
	public TermType getTermType()
	{
		return TermType.IF;
	}

	@Override
	public void reset()
	{
		
	}

	@Override
	public void executeActions()
	{
		boolean firstCondition = false;
		
		// check conditions for if
		firstCondition = conditions.evaluate();
		
		// carry out actions if conditions met
		if (firstCondition)
		{
			actions.executeActions();
		}
		
		// carry out else (if else exists) if conditions not met
		else if (elseActions != null && elseActions.children.size()>0)
		{
			// might not exists condition aspect to else, so in that case just do the actions
			boolean doElseActions = true;
			if (elseConditions != null && elseConditions.children.size()>0)
			{
				doElseActions = elseConditions.evaluate();
			}
			
			if (doElseActions)
			{
				elseActions.executeActions();
			}
		}
	}

//	private void doActions(List<DoTerm> actions)
//	{
//		for (DoTerm actionTerm : actions)
//		{
//			actionTerm.executeActions();
//		}
//	}
//
//	private boolean checkConditions(List<DoTerm> conditions)
//	{
//		boolean result = false;
//		for (DoTerm conditionTerm : conditions)
//		{
//			result = conditionTerm.evaluate();
//			
//			if (result == false)
//				break;
//		}
//		return result;
//	}

	@Override
	public boolean evaluate()
	{
		throw new UnsupportedOperationException("Trying to evaluate in a DoIfTerm which should never happen.");
	}

}
