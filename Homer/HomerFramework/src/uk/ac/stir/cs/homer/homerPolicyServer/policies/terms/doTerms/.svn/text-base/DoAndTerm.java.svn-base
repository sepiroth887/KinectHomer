package uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.doTerms;

import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;

public class DoAndTerm extends DoTerm
{

	@Override
	public TermType getTermType()
	{
		return TermType.AND;
	}

	@Override
	public void reset()
	{
		
	}

	@Override
	public void executeActions()
	{
		for (DoTerm child: children)
		{
			child.executeActions();
		}
	}

	@Override
	public boolean evaluate()
	{		
		for (DoTerm child: children)
		{
			if (!child.evaluate()) return false;
		}
		
		return true;
	}

}
