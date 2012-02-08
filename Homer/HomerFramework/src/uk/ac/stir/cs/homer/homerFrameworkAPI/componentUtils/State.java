package uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils;

public class State 
{
	public static final State UNKNOWN_STATE = new State("Unknown");
	
	private final String conditionID;
	private final String[] parametersArray;
	private String parametersCSV;
	
	public State(String conditionID, String... parameters) 
	{
		this.conditionID = conditionID;
		this.parametersArray = parameters;
		if (parameters!=null)
		{
			for (String p : parameters)
			{
				parametersCSV += (p +",");
			}
		}
		else
		{
			parametersCSV = null;
		}
	}
	
	public String getConditionID() {
		return conditionID;
	}
	
	public String[] getParametersAsArray() {
		return parametersArray;
	}
	
	public String getParametersCommaSeperated() {
		return parametersCSV;
	}

}
