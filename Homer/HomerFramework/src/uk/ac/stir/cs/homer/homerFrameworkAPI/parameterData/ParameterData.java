package uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class ParameterData
{
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	
	public ParameterData(Parameter... params)
	{
		for (Parameter p: params)
		{
			parameters.add(p);
		}
	}
	public ParameterData(Parameter p)
	{
		parameters.add(p);
	}
	
	public ParameterData(String parameterDataJSON) throws JSONException
	{
		if (parameterDataJSON==null) throw new JSONException("No Parameter data");
		
		JSONArray parameterData = new JSONArray(parameterDataJSON);
		for (int i = 0; i< parameterData.length(); i++)
		{
			parameters.add(new Parameter(parameterData.getJSONObject(i)));
		}
	}
	
	public String convertToJSONString() throws JSONException
	{
		JSONArray parameterData = new JSONArray();
		for (Parameter parameter : parameters)
		{
			parameterData.put(parameter.getJSONObject());
		}
		return parameterData.toString();
	}
	
	public ArrayList<Parameter> getParameters()
	{
		return parameters;
	}
	
}
