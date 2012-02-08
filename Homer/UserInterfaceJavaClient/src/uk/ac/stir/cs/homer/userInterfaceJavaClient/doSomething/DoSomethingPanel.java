package uk.ac.stir.cs.homer.userInterfaceJavaClient.doSomething;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.SystemGateway;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Action;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.Image.ImageIconUtils;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.dialogs.Dialogs;

public class DoSomethingPanel extends JScrollPane {

	private final HomerDatabase database;
	private final JPanel masterPanel;
	
	public DoSomethingPanel(HomerDatabase database) {
		
		this.setAutoscrolls(true);
		this.setWheelScrollingEnabled(true);	
		this.database = database;
		
		masterPanel = new JPanel();
		initiateGUI();
		
		this.setViewportView(masterPanel);
		this.validate();
	}

	private void initiateGUI() {

		masterPanel.removeAll();
		
		for (final UserDeviceType userDeviceType: database.getAllUserDeviceTypes())
		{
			final JPanel userDeviceTypePanel = getNamedPanel(userDeviceType.getName());
			boolean containedActions = false;
			
			for (final UserDevice userDevice: database.getUserDevices(new QueryObject().userDeviceType(userDeviceType.getId())))
			{
				final JPanel userDevicePanel = getNamedPanel(userDevice.getName());
				userDevicePanel.setLayout(new GridLayout(0,1));
				Action[] actionsForUserDeviceID = database.getActions(new QueryObject().userDevice(userDevice.getId()));
				for (final Action action: actionsForUserDeviceID)
				{
					JButton actionButton = new JButton(action.getDescription());
					actionButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							String[] parameters = null;
							if (action.getJsonParamData()!=null)
							{
								parameters = Dialogs.showNewJSONParametersDialog(userDevicePanel, action.getId(), action.getJsonParamData());
								if (parameters == null) return;
							}
							
							SystemGateway.Singleton.get().doAction(userDevice.getId(), action.getId(), parameters);
						}
						
					});
					if (action.getImage()!=null && !action.getImage().isEmpty())
					{
						actionButton.setIcon(ImageIconUtils.scaleImageIcon(40, action.getImage()));
					}
					userDevicePanel.add(actionButton);
				}
				if (actionsForUserDeviceID.length>0)
				{
					userDeviceTypePanel.add(userDevicePanel);
					containedActions = true;
				}
			}
			if (containedActions) masterPanel.add(userDeviceTypePanel);
		}
	}
	
	private JPanel getNamedPanel(String name)
	{
		JPanel newPanel = new JPanel();
		newPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(name),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		return newPanel;
	}

	public void refreshPanel() 
	{
		initiateGUI();
	}
		
}
