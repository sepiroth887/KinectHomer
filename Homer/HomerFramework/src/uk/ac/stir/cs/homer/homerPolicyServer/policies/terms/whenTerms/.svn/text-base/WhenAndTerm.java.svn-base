package uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.whenTerms;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.stir.cs.homer.homerPolicyServer.LivePolicy;
import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;

public class WhenAndTerm extends WhenTerm implements TermListener
{
	private final static Logger logger = LoggerFactory.getLogger(WhenAndTerm.class);
	
	private int numAndTermsOccured = 0;

	private final int duration;

	private ScheduledFuture<?> timer;
	private boolean timerRunning = false;
	
	public WhenAndTerm(int duration)
	{
		this.duration = duration==0? LivePolicy.DEFAULT_WHEN_DURATION : duration;
	}

	@Override
	public boolean evaluate() {
		return numAndTermsOccured == children.size();
	}

	@Override
	public void reset() {
		logger.trace("Reseting and term.");
		
		numAndTermsOccured = 0;
		
		if (timerRunning) 
		{
			timerRunning = false;
			timer.cancel(true);
		}
		
		for (WhenTerm child : children)
		{
			child.reset();
		}
	}

	@Override
	public TermType getTermType() {
		return TermType.AND;
	}

	@Override
	public void termNowTrue(WhenTerm term)
	{
		logger.debug("Child of AND term is now true.");
		
		term.stopListening();
		numAndTermsOccured++;
		
		if (parentListener!=null && evaluate()) 
		{
			logger.debug("All and terms have now occured.");
			timer.cancel(true);
			timerRunning = false;
			parentListener.termNowTrue(this);
		}
		else 
		{
			evaluateEventConditions(this);
			
			if (!timerRunning)
			{
				logger.debug("First term of AND occured, starting timer for: " + duration + " seconds.");
				
				timer = Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
					public void run() {
						reset();
						logger.debug("Timer ran out, so AND term failed.");
					}
					
				}, duration, TimeUnit.SECONDS);
			}
		}
	}
}
