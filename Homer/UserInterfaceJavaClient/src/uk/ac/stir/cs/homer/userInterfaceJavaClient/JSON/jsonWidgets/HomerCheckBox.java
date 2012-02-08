package uk.ac.stir.cs.homer.userInterfaceJavaClient.JSON.jsonWidgets;

import javax.swing.JCheckBox;

public class HomerCheckBox extends JCheckBox implements JsonWidget 
{

	public HomerCheckBox()
	{
		super();
	}
	public HomerCheckBox(String name)
	{
		super(name);
	}
	
	@Override
	public String getParameterValue()
	{
		return new Boolean(this.isSelected()).toString();
	}

	@Override
	public void setParameterValue(String paramValue)
	{
		this.setSelected(new Boolean(paramValue));
	}

}
