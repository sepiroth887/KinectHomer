package uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData;

import org.json.JSONException;
import org.json.JSONObject;

public class Parameter 
{
	private String preText = null;
	private DataType dataType;
	private String postText = null;
	private Comparitor comparitor;
	
	public Parameter(String preText, DataType dataType, String postText, Comparitor comparitor)
	{
		this.preText = preText;
		this.dataType = dataType;
		this.postText = postText;
		this.comparitor = comparitor;
	}
	
	public Parameter(DataType dataType, Comparitor comparitor)
	{
		this.dataType = dataType;
		this.comparitor = comparitor;
	}

	public Parameter(JSONObject jsonObject) throws JSONException
	{
		if (jsonObject.has("preText")) this.preText = jsonObject.getString("preText");
		if (jsonObject.has("dataType")) this.dataType = DataTypeUtils.getDataType(jsonObject.getJSONObject("dataType"));
		if (jsonObject.has("postText")) this.postText = jsonObject.getString("postText");
		if (jsonObject.has("comparitor")) this.comparitor = Comparitor.valueOf(jsonObject.getString("comparitor"));
	}
	
	public JSONObject getJSONObject() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("preText", preText);
		o.put("dataType", dataType.getJSONObject());
		o.put("postText", postText);
		o.put("comparitor", comparitor.toString());
		return o;
	}
	
	public Comparitor getComparitor()
	{
		return comparitor;
	}
	
	public boolean isNumberType()
	{
		return dataType.getDataType() == DataType.NUMBER;
	}
	
}
