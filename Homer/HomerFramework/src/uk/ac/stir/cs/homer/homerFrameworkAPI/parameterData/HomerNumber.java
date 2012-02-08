package uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData;

import org.json.JSONException;
import org.json.JSONObject;

public class HomerNumber implements DataType
{
	private final double minimumValue;
	private final double maximumValue;
	private final double step;
	private final int numDecimalPlaces;
	private final double defaultValue;
	private Unit unit;
	
	public HomerNumber (double minimumValue, double maximumValue, double step, int numDecimalPlaces, double defaultValue, Unit unit)
	{
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
		this.step = step;
		this.numDecimalPlaces = numDecimalPlaces;
		this.defaultValue = defaultValue;
		this.unit = unit;
	}
	
	public HomerNumber(JSONObject data) throws JSONException {
		this.minimumValue = data.getDouble("minimumValue");
		this.maximumValue = data.getDouble("maximumValue");
		this.step = data.getDouble("step");
		this.numDecimalPlaces = data.getInt("numDecimalPlaces");
		this.defaultValue = data.getDouble("defaultValue");
		try {
			this.unit = Unit.valueOf(data.getString("unit"));
		} catch (JSONException e) {
			this.unit = Unit.NONE;
		}
	}
	
	@Override
	public JSONObject getJSONObject() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("type", DataType.NUMBER);
		o.put("minimumValue", minimumValue);
		o.put("maximumValue", maximumValue);
		o.put("step", step);
		o.put("numDecimalPlaces", numDecimalPlaces);
		o.put("defaultValue", defaultValue);
		if (unit!=null) o.put("unit", unit.name());
		return o;
	}
	
	public double getDefaultValue() {
		return defaultValue;
	}
	public double getMaximumValue() {
		return maximumValue;
	}
	public double getMinimumValue() {
		return minimumValue;
	}
	public int getNumDecimalPlaces() {
		return numDecimalPlaces;
	}
	public double getStep() {
		return step;
	}
	public Unit getUnit() {
		return unit;
	}

	@Override
	public int getDataType() {
		return DataType.NUMBER;
	}
}
