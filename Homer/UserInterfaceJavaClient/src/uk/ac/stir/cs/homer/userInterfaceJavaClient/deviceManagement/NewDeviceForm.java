package uk.ac.stir.cs.homer.userInterfaceJavaClient.deviceManagement;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.SystemGateway;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.ActionEnvironEffects;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Condition;
import uk.ac.stir.cs.homer.serviceDatabase.objects.IDsForUserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Location;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.JSON.JSONPanel;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.dialogs.ActionEnvironEffectsDialog;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.dialogs.Dialogs;

public class NewDeviceForm extends Observable {

	private final HomerDatabase database;

	private final SystemDeviceType[] allSystemDeviceTypes;
	
	private Location location;
	private UserDeviceType userDeviceType;
	private Set<SystemDeviceType> systemDeviceTypes;
	private List<Condition> states;
	private UserDevice userDevice;

	private JSONPanel jsonPanel;

	private JList<String> sysDeviceTypeComboBox;
	private DefaultListModel<String> sysDeviceTypeComboBoxData;
	
	private JComboBox<String> userDeviceTypeComboBox;
	private JComboBox<String> locationsComboBox;
	private JComboBox<String> defaultStateComboBox;
	
	private JFrame frame;

	private boolean changesMade;
	
	private JPanel masterPanel;

	private ChooseImageButton imagePanel;
	
	public NewDeviceForm(HomerDatabase database, Location location, UserDeviceType userDeviceType, SystemDeviceType systemDeviceType, UserDevice userDevice) {
		this.database = database;
		this.location = location;
		this.userDeviceType = userDeviceType;
		this.systemDeviceTypes = new HashSet<SystemDeviceType>();
		this.systemDeviceTypes.add(systemDeviceType);
		this.userDevice = userDevice;
		allSystemDeviceTypes = database.getAllSystemDeviceTypes();
		createDeviceDetailsFrame();
	}
	
	private void createDeviceDetailsFrame() {
		IDsForUserDevice ids = (userDevice==null? null : database.getDetailsAboutUserDeviceID(userDevice.getId()));
		
		frame = new JFrame();
		masterPanel = new JPanel(new BorderLayout());
		
		imagePanel = new ChooseImageButton(userDevice, userDeviceType, location);
		
		JPanel tempNamePanel = new JPanel();
		tempNamePanel.add(new JLabel("Name:"));
		final JTextField nameValue = new JTextField(20);
		tempNamePanel.add(nameValue);
		if (userDevice!=null) nameValue.setText(userDevice.getName());
		
		JPanel defaultStatePanel = new JPanel();
		defaultStatePanel.add(new JLabel("Default State: "));
		defaultStateComboBox = new JComboBox();
		defaultStatePanel.add(defaultStateComboBox);
		states = new ArrayList<Condition>();
		
		jsonPanel = new JSONPanel();
		final JPanel sysDeviceTypeChoicePanel = new JPanel();
		sysDeviceTypeChoicePanel.add(new JLabel("Hardware:"));
		
		if (ids != null)
		{
			systemDeviceTypes.clear();
			for (SystemDeviceType sdt : database.getSystemDeviceTypes(new QueryObject().userDevice(ids.getUserDeviceID())))
			{
				systemDeviceTypes.add(sdt);
			}
		}
		updateSystemDeviceType(systemDeviceTypes);
		sysDeviceTypeChoicePanel.add(sysDeviceTypeComboBox);
		updateDefaultStateComboBox(userDevice==null? null : userDevice.getDefaultState());
		
		final JPanel userDeviceTypeChoicePanel = new JPanel();
		userDeviceTypeChoicePanel.add(new JLabel("Type:"));
		String id = (userDeviceType==null? (ids==null? null : ids.getUserDeviceTypeID()) : userDeviceType.getId());
		updateUserDeviceType(id);
		userDeviceTypeChoicePanel.add(userDeviceTypeComboBox);
		
		JButton addNewDeviceTypeButton = new JButton("New Device Type");
		addNewDeviceTypeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createNewUserDeviceTypePopUp();
			}
		});
		userDeviceTypeChoicePanel.add(addNewDeviceTypeButton);
		
		if (id != null && userDeviceType==null)
		{
			userDeviceType = database.getUserDeviceTypes(new QueryObject().userDeviceType(ids.getUserDeviceTypeID()))[0];
		}
		
		JPanel locationChoicePanel = new JPanel();
		locationChoicePanel.add(new JLabel("Location:"));
		id = (location==null? (ids==null? null : ids.getLocationID()) : location.getId());
		updateLocation(id);
		locationChoicePanel.add(locationsComboBox);
		
		JButton addNewLocationButton = new JButton("New Location");
		addNewLocationButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				createNewLocationPopUp();
			}
		});
		locationChoicePanel.add(addNewLocationButton);
		
		if (id!=null && location == null)
		{
			location = database.getLocations(new QueryObject().location(ids.getLocationID()))[0];
		}
		
		Box informationDetails = new Box(BoxLayout.Y_AXIS);
		informationDetails.add(imagePanel.getMasterPanel());
		informationDetails.add(tempNamePanel);
		informationDetails.add(sysDeviceTypeChoicePanel);
		informationDetails.add(userDeviceTypeChoicePanel);
		informationDetails.add(defaultStatePanel);
		informationDetails.add(locationChoicePanel);
		informationDetails.add(jsonPanel);
		
		JPanel buttonPanel = new JPanel();
	
		if (userDevice!=null)
		{
			JButton deleteButton = new JButton("Delete");
			deleteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int n = JOptionPane.showConfirmDialog(
						    frame,
						    "Are you sure that you would like to delete " +
						    userDevice.getName() + "?",
						    "Delete "+userDevice.getName() + "??",
						    JOptionPane.YES_NO_OPTION);
					
					if (n==0)
					{
						SystemGateway.Singleton.get().deleteUserDevice(userDevice.getId());
						changesMade=true;
						closeForm();
					}
				}
			});	
			buttonPanel.add(deleteButton);
		}
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				location = (locationsComboBox.getSelectedIndex()>=0? database.getAllLocations()[locationsComboBox.getSelectedIndex()] : null);
				userDeviceType = (userDeviceTypeComboBox.getSelectedIndex()>=0? database.getAllUserDeviceTypes()[userDeviceTypeComboBox.getSelectedIndex()] : null);
				systemDeviceTypes = (systemDeviceTypes.size()==0? null : systemDeviceTypes);
				
				boolean needName = nameValue.getText().isEmpty();
				boolean needSysDeviceType = systemDeviceTypes == null;
				boolean needUserDeviceType = userDeviceType==null;
				boolean needLocation = location == null;
				
				if (needName || needSysDeviceType || needUserDeviceType || needLocation || imagePanel.isImageNeedsSet())
				{
					StringBuffer message = new StringBuffer("Please ");
					if (imagePanel.isImageNeedsSet()) 
					{
						message.append("choose an image");
						if (needName || needSysDeviceType || needUserDeviceType || needLocation) message.append(" and ");
					}
					if (needName || needSysDeviceType || needUserDeviceType || needLocation) message.append("enter a ");
					if (needName) message.append("name");
					if (needName && (needSysDeviceType || needUserDeviceType || needLocation))
					{
						if ((needUserDeviceType && needLocation)
								|| (needUserDeviceType && needSysDeviceType)
								|| (needSysDeviceType && needLocation))
							message.append(", ");
						else message.append(" and ");
					}
					if (needSysDeviceType) message.append("hardware");
					if (needSysDeviceType && (needUserDeviceType || needLocation))
					{
						if (needUserDeviceType && needLocation) message.append(", ");
						else message.append(" and ");
					}
					if (needUserDeviceType) message.append("device type");
					if (needUserDeviceType && needLocation) message.append(" and ");
					
					if (needLocation) message.append("location");
					
					message.append(".");
					
					JOptionPane.showMessageDialog(frame, message);
				}
				else
				{
					int selectedIndex = defaultStateComboBox.getSelectedIndex();
					String defaultStateID = selectedIndex==0? null : ((Condition)states.get(selectedIndex-1)).getId();
					if (userDevice==null) 
						userDevice = SystemGateway.Singleton.get().registerNewDevice(systemDeviceTypes.toArray(new SystemDeviceType[0]), jsonPanel.getData(), userDeviceType.getId(), nameValue.getText(), location.getId(), imagePanel.getImageName(), defaultStateID);
					else
					{
						userDevice.setName(nameValue.getText());
						userDevice.setLocationID(location.getId());
						userDevice.setUserDeviceTypeID(userDeviceType.getId());
						userDevice.setImage(imagePanel.getImageName());
						userDevice.setDefaultState(defaultStateID);
						
						SystemGateway.Singleton.get().updateDevice(userDevice, systemDeviceTypes.toArray(new SystemDeviceType[0]), jsonPanel.getData());
					}
					SystemDeviceType[] sysDevicesRequiringEnvEffInfo = SystemGateway.Singleton.get().sysDevTypesRequiringActEnvEffInfo(userDevice.getTypeID(), systemDeviceTypes.toArray(new SystemDeviceType[0]));
					
					if (sysDevicesRequiringEnvEffInfo.length > 0 )
					{
						// pop up with a choose how each affects the environment
						ActionEnvironEffectsDialog envEffsDialog = new ActionEnvironEffectsDialog(database, masterPanel, userDevice, sysDevicesRequiringEnvEffInfo);
						ActionEnvironEffects[] newEnvirons = envEffsDialog.showDialog();
						if (newEnvirons != null)
						{
							SystemGateway.Singleton.get().addActionEnvironEffects(newEnvirons);
						}
					}
					
					changesMade = true;
					closeForm();
				}
			}
		});
		buttonPanel.add(save);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				closeForm();
			}
			
		});	
		buttonPanel.add(cancel);
		
		masterPanel.add(BorderLayout.CENTER, informationDetails);
		masterPanel.add(BorderLayout.SOUTH, buttonPanel);
		
		frame.add(masterPanel);
		frame.setSize(400,600);
		frame.setTitle("Device Management");
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void updateDefaultStateComboBox(String defaultStateID) {
		defaultStateComboBox.removeAllItems();
		defaultStateComboBox.addItem("None");
		states.clear();
		
		for (int i : sysDeviceTypeComboBox.getSelectedIndices())
		 {
			 SystemDeviceType sdt = allSystemDeviceTypes[i];
			 for (Condition c : database.getConditions(new QueryObject().systemDeviceType(sdt.getId())))
			 {
				 states.add(c);
				 defaultStateComboBox.addItem(c.getDescription());
			 }
		 }
		if (defaultStateID != null)
		{
			Condition toSelect = new Condition(defaultStateID, null, null, null, null, null);
			defaultStateComboBox.setSelectedIndex(states.indexOf(toSelect)+1);
		}
		else
		{
			if (defaultStateComboBox.getSelectedIndex() == -1)
			{
				defaultStateComboBox.setSelectedIndex(1);
			}
		}
	}

	private void updateSystemDeviceType(Set<SystemDeviceType> systemDeviceTypesToBeSelected) {
		
		if (sysDeviceTypeComboBox==null) 
		{
			sysDeviceTypeComboBoxData = new DefaultListModel();
		
			sysDeviceTypeComboBox = new JList(sysDeviceTypeComboBoxData);
			sysDeviceTypeComboBox.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			sysDeviceTypeComboBox.setLayoutOrientation(JList.VERTICAL_WRAP);
			sysDeviceTypeComboBox.addListSelectionListener(new ListSelectionListener(){

				@Override
				public void valueChanged(ListSelectionEvent e) {
					 if (e.getValueIsAdjusting() == false) 
					 {
						 jsonPanel.removeAll();
						 int[] selectedIndices = sysDeviceTypeComboBox.getSelectedIndices();
						 systemDeviceTypes.clear();
						 for (int i : selectedIndices)
						 {
							 SystemDeviceType sdt = allSystemDeviceTypes[i];
							 systemDeviceTypes.add(sdt);
							 jsonPanel.addPanel(sdt);
							 if (userDevice!=null ) {
								SystemDevice[] systemDevices = database.getSystemDevices(new QueryObject().userDevice(userDevice.getId()).systemDeviceType(sdt.getId()));
								if (systemDevices.length > 0)
									jsonPanel.populateFormForSDT(sdt.getId(), systemDevices[0].getJsonConfigData());
							 }
						 }
						 updateDefaultStateComboBox(null);
						 jsonPanel.updateUI();
					 }
				}
				
			});
		}
		
		if (allSystemDeviceTypes.length==0) 
			sysDeviceTypeComboBox.setVisible(false);
		else
		{
			sysDeviceTypeComboBoxData.removeAllElements();
			
			int[] selectedIndices = new int[systemDeviceTypesToBeSelected.size()];
	
			for (int i = 0; i< allSystemDeviceTypes.length; i++)
			{
				sysDeviceTypeComboBoxData.addElement(allSystemDeviceTypes[i].getName());
				
				if (systemDeviceTypesToBeSelected != null && systemDeviceTypesToBeSelected.contains(allSystemDeviceTypes[i]))
				{
					selectedIndices[systemDeviceTypesToBeSelected.size() - 1] = i;
					systemDeviceTypesToBeSelected.remove(allSystemDeviceTypes[i]);
				}
			}
			
			sysDeviceTypeComboBox.setVisible(true);
			sysDeviceTypeComboBox.setSelectedIndices(selectedIndices);
		}
		
		if (sysDeviceTypeComboBox.getListSelectionListeners().length == 0)
		{
			sysDeviceTypeComboBox.addListSelectionListener(new ListSelectionListener(){

				@Override
				public void valueChanged(ListSelectionEvent e) {
					 if (e.getValueIsAdjusting() == false) 
					 {
						 jsonPanel.removeAll();
						 int[] selectedIndices = sysDeviceTypeComboBox.getSelectedIndices();
						 systemDeviceTypes.clear();
						 for (int i : selectedIndices)
						 {
							 SystemDeviceType sdt = allSystemDeviceTypes[i];
							 systemDeviceTypes.add(sdt);
							 jsonPanel.addPanel(sdt);
							 if (userDevice!=null ) {
								SystemDevice[] systemDevices = database.getSystemDevices(new QueryObject().userDevice(userDevice.getId()).systemDeviceType(sdt.getId()));
								if (systemDevices.length > 0)
									jsonPanel.populateFormForSDT(sdt.getId(), systemDevices[0].getJsonConfigData());
							 }
						 }
						 jsonPanel.updateUI();
					 }
				}
				
			});
		}
	}
	private void updateUserDeviceType(String idToBeSelected) {
		
		final UserDeviceType[] userDeviceTypes = database.getAllUserDeviceTypes();
		if (userDeviceTypeComboBox==null) userDeviceTypeComboBox = new JComboBox();
		
		if (userDeviceTypes == null || userDeviceTypes.length==0) 
			userDeviceTypeComboBox.setVisible(false);
		else
		{
			userDeviceTypeComboBox.removeAllItems();
			int selectedIndex = 0;
			
			for (int i = 0; i< userDeviceTypes.length; i++)
			{
				userDeviceTypeComboBox.addItem(userDeviceTypes[i].getName());
				
				if (idToBeSelected != null)
				{
					if (userDeviceTypes[i].getId().equals(idToBeSelected)) 
						selectedIndex = i;
				}
			}
			userDeviceTypeComboBox.setSelectedIndex(selectedIndex);
			userDeviceTypeComboBox.setVisible(true);
		}
	}

	private void updateLocation(String idToBeSelected) {
		
		final Location[] locations = database.getAllLocations();
		if (locationsComboBox == null) locationsComboBox = new JComboBox();
		
		if (locations == null || locations.length==0) 
			locationsComboBox.setVisible(false);
		else
		{
			locationsComboBox.removeAllItems();
			
			int selectedIndex = 0;
			
			for (int i = 0; i< locations.length; i++)
			{
				locationsComboBox.addItem("(" + database.getLocationContexts(new QueryObject().locationContext(locations[i].getLocationContextID()))[0].getName() + ") " + locations[i].getName());
				
				if (idToBeSelected != null)
				{
					if (locations[i].getId().equals(idToBeSelected)) 
						selectedIndex = i;
				}
			}
			
			locationsComboBox.setSelectedIndex(selectedIndex);
			locationsComboBox.setVisible(true);
		}
	}

	private void createNewUserDeviceTypePopUp() {
		UserDeviceType newUserDeviceType = Dialogs.showNewUserDeviceDialog(masterPanel);
		if (newUserDeviceType != null)
		{
			String id = database.addUserDeviceType(newUserDeviceType.getName(), newUserDeviceType.getImage(), newUserDeviceType.getParent());
			updateUserDeviceType(id);
			changesMade = true;
		}
	}
	
	protected void createNewLocationPopUp() {
		Location newLocation = Dialogs.showNewLocationDialog(database, masterPanel);
		if (newLocation != null)
		{
			if (newLocation.getName() != null)
			{
				String id = database.addLocation(newLocation.getName(), newLocation.getLocationContextID(), newLocation.getImage());
				updateLocation(id);
			}
			changesMade = true;
		}
	}
	
	private void closeForm()
	{
		this.setChanged();
		this.notifyObservers(changesMade);
		frame.dispose();
		
	}
}
