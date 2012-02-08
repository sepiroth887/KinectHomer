package uk.ac.stir.cs.homer.userInterfaceJavaClient.policyManager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.SystemGateway;
import uk.ac.stir.cs.homer.serviceDatabase.objects.Policy;

public class ManagePolicicesWindow extends JPanel
{
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	private SystemGateway systemGateway;
	
	public ManagePolicicesWindow()
	{
		systemGateway = SystemGateway.Singleton.get();
		displayPolicies();
	}
	
	private void displayPolicies()
	{
		this.removeAll();
		
		List<Policy> policies = systemGateway.getPolicies();
		JPanel policiesPanel = new JPanel(new GridLayout(policies.size()+1, 6));
		
		policiesPanel.add(new JLabel("Name"));
		policiesPanel.add(new JLabel("Author"));
		policiesPanel.add(new JLabel("Date Created"));
		policiesPanel.add(new JLabel("Date Edited"));
		policiesPanel.add(new JLabel("Enabled"));
		policiesPanel.add(new JLabel("Delete?"));
		
		for (final Policy policy : policies)
		{
			final JLabel dateEdited = new JLabel(dateFormatter.format(new Date(Long.parseLong(policy.getDateLastEditedInMillisecs())))+"   ");

			final JButton nameButton = new JButton(policy.getName());
			nameButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0)
				{
					changeName("Name", policy.getName(), policy);
					dateEdited.setText(dateFormatter.format(new Date(Long.parseLong(policy.getDateLastEditedInMillisecs())))+"   ");
					nameButton.setText(policy.getName());
				}
			});
			
			final JButton authorButton = new JButton(policy.getAuthor());
			authorButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0)
				{
					changeName("Author", policy.getAuthor(), policy);
					dateEdited.setText(dateFormatter.format(new Date(Long.parseLong(policy.getDateLastEditedInMillisecs())))+"   ");
					authorButton.setText(policy.getAuthor());
				}
			});
			
			JLabel dateCreated = new JLabel(dateFormatter.format(new Date(Long.parseLong(policy.getDateCreatedInMillisecs())))+"   ");
			
			final JCheckBox enabledCheckBox = new JCheckBox();
			enabledCheckBox.setSelected(policy.isEnabled());
			enabledCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					systemGateway.enablePolicy(policy, enabledCheckBox.isSelected());
					dateEdited.setText(dateFormatter.format(new Date(Long.parseLong(policy.getDateLastEditedInMillisecs())))+"   ");
				}
			});
			
			JButton deleteButton = new JButton("Delete");
			deleteButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0)
				{
					deletePolicy(policy);
				}
			});
			
			policiesPanel.add(nameButton);
			policiesPanel.add(authorButton);
			policiesPanel.add(dateCreated);
			policiesPanel.add(dateEdited);
			policiesPanel.add(enabledCheckBox);
			policiesPanel.add(deleteButton);
		}
		
		this.add(policiesPanel);
		
		this.updateUI();
	}
	
	private void changeName(String changingProperty, String currentValue, Policy policy)
	{
		String newName = (String)JOptionPane.showInputDialog(
		                    this,
		                    "",
		                    "Change " + changingProperty,
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    null,
		                    currentValue);

		//If a string was returned, say so.
		if ((newName != null) && (newName.length() > 0) && !newName.equals(currentValue)) {
			if (changingProperty.equals("Name"))
			{
				policy.setName(newName);
			}
			else
			{
				policy.setAuthor(newName);
			}
		    systemGateway.updatePolicy(policy);
		}
	}
	
	private void deletePolicy(Policy policy)
	{
		//default icon, custom title
		int n = JOptionPane.showConfirmDialog(
		    this,
		    "Are you sure that you would like to delete this policy?",
		    "Delete Policy?",
		    JOptionPane.YES_NO_OPTION);
		
		if (n==0)
		{
			systemGateway.deletePolicy(policy);
			displayPolicies();
		}
	}
}
