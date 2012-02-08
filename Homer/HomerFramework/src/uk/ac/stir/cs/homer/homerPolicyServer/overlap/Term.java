package uk.ac.stir.cs.homer.homerPolicyServer.overlap;

import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.Comparitor;
import uk.ac.stir.cs.homer.homerPolicyServer.policies.terms.TermType;

public class Term
{

	private final String name;
	private final int value;
	private final int time;

	public Term(String name, int value, int time, Comparitor comparitor, TermType termType)
	{
		this.name = name;
		this.value = value;
		this.time = time;
	}
	
	public String getName()
	{
		return name;
	}
	public int getTime()
	{
		return time;
	}
	public int getValue()
	{
		return value;
	}
}
