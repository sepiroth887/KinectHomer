package uk.ac.stir.cs.homer.userInterfaceJavaClient.policyWriter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Action;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Condition;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Location;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;

public class DoTerm extends JPanel
{
	
	private final DoTerms doTerms;
	private final HomerDatabase database;
	private final boolean firstTerm;

	private JComboBox devicesComboBox;
	private UserDevice[] userDevices;

	private JComboBox consActsComboBox;
	private Condition[] conditions;
	private Action[] actions;
	private JComboBox operatorComboBox;
	
	private JLabel ifLabel;
	
	public DoTerm(DoTerms doTerms, HomerDatabase database, boolean firstTerm)
	{
		this.doTerms = doTerms;
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
		
		ifLabel = new JLabel();
		this.add(ifLabel);
		
		this.add(getDeviceComboBox());
		if (userDevices.length>0)
		{
			this.add(getConsActsComboBox());
			
			updateConsAndActsComboBox();
			updateIfLabel();
			
			this.add(getNewDoTermButton());
		}
	}

	private JComboBox getOperatorComboBox()
	{
		operatorComboBox = new JComboBox(new String[] {"and"});
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
				updateConsAndActsComboBox();
			}
			
		});
		
		return devicesComboBox;
	}
	

	private JComboBox getConsActsComboBox()
	{
		consActsComboBox = new JComboBox();
		return consActsComboBox;
	}
	
	private void updateConsAndActsComboBox()
	{
		consActsComboBox.removeAllItems();
		
		UserDevice selectedDevice = getSelectedUserDevice();
		
		if (selectedDevice!=null)
		{
			conditions = database.getConditions(new QueryObject().userDevice(selectedDevice.getId()));
			Arrays.sort(conditions);
			
			actions = database.getActions(new QueryObject().userDevice(selectedDevice.getId()));
			Arrays.sort(actions);
			
			for (Condition condition : conditions)
			{
				consActsComboBox.addItem(condition.getDescription());
			}
	
			for (Action action: actions)
			{
				consActsComboBox.addItem(action.getDescription());
			}
			
			consActsComboBox.addActionListener(new ActionListener(){
	
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					updateIfLabel();
				}
				
			});
		}
	}
	
	private void updateIfLabel()
	{
		if (isConditionSelected())
		{
			ifLabel.setText("( if");
		}
		else
		{
			ifLabel.setText("");
		}
	}
	
	private JButton getNewDoTermButton()
	{
		JButton newDoTermButton = new JButton("+");
		newDoTermButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				addNewDoTerm();
			}
			
		});
		return newDoTermButton;
	}

	private void addNewDoTerm()
	{
		doTerms.addNewTerm(this);
	}

	private UserDevice getSelectedUserDevice()
	{
		return devicesComboBox.getSelectedIndex() >=0 ? userDevices[devicesComboBox.getSelectedIndex()] : null;
	}
	
	private String getSelectedEventID()
	{
		int selectedEventIndex = consActsComboBox.getSelectedIndex();
		
		if (isConditionSelected())
		{
			return conditions[selectedEventIndex].getId();
		}
		else
		{
			return actions[selectedEventIndex-conditions.length].getId();
		}
	}
	
	private boolean isConditionSelected()
	{
		return (consActsComboBox.getSelectedIndex() < conditions.length);
	}
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject term = new JSONObject();
		
		//term.put("term: ",devicesComboBox.getSelectedItem().toString() + " " + consActsComboBox.getSelectedItem().toString() );
		term.put("userdeviceid", getSelectedUserDevice().getId());
		term.put("eventid", getSelectedEventID());
		term.put("type", isConditionSelected()? "CONDITION" : "ACTION");
		return term;
	}
}
