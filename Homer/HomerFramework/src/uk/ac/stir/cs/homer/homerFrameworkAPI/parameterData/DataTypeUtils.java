package uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData;

import org.json.JSONException;
import org.json.JSONObject;

public class DataTypeUtils
{
	public static DataType getDataType(JSONObject data) throws JSONException {
		int type = data.getInt("type");
		if (type == DataType.NUMBER)
		{
			return new HomerNumber(data);
		}
		else if (type == DataType.CHOICE)
		{
			return new HomerChoice(data);
		}
		else if (type == DataType.TEXT)
		{
			return new HomerText(data);
		}
		else if (type == DataType.TOGGLE)
		{
			return new HomerChoice(data);
		}
		return null;
	}
}
