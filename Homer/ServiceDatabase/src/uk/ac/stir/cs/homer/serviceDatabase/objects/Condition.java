package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class Condition extends DBObject implements Comparable<Condition>{

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS Condition (" +
						"id VARCHAR, " +
						"description VARCHAR, " +
						"jsonParameterData VARCHAR, " +
						"forType VARCHAR REFERENCES SystemDeviceType(id) ON DELETE CASCADE, " +
						"image INT REFERENCES Resources (id), " +
						"actionIDresultingInThisState VARCHAR, " +
						"PRIMARY KEY (id, forType) " +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO Condition VALUES (?,?,?,?,?,?)";
	public static final String SQL_UPDATE = "UPDATE Condition SET description=?, jsonParameterData=?, forType=?, image=?, actionIDresultingInThisState=? WHERE id=?;";
	public static final String SQL_DELETE = "DELETE FROM Condition WHERE id = ?";
	
	private String id;
	private String description;
	private String jsonParamData;
	private String forType;
	private String image;
	private String triggerIDsAndOrActionIDsResultingInThisState;
	
	public Condition() {};
	
	public Condition(String id, String description, String jsonParamData, String forType, String image, String triggerIDsAndOrActionIDsResultingInThisState)
	{
		init(new String[] {id, description, jsonParamData, forType,image, triggerIDsAndOrActionIDsResultingInThisState});
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
		return 6;
	}

	@Override
	public void init(String[] params) {
		this.id = params[0];
		this.description = params[1];
		this.jsonParamData = params[2];
		this.forType = params[3];
		this.image = params[4];
		this.triggerIDsAndOrActionIDsResultingInThisState = params[5];
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
	public String getTriggerIDsAndOrActionIDsResultingInThisState() {
		return triggerIDsAndOrActionIDsResultingInThisState;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0!=null && arg0 instanceof Condition)
		{
			return this.id.equals(((Condition)arg0).getId());
		}
		return false;
	}
	
	@Override
	public int compareTo(Condition arg0)
	{
		if (arg0 != null)
		{
			return this.description.compareToIgnoreCase(arg0.getDescription());
		}
		return 0;
	}
}
