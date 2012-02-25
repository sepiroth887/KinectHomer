package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class Action extends DBObject implements Comparable<Action> {

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS Action (" +
						"id VARCHAR, " +
						"description VARCHAR, " +
						"jsonParameterData VARCHAR, " +
						"forType VARCHAR REFERENCES SystemDeviceType(id) ON DELETE CASCADE, " +
						"image INT REFERENCES Resources (id), " +
						"PRIMARY KEY (id, forType) " +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO Action VALUES (?,?,?,?,?)";
	public static final String SQL_UPDATE = "UPDATE Action SET description=?, jsonParameterData=?, forType=?, image=? WHERE id=?;";
	public static final String SQL_DELETE = "DELETE FROM Action WHERE id = ?";
	
	private String id;
	private String description;
	private String jsonParamData;
	private String forType;
	private String image;
	
	public Action() {};
	
	public Action(String id, String description, String jsonParamData, String forType, String image)
	{
		init(new String[] {id, description, jsonParamData, forType,image});
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

	public String getImage() {
		return image;
	}
	public String getSystemDeviceType() {
		return forType;
	}
	
	
	@Override
	public int getConstructorSize() {
		return 5;
	}

	@Override
	public void init(String[] params) {
		this.id = params[0];
		this.description = params[1];
		this.jsonParamData = params[2];
		this.forType = params[3];
		this.image = params[4];
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setSystemDeviceType(String forType) {
		this.forType = forType;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setJsonParamData(String jsonParamData) {
		this.jsonParamData = jsonParamData;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Action)
		{
			return this.id.equals(((Action)arg0).getId());
		}
		return false;
	}

	@Override
	public int compareTo(Action arg0)
	{
		if (arg0 != null)
		{
			return this.description.compareToIgnoreCase(arg0.getDescription());
		}
		return 0;
	}
	
	@Override
	public String toString(){
		return getDescription();
	}
}
