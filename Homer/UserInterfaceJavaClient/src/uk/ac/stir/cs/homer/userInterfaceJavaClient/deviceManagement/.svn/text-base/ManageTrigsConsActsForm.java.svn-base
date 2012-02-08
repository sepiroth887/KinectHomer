package uk.ac.stir.cs.homer.userInterfaceJavaClient.deviceManagement;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Action;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Condition;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Trigger;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.Image.ImageIconUtils;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.dialogs.Dialogs;

public class ManageTrigsConsActsForm {

	private JFrame frame;
	private JPanel systemDeviceTypePanel;
	private final HomerDatabase database;

	public ManageTrigsConsActsForm(HomerDatabase database) {
		this.database = database;
		initiateGUI();
	}
	private void initiateGUI() {

		frame = new JFrame();
		frame.setSize(700,700);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Manage Triggers, Conditions and Actions"); 
		
		systemDeviceTypePanel = new JPanel();
		
		JScrollPane systemDeviceTypeScrollPane = new JScrollPane(systemDeviceTypePanel);
		systemDeviceTypeScrollPane.setAutoscrolls(true);
		systemDeviceTypeScrollPane.setWheelScrollingEnabled(true);	
		
		JPanel masterPanel = new JPanel(new BorderLayout());
		masterPanel.add(BorderLayout.CENTER, systemDeviceTypeScrollPane);
		
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
		
		refreshScreen();
		frame.setVisible(true);
		
	}
	private void refreshScreen()
	{
		refreshSystemDeviceTypePanel();
	}
	
	private void refreshSystemDeviceTypePanel() {
		for (final SystemDeviceType sysDeviceType: database.getAllSystemDeviceTypes())
		{
			final JPanel internalSysDeviceTypePanel = getNamedPanel(sysDeviceType.getName());
			
			Trigger[] triggers = database.getTriggers(new QueryObject().systemDeviceType(sysDeviceType.getId()));
			Condition[] conditions = database.getConditions(new QueryObject().systemDeviceType(sysDeviceType.getId()));
			Action[] actions = database.getActions(new QueryObject().systemDeviceType(sysDeviceType.getId()));
			
			addTrigsConsActButtons(internalSysDeviceTypePanel, triggers, conditions, actions);
			systemDeviceTypePanel.add(internalSysDeviceTypePanel);
		}
	}
	private void addTrigsConsActButtons(JPanel internalUserDeviceTypePanel, Trigger[] triggers, Condition[] conditions,
			Action[] actions) {
		
		if (triggers.length > 0)
		{
			final JPanel triggerPanel = getNamedPanel("Triggers");
			updateTriggers(triggerPanel, triggers);
			internalUserDeviceTypePanel.add(triggerPanel);
		}
		
		if (conditions.length > 0)
		{
			final JPanel conditionPanel = getNamedPanel("Conditions");
			updateConditions(conditionPanel, conditions);
			internalUserDeviceTypePanel.add(conditionPanel);
		}
		
		if (actions.length > 0)
		{
			final JPanel actionPanel = getNamedPanel("Actions");
			updateActions(actionPanel, actions);
			internalUserDeviceTypePanel.add(actionPanel);
		}
	}
	private void updateActions(final JPanel actionPanel, Action[] actions) {
		actionPanel.removeAll();
		actionPanel.setLayout(new GridLayout(0,1));
		for (final Action a: actions)
		{
			JButton actionButton = new JButton(a.getDescription());
			actionButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					Action action = Dialogs.showEditActionDialog(actionPanel, a);
					if (action!=null)
					{
						a.setDescription(action.getDescription());
						a.setImage(action.getImage());
						database.updateAction(a);	
						updateActions(actionPanel, database.getActions(new QueryObject().systemDeviceType(a.getSystemDeviceType())));
					}
				}
				
			});
			putImageOnButton(a.getImage(), actionButton);
			actionPanel.add(actionButton);
		}
		actionPanel.updateUI();
	}
	private void updateConditions(final JPanel conditionPanel,
			Condition[] conditions) {
		conditionPanel.removeAll();
		conditionPanel.setLayout(new GridLayout(0,1));
		for (final Condition c: conditions)
		{
			JButton conditionButton = new JButton(c.getDescription());
			conditionButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					Condition condition = Dialogs.showEditConditionDialog(conditionPanel, c);
					if (condition!=null)
					{
						c.setDescription(condition.getDescription());
						c.setImage(condition.getImage());
						database.updateCondition(c);	
						updateConditions(conditionPanel, database.getConditions(new QueryObject().systemDeviceType(c.getSystemDeviceType())));
					}
				}
				
			});
			putImageOnButton(c.getImage(), conditionButton);
			conditionPanel.add(conditionButton);
		}
		conditionPanel.updateUI();
	}
	private void updateTriggers(final JPanel triggerPanel, Trigger[] triggers) {
		triggerPanel.removeAll();
		triggerPanel.setLayout(new GridLayout(0,1));
		for (final Trigger t: triggers)
		{
			JButton triggerButton = new JButton(t.getDescription());
			triggerButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					Trigger trigger = Dialogs.showEditTriggerDialog(triggerPanel, t);
					if (trigger!=null)
					{
						t.setDescription(trigger.getDescription());
						t.setBeforeImage(trigger.getBeforeImage());
						t.setAfterImage(trigger.getAfterImage());
						database.updateTrigger(t);
						updateTriggers(triggerPanel, database.getTriggers(new QueryObject().systemDeviceType(t.getSystemDeviceType())));
					}
				}
				
			});
			putImageOnButton(t.getBeforeImage(), t.getAfterImage(), triggerButton);
			triggerPanel.add(triggerButton);
		}
		triggerPanel.updateUI();
	}
	private void putImageOnButton(String beforeImage, String afterImage,
			JButton triggerButton)
	{
		triggerButton.setIcon(ImageIconUtils.scaleImageIconForTrigger(20,beforeImage, afterImage));
	}
	private void putImageOnButton(String image, JButton triggerButton) {
		if (image!=null && !image.isEmpty())
		{
			triggerButton.setIcon(ImageIconUtils.scaleImageIcon(20, image));
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
}
