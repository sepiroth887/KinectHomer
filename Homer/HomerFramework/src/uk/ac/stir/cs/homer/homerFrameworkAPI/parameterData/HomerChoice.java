package uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomerChoice implements DataType
{
	private String defaultValue;
	private String[] possibleValues;
	private Unit unit;

	public HomerChoice (String defaultValue, String[] possibleValues, Unit unit)
	{
		this.defaultValue = defaultValue;
		this.possibleValues = possibleValues;
		this.unit = unit;
	}
	
	public HomerChoice(JSONObject data) {

		try
		{
			JSONArray jsonPossibleValues = data.getJSONArray("posValues");
			possibleValues = new String[jsonPossibleValues.length()];
			for (int i = 0; i<possibleValues.length; i++)
			{
				possibleValues[i] = jsonPossibleValues.getString(i);
			}
		} catch(JSONException e)
		{
			possibleValues = new String[] {};
		}
		
		try {
			this.defaultValue = data.getString("defaultValue");
		} catch (JSONException e) {
			this.defaultValue = "";
		}
		try {
			this.unit = Unit.valueOf(data.getString("unit"));
		} catch (JSONException e) {
			this.unit = Unit.NONE;
		}
	}
	
	@Override
	public JSONObject getJSONObject() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("type", DataType.CHOICE);
		o.put("defaultValue", defaultValue);
		if (unit!=null) o.put("unit", unit.name());
		
		JSONArray jsonPossibleValues = new JSONArray(possibleValues);
		o.put("posValues", jsonPossibleValues);
		
		return o;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	public String[] getPossibleValues() {
		return possibleValues;
	}
	public Unit getUnit() {
		return unit;
	}

	@Override
	public int getDataType() {
		return DataType.CHOICE;
	}
}
