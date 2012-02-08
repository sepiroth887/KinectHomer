package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class LocationContext extends DBObject {

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS LocationContext (" +
						"id INT PRIMARY KEY AUTO_INCREMENT, " +
						"name VARCHAR NOT NULL, " +
						"image INT REFERENCES Resources (id), " +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO LocationContext (name,image) VALUES (?,?)";
	public static final String SQL_UPDATE = "UPDATE LocationContext SET name=?, image=? WHERE id=?";
	public static final String SQL_DELETE = "DELETE FROM LocationContext WHERE id = ?";
	public static final String SQL_GET_ID = "SELECT id FROM LocationContext WHERE name = ?";
	
	private String id;
	private String name;
	private String image;
	
	public LocationContext(){}
	
	public LocationContext(String id, String name, String image) 
	{
		init(new String[] {id, name, image});
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public String getImage() {
		return image;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getConstructorSize() {
		return 3;
	}

	@Override
	public void init(String[] params) {
		id = params[0];
		name = params[1];
		image = params[2];
	}

	public void setImage(String image) {
		this.image =  image;
	}
}
