package uk.ac.stir.cs.homer.homerFrameworkAPI.tcas;

import org.json.JSONException;

import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.ParameterData;

public class Trigger {
	public static final String DEFAULT_IS_ON_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.is_on");
	public static final String DEFAULT_IS_OFF_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.is_off");
	public static final String DEFAULT_LIGHT_ON_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.lighton");
	public static final String DEFAULT_LIGHT_OFF_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.lightoff");
	public static final String DEFAULT_WINDOW_OPENS_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.window_open");
	public static final String DEFAULT_WINDOW_CLOSES_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.window_closed");
	public static final String DEFAULT_DOOR_OPENS_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.door_open");
	public static final String DEFAULT_DOOR_CLOSES_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.door_closed");
	public static final String DEFAULT_FINGER_POINT_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.finger_point");
	public static final String DEFAULT_MOVEMENT_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.movement");
	public static final String DEFAULT_COLD_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.cold");
	public static final String DEFAULT_HOT_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.hot");
	public static final String DEFAULT_EMAIL_RECEIVED_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.email.received");
	public static final String DEFAULT_SMS_RECEIVED_IMAGE = System.getProperty("uk.ac.stir.cs.homer.user.icons.sms.received");
	
	private String id;
	private String description;
	private ParameterData parameterData;
	private String beforeImage;
	private String afterImage;
	
	public Trigger(String id, String description) {
		this.id = id;
		this.description = description;
	}
	public Trigger(String id, String description, String beforeImage, String afterImage) {
		this.id = id;
		this.description = description;
		this.beforeImage = beforeImage;
		this.afterImage = afterImage;
		
	}
	
	public Trigger(String id, String description, ParameterData parameterData) {
		this.id = id;
		this.description = description;
		this.parameterData = parameterData;
	}
	public Trigger(String id, String description, String beforeImage, String afterImage, ParameterData parameterData) {
		this.id = id;
		this.description = description;
		this.beforeImage = beforeImage;
		this.afterImage = afterImage;
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
	public String getAfterImage() {
		return afterImage;
	}
	public String getBeforeImage() {
		return beforeImage;
	}
	
	public String convertParameterDataToJSONString() {
		String convertToJSONString = null;
		try {
			if (getParameterData() != null)
				convertToJSONString = getParameterData().convertToJSONString();
		} catch (JSONException e) {
			System.err.println("can't convert trigger parameters to json object: " + e);
		}
		return convertToJSONString;
	}
}
