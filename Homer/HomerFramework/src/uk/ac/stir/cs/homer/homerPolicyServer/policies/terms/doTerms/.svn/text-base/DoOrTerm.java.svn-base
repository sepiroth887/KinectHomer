package uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.doTerms;

import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;

public class DoOrTerm extends DoTerm
{

	@Override
	public TermType getTermType()
	{
		return TermType.OR;
	}

	@Override
	public void reset()
	{
		
	}

	@Override
	public void executeActions()
	{
		throw new UnsupportedOperationException("Trying to execute in a DoOrTerm which should never happen.");
	}

	@Override
	public boolean evaluate()
	{
		for (DoTerm child: children)
		{
			if (child.evaluate())
			{
				return true;
			}
		}
		return false;
	}

}
