package uk.ac.stir.cs.homer.userInterfaceJavaClient.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Action;
import uk.ac.stir.cs.homer.serviceDatabase.objects.ActionEnvironEffects;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Environ;
import uk.ac.stir.cs.homer.serviceDatabase.objects.EnvironEffect;
import uk.ac.stir.cs.homer.serviceDatabase.objects.SystemDeviceType;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.queryBuilder.QueryObject;
import utils.VerticalFlowLayout;

public class ActionEnvironEffectsDialog extends JDialog implements ActionListener
{
	private final Frame frame;

	private final HomerDatabase database;

	private JButton cancelButton;
	private JButton saveButton;
	private final UserDevice userDevice;
	private final SystemDeviceType[] systemDeviceTypes;

	private boolean changesMade = false;
	
	private final Map<SystemDeviceType, ActionEnvironEffects> actionEnvironEffects = new HashMap<SystemDeviceType, ActionEnvironEffects>();
	
	public ActionEnvironEffectsDialog(HomerDatabase database, JPanel parentPanel, UserDevice userDevice, SystemDeviceType[] systemDeviceTypes)
	{
		super(JOptionPane.getFrameForComponent((Component)parentPanel), "Environment Effects", true);
		this.frame = JOptionPane.getFrameForComponent((Component)parentPanel);
		this.database = database;
		this.userDevice = userDevice;
		this.systemDeviceTypes = systemDeviceTypes;
		
		createPanel();
	}
	public ActionEnvironEffects[] showDialog()
	{
		setSize(600,600);
		setLocationRelativeTo(null);
		setVisible(true);
		//setLocationRelativeTo(frame);
		return changesMade ? actionEnvironEffects.values().toArray(new ActionEnvironEffects[] {}) : null;
	}
	
	private void createPanel()
	{
		//Create main panel
		JPanel masterPanel = new JPanel();
		String userDeviceTypeName = database.getUserDeviceTypes(new QueryObject().userDeviceType(userDevice.getTypeID()))[0].getName();
		String systemDeviceTypeName = systemDeviceTypes[0].getName();
		for (int devCount = 1; devCount < systemDeviceTypes.length; devCount++)
		{
			if (devCount-1 == systemDeviceTypes.length)
			{
				systemDeviceTypeName+= (" and " + systemDeviceTypes[devCount].getName());
			}
			else
			{				
				systemDeviceTypeName+= (", " + systemDeviceTypes[devCount].getName());
			}
			
		}
		masterPanel.add(new JLabel("This is the first time you've had a " + userDeviceTypeName));
		masterPanel.add(new JLabel("use " + systemDeviceTypeName + ". Please tell us"));
		masterPanel.add(new JLabel("how it affects your home environment."));
		
		for (SystemDeviceType systemDeviceType: systemDeviceTypes)
		{
			final ActionEnvironEffects actionEnvironEffectsForSysDev = new ActionEnvironEffects(userDevice.getTypeID(), systemDeviceType.getId());
			actionEnvironEffects.put(systemDeviceType, actionEnvironEffectsForSysDev);
			
			JPanel sysDevTypePanel = new JPanel(new VerticalFlowLayout());
			sysDevTypePanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(systemDeviceType.getName()),
					BorderFactory.createEmptyBorder(5,5,5,5)));
			
			for (final Action action : database.getActions(new QueryObject().systemDeviceType(systemDeviceType.getId())))
			{
				final JPanel actionPanel = new JPanel();
				actionPanel.add(new JLabel(action.getDescription() + ": "));
				final JLabel noneDefaultLabel = new JLabel("none");
				actionPanel.add(noneDefaultLabel);
				
				final JButton addNewButton = new JButton("+");
				actionPanel.add(addNewButton);
				addNewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0)
					{
						actionPanel.remove(noneDefaultLabel);
						actionPanel.remove(addNewButton);
						
						final JComboBox<String> effects = new JComboBox<String>();
						for (EnvironEffect effect : EnvironEffect.values())
						{
							effects.addItem(effect.name().toLowerCase());
						}
						actionPanel.add(effects);
						effects.setSelectedIndex(0);
						
						final JComboBox<String> environs = new JComboBox<String>();
						for (Environ environ : database.getAllEnirons())
						{
							environs.addItem(environ.getName());
						}
						actionPanel.add(environs);
						
						effects.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent arg0)
							{
								Environ environ = database.getEnvironWithName(environs.getSelectedItem().toString());
								actionEnvironEffectsForSysDev.updateRelationshipEffect(action.getId(), environ, effects.getSelectedItem().toString());
							}
						});
						environs.addActionListener(new ActionListener(){
							private Environ previousEnviron = database.getEnvironWithName(environs.getSelectedItem().toString());
							
							public void actionPerformed(ActionEvent arg0)
							{
								Environ newEnviron = database.getEnvironWithName(environs.getSelectedItem().toString());
								actionEnvironEffectsForSysDev.updateRelationshipEnviron(action.getId(), previousEnviron, newEnviron, effects.getSelectedItem().toString());
								previousEnviron = newEnviron;
							}
						});
						
						environs.setSelectedIndex(0);

						actionPanel.add(addNewButton);
						
						actionPanel.updateUI();
					}
				});
				sysDevTypePanel.add(actionPanel);
			}
			
			masterPanel.add(sysDevTypePanel);
		}
		
		//Create and initialise the buttons.
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        getRootPane().setDefaultButton(saveButton);
        
        //Lay out the buttons from left to right.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        //buttonPane.add(Box.createHorizontalGlue());
       // buttonPane.add(cancelButton);
        //buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(saveButton);
        
        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = getContentPane();
        contentPane.add(masterPanel, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource().equals(cancelButton))
		{
		}
		else if (event.getSource().equals(saveButton))
		{
			changesMade = true;
		}
		setVisible(false);
	}

}
