package uk.ac.stir.cs.homer.homerFrameworkAPI.configData;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigData
{
	private ArrayList<ConfigPart> parameters = new ArrayList<ConfigPart>();
	private static Logger logger = LoggerFactory.getLogger(ConfigData.class);
	
	public ConfigData(String jsonConfigData)
	{
		try {
			JSONArray configData = new JSONArray(jsonConfigData);
			
			for (int paramNumber = 0; paramNumber < configData.length(); paramNumber++)
			{
				JSONObject parameter = configData.getJSONObject(paramNumber);
				parameters.add(new ConfigPart(parameter));
			}
			
		} catch (JSONException e) {
			//if (!e.getMessage().contains("] not found"))
				logger.error("Error converting Config Data to JSON object: ", e);
		}
		
	}
	
	public ConfigData(ConfigPart...parts)
	{
		if (parts!=null)
		{
			for (ConfigPart p: parts)
			{
				parameters.add(p);
			}
		}
	}

	public String convertToJSONString()
	{
		String paramData = null;
		try {
			JSONArray parameterData = new JSONArray();
			for (ConfigPart configPart : parameters)
			{
				parameterData.put(configPart.getJSONObject());
			}
			paramData = parameterData.toString();
		} catch (JSONException e) {
			System.err.println("Couldn't parse parameter data, so saving null to database instead: " + e);
		}
		return paramData;
	}
	
	public ArrayList<ConfigPart> getParameters() {
		return parameters;
	}
	
}
