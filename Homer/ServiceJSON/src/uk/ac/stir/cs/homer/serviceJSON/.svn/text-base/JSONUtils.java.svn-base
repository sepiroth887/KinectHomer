package uk.ac.stir.cs.homer.serviceJSON;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONUtils {
	
	public static String convertJavaArrayToJSONString(Object[] array)
	{
		String jsonArray = JSONUtils.convertJavaObjectArrayToJSONArray(array);
		return (jsonArray == null)? null : jsonArray.toString();
	}
	
	public static String convertJavaArrayToJSONString(String[] array)
	{
		String jsonArray = JSONUtils.convertJavaStringArrayToJSONArray(array);
		return (jsonArray == null)? null : jsonArray.toString();
	}
	
	public static String[] convertJSONArrayToJavaStringArray(String jsonArray) {
		
		if (jsonArray != null && !jsonArray.equals(""))
		{
			List<String> jArray = convertJSONArrayToJavaStringList(jsonArray);
			return jArray.toArray(new String[]{});
		}
		return new String[]{};
	}
	public static List<String> convertJSONArrayToJavaStringList(String jsonArray) {
		
		if (jsonArray != null && !jsonArray.equals(""))
		{
			try {
				JSONArray array = new JSONArray(jsonArray);
				ArrayList<String> javaArray = new ArrayList<String>(array.length());
				for (int i = 0; i < array.length(); i++)
				{
					javaArray.add(array.getString(i));
				}
				return javaArray;
			} catch (JSONException e) {
				System.err.println("can't convert jsonArray to java string array: " + e);
			}
		}
		return new ArrayList<String>(0);
	}
	public static String convertJavaStringArrayToJSONArray(String[] parameters) {
		String jsonConfig = null;
		if (parameters != null && parameters.length>0)
		{
			JSONArray jsonArray = null;
			try {
				jsonArray = new JSONArray(parameters);
				jsonConfig = jsonArray.toString();
			} catch (JSONException e) {
				System.err.println("can't convert java string array to json array: " + e);
			}
		}
		return jsonConfig;
	}
	
	public static String convertJavaObjectArrayToJSONArray(Object[] parameters) {
		String jsonConfig = null;
		if (parameters != null && parameters.length>0)
		{
			JSONArray jsonArray = new JSONArray();
			for (Object o : parameters)
			{
				jsonArray.put(o.toString());
			}
			jsonConfig = jsonArray.toString();
		}
		return jsonConfig;
	}
}
