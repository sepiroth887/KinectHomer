package uk.ac.stir.cs.homer.userInterfaceJavaClient.deviceManagement;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.SystemGateway;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Location;
import uk.ac.stir.cs.homer.serviceDatabase.objects.LocationContext;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.Image.ImageIconUtils;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.dialogs.Dialogs;

public class ManageDevicesForm {

	private final HomerDatabase database;
	private JPanel systemDeviceTypePanel;
	private JPanel locationPanel;
	private JPanel userDeviceTypePanel;
	private JPanel masterPanel;
	private JFrame frame;

	public ManageDevicesForm(HomerDatabase database) {
		this.database = database;
		initialiseInterface();
	}

	private void initialiseInterface() {
		frame = new JFrame();
		masterPanel = new JPanel(new BorderLayout());
		
		JTabbedPane tabs = new JTabbedPane();
		
		updateSystemDeviceTypePanel();
		updateUserDeviceTypePanel();
		updateLocationPanel();
		
		JScrollPane systemDeviceTypeScrollPane = new JScrollPane(systemDeviceTypePanel);
		systemDeviceTypeScrollPane.setAutoscrolls(true);
		systemDeviceTypeScrollPane.setWheelScrollingEnabled(true);	
		
		JScrollPane userDeviceTypeScrollPane = new JScrollPane(userDeviceTypePanel);
		userDeviceTypeScrollPane.setAutoscrolls(true);
		userDeviceTypeScrollPane.setWheelScrollingEnabled(true);
		
		JScrollPane locationScrollPane = new JScrollPane(locationPanel);
		locationScrollPane.setAutoscrolls(true);
		locationScrollPane.setWheelScrollingEnabled(true);
		
		tabs.add("By Hardware", systemDeviceTypeScrollPane);
		tabs.add("By Device Type", userDeviceTypeScrollPane);
		tabs.add("By Location", locationScrollPane);
		
		masterPanel.add(BorderLayout.CENTER, tabs);
		
		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		JPanel doneButtonPanel = new JPanel();
		doneButtonPanel.add(doneButton);
		
		masterPanel.add(BorderLayout.SOUTH, doneButtonPanel);
		
		frame.add(masterPanel);
		frame.setSize(700,700);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Manage Devices");
		frame.setVisible(true);
	}

	private JPanel updateLocationPanel() {
		if (locationPanel==null) locationPanel= new JPanel();
		else locationPanel.removeAll();
		
		for (LocationContext locationContext: database.getAllLocationContexts())
		{
			final JPanel locationContextBorderedPanel = getNamedPanel(locationContext.getName());
			locationContextBorderedPanel.setLayout(new BorderLayout());
			
			final JPanel locationContextPanel = new JPanel();
			locationContextPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					BorderFactory.createEmptyBorder(5,5,5,5)));
			
			for (Location location: database.getLocations(new QueryObject().locationContext(locationContext.getId())))
			{
				JPanel internalLocationPanel = getNamedPanel(location.getName());
				internalLocationPanel.setLayout(new BorderLayout());
				
				JPanel internalLocationBorderedPanel = new JPanel();
				internalLocationBorderedPanel.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
						BorderFactory.createEmptyBorder(5,5,5,5)));
				internalLocationBorderedPanel.setLayout(new GridLayout(0,1));
				
				for (final UserDevice userDevice: database.getUserDevices(new QueryObject().location(location.getId())))
				{
					createDeviceButton(internalLocationBorderedPanel, location, null, null, userDevice);
				}
				createNewDeviceButton(internalLocationBorderedPanel, location, null, null);
				
				internalLocationPanel.add(BorderLayout.CENTER, internalLocationBorderedPanel);
				internalLocationPanel.add(BorderLayout.NORTH, createImageRenameDeleteButtonsPanel(getLocationImageChangingObserver(location), location.getImage(), getLocationRenameButtonListener(location), getLocationDeleteButtonListener(location)));
					
				locationContextPanel.add(internalLocationPanel);
				
				
			}
			final JPanel newLocationBorderedPanel = getNamedPanel("New?");
			JButton newLocationButton = new JButton("New Location");
			newLocationButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Location location = Dialogs.showNewLocationDialog(database, newLocationBorderedPanel);
					if (location != null)
					{
						database.addLocation(location.getName(), location.getLocationContextID(), location.getImage());
						updateLocationPanel();
					}
				}
			});
			newLocationBorderedPanel.add(newLocationButton);
			locationContextPanel.add(newLocationBorderedPanel);
			locationPanel.add(locationContextBorderedPanel);
			
			locationContextBorderedPanel.add(BorderLayout.NORTH, createImageRenameDeleteButtonsPanel(getLocationContextImageChangingObserver(locationContext), locationContext.getImage(), getLocationContextRenameButtonListener(locationContext), getLocationContextDeleteButtonListener(locationContext)));
			locationContextBorderedPanel.add(BorderLayout.CENTER,locationContextPanel);
		}
		
		final JPanel newLocationContextBorderedPanel = getNamedPanel("New?");
		JButton newLocationContextButton = new JButton("New Location Context");
		newLocationContextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LocationContext locationContext = Dialogs.showNewLocationContextDialog(newLocationContextBorderedPanel);
				if (locationContext != null)
				{
					database.addLocationContext(locationContext.getName(), locationContext.getImage());
					updateLocationPanel();
				}
			}
		});
		newLocationContextBorderedPanel.add(newLocationContextButton);
		locationPanel.add(newLocationContextBorderedPanel);
		locationPanel.updateUI();
		return locationPanel;
	}

	private JPanel updateSystemDeviceTypePanel() {
		if (systemDeviceTypePanel==null) systemDeviceTypePanel= new JPanel();
		else systemDeviceTypePanel.removeAll();
		
		for (final SystemDeviceType systemDeviceType: database.getAllSystemDeviceTypes())
		{
			final JPanel borderedPanel = getNamedPanel(systemDeviceType.getName());
			borderedPanel.setLayout(new BorderLayout());
			
			final JPanel internalSystemDeviceTypePanel = new JPanel();
			internalSystemDeviceTypePanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					BorderFactory.createEmptyBorder(5,5,5,5)));
			internalSystemDeviceTypePanel.setLayout(new GridLayout(0,1));
			
			for (final UserDevice userDevice: database.getUserDevices(new QueryObject().systemDeviceType(systemDeviceType.getId())))
			{
				createDeviceButton(internalSystemDeviceTypePanel, null, null, systemDeviceType, userDevice);
			}
			createNewDeviceButton(internalSystemDeviceTypePanel, null, null, systemDeviceType);
			
			borderedPanel.add(BorderLayout.NORTH, createImageRenameDeleteButtonsPanel(null, null, getSystemDeviceRenameButtonListener(systemDeviceType), null));
			borderedPanel.add(BorderLayout.CENTER, internalSystemDeviceTypePanel);
			systemDeviceTypePanel.add(borderedPanel);
		}
		systemDeviceTypePanel.updateUI();
		return systemDeviceTypePanel;
	}

	

	private JPanel updateUserDeviceTypePanel() {
		if (userDeviceTypePanel==null) userDeviceTypePanel= new JPanel();
		else userDeviceTypePanel.removeAll();
		
		for (final UserDeviceType userDeviceType: database.getAllUserDeviceTypes())
		{
			final JPanel borderedPanel = getNamedPanel(userDeviceType.getName());
			borderedPanel.setLayout(new BorderLayout());
			
			final JPanel internalUserDeviceTypePanel = new JPanel();
			internalUserDeviceTypePanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					BorderFactory.createEmptyBorder(5,5,5,5)));
			internalUserDeviceTypePanel.setLayout(new GridLayout(0,1));
			
			for (final UserDevice userDevice: database.getUserDevices(new QueryObject().userDeviceType(userDeviceType.getId())))
			{
				createDeviceButton(internalUserDeviceTypePanel, null, userDeviceType, null, userDevice);
				
			}
			createNewDeviceButton(internalUserDeviceTypePanel, null, userDeviceType, null);
			
			borderedPanel.add(BorderLayout.NORTH, createImageRenameDeleteButtonsPanel(getUserDeviceTypeImageChangingObserver(userDeviceType), userDeviceType.getImage(), getUserDeviceTypeRenameButtonListener(userDeviceType), getUserDeviceTypeDeleteButtonListener(userDeviceType)));
			borderedPanel.add(BorderLayout.CENTER, internalUserDeviceTypePanel);
			
			userDeviceTypePanel.add(borderedPanel);
		}
		final JPanel borderedPanel = getNamedPanel("New?");
		JButton newDeviceButton = new JButton("New Device");
		newDeviceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UserDeviceType userDeviceType = Dialogs.showNewUserDeviceDialog(borderedPanel);
				if (userDeviceType != null)
				{
					database.addUserDeviceType(userDeviceType.getName(), userDeviceType.getImage(), userDeviceType.getParent());
					redrawScreen();
				}
			}
		});
		borderedPanel.add(newDeviceButton);
		userDeviceTypePanel.add(borderedPanel);
		return userDeviceTypePanel;
	}

	private JPanel createImageRenameDeleteButtonsPanel(Observer imageChangingObserver, String imagePath, ActionListener renameButtonListener,
			ActionListener deleteButtonListener) {
		
		JPanel buttonsPanel = new JPanel();
		
		if (imageChangingObserver!=null)
		{
			ChooseImageButton imageButton = new ChooseImageButton(imagePath);
			imageButton.setIconWidth(40);
			imageButton.addObserver(imageChangingObserver);
			buttonsPanel.add(imageButton.getMasterPanel());
		}
		if (renameButtonListener != null)
		{
			JButton renameButton = new JButton("Rename");
			renameButton.addActionListener(renameButtonListener);
			buttonsPanel.add(renameButton);
		}
		if (deleteButtonListener != null)
		{
			JButton deleteButton = new JButton("Delete");
			deleteButton.addActionListener(deleteButtonListener);
			buttonsPanel.add(deleteButton);
		}
		
		return buttonsPanel;
	}

	private JPanel getNamedPanel(String name)
	{
		JPanel newPanel = new JPanel();
		newPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(name),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		return newPanel;
	}
	
	private void createDeviceButton(JPanel parentPanel, final Location location,
			final UserDeviceType userDeviceType, final SystemDeviceType systemDeviceType, final UserDevice userDevice) {
		
		JButton deviceButton = new JButton(userDevice.getName());
		if (userDevice!=null) deviceButton.setIcon(ImageIconUtils.scaleImageIcon(30, userDevice.getImage()));
		deviceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				deviceSelected(location, userDeviceType, systemDeviceType, userDevice);
			}
		});
		parentPanel.add(deviceButton);
	}

	private void createNewDeviceButton(JPanel parentPanel, final Location location, final UserDeviceType userDeviceType, final SystemDeviceType systemDeviceType) 
	{
		JButton deviceButton = new JButton("New Device");
		deviceButton.setIcon(ImageIconUtils.scaleImageIcon(30, "145"));
		deviceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				deviceSelected(location, userDeviceType, systemDeviceType, null);
			}
		});
		parentPanel.add(deviceButton);
	}
	protected void deviceSelected(Location location, UserDeviceType userDeviceType, SystemDeviceType systemDeviceType, UserDevice userDevice) 
	{
		NewDeviceForm newDeviceForm = new NewDeviceForm(database, location, userDeviceType, systemDeviceType, userDevice);
		newDeviceForm.addObserver(new Observer(){

			public void update(Observable arg0, Object arg1) 
			{
				boolean changesMade = (Boolean) arg1;
				if (changesMade) redrawScreen();
			}
			
		});
		

	}
	private void redrawScreen()
	{
		updateSystemDeviceTypePanel();
		updateUserDeviceTypePanel();
		updateLocationPanel();
	}
	
	private ActionListener getLocationRenameButtonListener(
			final Location location) {
		ActionListener locationRenameButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				String newName = (String)JOptionPane.showInputDialog(
				                    frame,
				                    "New Name:",
				                    "Rename "+ location.getName(),
				                    JOptionPane.OK_CANCEL_OPTION,
				                    null,
				                    null,
				                    location.getName());

				if ((newName != null) && (newName.length() > 0)) {
				    if (!newName.equals(location.getName()))
				    {
				    	location.setName(newName);
				    	database.updateLocationDetails(location);
				    	updateLocationPanel();
				    }
				}
			}
		};
		return locationRenameButtonListener;
	}
	private ActionListener getLocationDeleteButtonListener(
			final Location location) {
		ActionListener locationDeleteButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int n = JOptionPane.showConfirmDialog(
					    frame,
					    "Are you sure that you would like to delete " +
					    location.getName() + " and all the devices within it??",
					    "Delete "+location.getName() + "??",
					    JOptionPane.YES_NO_OPTION);
				
				if (n==0)
				{
					SystemGateway.Singleton.get().deleteLocation(location.getId());	
					updateLocationPanel();
				}
			}
		};
		return locationDeleteButtonListener;
	}
	private Observer getLocationImageChangingObserver(
			final Location location) {
		Observer locationImageChangingObserver = new Observer(){
				
				public void update(Observable arg0, Object arg1) 
				{
					location.setImage(arg1.toString());
					database.updateLocationDetails(location);
				}
				
			};
		return locationImageChangingObserver;
	}
	private ActionListener getLocationContextRenameButtonListener(
			final LocationContext locationContext) {
		ActionListener locationContextRenameButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				String newName = (String)JOptionPane.showInputDialog(
				                    frame,
				                    "New Name:",
				                    "Rename "+ locationContext.getName(),
				                    JOptionPane.OK_CANCEL_OPTION,
				                    null,
				                    null,
				                    locationContext.getName());

				if ((newName != null) && (newName.length() > 0)) {
				    if (!newName.equals(locationContext.getName()))
				    {
				    	locationContext.setName(newName);
				    	database.updateLocationContextDetails(locationContext);
				    	updateLocationPanel();
				    }
				}
			}
		};
		return locationContextRenameButtonListener;
	}
	private ActionListener getLocationContextDeleteButtonListener(
			final LocationContext locationContext) {
		ActionListener locationContextDeleteButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int n = JOptionPane.showConfirmDialog(
					    frame,
					    "Are you sure that you would like to delete " +
					    locationContext.getName() + " and all the locations and devices within it??",
					    "Delete "+locationContext.getName() + "??",
					    JOptionPane.YES_NO_OPTION);
				
				if (n==0)
				{
					SystemGateway.Singleton.get().deleteLocationContext(locationContext.getId());
					updateLocationPanel();
				}
			}
		};
		return locationContextDeleteButtonListener;
	}
	private Observer getLocationContextImageChangingObserver(
			final LocationContext locationContext) {
		Observer locationContextImageChangingObserver = new Observer(){
				
				public void update(Observable arg0, Object arg1) 
				{
					locationContext.setImage(arg1.toString());
					database.updateLocationContextDetails(locationContext);
				}
				
			};
		return locationContextImageChangingObserver;
	}

	private ActionListener getSystemDeviceRenameButtonListener(
			final SystemDeviceType systemDeviceType) {
		ActionListener systemDeviceTypeRenameButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				String newName = (String)JOptionPane.showInputDialog(
				                    frame,
				                    "New Name:",
				                    "Rename "+ systemDeviceType.getName(),
				                    JOptionPane.OK_CANCEL_OPTION,
				                    null,
				                    null,
				                    systemDeviceType.getName());

				if ((newName != null) && (newName.length() > 0)) {
				    if (!newName.equals(systemDeviceType.getName()))
				    {
				    	database.updateSysDeviceTypeDisplayName(systemDeviceType.getId(), newName);
				    	systemDeviceType.setName(newName);
				    	updateSystemDeviceTypePanel();
				    }
				}
			}
		};
		
		return systemDeviceTypeRenameButtonListener;
	}
	
	private ActionListener getUserDeviceTypeDeleteButtonListener(
			final UserDeviceType userDeviceType) {
		ActionListener userDeviceTypeDeleteListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int n = JOptionPane.showConfirmDialog(
					    frame,
					    "Are you sure that you would like to delete " +
					    userDeviceType.getName() + " and all the devices within it??",
					    "Delete "+userDeviceType.getName() + "??",
					    JOptionPane.YES_NO_OPTION);
				
				if (n==0)
				{
					SystemGateway.Singleton.get().deleteUserDeviceType(userDeviceType.getId());
					updateUserDeviceTypePanel();
				}
			}
		};
		return userDeviceTypeDeleteListener;
	}
	private ActionListener getUserDeviceTypeRenameButtonListener(
			final UserDeviceType userDeviceType) {
		ActionListener userDeviceTypeRenameButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				String newName = (String)JOptionPane.showInputDialog(
				                    frame,
				                    "New Name:",
				                    "Rename "+ userDeviceType.getName(),
				                    JOptionPane.OK_CANCEL_OPTION,
				                    null,
				                    null,
				                    userDeviceType.getName());

				if ((newName != null) && (newName.length() > 0)) {
				    if (!newName.equals(userDeviceType.getName()))
				    {
				    	userDeviceType.setName(newName);
				    	database.updateUserDeviceTypeDetails(userDeviceType);
				    	updateUserDeviceTypePanel();
				    }
				}
			}
		};
		return userDeviceTypeRenameButtonListener;
	}
	private Observer getUserDeviceTypeImageChangingObserver(
			final UserDeviceType userDeviceType) {
		Observer userDeviceTypeImageChangingObserver = new Observer(){
	
				public void update(Observable arg0, Object arg1) 
				{
					userDeviceType.setImage(arg1.toString());
					database.updateUserDeviceTypeDetails(userDeviceType);
				}
				
			};
		return userDeviceTypeImageChangingObserver;
	}
}
