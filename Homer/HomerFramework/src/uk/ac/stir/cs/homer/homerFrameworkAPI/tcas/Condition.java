package uk.ac.stir.cs.homer.homerFrameworkAPI.tcas;

import org.json.JSONException;

import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.ParameterData;

public class Condition {
	public static final String DEFAULT_IS_ON_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.is_on");
	public static final String DEFAULT_IS_OFF_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.is_off");
	public static final String DEFAULT_LIGHT_ON_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.lighton");
	public static final String DEFAULT_LIGHT_OFF_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.lightoff");
	public static final String DEFAULT_DOOR_IS_OPEN_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.door_open");
	public static final String DEFAULT_DOOR_IS_CLOSED_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.door_closed");
	public static final String DEFAULT_WINDOW_IS_CLOSED_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.window_closed");
	public static final String DEFAULT_WINDOW_IS_OPEN_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.window_open");
	public static final String DEFAULT_DATE_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.date");
	public static final String DEFAULT_TIME_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.time");

	private String id;
	private String description;
	private ParameterData parameterData;
	private String image;
	
	private String[] triggerIDsAndOrActionIDsResultingInThisState;
	
	
	public Condition(String id, String description, String image, ParameterData parameterData, String... triggerIDsAndOrActionIDsResultingInThisState) {
		this.id = id;
		this.description = description;
		this.image = image;
		this.parameterData = parameterData;
		this.triggerIDsAndOrActionIDsResultingInThisState = triggerIDsAndOrActionIDsResultingInThisState;
	}
	
	public String getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ParameterData getParameterData() {
		return parameterData;
	}
	public String getImage() {
		return image;
	}
	
	public String[] getTriggerIDsAndOrActionIDsResultingInThisState() {
		return triggerIDsAndOrActionIDsResultingInThisState;
	}
	
	public String convertParameterDataToJSONString() {
		String convertToJSONString = null;
		try {
			if (getParameterData() != null)
				convertToJSONString = getParameterData().convertToJSONString();
		} catch (JSONException e) {
			System.err.println("can't convert condition parameters to json object: " + e);
		}
		return convertToJSONString;
	}
}
