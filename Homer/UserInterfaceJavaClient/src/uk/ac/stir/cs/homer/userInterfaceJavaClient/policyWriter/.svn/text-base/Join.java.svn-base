package uk.ac.stir.cs.homer.userInterfaceJavaClient.policyWriter;

public class Join
{
	private JoinType type;
	private JoinOperator operator;

	public Join(String joiningText)
	{
		if (joiningText.contains("("))
		{
			this.type = JoinType.OPEN;
		}
		else if (joiningText.contains(")"))
		{
			this.type = JoinType.CLOSE;
		}
		else
		{
			this.type = JoinType.JOIN;
		}
		
		if (joiningText.contains("and"))
		{
			this.operator = JoinOperator.AND;
		}
		else if (joiningText.contains("or"))
		{
			this.operator = JoinOperator.OR;
		}
		else if (joiningText.contains("then"))
		{
			this.operator = JoinOperator.THEN;
		}
	}
	
	public JoinOperator getOperator()
	{
		return operator;
	}
	
	public JoinType getType()
	{
		return type;
	}
}
