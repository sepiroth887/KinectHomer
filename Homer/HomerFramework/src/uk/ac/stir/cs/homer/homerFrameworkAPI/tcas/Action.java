package uk.ac.stir.cs.homer.homerFrameworkAPI.tcas;

import org.json.JSONException;

import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.ParameterData;

public class Action {
	public static final String DEFAULT_TURN_ON_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.turn_on");
	public static final String DEFAULT_TURN_OFF_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.turn_off");
	public static final String DEFAULT_LIGHT_ON_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.lighton");
	public static final String DEFAULT_LIGHT_OFF_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.lightoff");
	public static final String DEFAULT_SEND_EMAIL_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.email.send");
	public static final String DEFAULT_SEND_SMS_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.sms.send");
	
	private String id;
	private String description;
	private String image;
	private ParameterData parameterData;

	public Action(String id, String description) {
		this.id = id;
		this.description = description;
	}
	public Action(String id, String description, String image) {
		this.id = id;
		this.description = description;
		this.image = image;
	}
	public Action(String id, String description, ParameterData parameterData) {
		this.id = id;
		this.description = description;
		this.parameterData = parameterData;
	}
	public Action(String id, String description, String image, ParameterData parameterData) {
		this.id = id;
		this.description = description;
		this.image = image;
		this.parameterData = parameterData;
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
	
	public String convertParameterDataToJSONString() {
		String convertToJSONString = null;
		try {
			if (getParameterData() != null)
				convertToJSONString = getParameterData().convertToJSONString();
		} catch (JSONException e) {
			System.err.println("can't convert action parameters to json object: " + e);
		}
		return convertToJSONString;
	}
}
