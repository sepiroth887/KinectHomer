package uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData;

import org.json.JSONException;
import org.json.JSONObject;

public class HomerText implements DataType
{
	private String defaultValue;
	private final int maximumNumberOfCharacters;
	private final int minimumNumberOfCharacters;
	private Unit unit;
	
	public HomerText (String defaultValue, int minimumNumberOfCharacters, int maximumNumberOfCharacters, Unit unit)
	{
		this.defaultValue = defaultValue;
		this.minimumNumberOfCharacters = minimumNumberOfCharacters;
		this.maximumNumberOfCharacters = maximumNumberOfCharacters;
		this.unit = unit;
	}
	
	public HomerText(JSONObject data) throws JSONException {

		this.maximumNumberOfCharacters = data.getInt("max");
		this.minimumNumberOfCharacters = data.getInt("min");
		
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
		o.put("type", DataType.TEXT);
		o.put("min", minimumNumberOfCharacters);
		o.put("max", maximumNumberOfCharacters);
		o.put("defaultValue", defaultValue);
		if (unit!=null) o.put("unit", unit.name());
		return o;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	public int getMaximumNumberOfCharacters() {
		return maximumNumberOfCharacters;
	}
	public int getMinimumNumberOfCharacters() {
		return minimumNumberOfCharacters;
	}
	public Unit getUnit() {
		return unit;
	}

	@Override
	public int getDataType() {
		return DataType.TEXT;
	}
}
