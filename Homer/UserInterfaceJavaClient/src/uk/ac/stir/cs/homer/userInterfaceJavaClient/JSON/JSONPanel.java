package uk.ac.stir.cs.homer.userInterfaceJavaClient.JSON;

import uk.ac.stir.cs.homer.homerFrameworkAPI.configData.ConfigData;
import uk.ac.stir.cs.homer.homerFrameworkAPI.configData.ConfigPart;
import uk.ac.stir.cs.homer.homerFrameworkAPI.parameterData.*;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDeviceType;
import uk.ac.stir.cs.homer.serviceJSON.JSONUtils;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.JSON.jsonWidgets.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSONPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Map<String, ArrayList<JsonWidget>> paramsForSysDeviceTypeID = new HashMap<String, ArrayList<JsonWidget>>(); 
	
	public JSONPanel(SystemDeviceType sdt)
	{
		super();
		addPanel(sdt);
	}
	public JSONPanel(String id, String jsonParameterTypeData) {
		super();
		addPanel(id, jsonParameterTypeData);
	}
	
	public JSONPanel() {
		super();
	}

	private ArrayList<JsonWidget> createPanel(String jsonString)
	{
		if (jsonString!=null && !jsonString.isEmpty())
		{
			ArrayList<JsonWidget> parameters = new ArrayList<JsonWidget>();
			ConfigData configData = new ConfigData(jsonString);
			for (ConfigPart part: configData.getParameters())
			{
				JPanel partPanel = new JPanel();
				partPanel.add(new JLabel(part.getPreText()));
				
				DataType dataType = part.getDataType();
				if (dataType.getDataType() == DataType.NUMBER)
				{
					HomerNumber numberDataType = (HomerNumber) dataType;
					parameters.add(createNumberPanel(partPanel, numberDataType));
				}
				else if (dataType.getDataType() == DataType.TEXT)
				{
					HomerText textDataType = (HomerText) dataType;
					parameters.add(createTextPanel(partPanel, textDataType));
				}
				else if (dataType.getDataType() == DataType.CHOICE)
				{
					HomerChoice choiceDataType = (HomerChoice) dataType;
					parameters.add(createChoicePanel(partPanel, choiceDataType));
				}
				else if (dataType.getDataType() == DataType.TOGGLE)
				{
					HomerToggle choiceDataType = (HomerToggle) dataType;
					parameters.add(createTogglePanel(partPanel, choiceDataType));
				}
				if (dataType.getUnit() != Unit.NONE)
				{
					partPanel.add(new JLabel(dataType.getUnit().name()));
				}
				
				partPanel.add(new JLabel(part.getPostText()));
				
				this.add(partPanel);
			}
			return parameters;
		}
		return new ArrayList<JsonWidget>(0);
	}
	
	private JsonWidget createTogglePanel(JPanel parentPanel, HomerToggle toggleDataType) {
		HomerCheckBox comboBox = new HomerCheckBox(toggleDataType.getText());
		comboBox.setSelected(toggleDataType.getDefaultState());
		parentPanel.add(comboBox);
		return comboBox;
	}
	
	private JsonWidget createChoicePanel(JPanel parentPanel, HomerChoice choiceDataType) {
		String[] params = choiceDataType.getPossibleValues();
		if (params.length == 0)
		{
			HomerTextField textField = new HomerTextField();
			textField.setColumns(10);
			textField.setText(choiceDataType.getDefaultValue());
			parentPanel.add(textField);
			return textField;
		}
		else if (params.length < 4)
		{
			HomerRadioButtonGroup buttonGroup = new HomerRadioButtonGroup();
			for (String p: params)
			{
				JRadioButton radioButton = new JRadioButton(p);
				radioButton.setSelected(choiceDataType.equals(p));
				buttonGroup.add(radioButton);
				parentPanel.add(radioButton);
			}
			return buttonGroup;
		}
		else {
			HomerComboBox comboBox = new HomerComboBox(params);
			comboBox.setSelectedItem(choiceDataType.getDefaultValue());
			parentPanel.add(comboBox);
			return comboBox;
		}
	}

	private JsonWidget createTextPanel(JPanel parentPanel, HomerText textDataType) {
		HomerTextField textField = new HomerTextField();
		textField.setColumns(10);
		textField.setText(textDataType.getDefaultValue());
		parentPanel.add(textField);
		return textField;
	}

	private JsonWidget createNumberPanel(JPanel parentPanel, HomerNumber dataType) {
		double defaultValue = dataType.getDefaultValue();
		double maximumValue = dataType.getMaximumValue();
		double minimumValue = dataType.getMinimumValue();
		int numDecimalPlaces = dataType.getNumDecimalPlaces();
		double step = dataType.getStep();
		
		if (minimumValue>=0 && maximumValue>minimumValue)
		{
			double range = maximumValue - minimumValue;
			if ((range / (step==0? 1 : step)) <=10)
			{
				HomerSlider slider = new HomerSlider();
				slider.setMinimum((int)Math.round(minimumValue));
				slider.setMaximum((int)Math.round(maximumValue) );
				
				if (step!=0) 
				{
					slider.setSnapToTicks(true);
					slider.setMinorTickSpacing((int)Math.round(step));
					slider.setMajorTickSpacing((int)Math.round(step));
				}
				else
				{
					slider.setMinorTickSpacing(10);
					slider.setMajorTickSpacing(5);
				}
	
				slider.setPaintTicks(true);
				
				slider.setValue((int)Math.round(defaultValue));
				slider.setPaintLabels(true);
				
				parentPanel.add(slider);
				return slider;
			}
			else if ((range / (step==0? 1 : step)) <=50)
			{
				HomerComboBox comboBox = new HomerComboBox();
				int indexToSelect = 0;
				int count = 0;
				for (double i = minimumValue; i <= maximumValue; i+=step)
				{
					if (defaultValue == i) indexToSelect = count;
					
					BigDecimal bd = new BigDecimal(i);
					bd = bd.setScale(numDecimalPlaces, BigDecimal.ROUND_HALF_UP);
					
					comboBox.addItem(bd.toString());
					count++;
					
				}
				comboBox.setSelectedIndex(indexToSelect);
				parentPanel.add(comboBox);
				return comboBox;
			}
		}
		
		BigDecimal bd = new BigDecimal(defaultValue);
		bd = bd.setScale(numDecimalPlaces, BigDecimal.ROUND_HALF_UP);
		
		HomerTextField textField = new HomerTextField();
		textField.setColumns(3);
		textField.setText(bd.toString());
		parentPanel.add(textField);
		return textField;
	}

	public Map<String, String[]> getData()
	{
		Map<String, String[]> allData = new HashMap<String, String[]>(paramsForSysDeviceTypeID.size());
		
		for (String sdtID : paramsForSysDeviceTypeID.keySet())
		{
			ArrayList<JsonWidget> parameters = paramsForSysDeviceTypeID.get(sdtID);
			String[] data = new String[parameters.size()];
			
			for (int i=0; i< data.length; i++)
			{
				data[i] = parameters.get(i).getParameterValue();
			}
			allData.put(sdtID, data);
		}
		return allData;
	}
	public String getDataInJSON()
	{
		if (paramsForSysDeviceTypeID.size() == 0)
		{
			return null;
		}
		
		ArrayList<JsonWidget> parameters = paramsForSysDeviceTypeID.get(0);
		String[] data = new String[parameters.size()];
		
		for (int i=0; i< data.length; i++)
		{
			data[i] = parameters.get(i).getParameterValue();
		}
		return JSONUtils.convertJavaStringArrayToJSONArray(data);
	}
	
	private void addPanel(String id, String jsonParameterTypeData) {
		ArrayList<JsonWidget> panel = createPanel(jsonParameterTypeData);
		if (panel.size() > 0)
		{
			paramsForSysDeviceTypeID.put(id, panel);
		}
	}
	public void addPanel(SystemDeviceType sdt)
	{
		addPanel(sdt.getId(), sdt.getJsonConfigData());
	}
	public void removeAll()
	{
		super.removeAll();	
	}
	public void populateFormForSDT(String sdtID, String[] newParameters) {
		if (newParameters.length > 0)
		{
			ArrayList<JsonWidget> parameters = paramsForSysDeviceTypeID.get(sdtID);
			for (int i = 0; i< parameters.size(); i++)
			{
				parameters.get(i).setParameterValue(newParameters[i]);
			}
		}
	}
	public void populateFormForSDT(String sdtID, String newParametersStillAsJSON) {
		this.populateFormForSDT(sdtID, JSONUtils.convertJSONArrayToJavaStringArray(newParametersStillAsJSON));
	}
}
