package uk.ac.stir.cs.homer.userInterfaceJavaClient.JSON.jsonWidgets;

import javax.swing.JSlider;

public class HomerSlider extends JSlider implements JsonWidget 
{
	
	@Override
	public String getParameterValue() {
		return String.valueOf(this.getValue());
	}  
	public void setParameterValue(String paramValue)
	{
		this.setValue(Integer.parseInt(paramValue));
	}
}
