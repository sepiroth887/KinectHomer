package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class UserDeviceType extends DBObject {

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS UserDeviceType (" +
						"id INT PRIMARY KEY AUTO_INCREMENT, " +
						"name VARCHAR, " +
						"image INT REFERENCES Resources (id), " +
						"parent INT REFERENCES UserDeviceType(id) " +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO UserDeviceType(name,image,parent) VALUES (?,?,?)";
	public static final String SQL_UPDATE = "UPDATE UserDeviceType SET name=?, image=?, parent=? WHERE id=?;";
	public static final String SQL_DELETE = "DELETE FROM UserDeviceType WHERE id = ?";
	
	private String id;
	private String name;
	private String image;
	private String parent;
	
	public UserDeviceType() {}
	
	public UserDeviceType(String id, String name, String image, String parent)
	{
		init(new String[] {id, name, image, parent});
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
	public String getParent()
	{
		return parent;
	}
	
	@Override
	public int getConstructorSize() {
		return 4;
	}

	@Override
	public void init(String[] params) {
		this.id = params[0];
		this.name = params[1];
		this.image = params[2];
		this.parent = params[3];
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public void setParent(String parent)
	{
		this.parent = parent;
	}
}
