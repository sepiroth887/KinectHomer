package uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.whenTerms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;

public class WhenOrTerm extends WhenTerm
{
	private final static Logger logger = LoggerFactory.getLogger(WhenOrTerm.class);
	
	private boolean oneOrWasMet = false;

	@Override
	public boolean evaluate() {
		return oneOrWasMet;
	}

	@Override
	public void reset() {
		oneOrWasMet = false;
		for (WhenTerm child : children)
		{
			child.reset();
		}
	}

	@Override
	public TermType getTermType() {
		return TermType.OR;
	}

	@Override
	public void termNowTrue(WhenTerm term) {
		oneOrWasMet = true;
		
		// stop listening to the children, as since once has reported that
		// they are true means that the OR condition has been met
		for (WhenTerm child : children)
		{
			child.stopListening();
		}
		
		if (parentListener!=null) parentListener.termNowTrue(this);
	}
}
