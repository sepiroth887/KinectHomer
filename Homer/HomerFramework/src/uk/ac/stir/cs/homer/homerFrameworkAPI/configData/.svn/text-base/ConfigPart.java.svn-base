package uk.ac.stir.cs.homer.homerFrameworkAPI.configData;

import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.DataType;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.DataTypeUtils;

public class ConfigPart 
{
	private String preText = null;
	private DataType dataType;
	private String postText = null;

	public ConfigPart(String preText, DataType dataType, String postText)
	{
		this.preText = preText;
		this.dataType = dataType;
		this.postText = postText;
	}
	
	public ConfigPart(DataType dataType)
	{
		this.dataType = dataType;
	}

	public ConfigPart(JSONObject parameter) throws JSONException
	{
		try {
			preText = parameter.getString("preText");
		} catch (JSONException e1) {
			preText = "";
		}
		try {
			postText = parameter.getString("postText");
		} catch (JSONException e) {
			postText = "";;
		}
		
		JSONObject data = parameter.getJSONObject("dataType");
		dataType = DataTypeUtils.getDataType(data);
	}

	public JSONObject getJSONObject() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("preText", preText);
		o.put("dataType", dataType.getJSONObject());
		o.put("postText", postText);
		
		return o;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	public String getPostText() {
		return postText;
	}
	public String getPreText() {
		return preText;
	}
}
