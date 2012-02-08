package uk.ac.stir.cs.homer.serviceDatabase.objects;

import java.util.Calendar;

public class Policy extends DBObject
{
	public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS Policy (" +
						"id INT PRIMARY KEY AUTO_INCREMENT, " +
						"name VARCHAR, " +
						"author VARCHAR, " +
						"dateCreated LONG NOT NULL, " +
						"dateLastEdited LONG NOT NULL," +
						"enabled BOOLEAN NOT NULL, " +
						"whenClause VARCHAR, " +
						"doClause VARCHAR" +
					")";
		
	public static final String SQL_INSERT = "INSERT INTO Policy (name, author, dateCreated, dateLastEdited, enabled, whenClause, doClause) VALUES (?,?,?,?,?,?,?)";
	public static final String SQL_UPDATE = "UPDATE Policy SET name=?, author=?, dateCreated=?, dateLastEdited=?, enabled=?, whenClause=?, doClause=? WHERE id=?;";
	public static final String SQL_DELETE = "DELETE FROM Policy WHERE id = ?";
	
	private String policyID;
	private String policyName;
	private String author;
	private boolean enabled;
	private String dateCreatedInMillisecs;
	private String dateLastEditedInMillisecs;
	
	private String doClauseAsJSON;
	private String whenClauseAsJSON;

	public Policy(){};
	
	public Policy(String name, String author, String dateCreated, String dateLastEdited, boolean enabled, String whenClause,
			String doClause)
	{
		this.policyName = name;
		this.author = author;
		this.dateCreatedInMillisecs = dateCreated;
		this.dateLastEditedInMillisecs = dateLastEdited;
		this.enabled = enabled;
		this.whenClauseAsJSON = whenClause;
		this.doClauseAsJSON = doClause;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public String getID() {
		return policyID;
	}
	public String getName() {
		return policyName;
	}
	public String getDoClauseAsJSON() {
		return doClauseAsJSON;
	}
	public String getWhenClauseAsJSON() {
		return whenClauseAsJSON;
	}
	public String getAuthor()
	{
		return author;
	}
	public String getDateCreatedInMillisecs()
	{
		return dateCreatedInMillisecs;
	}
	public String getDateLastEditedInMillisecs()
	{
		return dateLastEditedInMillisecs;
	}
	
	public void setAuthor(String author)
	{
		this.author = author;
		refreshLastDateEdited();
	}
	public void setName(String policyName)
	{
		this.policyName = policyName;
		refreshLastDateEdited();
	}
	public void refreshLastDateEdited()
	{
		this.dateLastEditedInMillisecs = Long.toString(Calendar.getInstance().getTimeInMillis());
	}
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		refreshLastDateEdited();
	}
	
	
	
	@Override
	public int getConstructorSize() {
		return 8;
	}

	@Override
	public void init(String[] params) {
		this.policyID = params[0];
		this.policyName = params[1];
		this.author = params[2];
		this.dateCreatedInMillisecs = params[3];
		this.dateLastEditedInMillisecs = params[4];
		this.enabled = Boolean.parseBoolean(params[5]);
		this.whenClauseAsJSON = params[7];
		this.doClauseAsJSON = params[8];
	}	
}
