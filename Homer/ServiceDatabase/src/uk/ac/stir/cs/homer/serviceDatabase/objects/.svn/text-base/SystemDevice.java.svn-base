package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class SystemDevice extends DBObject {

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS SystemDevice (" +
						"id INT PRIMARY KEY AUTO_INCREMENT, " +
						"jsonConfig VARCHAR, " +
						"type VARCHAR REFERENCES SystemDeviceType(id) ON DELETE CASCADE, " +
						"parent INT REFERENCES UserDevice(id) ON DELETE CASCADE, " +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO SystemDevice (jsonConfig, type, parent) VALUES (?,?,?)";
	public static final String SQL_UPDATE = "UPDATE SystemDevice SET jsonConfig=?, type=?, parent=? WHERE id=?;";
	public static final String SQL_DELETE = "DELETE FROM SystemDevice WHERE id = ?";
	
	private String id;
	private String jsonConfigData;
	private String systemDeviceTypeID;
	private String userDeviceID;

	public SystemDevice(){};
	
	public SystemDevice(String id, String jsonConfigData, String systemDeviceTypeID, String userDeviceID)
	{
		init(new String[] {id, jsonConfigData, systemDeviceTypeID, userDeviceID});
	}
	
	public String getId() {
		return id;
	}
	
	public String getJsonConfigData() {
		return jsonConfigData;
	}
	public String getSystemDeviceTypeID() {
		return systemDeviceTypeID;
	}
	
	public String getUserDeviceID() {
		return userDeviceID;
	}

	@Override
	public int getConstructorSize() {
		return 4;
	}

	@Override
	public void init(String[] params) {
		this.id = params[0];
		this.jsonConfigData = params[1];
		this.systemDeviceTypeID = params[2];
		this.userDeviceID = params[3];
	}
	
}
