/*
 * Created on Fri Jul 30 11:35:13 BST 2010
 */
package uk.ac.stir.cs.homer.userInterfaceJavaClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import uk.ac.stir.cs.homer.homerFrameworkAPI.osgi.ServiceUtils;
import uk.ac.stir.cs.homer.homerFrameworkAPI.systemUtils.Homer;
import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.Image.ImageUploader;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.deviceManagement.ManageDevicesForm;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.deviceManagement.ManageTrigsConsActsForm;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.doSomething.DoSomethingPanel;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.log.EventLogFrame;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.policyManager.ManagePolicicesWindow;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.policyWriter.NewPolicyWindow;

public class UserInterfaceJavaClientActivator implements BundleActivator {
  
  /* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
   */
  public void start(final BundleContext context) throws Exception 
  {
	  if (Homer.SHOW_GUI)
	  {
		  final HomerDatabase database = ServiceUtils.getService(context, HomerDatabase.class);
		  
		  JFrame frame = new JFrame();
		  frame.setSize(600,600);
		  
		  JMenuBar menuBar = new JMenuBar();
		  
		  JMenu menuManage = new JMenu("Manage");
		  JMenuItem menuItemDevices = new JMenuItem("Devices");
		  menuItemDevices.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new ManageDevicesForm(database);
				}
			  });
		
		  JMenuItem menuTrigsConsActs = new JMenuItem("Triggers, Conditions and Actions");
		  menuTrigsConsActs.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new ManageTrigsConsActsForm(database);
				}
			  });
		  
		  JMenuItem menuUploadImages = new JMenuItem("Upload Image");
		  menuUploadImages.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new ImageUploader(database);
				}
			  });
		  
		  JMenuItem menuShutDown = new JMenuItem("Exit");
		  menuShutDown.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					context.getBundle(0).stop();
				} catch (BundleException e)
				{
					e.printStackTrace();
				}
			}
			  	
		  });
		  
		  menuBar.add(menuManage);
		  menuManage.add(menuItemDevices);
		  menuManage.add(menuTrigsConsActs);
		  menuManage.add(menuUploadImages);
		  menuManage.add(menuShutDown);
		  
		  frame.setJMenuBar(menuBar);
		  
		  final JTabbedPane tabs = new JTabbedPane();
		  tabs.add("Log", new EventLogFrame(context, database));
		  final DoSomethingPanel doSomethingPanel = new DoSomethingPanel(database);
		  tabs.add("Do Something!", doSomethingPanel);
		  final NewPolicyWindow createNewPolicy = new NewPolicyWindow(database);
		  tabs.add("Write new Policy", createNewPolicy);
		  final ManagePolicicesWindow managePoliciesWindow = new ManagePolicicesWindow();
		  tabs.add("Manage Policies", managePoliciesWindow);
		  
		  tabs.addChangeListener(new ChangeListener() {
	
			@Override
			public void stateChanged(ChangeEvent arg0) {
				
				if (tabs.getSelectedIndex() == 1)
				{
					doSomethingPanel.refreshPanel();
				}
			}
			  
		  });
		  
		  frame.add(tabs);
		  frame.setLocationRelativeTo(null);
		  frame.setVisible(true);
	  }
  }

  /* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception {
  }
}