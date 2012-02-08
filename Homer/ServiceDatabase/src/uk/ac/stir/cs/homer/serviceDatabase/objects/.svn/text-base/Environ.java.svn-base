package uk.ac.stir.cs.homer.serviceDatabase.objects;

public class Environ extends DBObject
{
	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS Environ (" +
						"id INT PRIMARY KEY AUTO_INCREMENT, " +
						"name VARCHAR, " +
						"type VARCHAR, " +
						"report BOOLEAN " +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO Environ(name, type, report) VALUES (?,?,?)";
	public static final String SQL_UPDATE = "UPDATE Environ SET name=?, type=?, report=? WHERE id=?;";
	public static final String SQL_DELETE = "DELETE FROM Environ WHERE id = ?";
	
	
	private String id;
	private String name;
	private EnvironType type;
	private boolean reportConflict;

	public Environ() {}
	
	public Environ(String id, String name, EnvironType type, boolean reportConflict)
	{
		this.id = id;
		this.name = name;
		this.type = type;
		this.reportConflict = reportConflict;
	}

	public String getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public EnvironType getType()
	{
		return type;
	}
	public boolean isReportConflict()
	{
		return reportConflict;
	}
	
	@Override
	public int getConstructorSize()
	{
		return 4;
	}

	@Override
	public void init(String[] params)
	{
		this.id = params[0];
		this.name = params[1];
		this.type = EnvironType.valueOf(params[2]);
		this.reportConflict = Boolean.getBoolean(params[3]);
	}
	
	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 != null && arg0 instanceof Environ)
		{
			Environ other = (Environ) arg0;
			return other.getId().equals(this.getId());
		}
		return super.equals(arg0);
	}
	
	@Override
	public int hashCode()
	{
		return getId().hashCode();
	}
}
