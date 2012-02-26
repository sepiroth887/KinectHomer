package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class User extends DBObject {

	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS User (" +
						"id INT PRIMARY KEY AUTO_INCREMENT, " +
						"name VARCHAR, " +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO User(name) VALUES (?)";
	public static final String SQL_UPDATE = "UPDATE User SET name=? WHERE id=?;";
	public static final String SQL_DELETE = "DELETE FROM User WHERE id = ?";
	public static final String SQL_SELECT = "SELECT ? FROM User WHERE id = ?"; 
	
	private String id;
	private String name;
	
	public User() {}
	
	public User(int i, String name)
	{
		init(new String[] {""+i, name});
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	
	@Override
	public int getConstructorSize() {
		return 2;
	}

	@Override
	public void init(String[] params) {
		this.id = params[0];
		this.name = params[1];
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString(){
		return name;
	}
}
