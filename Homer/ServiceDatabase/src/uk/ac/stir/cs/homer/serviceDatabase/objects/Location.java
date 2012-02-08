package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class Location extends DBObject {

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS Location (" +
						"id INT PRIMARY KEY AUTO_INCREMENT, " +
						"name VARCHAR NOT NULL, " +
						"context INT REFERENCES LocationContext (id)  ON DELETE CASCADE, " +
						"image INT REFERENCES Resources (id), " +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO Location (name, context,image) VALUES (?,?,?)";
	public static final String SQL_UPDATE = "UPDATE Location SET name=?, context=?, image=? WHERE id=?";
	public static final String SQL_DELETE = "DELETE FROM Location WHERE id = ?";
	public static final String SQL_GET_ID = "SELECT id FROM Location WHERE name=? AND context=? AND image=?";
	
	private String id;
	private String name;
	private String locationContextID;
	private String image;

	public Location(){}
	
	public Location(String id, String name, String locationContextID, String image)
	{
		init(new String[] {id, name, locationContextID, image});
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLocationContextID() {
		return locationContextID;
	}

	public String getImage() {
		return image;
	}
	
	@Override
	public int getConstructorSize() {
		return 4;
	}

	@Override
	public void init(String[] params) {
		this.id = params[0];
		this.name = params[1];
		this.locationContextID = params[2];
		this.image = params[3];
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImage(String image) {
		this.image = image;
	}
}