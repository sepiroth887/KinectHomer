package uk.ac.stir.cs.homer.userInterfaceJavaClient.JSON.jsonWidgets;

import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class HomerRadioButtonGroup extends ButtonGroup implements JsonWidget{

	private Set<JRadioButton> radioButtons = new HashSet<JRadioButton>();
	
	@Override
	public void add(AbstractButton arg0) {
		super.add(arg0);
		radioButtons.add((JRadioButton) arg0);
	}
	
	@Override
	public String getParameterValue() {
		for (JRadioButton radioButton: radioButtons)
		{
			if (radioButton.isSelected()) return radioButton.getText();
		}
		return "";
	}
	
	public void setParameterValue(String paramValue)
	{
		for (JRadioButton b: radioButtons)
		{
			if (b.getText().equals(paramValue)) 
			{
				b.setSelected(true);
				return;
			}
		}
	}

}
