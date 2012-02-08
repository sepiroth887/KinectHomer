package uk.ac.stir.cs.homer.homerPolicyServer.overlap;

import java.util.ArrayList;
import java.util.List;

public class Overlap
{
	private List<OverlappingVariable> variables = new ArrayList<OverlappingVariable>();
	
	public void addVariable(OverlappingVariable overlappingVariable)
	{
		variables.add(overlappingVariable);
	}
	
	public List<OverlappingVariable> getVariables()
	{
		return variables;
	}
}
