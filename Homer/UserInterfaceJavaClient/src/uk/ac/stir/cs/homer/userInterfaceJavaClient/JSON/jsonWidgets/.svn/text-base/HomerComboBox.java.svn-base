package uk.ac.stir.cs.homer.userInterfaceJavaClient.JSON.jsonWidgets;

import javax.swing.JComboBox;

public class HomerComboBox extends JComboBox<String> implements JsonWidget 
{
	
	public HomerComboBox(String[] params) {
		super(params);
	}

	public HomerComboBox() {
		super();
	}

	@Override
	public String getParameterValue() {
		return this.getSelectedItem().toString();
	}  
	
	public void setParameterValue(String paramValue)
	{
		for (int i = 0; i < this.getItemCount(); i++)
		{
			if (this.getItemAt(i).toString().equals(paramValue)) 
			{
				this.setSelectedIndex(i);
				return;
			}
		}
	}
}
