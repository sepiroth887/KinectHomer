package uk.ac.stir.cs.homer.userInterfaceJavaClient.policyWriter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Condition;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Location;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Trigger;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.JSON.JSONPanel;

public class WhenTerm extends JPanel
{

	private final WhenTerms whenTerms;
	private final HomerDatabase database;
	private final boolean firstTerm;
	
	private JComboBox devicesComboBox;
	private UserDevice[] userDevices;

	private JComboBox trigsConsComboBox;
	private Trigger[] triggers;
	private Condition[] conditions;
	private JComboBox operatorComboBox;
	private JSONPanel parameterData;
	
	public WhenTerm(WhenTerms whenTerms, HomerDatabase database, boolean firstTerm)
	{
		this.whenTerms = whenTerms;
		this.database = database;
		this.firstTerm = firstTerm;
		
		initialise();
	}

	private void initialise()
	{
		if (!firstTerm)
		{
			this.add(getOperatorComboBox());
		}
		
		this.add(getDeviceComboBox());
		
		if (userDevices.length > 0)
		{
			this.add(getTrigsConsComboBox());
			updateTrigsConsComboBox();
			
			this.add(getNewWhenTermButton());
		}
	}

	private JComboBox getOperatorComboBox()
	{
		operatorComboBox = new JComboBox(new String[] {"and", ") and", "and (", "or", ") or", "or (", "then", ") then", "then ("});
		return operatorComboBox;
	}
	
	public JoinOperator getJoinOperator()
	{
		if (firstTerm) return JoinOperator.AND;
		
		return new Join(operatorComboBox.getSelectedItem().toString()).getOperator();
	}
	public JoinType getJoinType()
	{
		if (firstTerm) return JoinType.JOIN;
		return new Join(operatorComboBox.getSelectedItem().toString()).getType();
	}
	
	private JComboBox getDeviceComboBox()
	{
		devicesComboBox = new JComboBox();
		
		userDevices = database.getAllUserDevices();
		Arrays.sort(userDevices);
		
		Map<String, String> knownLocations = new HashMap<String, String>();
		
		for (UserDevice userDevice : userDevices)
		{
			String locationName;
			String locationID = userDevice.getLocationID();
			
			if (knownLocations.containsKey(locationID))
			{
				locationName = knownLocations.get(locationID);
			}
			else
			{
				Location[] locations = database.getLocations(new QueryObject().location(locationID));
				locationName = locations[0].getName();
				knownLocations.put(locationID, locationName);
			}
			
			devicesComboBox.addItem(userDevice.getName() + " (" + locationName + ")");
		}
		devicesComboBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				updateTrigsConsComboBox();
			}
			
		});
		
		return devicesComboBox;
	}
	

	private JComboBox getTrigsConsComboBox()
	{
		trigsConsComboBox = new JComboBox();
		trigsConsComboBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent arg0)
			{
				if (arg0.getStateChange() == ItemEvent.SELECTED)
				{
					updateParamData();
				}
			}			
		});
		return trigsConsComboBox;
	}
	
	protected void updateParamData()
	{
		if (parameterData!=null) this.remove(parameterData);
		
		String id;
		String params;
		
		int selectedEventIndex = trigsConsComboBox.getSelectedIndex();
		
		if (isTriggerSelected())
		{
			id = triggers[selectedEventIndex].getId();
			params = triggers[selectedEventIndex].getJsonParamData();
		}
		else
		{
			id = conditions[selectedEventIndex-triggers.length].getId();
			params = conditions[selectedEventIndex-triggers.length].getJsonParamData();
		}
		parameterData = new JSONPanel(id, params);
		this.add(parameterData);
	}

	private void updateTrigsConsComboBox()
	{
		trigsConsComboBox.removeAllItems();
		
		UserDevice selectedDevice = getSelectedUserDevice();
		
		if (selectedDevice != null)
		{
			triggers = database.getTriggers(new QueryObject().userDevice(selectedDevice.getId()));
			Arrays.sort(triggers);
			
			conditions = database.getConditions(new QueryObject().userDevice(selectedDevice.getId()));
			Arrays.sort(conditions);
			
			for (Trigger trigger : triggers)
			{
				trigsConsComboBox.addItem(trigger.getDescription());
			}
			
			for (Condition condition : conditions)
			{
				trigsConsComboBox.addItem(condition.getDescription());
			}
		}
	}
	
	private JButton getNewWhenTermButton()
	{
		JButton newWhenTermButton = new JButton("+");
		newWhenTermButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				addNewWhenTerm();
			}
			
		});
		return newWhenTermButton;
	}

	private void addNewWhenTerm()
	{
		whenTerms.addNewTerm(this);
	}

	private UserDevice getSelectedUserDevice()
	{
		return devicesComboBox.getSelectedIndex() >= 0? userDevices[devicesComboBox.getSelectedIndex()] : null;
	}
	
	private String getSelectedEventID()
	{
		int selectedEventIndex = trigsConsComboBox.getSelectedIndex();
		
		if (isTriggerSelected())
		{
			return triggers[selectedEventIndex].getId();
		}
		else
		{
			return conditions[selectedEventIndex-triggers.length].getId();
		}
	}
	
	private boolean isTriggerSelected()
	{
		return (trigsConsComboBox.getSelectedIndex() < triggers.length);
	}
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject term = new JSONObject();
		
		//term.put("term: ",devicesComboBox.getSelectedItem().toString() + " " + trigsConsComboBox.getSelectedItem().toString() );
		term.put("userdeviceid", getSelectedUserDevice().getId());
		term.put("eventid", getSelectedEventID());
		term.put("type", isTriggerSelected()? "TRIGGER" : "CONDITION");
		if (parameterData!=null) term.put("parameters", parameterData.getDataInJSON());
		return term;
	}

	
}
