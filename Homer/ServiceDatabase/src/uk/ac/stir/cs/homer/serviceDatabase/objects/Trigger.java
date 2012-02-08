package uk.ac.stir.cs.homer.serviceDatabase.objects;


public class Trigger extends DBObject implements Comparable<Trigger>{

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS Trigger (" +
		"id VARCHAR, " +
		"description VARCHAR, " +
		"jsonParameterData VARCHAR, " +
		"forType VARCHAR REFERENCES SystemDeviceType(id) ON DELETE CASCADE, " +
		"beforeImage INT REFERENCES Resources (id), " +
		"afterImage INT REFERENCES Resources (id), " +
		"PRIMARY KEY (id, forType) " +
	")";
		
	public static final String SQL_INSERT = "INSERT INTO Trigger VALUES (?,?,?,?,?,?)";
	public static final String SQL_UPDATE = "UPDATE Trigger SET description=?, jsonParameterData=?, forType=?, beforeImage=?, afterImage=? WHERE id=?;";
	public static final String SQL_DELETE = "DELETE FROM Trigger WHERE id = ?";
	
	
	private String id;
	private String description;
	private String jsonParamData;
	private String forType;
	private String beforeImage;
	private String afterImage;
	
	public Trigger() {};
	
	public Trigger(String id, String description, String jsonParamData, String forType, String beforeImage, String afterImage)
	{
		init(new String[] {id, description, jsonParamData, forType, beforeImage, afterImage});
	}
	
	public String getDescription() {
		return description;
	}
	public String getId() {
		return id;
	}
	public String getJsonParamData() {
		return jsonParamData;
	}

	public String getBeforeImage() {
		return beforeImage;
	}
	public String getAfterImage() {
		return afterImage;
	}
	public String getSystemDeviceType() {
		return forType;
	}
	@Override
	public int getConstructorSize() {
		return 6;
	}

	@Override
	public void init(String[] params) {
		this.id = params[0];
		this.description = params[1];
		this.jsonParamData = params[2];
		this.forType = params[3];
		this.beforeImage = params[4];
		this.afterImage = params[5];
	}

	public void setJsonParamData(String jsonParamData) {
		this.jsonParamData = jsonParamData;
	}

	public void setSystemDeviceTypeId(String forType) {
		this.forType = forType;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void setAfterImage(String afterImage) {
		this.afterImage = afterImage;
	}
	public void setBeforeImage(String beforeImage) {
		this.beforeImage = beforeImage;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Trigger)
		{
			return this.id.equals(((Trigger)arg0).getId());
		}
		return false;
	}

	@Override
	public int compareTo(Trigger arg0)
	{
		if (arg0 != null)
		{
			return this.description.compareToIgnoreCase(arg0.getDescription());
		}
		return 0;
	}



}
