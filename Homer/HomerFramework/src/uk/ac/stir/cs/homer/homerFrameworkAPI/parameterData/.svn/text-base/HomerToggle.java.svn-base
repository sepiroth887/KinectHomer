package uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData;

import org.json.JSONException;
import org.json.JSONObject;

public class HomerToggle implements DataType
{
	private boolean defaultState;
	private String text;
	private Unit unit;
	
	public HomerToggle(String text, Unit unit, boolean defaultState)
	{
		this.text = text;
		this.unit = unit;
		this.defaultState = defaultState;
	}
	
	public HomerToggle(JSONObject data) throws JSONException
	{
		this.text = data.getString("text");
		
		try {
			this.defaultState = data.getBoolean("defaultValue");
		} catch (JSONException e) {
			this.defaultState = false;
		}
		try {
			this.unit = Unit.valueOf(data.getString("unit"));
		} catch (JSONException e) {
			this.unit = Unit.NONE;
		}
		
	}
	
	@Override
	public JSONObject getJSONObject() throws JSONException
	{
		JSONObject o = new JSONObject();
		o.put("type", DataType.TOGGLE);
		o.put("text", text);
		o.put("defaultValue", defaultState);
		if (unit!=null) o.put("unit", unit.name());
		return o;
	}

	public String getText()
	{
		return text;
	}
	 
	public boolean getDefaultState()
	{
		return defaultState;
	}
	
	@Override
	public int getDataType()
	{
		return DataType.TOGGLE;
	}

	@Override
	public Unit getUnit()
	{
		return unit;
	}

}
