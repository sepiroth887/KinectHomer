package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class ActionEnvironEffect extends DBObject
{
	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS ActionEnvironEffect (" +
						"userdevicetype INT REFERENCES UserDeviceType(id) ON DELETE CASCADE, " +
						"systemdevicetype VARCHAR REFERENCES SystemDeviceType(id) ON DELETE CASCADE, " +
						"action VARCHAR, " + // ideally, but need to allow '*': REFERENCES Action(id) ON DELETE CASCADE, " +
						"environ VARCHAR, " + // ideally, but need to allow '*': REFERENCES Environ(id) ON DELETE CASCADE, " +
						"effect VARCHAR, " +
						"PRIMARY KEY (userdevicetype, systemdevicetype, action, environ)" +
					")"; 
		
	public static final String SQL_INSERT = "INSERT INTO ActionEnvironEffect VALUES (?,?,?,?,?)";
	public static final String SQL_UPDATE = "UPDATE ActionEnvironEffect SET effect=? WHERE userdevicetype=? AND systemdevicetype=? AND action=? AND environ=?;";
	public static final String SQL_DELETE = "DELETE FROM ActionEnvironEffect WHERE userdevicetype=? AND systemdevicetype=?;";

	private String userDeviceTypeID;
	private String systemDeviceTypeID;
	private String actionID;
	private String environID;
	private String environEffect;

	
	public ActionEnvironEffect() {}
	
	public ActionEnvironEffect(String userDeviceTypeID, String systemDeviceTypeID, String actionID, String environID, String environEffect)
	{
		this.userDeviceTypeID = userDeviceTypeID;
		this.systemDeviceTypeID = systemDeviceTypeID;
		this.actionID = actionID;
		this.environID = environID; 
		this.environEffect = environEffect;
	}
	
	@Override
	public int getConstructorSize()
	{
		return 5;
	}

	@Override
	public void init(String[] params)
	{
		this.userDeviceTypeID = params[0];
		this.systemDeviceTypeID = params[1];
		this.actionID = params[2];
		this.environID = params[3];
		this.environEffect = params[4];
	}
	
	public String getActionID()
	{
		return actionID;
	}
	public String getEnvironEffectString()
	{
		return environEffect;
	}
	public EnvironEffect getEnvironEffect()
	{
		return EnvironEffect.valueOf(environEffect);
	}
	public String getEnvironID()
	{
		return environID;
	}
	public String getUserDeviceTypeID()
	{
		return userDeviceTypeID;
	}
	public String getSystemDeviceTypeID()
	{
		return systemDeviceTypeID;
	}
	
	public boolean isEmpty()
	{
		return actionID.equals("*");
	}
}
