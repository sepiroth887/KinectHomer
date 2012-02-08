package uk.ac.stir.cs.homer.userInterfaceJavaClient.dialogs;

import javax.swing.*;

import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Action;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Condition;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Location;
import uk.ac.stir.cs.homer.serviceDatabase.objects.LocationContext;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Trigger;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDeviceType;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.JSON.JSONPanel;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.deviceManagement.ChooseImageButton;

import java.awt.*;
import java.awt.event.*;


public class Dialogs extends JDialog
                        implements ActionListener {
   
     //for location dialog
	private JComboBox locationContextComboBox;
	private HomerDatabase database;

	//for json parameters dialog
	private JSONPanel jsonPanel;
	
	//for trigger dialog
	private ChooseImageButton secondImagePanel;
	
	
    private boolean changesMade = false; 
    private JButton saveButton;
    private JButton cancelButton;
	private JTextField nameValue;
	private ChooseImageButton imagePanel;
	private JPanel masterPanel;
	private LocationContext[] locationContexts;
	

	public static Trigger showEditTriggerDialog(JPanel parentPanel, Trigger trigger) {
		Frame frame = JOptionPane.getFrameForComponent((Component)parentPanel);
		Dialogs dialog = new Dialogs(frame, "Edit Trigger", "", trigger.getBeforeImage(), null, null, trigger, null, null);
		dialog.setVisible(true);
		return (dialog.isChangesMade()? new Trigger(null, dialog.nameValue.getText(), null, null, dialog.imagePanel.getImageName(), dialog.secondImagePanel.getImageName()) : null);
	}
	public static Condition showEditConditionDialog(JPanel parentPanel, Condition condition) {
		Frame frame = JOptionPane.getFrameForComponent((Component)parentPanel);
		Dialogs dialog = new Dialogs(frame, "Edit Condition", "", condition.getImage(), null, null, null, condition, null);
		dialog.setVisible(true);
		return (dialog.isChangesMade()? new Condition(null, dialog.nameValue.getText(), null, null, dialog.imagePanel.getImageName(), null) : null);
	}
	public static Action showEditActionDialog(JPanel parentPanel, Action action) {
		Frame frame = JOptionPane.getFrameForComponent((Component)parentPanel);
		Dialogs dialog = new Dialogs(frame, "Edit Action", "", action.getImage(), null, null, null, null, action);
		dialog.setVisible(true);
		return (dialog.isChangesMade()? new Action(null, dialog.nameValue.getText(), null, null, dialog.imagePanel.getImageName()) : null);
	}
    public static UserDeviceType showNewUserDeviceDialog(JPanel parentPanel) {
		Frame frame = JOptionPane.getFrameForComponent((Component)parentPanel);
		Dialogs dialog = new Dialogs(frame, "New Device", "", null, null, null, null, null, null);
		dialog.setVisible(true);
		return (dialog.isChangesMade()? new UserDeviceType(null, dialog.nameValue.getText(), dialog.imagePanel.getImageName(), null) : null);
    }
    public static Location showNewLocationDialog(HomerDatabase database, JPanel parentPanel) {
		Frame frame = JOptionPane.getFrameForComponent((Component)parentPanel);
		Dialogs dialog = new Dialogs(frame, "New Location", "", null, database, null, null, null, null);
		dialog.setVisible(true);
		return (dialog.isChangesMade()? new Location(null, dialog.nameValue.getText(), dialog.locationContexts[dialog.locationContextComboBox.getSelectedIndex()].getId(), dialog.imagePanel.getImageName()) : null);
    }
    public static LocationContext showNewLocationContextDialog(JPanel parentPanel) {
		Frame frame = JOptionPane.getFrameForComponent((Component)parentPanel);
		Dialogs dialog = new Dialogs(frame, "New Location Context", "", null, null, null, null, null, null);
		dialog.setVisible(true);
		return (dialog.isChangesMade()? new LocationContext(null, dialog.nameValue.getText(), dialog.imagePanel.getImageName()) : null);
    }
    public static String[] showNewJSONParametersDialog(JPanel parentPanel, String id, String jsonParameterTypeData) {
		Frame frame = JOptionPane.getFrameForComponent((Component)parentPanel);
		Dialogs dialog = new Dialogs(frame, "Please enter details", "", null, null, new JSONPanel(id, jsonParameterTypeData), null, null, null);
		dialog.setVisible(true);
		return (dialog.isChangesMade()? dialog.jsonPanel.getData().get(id) : null);
    }
    private Dialogs(Frame frame, String title, String currentNameValue, String currentImagePath, HomerDatabase database, JSONPanel newJsonPanel, Trigger trigger, Condition condition, Action action) {
        super(frame, title, true);

        //Create and initialise the buttons.
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        getRootPane().setDefaultButton(saveButton);

        imagePanel = new ChooseImageButton(currentImagePath);

        JLabel label = new JLabel("Name:");
        label.setLabelFor(nameValue);
        nameValue = new JTextField(20);
        if (currentNameValue!=null) nameValue.setText(currentNameValue);
        JPanel namePanel = new JPanel();
        namePanel.add(label);
        namePanel.add(nameValue);
        
        masterPanel = new JPanel();
        
        //Lay out the buttons from left to right.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(saveButton);

        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = getContentPane();
        contentPane.add(masterPanel, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        
        if (newJsonPanel != null)
        {
        	this.jsonPanel = newJsonPanel;
        	masterPanel.add(jsonPanel);
        	saveButton.setText("Go!");
        	pack();
        }
        else if (trigger!=null || condition != null || action!=null)
        {
        	masterPanel.add(imagePanel.getMasterPanel());
        	
        	if (trigger!=null)
        	{
        		nameValue.setText(trigger.getDescription());
        		secondImagePanel = new ChooseImageButton(trigger.getAfterImage());
        		masterPanel.add(new JLabel("\u2192"));
        		masterPanel.add(secondImagePanel.getMasterPanel());
        		setSize(450, 250);
        	}
        	else
        	{
        		nameValue.setText((condition!=null? condition.getDescription() : action.getDescription()));
        		setSize(350, 250);
        	}
        	 masterPanel.add(namePanel);
        }
        else
        {
	        masterPanel.add(imagePanel.getMasterPanel());
	        masterPanel.add(namePanel);
	
	        if (database != null)
	        {
	        	setDatabase(database);
	        	locationContextComboBox = new JComboBox<String>();
	        	locationContexts = database.getAllLocationContexts();
				updateLocationContextComboBox(null);
	        	JButton newLocationContextButton = new JButton("New Location Context");
				newLocationContextButton.addActionListener(this);
	        	JPanel locConPanel = new JPanel();
	        	locConPanel.add(locationContextComboBox);
	        	locConPanel.add(newLocationContextButton);
	        	masterPanel.add(locConPanel);
	        	setSize(400, 300);
	        }
	        else
	        {
	        	setSize(450, 200);
	        }
        }
        setLocationRelativeTo(frame);
    }
 
    private void updateLocationContextComboBox(String idToBeSelected) {
    	locationContexts = database.getAllLocationContexts();
		if (locationContextComboBox == null) locationContextComboBox = new JComboBox();
		
		if (locationContexts == null || locationContexts.length==0) 
			locationContextComboBox.setVisible(false);
		else
		{
			locationContextComboBox.removeAllItems();
			
			int selectedIndex = 0;
			
			for (int i = 0; i< locationContexts.length; i++)
			{
				locationContextComboBox.addItem(locationContexts[i].getName());
				
				if (idToBeSelected != null)
				{
					if (locationContexts[i].getId().equals(idToBeSelected)) 
						selectedIndex = i;
				}
			}
			
			locationContextComboBox.setSelectedIndex(selectedIndex);
			locationContextComboBox.setVisible(true);
		}
	}
    
	//Handle clicks on the Set and Cancel buttons.
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {

        	if (jsonPanel == null && (getName().isEmpty() || imagePanel.getImageName().isEmpty()))
        	{
        		JOptionPane.showMessageDialog(masterPanel, "Please both enter a name and choose an image.");
        	}
        	else if (locationContextComboBox!=null && locationContextComboBox.getSelectedIndex()<0)
        	{
        		JOptionPane.showMessageDialog(masterPanel, "Please choose the location context.");
        	}
        	else
        	{
        		setChangesMade(true);
        		setVisible(false);
        	}
        }
        else if (e.getSource() == cancelButton)
        {
        	if (isChangesMade())
        	{
        		nameValue.setText("");
        		imagePanel.setImageName("");
        	}
        	jsonPanel = null;
        	setVisible(false);
        }
        else
        {
        	LocationContext locationContext = Dialogs.showNewLocationContextDialog(masterPanel);
        	if (locationContext!=null)
        	{
        		setChangesMade(true);
        		String id = database.addLocationContext(locationContext.getName(), locationContext.getImage());
        		updateLocationContextComboBox(id);
        	}
        }
      
    }
	public void setChangesMade(boolean changesMade) {
		this.changesMade = changesMade;
	}
	public boolean isChangesMade() {
		return changesMade;
	}
	public void setDatabase(HomerDatabase database) {
		this.database = database;
	}
	public HomerDatabase getDatabase() {
		return database;
	}
	
}