package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class UserDevice extends DBObject implements Comparable<UserDevice>{

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS UserDevice (" +
						"id INT PRIMARY KEY AUTO_INCREMENT, " +
						"name VARCHAR, " +
						"location INT REFERENCES Location(id) ON DELETE CASCADE, " +
						"type INT REFERENCES UserDeviceType(id) ON DELETE CASCADE, " +
						"image INT REFERENCES Resources (id), " +
						"defaultState VARCHAR REFERENCES Condition(id), " +
						"currentState VARCHAR REFERENCES Condition(id), " +
						"currentStateParameters VARCHAR " +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO UserDevice(name, location, type, image, defaultState) VALUES (?,?,?,?,?)";
	public static final String SQL_UPDATE = "UPDATE UserDevice SET name=?, location=?, type=?, image=?, defaultState=?, currentState=?, currentStateParameters=? WHERE id=?;";
	public static final String SQL_UPDATE_STATE = "UPDATE UserDevice SET currentState=?, currentStateParameters=? WHERE id=?;";
	public static final String SQL_DELETE = "DELETE FROM UserDevice WHERE id = ?";

	
	
	private String id;
	private String name;
	private String locationID;
	private String userDeviceTypeID;
	private String image;
	private String defaultState;
	private String currentState;
	private String currentStateParameters;
	
	public UserDevice() {}
	
	public UserDevice(String id, String name, String locationID, String userDeviceTypeID, String image, String defaultState, String currentState, String currentStateParameters)
	{
		init(new String[] {id, name, locationID, userDeviceTypeID, image, defaultState, currentState, currentStateParameters});
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getLocationID() {
		return locationID;
	}
	public String getTypeID() {
		return userDeviceTypeID;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUserDeviceTypeID(String userDeviceTypeID) {
		this.userDeviceTypeID = userDeviceTypeID;
	}
	public String getDefaultState() {
		return defaultState;
	}
	public void setDefaultState(String defaultState) {
		this.defaultState = defaultState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public void setCurrentStateParameters(String currentStateParameters) {
		this.currentStateParameters = currentStateParameters;
	}
	public String getCurrentStateParameters() {
		return currentStateParameters;
	}
	public String getCurrentState() {
		return currentState;
	}
	
	@Override
	public int getConstructorSize() {
		return 8;
	}

	@Override
	public void init(String[] params) {
		this.id = params[0];
		this.name = params[1];
		this.locationID = params[2];
		this.userDeviceTypeID = params[3];
		this.image = params[4];
		this.defaultState = params[5];
		this.currentState = params[6];
		this.currentStateParameters = params[7];
	}

	@Override
	public int compareTo(UserDevice otherUserDevice)
	{
		if (otherUserDevice != null)
		{
			return this.name.compareToIgnoreCase(otherUserDevice.getName());
		}
		return 0;
	}
}
