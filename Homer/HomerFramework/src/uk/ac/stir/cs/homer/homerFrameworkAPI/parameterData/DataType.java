package uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData;

import org.json.JSONException;
import org.json.JSONObject;

public interface DataType 
{
	public static final int NUMBER = 1;
	public static final int TEXT = 2;
	public static final int CHOICE = 3;
	public static final int TOGGLE = 4;
	
	public JSONObject getJSONObject() throws JSONException;
	public int getDataType();
	public Unit getUnit();
}
