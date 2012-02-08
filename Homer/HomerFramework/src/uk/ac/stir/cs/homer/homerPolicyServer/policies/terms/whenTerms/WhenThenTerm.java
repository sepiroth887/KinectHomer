package uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.whenTerms;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerPolicyServer.LivePolicy;
import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;

public class WhenThenTerm extends WhenTerm
{
	private final static Logger logger = LoggerFactory.getLogger(WhenThenTerm.class);
	
	private final int duration;
	private ScheduledFuture<?> timer;
	
	private boolean allThensHappened = false;
	private int currentIndexOfChildBeingListened = 0;

	public WhenThenTerm(int duration)
	{
		this.duration = duration==0? LivePolicy.DEFAULT_WHEN_DURATION : duration;
	}

	@Override
	public boolean evaluate() 
	{
		return allThensHappened;
	}

	@Override
	public void reset() 
	{
		logger.trace("Reseting then term.");
		
		if (timer!=null) timer.cancel(true);
		
		currentIndexOfChildBeingListened = 0;
		allThensHappened = false;
		
		for (WhenTerm child : children)
		{
			child.reset();
			child.stopListening();
		}
	}

	public void startListening() 
	{
		WhenTerm nextWhenTermToListen = children.get(currentIndexOfChildBeingListened);
		nextWhenTermToListen.startListening();
		evaluateEventConditions(nextWhenTermToListen);
	}
	
	@Override
	public TermType getTermType() {
		return TermType.THEN;
	}

	@Override
	public void termNowTrue(WhenTerm term) {
		
		logger.debug("Child of then term is now true.");
		
		if (currentIndexOfChildBeingListened==0)
		{
			logger.debug("First term of THEN occured, starting timer for: " + duration +" seconds.");
			
			timer = Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
				public void run() {
					reset();
					logger.debug("Timer ran out, so THEN term failed.");
				}
				
			}, duration, TimeUnit.SECONDS);
		}
		
		// stop listening to the current child, now start listening to next child. 
		// if all the children have responded then set the flag to true
		children.get(currentIndexOfChildBeingListened).stopListening();
		currentIndexOfChildBeingListened++;
		if (currentIndexOfChildBeingListened == children.size())
		{
			logger.debug("All then terms have now occured.");
			timer.cancel(true);
			allThensHappened = true;
			if (parentListener!=null) parentListener.termNowTrue(this);
		}
		else
		{
			logger.debug("Listening for next then child.");
			startListening();
		}	
	}	
}
